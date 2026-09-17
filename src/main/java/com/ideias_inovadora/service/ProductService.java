package com.ideias_inovadora.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.ProductDTO;
import com.ideias_inovadora.dto.StockDTO;
import com.ideias_inovadora.erros.OperationNotAllowedException;
import com.ideias_inovadora.model.AccessLevel;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.FileCategory;
import com.ideias_inovadora.model.Files;
import com.ideias_inovadora.model.MovementType;
import com.ideias_inovadora.model.Product;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.model.Stock;
import com.ideias_inovadora.repository.ProductRepository;
import com.ideias_inovadora.repository.SaleItemRepository;
import com.ideias_inovadora.repository.SaleRepository;
import com.ideias_inovadora.repository.ShopRepository;
import com.ideias_inovadora.repository.StockRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;
	@Autowired
	FilesService filesService;
	@Autowired
	ShopRepository shopRepository;
	@Autowired
	StockRepository stockRepository;
	@Autowired
	StockMovementService stockMovementService;
	@Autowired
	SaleItemRepository saleItemRepository;
	
	
	
	/**
	 * Método Auxiliar para obter o funcionário logado via Token JWT.
	 * Evita ter de repetir a lógica de busca do SecurityContext em todos os métodos.
	 */
	private Employee getUtilizadorLogado() {
		return (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	/**
	 * MÉTODO: create (Salvar/Configurar Produto)
	 * ----------------------------------------------------------------------------
	 * Este método decide se deve apenas criar a Identidade Global (Catálogo) 
	 * ou se deve também configurar o Comércio Local (Stock/Preço).
	 * ----------------------------------------------------------------------------
	 */
	@Transactional(rollbackOn = Exception.class) 
	@CacheEvict(value = "product", allEntries = true) 
	public String create(ProductDTO productDTO) throws Exception {
	    
	    Employee logado = getUtilizadorLogado();
	    String role = (logado.getAccessLevel() != null) ? logado.getAccessLevel().getDescricao() : "NO_ACCESS";

	    // 1. VERIFICAÇÃO DE IDENTIDADE GLOBAL (CÓDIGO DE BARRAS)
	    Optional<Product> productExistente = productRepository.findByCodigobarra(productDTO.getCodigobarra());
	    Product product;
	    
	    if (productExistente.isPresent()) {
	        product = productExistente.get();
	    } else {
	        // Bloqueio: Gerente não cria ficha técnica nova
	        if (!role.equals("ROLE_ADMIN")) {
	            throw new OperationNotAllowedException("Não tem permissão para criar novos produtos no catálogo global.");
	        }

	        Files file = null;
	        if (productDTO.getFile() != null && !productDTO.getFile().isEmpty()) {
	            file = filesService.salvarArquivoFoto(productDTO.getFile(), FileCategory.PRODUCT);
	        }
	        
	        product = new Product();
	        product.setCodigobarra(productDTO.getCodigobarra());
	        product.setNome(productDTO.getNome().toUpperCase());
	        product.setDescricao(productDTO.getDescricao());
	        product.setCategory(productDTO.getCategory());
	        product.setSupplier(productDTO.getSupplier());
	        product.setTax(productDTO.getTax());
	        product.setFiles(file);
	        product.setEmployee(logado); 
	        product.setDataCriacao(LocalDateTime.now());
	        product.setControlaStock(productDTO.isControlaStock()); 
	        product = productRepository.save(product);
	    }

	    // ========================================================================
	    // 2. DETECÇÃO DE INTENÇÃO (SÉNIOR): CHAMAR OU NÃO O ADD_STOCK?
	    // ========================================================================
	    
	    // Verificamos se existe qualquer menção a uma loja (na lista OU na raiz)
	    boolean temLojaNaLista = (productDTO.getStockDTOs() != null && !productDTO.getStockDTOs().isEmpty());
	    boolean temLojaNoCorpo = (productDTO.getStoreId() != null && productDTO.getStoreId() != 0);

	    if (temLojaNaLista || temLojaNoCorpo) {
	        // ✅ Existe intenção comercial (Vincular à loja). Chamamos o addStock.
	        addStock(product, productDTO, logado);
	    } else {
	        // ❌ Lista vazia e sem storeId na raiz.
	        
	        if (role.equals("ROLE_MANAGER")) {
	            // Se for Gerente, ele NÃO pode salvar sem loja.
	            throw new OperationNotAllowedException("Erro: Como Gerente, deve associar o produto à sua unidade.");
	        }
	        // Se for Admin, o sistema entende como "Apenas Catálogo Global".
	        // Ignora o addStock e termina a operação com sucesso.
	    }

	    return "Operação realizada com sucesso.";
	}


	@Transactional
	@CacheEvict(value = "product", allEntries = true)
	public String update(ProductDTO dto, Long shopId) {
	    // 1. Busca o produto original na base de dados (Identidade)
	    Product productExistente = productRepository.findById(dto.getId())
	            .orElseThrow(() -> new OperationNotAllowedException("Produto não encontrado para atualização."));

	    // 2. ATUALIZAÇÃO DOS DADOS GLOBAIS (Merge seletivo)
	    // Atualizamos apenas o que é Identidade. 
	    // Note que NÃO tocamos no atributo 'files' se o DTO não trouxer um novo.
	    productExistente.setNome(dto.getNome().toUpperCase());
	    productExistente.setCodigobarra(dto.getCodigobarra());
	    productExistente.setDescricao(dto.getDescricao());
	    productExistente.setCategory(dto.getCategory());
	    productExistente.setSupplier(dto.getSupplier());
	    productExistente.setTax(dto.getTax());
	    productExistente.setControlaStock(dto.isControlaStock());
	    productExistente.setDataAtualizacao(LocalDateTime.now());

	    // 3. ATUALIZAÇÃO DO PREÇO LOCAL (Comércio)
	    // Se o Admin/Gerente estiver dentro de uma loja, atualizamos o preço local
	    if (shopId != null && shopId != 0) {
	        Optional<Stock> stockOpt = stockRepository.findByShopIdAndProductId(shopId, dto.getId());
	        
	        if (stockOpt.isPresent()) {
	            Stock stock = stockOpt.get();
	            // Atualiza apenas o preço na tabela Stock
	            stock.setPrecoUnitario(dto.getPrecoUnitario());
	            stockRepository.save(stock);
	        } 
	    }

	    // 4. Salva a Identidade (O hibernate garante que o ID de 'files' não se perde)
	    productRepository.save(productExistente);
	    
	    return "Produto e Preço atualizados com sucesso";
	}

	/**
	 * MÉTODO: atualizarMovimentacaoStock
	 * ----------------------------------------------------------------------------
	 * LÓGICA:
	 * 1. Bloqueia Admin no modo Global (idl=0).
	 * 2. Busca o saldo atual na tabela Stock.
	 * 3. Soma ou subtrai a quantidade enviada pelo Frontend.
	 * 4. ✅ REGRA: Não altera a quantidadeMinima (preserva o valor original).
	 * 5. ✅ INTEGRAÇÃO: Regista o log no StockMovementService.
	 * ----------------------------------------------------------------------------
	 */
	@Transactional(rollbackOn = Exception.class)
	public String atualizarMovimentacaoStock(ProductDTO productDTO) {
	    
	    // 1. Validação de entrada: Garante que há pelo menos um item na lista
	    if (productDTO.getStockDTOs() == null || productDTO.getStockDTOs().isEmpty()) {
	        throw new OperationNotAllowedException("Dados de movimentação não fornecidos.");
	    }

	    // Pegamos o único item da lista (como combinado, o Front envia um por vez)
	    StockDTO sDto = productDTO.getStockDTOs().get(0);
	    Employee logado = getUtilizadorLogado();
	    String role = logado.getAccessLevel().getDescricao();

	    // 2. SEGURANÇA DE CONTEXTO
	    // Admin Global (idl=0) não tem permissão para esta operação.
	    // Gerente só pode mexer na sua própria loja.
	    if (role.equals("ROLE_MANAGER")) {
	        if (logado.getShop() == null || logado.getShop().getId() != sDto.getStoreId()) {
	            throw new OperationNotAllowedException("Autorização Negada: Unidade inválida para o seu perfil.");
	        }
	    } else if (role.equals("ROLE_ADMIN")) {
	        // Se o Admin estiver no Global (sem storeId no JSON ou Header), o sistema não permite.
	        if (sDto.getStoreId() == null || sDto.getStoreId() == 0) {
	            throw new OperationNotAllowedException("Operação não permitida na Visão Global. Selecione uma unidade.");
	        }
	    }

	    // 3. BUSCA O STOCK ATUAL (Obrigatório existir)
	    Stock stockExistente = stockRepository.findByShopIdAndProductId(sDto.getStoreId(), sDto.getProduct())
	            .orElseThrow(() -> new OperationNotAllowedException("O produto não está configurado nesta unidade."));

	    // 4. LÓGICA DE AJUSTE DE SALDO (Soma ou Subtração)
	    int quantidadeInformada = sDto.getStock();
	    int saldoAtual = stockExistente.getQuantidade();
	    int novoSaldo;
	    MovementType tipoMovimento;
	    String motivo;

	    if ("adicionar".equals(productDTO.getAcao())) {
	        novoSaldo = saldoAtual + quantidadeInformada;
	        tipoMovimento = MovementType.ENTRADA;
	        motivo = "Aumento de Stock ";
	    } else {
	        // Validação para não permitir stock negativo
	        if (saldoAtual < quantidadeInformada) {
	            throw new OperationNotAllowedException("Quantidade insuficiente. Saldo atual: " + saldoAtual);
	        }
	        novoSaldo = saldoAtual - quantidadeInformada;
	        tipoMovimento = MovementType.SAÍDA;
	        motivo = "Baixa de Stock";
	    }

	    // 5. ATUALIZA APENAS A QUANTIDADE (Mantém quantidadeMinima intacta)
	    stockExistente.setQuantidade(novoSaldo);
	    stockRepository.save(stockExistente);

	    // 6. REGISTO DE AUDITORIA (Utilizando o seu StockMovementService)
	    stockMovementService.registerMovement(
	        stockExistente.getProduct(), 
	        stockExistente.getShop(), 
	        quantidadeInformada, 
	        motivo, 
	        tipoMovimento, 
	        logado
	    );

	    return "Stock do produto '" + stockExistente.getProduct().getNome() + "' atualizado para " + novoSaldo;
	}
	// Metudo para verificar id da loja
	private boolean existsByIdproduct(Long id) {
		return productRepository.existsById(id);
	}

	@Cacheable(value = "product", key = "'list'")
	public List<ProductDTO> list() {
		return productRepository.findAll().stream().map(p -> this.mapToProductDTO(p, null)).collect(Collectors.toList());
	}

	
	public List<ProductDTO> listidloja(Long shopId) {

	    List<Stock> stocks = stockRepository.findByShopId(shopId);
	    		// Buscamos todos os produtos e mapeamos o stock relativo à loja (shopId)
	    return stocks.stream()
	            .map(stock -> this.mapToProductDTO(stock.getProduct(), shopId))
	            .collect(Collectors.toList());
	}
	
	/**
	 * MÉTODO: findByIdInteligente
	 * ----------------------------------------------------------------------------
	 * Este é o ponto único de entrada para carregar um produto (para Ver ou Editar).
	 * 1. Se shopId for 0 ou null -> O sistema assume "Modo Catálogo Global".
	 *    Usa o mapeador de IDENTIDADE (sem preço/stock).
	 * 2. Se shopId for > 0 -> O sistema assume "Modo Operacional de Loja".
	 *    Usa o mapeador de COMÉRCIO (Identidade + Preço/Stock daquela unidade).
	 * ----------------------------------------------------------------------------
	 */
	public ProductDTO findByIdInteligente(Long id, Long shopId) {
	    // Busca a raiz do produto (Identidade)
	    Product product = productRepository.findById(id)
	            .orElseThrow(() -> new OperationNotAllowedException("Produto não encontrado no catálogo."));

	    // Decisão de contexto
	    if (shopId == null || shopId == 0) {
	        // ✅ CENÁRIO A: ADMIN NO LOBBY GLOBAL
	        // Queremos apenas carregar os campos técnicos para o formulário.
	        return this.mapToProductEditDTO(product);
	    } else {
	        // ✅ CENÁRIO B: ADMIN OU GERENTE DENTRO DE UMA LOJA
	        // Queremos os campos técnicos + a situação comercial (preço/stock) daquela loja.
	        return this.mapToProductDTO(product, shopId);
	    }
	}
	
	/**
	 * MÉTODO AUXILIAR: addStock
	 * ----------------------------------------------------------------------------
	 * LÓGICA DE DETERMINAÇÃO DE UNIDADE (CONTEXTUAL):
	 * 1. PRODUTO LIVRE (isControlaStock = false):
	 *    - Se Gerente: Pega a loja do perfil (Segurança).
	 *    - Se Admin: Pega o storeId da raiz do JSON (Contexto).
	 * 
	 * 2. PRODUTO FÍSICO (isControlaStock = true):
	 *    - Processa a lista stockDTOs, validando a autoridade se for Gerente.
	 * ----------------------------------------------------------------------------
	 */
	private void addStock(Product product, ProductDTO productDTO, Employee logado) {
	    
	    AccessLevel level = logado.getAccessLevel();
	    String role = (level != null) ? level.getDescricao() : "NO_ACCESS";
	    List<StockDTO> stockDTOs = productDTO.getStockDTOs();

	    // ========================================================================
	    // CASO A: PRODUTO LIVRE (SERVIÇOS / GRÁFICA)
	    // ========================================================================
	    if (!product.isControlaStock()) {
	        
	        Long idLojaDestino = null;

	        // 1. Determinar qual loja usar baseado em QUEM está logado
	        if (role.equals("ROLE_MANAGER")) {
	            // Se for Gerente, usamos a loja dele (já validada no login)
	            if (logado.getShop() != null) {
	                idLojaDestino = logado.getShop().getId();
	            }
	        } else if (role.equals("ROLE_ADMIN")) {
	            // Se for Admin Master, pegamos o ID que ele enviou na raiz do JSON
	            idLojaDestino = productDTO.getStoreId();
	        }

	        // Validação final: se não encontramos loja em lado nenhum, barramos
	        if (idLojaDestino == null) {
	            throw new OperationNotAllowedException("Erro: Não foi possível identificar a loja para este serviço.");
	        }

	        // Busca vínculo existente ou cria novo
	        Stock stock = stockRepository.findByShopIdAndProductId(idLojaDestino, product.getId())
	                                     .orElse(new Stock());

	        stock.setProduct(product);
	        
	        // Busca a Shop na base de dados para garantir integridade
	        Shop shop = shopRepository.findById(idLojaDestino)
	                .orElseThrow(() -> new OperationNotAllowedException("Loja de destino não encontrada."));
	        
	        stock.setShop(shop);
	        stock.setQuantidade(0); 
	        stock.setQuantidadeMinima(0);
	        stock.setPrecoUnitario(productDTO.getPrecoUnitario()); // Preço da raiz do JSON

	        stockRepository.save(stock);
	        return; // Finaliza aqui para serviços
	    }

	    // ========================================================================
	    // CASO B: PRODUTO COM CONTROLO FÍSICO (LIVRARIA)
	    // ========================================================================
	    if (stockDTOs == null || stockDTOs.isEmpty()) {
	        throw new OperationNotAllowedException("A configuração de stock é obrigatória para produtos físicos.");
	    }

	    for (StockDTO sDto : stockDTOs) {
	        
	        // Segurança: Se Gerente, ele só pode salvar se o ID da lista for o dele
	        if (role.equals("ROLE_MANAGER")) {
	            if (logado.getShop() == null || logado.getShop().getId() != sDto.getStoreId()) {
	                throw new OperationNotAllowedException("Autorização Negada: Não pode gerir stock de outra unidade.");
	            }
	        }

	        Stock stock = stockRepository.findByShopIdAndProductId(sDto.getStoreId(), product.getId())
	                                     .orElse(new Stock());

	        stock.setProduct(product);
	        stock.setShop(shopRepository.findById(sDto.getStoreId())
	                .orElseThrow(() -> new OperationNotAllowedException("Unidade não encontrada.")));

	        stock.setQuantidade(stock.getQuantidade() + sDto.getStock());
	        stock.setQuantidadeMinima(sDto.getStockMin());
	        stock.setPrecoUnitario(sDto.getPrecoUnitario()); // Preço vindo da lista

	        stockRepository.save(stock);

	        if (sDto.getStock() > 0) {
	            stockMovementService.registerMovement(product, stock.getShop(), sDto.getStock(), 
	                "Entrada via Cadastro", MovementType.ENTRADA, logado);
	        }
	    }
	}
	@Transactional
	private void addStocks(ProductDTO productDTO) {
		List<StockDTO> stockDTOs = productDTO.getStockDTOs();
		for (StockDTO stocks : stockDTOs) {
			Optional<Stock> stockopt = quatidadeStock(stocks.getStoreId(), stocks.getProduct());
			if (stockopt.isPresent()) {
				Stock stock2 = stockopt.get();
				MovementType movementType;
				String motivo;
				int novaQuantidade;
				if ("adicionar".equals(productDTO.getAcao())) {
					stock2.setQuantidade(stock2.getQuantidade() + stocks.getStock());
					movementType = MovementType.ENTRADA;
					motivo = "Adicionar Stock";
				} else {
					novaQuantidade = stock2.getQuantidade() - stocks.getStock();

					if (novaQuantidade < 0) {
						throw new OperationNotAllowedException(
								"A operação não pode ser concluída. A quantidade a retirar excede o stock disponível.");
					}
					movementType = MovementType.AJUSTE;
					motivo = "Ajustar Stock";
					stock2.setQuantidade(novaQuantidade);
				}

				stockRepository.saveAndFlush(stock2);
				stockMovementService.registerMovement(getIDProduct(stocks.getProduct()), getShopID(stocks.getStoreId()),
						stocks.getStock(), motivo, movementType, productDTO.getEmployee());
			}
		}
	}
	
	
	/**
	 * Mapeador simplificado para Edição
	 * Carrega apenas o que pertence à tabela 'product'
	 */
	private ProductDTO mapToProductEditDTO(Product product) {
	    ProductDTO dto = new ProductDTO();
	    dto.setId(product.getId());
	    dto.setCodigobarra(product.getCodigobarra());
	    dto.setNome(product.getNome());
	    dto.setDescricao(product.getDescricao());
	    dto.setControlaStock(product.isControlaStock());
	    dto.setDatacriacao(product.getDataCriacao());
	    dto.setDataAtualizacao(product.getDataAtualizacao());
	    
	    
	    // Associações de catálogo
	    if (product.getCategory() != null) {
	        dto.setIdcatgory(product.getCategory().getId());
	        dto.setCategorys(product.getCategory().getNome());
	    }
	    if (product.getTax() != null) {
	        dto.setIdtax(product.getTax().getId());
	        dto.setTaxs(product.getTax().getImposto());
	    }
	    if (product.getSupplier() != null) {
	        dto.setIdsupplier(product.getSupplier().getId());
	        dto.setSuppliers(product.getSupplier().getNome());
	    }
	    
	    dto.setLogoUrl(buscarLogoBase64(product));
	    return dto;
	}
	
	private ProductDTO mapToProductDTO(Product product, Long shopId) {
	    ProductDTO dto = new ProductDTO();
	    
	    // 1. DADOS GLOBAIS (Sempre iguais)
	    dto.setId(product.getId());
	    dto.setCodigobarra(product.getCodigobarra());
	    dto.setNome(product.getNome());
	    dto.setDescricao(product.getDescricao());
	    dto.setControlaStock(product.isControlaStock());
	    dto.setDatacriacao(product.getDataCriacao());
	    dto.setDataAtualizacao(product.getDataAtualizacao());
	    
	    // Mapeamento de Categoria, Imposto e Fornecedor (Identidade)
	    if (product.getCategory() != null) {
	        dto.setIdcatgory(product.getCategory().getId());
	        dto.setCategorys(product.getCategory().getNome());
	    }
	    if (product.getTax() != null) {
	        dto.setIdtax(product.getTax().getId());
	        dto.setTaxs(product.getTax().getImposto());
	        dto.setTaxa(product.getTax().getBaseCalculo());
	    }
	    if (product.getSupplier() != null) {
	        dto.setIdsupplier(product.getSupplier().getId());
	        dto.setSuppliers(product.getSupplier().getNome());
	    }

	    // 2. LÓGICA UNIFICADA DE COMÉRCIO (Preço e Quantidade)
	    // Se temos uma loja, a verdade está SEMPRE na tabela Stock
	    if (shopId != null) {
	        Optional<Stock> stockOpt = stockRepository.findByShopIdAndProductId(shopId, product.getId());
	        
	        if (stockOpt.isPresent()) {
	            Stock s = stockOpt.get();
	            // Aqui não importa se é Livro ou Impressão:
	            dto.setStock(s.getQuantidade());         // Será > 0 para produtos, 0 para serviços
	            dto.setStockMin(s.getQuantidadeMinima());
	            dto.setPrecoUnitario(s.getPrecoUnitario()); // O preço real daquela loja
	        } else {
	            // Se não existe na tabela Stock, o produto não está ativo nesta loja
	            dto.setStock(0);
	            dto.setPrecoUnitario(BigDecimal.ZERO);
	        }
	    } else {
	        // Visão Global (Admin Master sem loja selecionada)
	        dto.setStock(0);
	        dto.setPrecoUnitario(BigDecimal.ZERO);
	    }

	    dto.setLogoUrl(buscarLogoBase64(product));
	    return dto;
	}
	private ProductDTO mapStockToProductDTO(Stock stock) {
	    Product product = stock.getProduct();
	    ProductDTO dto = new ProductDTO();
	    dto.setId(product.getId());
	    dto.setCodigobarra(product.getCodigobarra());
	    dto.setNome(product.getNome());
	   dto.setPrecoUnitario(stock.getPrecoUnitario());
	    dto.setStock(stock.getQuantidade());
	    dto.setTaxa(product.getTax().getBaseCalculo());

	    return dto;
	}
	/**
	 * MÉTODO: eliminarInteligente
	 * Regras:
	 * - Admin Global: Só apaga se o produto não estiver em NENHUMA loja.
	 * - Admin/Gerente Local: Só apaga o stock se não houver Vendas registadas.
	 */
	@Transactional(rollbackOn = Exception.class)
	@CacheEvict(value = "product", allEntries = true) 
	public String eliminarInteligente(Long productId, Long shopIdContexto) {
	    Employee logado = getUtilizadorLogado();
	    String role = logado.getAccessLevel().getDescricao();
	    
	    // CASO 1: ADMIN MASTER NO MODO GLOBAL (idl = 0 no lobby)
	    if (role.equals("ROLE_ADMIN") && (shopIdContexto == null || shopIdContexto == 0)) {
	        
	        // Verifica se o produto existe em qualquer loja (tabela stock)
	        boolean vinculadoALoja = stockRepository.existsByProductId(productId);
	        
	        if (vinculadoALoja) {
	            throw new OperationNotAllowedException("Não pode eliminar do catálogo: Este produto está ativo em unidades.");
	        }
	        
	        productRepository.deleteById(productId);
	        return "Produto removido permanentemente do catálogo global.";
	    }

	    // CASO 2: CONTEXTO DE LOJA (Admin Local ou Gerente)
	    else {
	        Long targetShopId = role.equals("ROLE_MANAGER") ? logado.getShop().getId() : shopIdContexto;

	        // ✅ 1. Verifica se houve VENDAS deste produto nesta loja específica
	        // Utilizamos o SaleItemRepository que aponta para o histórico real
	        boolean temHistoricoVendas = saleItemRepository.existsByProductIdAndShopId(productId, targetShopId);
	        
	        if (temHistoricoVendas) {
	            throw new OperationNotAllowedException("Não pode remover: Este produto possui histórico de vendas nesta unidade.");
	        }

	        // ✅ 2. Se está limpo, removemos apenas o vínculo daquela loja (Tabela Stock)
	        Stock stock = stockRepository.findByShopIdAndProductId(targetShopId, productId)
	                .orElseThrow(() -> new OperationNotAllowedException("O produto não está configurado nesta loja."));

	        stockRepository.delete(stock);
	        
	        return "Produto removido do stock da unidade com sucesso.";
	    }
	}
	private Optional<Stock> quatidadeStock(long shop, long product) {
		Optional<Stock> stockOpt = stockRepository.findByShopIdAndProductId(shop, product);
		return stockOpt;
	}

	public String buscarLogoBase64(Product product) {
		if (product.getFiles() != null) {
			try {
				byte[] bytes = filesService.buscarArquivo(product.getFiles().getId());
				return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
			} catch (Exception e) {
				// Se falhar, apenas retorna null
				return null;
			}
		}
		return null;
	}

	public ProductDTO enviarfile(ProductDTO productDTO) {

		Product product = getIDProduct(productDTO.getId());
		productDTO.setLogoUrl(buscarLogoBase64(product));
		return productDTO;
	}
	@Transactional
	@CacheEvict(value = "shop", allEntries = true)
	public String upload_logo(ProductDTO productDTO) throws Exception {
		if (productDTO.getFile() != null) {
			Product product = productRepository.findById(productDTO.getId()).orElseThrow(
					() -> new OperationNotAllowedException("Loja não encontrada com o ID: " + productDTO.getId()));

			return filesService.atualizarArquivoFotos(productDTO.getFile(), product, FileCategory.PRODUCT);
		}
		return null;
	}

	private Product getIDProduct(long id) {
		return productRepository.findById(id).orElseThrow(() -> new OperationNotAllowedException("erro shop"));
	}

	private Shop getShopID(long id) {
		return shopRepository.findById(id).orElseThrow(() -> new OperationNotAllowedException("erro shop"));
	}
	/**
 * MÉTODO: atualizarFoto (Lógica de Substituição de Ficheiro)
 */
@Transactional
@CacheEvict(value = "product", allEntries = true) 
public String atualizarFoto(ProductDTO dto) throws Exception {
    // 1. Busca o produto
    Product product = productRepository.findById(dto.getId())
            .orElseThrow(() -> new OperationNotAllowedException("Produto não encontrado."));

    Files fileSalvo = null;

    // 2. Verifica se o produto já possui um ID de ficheiro vinculado
    if (product.getFiles() != null) {
        // CENÁRIO: Já existe. Atualizamos o conteúdo físico e os metadados.
        // O método 'atualizarArquivoFotos' deve tratar a substituição no disco usando o ID existente.
        filesService.atualizarArquivoFotos(dto.getFile(), product, FileCategory.PRODUCT);
    } else {
        // CENÁRIO: Não existe. Salva novo ficheiro e cria registo na tabela 'files'.
        fileSalvo = filesService.salvarArquivoFoto(dto.getFile(), FileCategory.PRODUCT);
        
        // Faz o Update no produto para guardar o novo ID de ficheiro
        product.setFiles(fileSalvo);
        productRepository.save(product);
    }

    return "Imagem actualizada com sucesso.";
}
}
