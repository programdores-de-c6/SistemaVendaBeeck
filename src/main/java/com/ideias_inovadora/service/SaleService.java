package com.ideias_inovadora.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.SaleRequestDTO;
import com.ideias_inovadora.dto.SaleResponseDTO;
import com.ideias_inovadora.model.*;
import com.ideias_inovadora.repository.*;

import jakarta.transaction.Transactional;

@Service
public class SaleService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private SerieService serieService;

    @Autowired
    private FilesService filesService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProFormaRepository proFormaRepository;


    /**
     * ====================================================
     * CRIAR VENDA
     * ====================================================
     */
    @Transactional
    public SaleResponseDTO saveSale(SaleRequestDTO dto) {

        // ====================================================
        // 1. VALIDAÇÃO BÁSICA
        // ====================================================

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Dados da venda não fornecidos."
            );
        }

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "A venda deve conter pelo menos um produto."
            );
        }

        // Uma venda nunca pode ser criada em contexto global.
        if (dto.getShopId() == null || dto.getShopId() == 0) {
            throw new IllegalArgumentException(
                    "É necessário seleccionar uma loja válida antes de realizar a venda."
            );
        }

        // Toda venda precisa de um caixa.
        if (dto.getCaixaId() == null || dto.getCaixaId() == 0) {
            throw new IllegalArgumentException(
                    "É necessário ter um caixa aberto para realizar a venda."
            );
        }


        // ====================================================
        // 2. CÁLCULOS FINANCEIROS
        // ====================================================

        BigDecimal subtotalProdutos =
                calcularSubtotal(dto.getItems());

        BigDecimal totalImposto =
                calcularTotalImposto(dto.getItems());

        BigDecimal totalBrutoComIva =
                subtotalProdutos.add(totalImposto);

        BigDecimal valorDescontoDbs =
                calcularValorRealDesconto(
                        totalBrutoComIva,
                        dto.getDiscountAmount(),
                        dto.getDiscountType()
                );

        BigDecimal totalLiquido =
                totalBrutoComIva
                        .subtract(valorDescontoDbs)
                        .max(BigDecimal.ZERO)
                        .setScale(2, RoundingMode.HALF_UP);

        BigDecimal troco = BigDecimal.ZERO;

        if ("DINHEIRO".equals(dto.getPaymentMethod())) {

            if (dto.getValorRecebido() != null
                    && dto.getValorRecebido()
                            .compareTo(totalLiquido) > 0) {

                troco =
                        dto.getValorRecebido()
                                .subtract(totalLiquido)
                                .setScale(
                                        2,
                                        RoundingMode.HALF_UP
                                );
            }
        }


        // ====================================================
        // 3. VALIDAR VALORES DO FRONTEND
        // ====================================================

        validarIntegridadeFinanceira(
                dto,
                subtotalProdutos,
                totalImposto,
                totalLiquido,
                troco
        );


        // ====================================================
        // 4. BUSCAR CAIXA
        // ====================================================

        Box box =
                boxRepository.findById(dto.getCaixaId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Caixa não encontrado: "
                                                + dto.getCaixaId()
                                )
                        );


        // O caixa utilizado na venda tem de estar aberto.
        if (box.getStatusCaixa() != StatusCaixa.ABERTO) {

            throw new IllegalStateException(
                    "O caixa seleccionado não está aberto."
            );
        }


        // ====================================================
        // 5. BUSCAR LOJA
        // ====================================================

        Shop shop =
                shopRepository.findById(dto.getShopId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loja não encontrada: "
                                                + dto.getShopId()
                                )
                        );


        // ====================================================
        // 6. VALIDAR CAIXA x LOJA
        // ====================================================

        // A partir de agora, o Box é directamente associado
        // à loja.
        if (box.getShop() == null) {

            throw new IllegalStateException(
                    "O caixa não está associado a nenhuma loja."
            );
        }

        if (box.getShop() == null) {
            throw new IllegalStateException(
                "O caixa não está associado a nenhuma loja."
            );
        }

        if (box.getShop().getId() != shop.getId()) {
            throw new SecurityException(
                "O caixa seleccionado não pertence à loja actual."
            );
        }

        // ====================================================
        // 7. PROCESSAR CLIENTE
        // ====================================================

        Map<String, Object> dadosCliente =
                customerService.processarClienteParaVenda(
                        dto.getCustomerId(),
                        dto.getCustomerName(),
                        dto.getCustomerNif()
                );

        Customer customerFinal =
                (Customer) dadosCliente.get("customer");

        String nomeInformal =
                (String) dadosCliente.get("nomeInformal");


        // ====================================================
        // 8. GRAVAR TRANSAÇÃO
        // ====================================================

        Transaction transaction =
                new Transaction();

        transaction.setBox(box);

        // Loja determinada pelo contexto da venda.
        transaction.setShop(shop);

        transaction.setCustomer(customerFinal);

        transaction.setDatatrasacao(
                LocalDateTime.now()
        );

        transaction.setPaymentMethod(
                PaymentMethod.valueOf(dto.getPaymentMethod())
        );

        transaction.setTransactionType(
                TransactionType.VENDA
        );

        transaction.setDiscountType(
                dto.getDiscountType()
        );

        transaction.setDiscountAmount(
                dto.getDiscountAmount()
        );

        transaction.setDiscountValue(
                valorDescontoDbs
        );

        transaction.setSubTotal(
                subtotalProdutos
        );

        transaction.setTotalImposto(
                totalImposto
        );

        transaction.setTotalGeral(
                totalLiquido
        );

        transaction.setValorRecebido(
                dto.getValorRecebido()
        );

        transaction.setTroco(
                troco
        );

        transaction =
                transactionRepository.save(transaction);


        // ====================================================
        // 9. GRAVAR VENDA
        // ====================================================

        Sale sale = new Sale();

        sale.setTransaction(
                transaction
        );

        sale.setDatavenda(
                LocalDateTime.now()
        );


        // Gera número da factura para esta loja.
        Map<String, Object> dadosFactura =
                serieService.gerarProximoNumeroFactura(
                        shop.getId()
                );

        sale.setSeries(
                (Series) dadosFactura.get(
                        "serieObject"
                )
        );

        sale.setNumeroFactura(
                (String) dadosFactura.get(
                        "numeroFactura"
                )
        );

        sale.setNomeclienteInformal(
                nomeInformal
        );

        sale =
                saleRepository.save(sale);


        // ====================================================
        // 10. PROCESSAR ITENS E STOCK
        // ====================================================

        List<SaleResponseDTO.ItemResponseDTO> itensParaDTO =
                new ArrayList<>();


        for (SaleRequestDTO.ItemDTO itemDto
                : dto.getItems()) {

            // ------------------------------------------------
            // Validar item
            // ------------------------------------------------

            if (itemDto == null
                    || itemDto.getProductId() == null
                    || itemDto.getQuantity() <= 0
                    || itemDto.getUnitPrice() == null
                    || itemDto.getUnitPrice()
                            .compareTo(BigDecimal.ZERO) < 0) {

                throw new IllegalArgumentException(
                        "Produto, quantidade e preço devem ser válidos."
                );
            }


            // ------------------------------------------------
            // Procurar produto
            // ------------------------------------------------

            Product product =
                    productRepository.findById(
                            itemDto.getProductId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Produto não encontrado ID: "
                                            + itemDto.getProductId()
                            )
                    );


            // ------------------------------------------------
            // STOCK
            // ------------------------------------------------

            if (product.isControlaStock()) {

                Stock stock =
                        stockRepository
                                .findByShopIdAndProductId(
                                        shop.getId(),
                                        product.getId()
                                )
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Produto sem stock na loja: "
                                                        + product.getNome()
                                        )
                                );


                // Verificar quantidade disponível.
                if (stock.getQuantidade()
                        < itemDto.getQuantity()) {

                    throw new RuntimeException(
                            "Stock insuficiente para: "
                                    + product.getNome()
                    );
                }


                // Baixar stock.
                stock.setQuantidade(
                        stock.getQuantidade()
                                - itemDto.getQuantity()
                );

                stockRepository.save(stock);


                // ------------------------------------------------
                // MOVIMENTO DE STOCK
                // ------------------------------------------------

                StockMovement movement =
                        new StockMovement();

                movement.setProduct(product);

                movement.setShop(shop);

                movement.setQuantidade(
                        itemDto.getQuantity()
                );

                movement.setMovementType(
                        MovementType.SAÍDA
                );

                movement.setMotivo(
                        "Venda: "
                                + sale.getNumeroFactura()
                );

                movement.setData(
                        LocalDateTime.now()
                );

                movement.setEmployee(
                        box.getEmployeeOpened()
                );

                stockMovementRepository.save(
                        movement
                );
            }


            // ------------------------------------------------
            // SUBTOTAL DO ITEM
            // ------------------------------------------------

            BigDecimal subtotalItem =
                    itemDto.getUnitPrice()
                            .multiply(
                                    new BigDecimal(
                                            itemDto.getQuantity()
                                    )
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );


            // ------------------------------------------------
            // SALE ITEM
            // ------------------------------------------------

            SaleItem saleItem =
                    new SaleItem();

            saleItem.setSale(
                    sale
            );

            saleItem.setProduct(
                    product
            );

            saleItem.setQuantidade(
                    itemDto.getQuantity()
            );

            saleItem.setPrecoUnitario(
                    itemDto.getUnitPrice()
            );

            saleItem.setSubTotal(
                    subtotalItem
            );

            saleItemRepository.save(
                    saleItem
            );


            // ------------------------------------------------
            // DTO PARA O FRONTEND / PDF
            // ------------------------------------------------

            SaleResponseDTO.ItemResponseDTO itemResp =
                    new SaleResponseDTO.ItemResponseDTO();

            itemResp.setProductName(
                    product.getNome()
            );

            itemResp.setQuantity(
                    itemDto.getQuantity()
            );

            itemResp.setUnitPrice(
                    itemDto.getUnitPrice()
            );

            itemResp.setSubtotal(
                    subtotalItem
            );

            itensParaDTO.add(
                    itemResp
            );
        }


        // ====================================================
        // 11. PRO FORMA -> VENDA
        // ====================================================

        if (dto.getProformaId() != null) {

            ProForma proForma =
                    proFormaRepository
                            .findById(
                                    dto.getProformaId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Pro Forma não encontrada: "
                                                    + dto.getProformaId()
                                    )
                            );


            // Só uma Pro Forma ABERTA pode ser convertida.
            if (!"ABERTA".equals(
                    proForma.getEstado()
            )) {

                throw new IllegalStateException(
                        "A Pro Forma não está disponível para conversão."
                );
            }


            if (proForma.getShop() == null
                    || proForma.getShop().getId() != shop.getId()) {

                throw new SecurityException(
                        "A Pro Forma não pertence à loja desta venda."
                );
            }
            // Marca como convertida.
            proForma.setEstado(
                    "CONVERTIDA"
            );

            // Persiste.
            proFormaRepository.save(
                    proForma
            );
        }


        // ====================================================
        // 12. RESPOSTA FINAL
        // ====================================================

        return buildSaleResponse(
                sale,
                transaction,
                shop,
                itensParaDTO
        );
    }


    // ========================================================
    // DESCONTO
    // ========================================================

    private BigDecimal calcularValorRealDesconto(
            BigDecimal base,
            BigDecimal valor,
            DiscountType tipo) {

        if (valor == null
                || valor.compareTo(
                        BigDecimal.ZERO
                ) <= 0) {

            return BigDecimal.ZERO;
        }

        if (tipo == DiscountType.PERCENTAGE) {

            return base
                    .multiply(valor)
                    .divide(
                            new BigDecimal("100"),
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        return valor.min(base);
    }


    // ========================================================
    // SUBTOTAL
    // ========================================================

    private BigDecimal calcularSubtotal(
            List<SaleRequestDTO.ItemDTO> items) {

        if (items == null
                || items.isEmpty()) {

            return BigDecimal.ZERO;
        }

        return items.stream()
                .filter(item -> item != null)
                .map(item ->
                        item.getUnitPrice()
                                .multiply(
                                        new BigDecimal(
                                                item.getQuantity()
                                        )
                                )
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                )
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }


    // ========================================================
    // IMPOSTO
    // ========================================================

    private BigDecimal calcularTotalImposto(
            List<SaleRequestDTO.ItemDTO> items) {

        if (items == null
                || items.isEmpty()) {

            return BigDecimal.ZERO;
        }

        return items.stream()
                .filter(item -> item != null)
                .map(item -> {

                    BigDecimal taxRate =
                            item.getTaxRate() != null
                                    ? item.getTaxRate()
                                    : BigDecimal.ZERO;

                    return item.getUnitPrice()
                            .multiply(
                                    new BigDecimal(
                                            item.getQuantity()
                                    )
                            )
                            .multiply(taxRate);
                })
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                )
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }


    // ========================================================
    // RESPOSTA DA VENDA
    // ========================================================

    private SaleResponseDTO buildSaleResponse(
            Sale sale,
            Transaction transaction,
            Shop shop,
            List<SaleResponseDTO.ItemResponseDTO> itens) {

        SaleResponseDTO res =
                new SaleResponseDTO();


        // ====================================================
        // CLIENTE
        // ====================================================

        String nomeCliente;

        if (sale.getNomeclienteInformal() != null
                && !sale.getNomeclienteInformal()
                        .isBlank()) {

            nomeCliente =
                    sale.getNomeclienteInformal();

        } else if (transaction.getCustomer() != null) {

            nomeCliente =
                    transaction.getCustomer()
                            .getNome();

        } else {

            nomeCliente =
                    "Venda ao Público";
        }


        // ====================================================
        // IDENTIFICAÇÃO
        // ====================================================

        res.setId(
                sale.getId()
        );

        res.setNumeroFactura(
                sale.getNumeroFactura()
        );

        res.setDataVenda(
                sale.getDatavenda() != null
                        ? sale.getDatavenda().toString()
                        : null
        );


        res.setNomeCliente(
                nomeCliente
        );


        // NIF
        if (transaction.getCustomer() != null) {

            res.setNifCliente(
                    transaction.getCustomer()
                            .getNumeroContribuinte()
            );

        } else {

            res.setNifCliente(
                    "999999999"
            );
        }


        // Número de autorização.
        if (sale.getSeries() != null) {

            res.setNumeroAutorizacao(
                    sale.getSeries()
                            .getNumeroAutorizacao()
            );
        }


        // ====================================================
        // LOJA
        // ====================================================

        if (shop != null) {

            res.setShopNome(
                    shop.getNome()
            );

            res.setShopNif(
                    shop.getNumeroContribuite()
            );

            res.setShopEndereco(
                    shop.getLocation() != null
                            ? shop.getLocation().getNome()
                            : "N/D"
            );

            res.setShopContacto(
                    shop.getContacto()
            );

            res.setShopEmail(
                    shop.getEmail()
            );

            res.setShopLogo(
                    buscarLogoBase64(shop)
            );
        }


        // ====================================================
        // FINANCEIRO
        // ====================================================

        res.setSubtotal(
                transaction.getSubTotal()
        );

        res.setTotalImposto(
                transaction.getTotalImposto()
        );

        res.setDesconto(
                transaction.getDiscountValue()
        );

        res.setTotalGeral(
                transaction.getTotalGeral()
        );


        // ====================================================
        // PAGAMENTO
        // ====================================================

        if (transaction.getPaymentMethod() != null) {

            res.setMetodoPagamento(
                    transaction
                            .getPaymentMethod()
                            .toString()
            );
        }

        res.setValorRecebido(
                transaction.getValorRecebido()
        );

        res.setTroco(
                transaction.getTroco()
        );


        // ====================================================
        // OPERADOR
        // ====================================================

        if (transaction.getBox() != null
                && transaction.getBox()
                        .getEmployeeOpened() != null) {

            res.setOperador(
                    transaction
                            .getBox()
                            .getEmployeeOpened()
                            .getNome()
            );

        } else {

            res.setOperador(
                    "N/D"
            );
        }


        // ====================================================
        // ITENS
        // ====================================================

        res.setItens(
                itens
        );

        return res;
    }


    // ========================================================
    // LOGÓTIPO
    // ========================================================

    private String buscarLogoBase64(
            Shop shop) {

        if (shop == null
                || shop.getFiles() == null) {

            return null;
        }

        try {

            byte[] bytes =
                    filesService.buscarArquivo(
                            shop.getFiles().getId()
                    );

            return "data:image/png;base64,"
                    + Base64.getEncoder()
                            .encodeToString(bytes);

        } catch (Exception e) {

            return null;
        }
    }


    // ========================================================
    // VENDAS POR CAIXA
    // ========================================================

    public List<SaleResponseDTO> listarVendasPorCaixa(
            Long boxId) {

        if (boxId == null || boxId == 0) {
            return List.of();
        }

        List<Sale> vendas =
                saleRepository.findByBoxId(
                        boxId
                );

        List<SaleResponseDTO> dtos =
                new ArrayList<>();

        for (Sale sale : vendas) {

            dtos.add(
                    mapToSummaryDTO(sale)
            );
        }

        return dtos;
    }


    // ========================================================
    // HISTÓRICO DE VENDAS
    // ========================================================

    public List<SaleResponseDTO> listarHistorico(
            Long shopId) {

        Employee logado =
                (Employee) getUtilizadorLogado();

        List<Sale> vendas;


        // Admin na visão global.
        if (logado.getAccessLevel()
                == AccessLevel.ROLE_ADMIN
                && (shopId == null || shopId == 0)) {

            vendas =
                    saleRepository.findAllGlobal();

        } else {

            Long idParaFiltrar;

            if (logado.getAccessLevel()
                    == AccessLevel.ROLE_ADMIN) {

                if (shopId == null || shopId == 0) {

                    throw new IllegalArgumentException(
                            "É necessário seleccionar uma loja."
                    );
                }

                idParaFiltrar =
                        shopId;

            } else {

                if (logado.getShop() == null) {

                    throw new SecurityException(
                            "O funcionário não está associado a uma loja."
                    );
                }

                idParaFiltrar =
                        logado.getShop().getId();
            }

            vendas =
                    saleRepository.findAllByShopId(
                            idParaFiltrar
                    );
        }


        return vendas.stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }


    // ========================================================
    // DETALHES DA VENDA
    // ========================================================

    public SaleResponseDTO obterDetalhesVenda(
            Long saleId) {

        Sale sale =
                saleRepository.findById(
                        saleId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Venda não encontrada com o ID: "
                                        + saleId
                        )
                );


        Transaction t =
                sale.getTransaction();


        if (t == null) {

            throw new RuntimeException(
                    "A venda não possui transacção associada."
            );
        }


        List<SaleItem> itensDaVenda =
                saleItemRepository.findBySale_Id(
                        saleId
                );


        List<SaleResponseDTO.ItemResponseDTO> itensDTO =
                new ArrayList<>();


        for (SaleItem si : itensDaVenda) {

            if (si == null
                    || si.getProduct() == null) {
                continue;
            }

            itensDTO.add(
                    new SaleResponseDTO.ItemResponseDTO(
                            si.getProduct().getNome(),
                            si.getQuantidade(),
                            si.getPrecoUnitario(),
                            si.getSubTotal()
                    )
            );
        }


        return buildSaleResponse(
                sale,
                t,
                t.getShop(),
                itensDTO
        );
    }


    // ========================================================
    // RESUMO
    // ========================================================

    private SaleResponseDTO mapToSummaryDTO(
            Sale s) {

        SaleResponseDTO dto =
                new SaleResponseDTO();

        dto.setId(
                s.getId()
        );

        dto.setNumeroFactura(
                s.getNumeroFactura()
        );

        dto.setDataVenda(
                s.getDatavenda() != null
                        ? s.getDatavenda().toString()
                        : null
        );


        // Cliente
        if (s.getNomeclienteInformal() != null
                && !s.getNomeclienteInformal()
                        .isBlank()) {

            dto.setNomeCliente(
                    s.getNomeclienteInformal()
            );

        } else if (s.getTransaction() != null
                && s.getTransaction()
                        .getCustomer() != null) {

            dto.setNomeCliente(
                    s.getTransaction()
                            .getCustomer()
                            .getNome()
            );

        } else {

            dto.setNomeCliente(
                    "Venda ao Público"
            );
        }


        // Dados financeiros
        if (s.getTransaction() != null) {

            Transaction t =
                    s.getTransaction();

            dto.setTotalGeral(
                    t.getTotalGeral()
            );

            if (t.getPaymentMethod() != null) {

                dto.setMetodoPagamento(
                        t.getPaymentMethod()
                                .toString()
                );
            }

            if (t.getBox() != null
                    && t.getBox()
                            .getEmployeeOpened() != null) {

                dto.setOperador(
                        t.getBox()
                                .getEmployeeOpened()
                                .getNome()
                );
            }
        }

        return dto;
    }


    // ========================================================
    // UTILIZADOR AUTENTICADO
    // ========================================================

    private Employee getUtilizadorLogado() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (auth == null
                || !auth.isAuthenticated()) {

            throw new SecurityException(
                    "Sessão expirada ou utilizador não autenticado."
            );
        }

        Object principal =
                auth.getPrincipal();

        if (principal instanceof Employee) {
            return (Employee) principal;
        }

        throw new SecurityException(
                "Não foi possível identificar o funcionário autenticado."
        );
    }


    // ========================================================
    // VALIDAR INTEGRIDADE FINANCEIRA
    // ========================================================

    private void validarIntegridadeFinanceira(
            SaleRequestDTO dto,
            BigDecimal sub,
            BigDecimal tax,
            BigDecimal total,
            BigDecimal troco) {

        BigDecimal tol =
                new BigDecimal("0.05");


        if (dto.getSubtotal() == null
                || sub.subtract(
                        dto.getSubtotal()
                ).abs().compareTo(tol) > 0) {

            throw new RuntimeException(
                    "Erro: Subtotal divergente."
            );
        }


        if (dto.getTotalImposto() == null
                || tax.subtract(
                        dto.getTotalImposto()
                ).abs().compareTo(tol) > 0) {

            throw new RuntimeException(
                    "Erro: Imposto divergente."
            );
        }


        if (dto.getTotalGeral() == null
                || total.subtract(
                        dto.getTotalGeral()
                ).abs().compareTo(tol) > 0) {

            throw new RuntimeException(
                    "Erro: Total final divergente."
            );
        }


        if (dto.getTroco() == null
                || troco.subtract(
                        dto.getTroco()
                ).abs().compareTo(tol) > 0) {

            throw new RuntimeException(
                    "Erro: Cálculo de troco divergente."
            );
        }
    }
}