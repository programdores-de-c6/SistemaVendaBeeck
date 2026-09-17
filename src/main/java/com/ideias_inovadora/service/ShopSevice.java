package com.ideias_inovadora.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.ideias_inovadora.dto.ShopDTO;
import com.ideias_inovadora.dto.ShopDTOs;
import com.ideias_inovadora.erros.OperationNotAllowedException;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.FileCategory;
import com.ideias_inovadora.model.Files;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.model.Location;
import com.ideias_inovadora.repository.LocationRepository;
import com.ideias_inovadora.repository.ShopRepository;
import jakarta.transaction.Transactional;

/**
 * SERVIÇO DE GESTÃO DE UNIDADES DE NEGÓCIO (SHOP)
 * Responsável por configurar e gerir a Gráfica, Livraria e Armazéns.
 */
@Service
public class ShopSevice {

    @Autowired
    private ShopRepository shopRepository; // Repositório para operações na tabela Shop
    @Autowired
    private LocationRepository locationRepository; // Repositório para buscar localidades
    @Autowired
    private FilesService filesService; // Serviço para manipular arquivos e fotos

    /**
     * Regista uma nova unidade no sistema (Gráfica ou Loja).
     */
    @Transactional(rollbackOn = Exception.class)
    @CacheEvict(value = "shop", allEntries = true) // Limpa cache para manter dados frescos
    public String create(ShopDTO shopDTO) throws Exception {
        
        // Valida se o nome ou NIF já estão a ser usados por outra unidade
        if (existsShop(shopDTO.getNome(), shopDTO.getNumeroContribuite())) {
            throw new OperationNotAllowedException("Erro: Nome da Loja ou NIF já registado.");
        }

        // Obtém o Administrador que está a realizar o cadastro (vido do Token)
        Employee creator = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Processa o logótipo da loja se o ficheiro foi enviado
        Files file = null;
        if (shopDTO.getFile() != null && !shopDTO.getFile().isEmpty()) {
            file = filesService.salvarArquivoFoto(shopDTO.getFile(), FileCategory.SHOP);
        }

        // Busca a localidade obrigatória para fins fiscais
       // Location location = locationRepository.findById(shopDTO.getIdlocation())
         //       .orElseThrow(() -> new OperationNotAllowedException("Localidade inválida."));

        // Criação e população da entidade persistente
        Shop shop = new Shop();
        shop.setNome(shopDTO.getNome().toUpperCase()); // Padroniza para maiúsculas
        shop.setEmail(shopDTO.getEmail());
        shop.setContacto(shopDTO.getContacto());
        shop.setShopType(shopDTO.getShopType()); // Define se é LOJA ou GRAFICA
        shop.setCaixaPostal(shopDTO.getCaixaPostal());
        shop.setNumeroContribuite(shopDTO.getNumeroContribuite());
        shop.setLocation(shopDTO.getLocation());
        shop.setFiles(file);
        shop.setEmployee(creator); // Regista quem criou a unidade
        shop.setDatacriacao(LocalDateTime.now());

        shopRepository.save(shop); // Grava na base de dados
        return "Operação concluída com sucesso";
    }

    /**
     * Atualiza os dados de uma unidade existente.
     */
    @Transactional
    @CacheEvict(value = "shop", allEntries = true)
    public String update(Shop shop) {
        // Verifica se a loja existe antes de tentar atualizar
        if (!shopRepository.existsById(shop.getId())) {
            throw new OperationNotAllowedException("Loja não encontrada para atualização.");
        }
        shop.setDataAtualizacao(LocalDateTime.now()); // Regista momento da alteração
        shopRepository.saveAndFlush(shop); // Força a gravação imediata
        return "Operação realizada com Sucesso";
    }

    /**
     * Lista completa de lojas com detalhes (Visão do Admin Master).
     */
    @Cacheable(value = "shop", key = "'list'")
    public List<ShopDTO> list() {
        return shopRepository.findAll().stream()
                .map(this::mapToShopDTO) // Converte Entidade para DTO de resposta
                .collect(Collectors.toList());
    }

    /**
     * Lista compacta (apenas ID e Nome) para preencher selects no Frontend.
     * Útil para o cadastro de funcionários.
     */
    @Cacheable(value = "shop", key = "'listComboBox'")
    public List<ShopDTOs> listCompobox() {
        return shopRepository.findAll().stream()
                .map(this::mapToShopDTOs) // Mapeia para o DTO simplificado
                .collect(Collectors.toList());
    }

    /**
     * Pesquisa lojas por nome ou NIF.
     */
    public List<ShopDTO> search(String nome) {
        return shopRepository.busca(nome).stream()
                .map(this::mapToShopDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca os dados de uma única loja pelo ID.
     */
    public ShopDTO listid(Long id) {
        return shopRepository.findById(id)
                .map(this::mapToShopDTO)
                .orElseThrow(() -> new OperationNotAllowedException("Unidade não encontrada."));
    }

    /**
     * ✅ REINTEGRADO: Método usado no fluxo de Login/Fetch.
     * Pega o DTO enviado, busca o logótipo e devolve o DTO com a URL Base64.
     */
    public ShopDTO enviarfile(ShopDTO shopDTO) {
        Shop shop = getShopID(shopDTO.getId()); // Busca a entidade
        shopDTO.setLogoUrl(buscarLogoBase64(shop)); // Injeta a imagem convertida
        return shopDTO;
    }

    /**
     * Converte o arquivo binário do logótipo em String Base64 para o navegador.
     */
    public String buscarLogoBase64(Shop shop) {
        if (shop.getFiles() != null) {
            try {
                byte[] bytes = filesService.buscarArquivo(shop.getFiles().getId());
                // Retorna o prefixo de imagem para que o React exiba diretamente no <img>
                return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
            } catch (Exception e) { return null; }
        }
        return null;
    }

    /**
     * Atualiza apenas o logótipo de uma unidade.
     */
    @Transactional
    @CacheEvict(value = "shop", allEntries = true)
    public String upload_logo(ShopDTO shopDto) throws Exception {
        // 1. Verificação inicial: existe um arquivo?
        if (shopDto.getFile() == null || shopDto.getFile().isEmpty()) {
            return "Nenhum arquivo enviado.";
        }

        // 2. Busca a loja no banco
        Shop shop = getShopID(shopDto.getId());
        String resultado;

        // 3. CENÁRIO A: Loja ainda não tem foto (Primeira vez)
        if (shop.getFiles() == null) {
            // Salva o arquivo e recebe a entidade Files de volta
            Files novoArquivo = filesService.salvarArquivoFoto(shopDto.getFile(), FileCategory.SHOP);
            
            // Faz o vínculo na tabela Shop (file_fk)
            shop.setFiles(novoArquivo);
            shopRepository.saveAndFlush(shop);
            
            resultado = "Operação realizada com sucesso";
        } 
        // 4. CENÁRIO B: Loja já tem foto (Substituição)
        else {
            // O seu método atualizarArquivoFoto já faz o update no banco e retorna a String
            resultado = filesService.atualizarArquivoFoto(shopDto.getFile(), shop, FileCategory.SHOP);
        }

        return resultado;
    }
    
    /**
     * Método auxiliar para salvar uma loja sem exigir auditoria de utilizador logado.
     * Útil para o Setup Inicial.
     */
    @Transactional
    public Shop saveInternalShop(ShopDTO dto, Files logo, Employee creator) {
        Shop shop = new Shop();
        shop.setNome(dto.getNome().toUpperCase());
        shop.setEmail(dto.getEmail());
        shop.setContacto(dto.getContacto());
        shop.setShopType(dto.getShopType());
        shop.setNumeroContribuite(dto.getNumeroContribuite());
        shop.setLocation(locationRepository.findById(dto.getIdlocation()).get());
        shop.setFiles(logo);
        shop.setEmployee(creator); // Pode ser null no primeiro passo do setup
        shop.setDatacriacao(LocalDateTime.now());
        return shopRepository.save(shop);
    }
    
    
    // --- MÉTODOS AUXILIARES (PRIVADOS) ---

    private boolean existsShop(String nome, String nif) {
        return shopRepository.existsByNome(nome) || shopRepository.existsByNumeroContribuite(nif);
    }

    private Shop getShopID(long id) {
        return shopRepository.findById(id)
                .orElseThrow(() -> new OperationNotAllowedException("Unidade ID " + id + " não localizada."));
    }

    private ShopDTOs mapToShopDTOs(Shop shop) {
        ShopDTOs dto = new ShopDTOs();
        dto.setId(shop.getId());
        dto.setNome(shop.getNome());
        return dto;
    }

    private ShopDTO mapToShopDTO(Shop shop) {
        ShopDTO dto = new ShopDTO();
        dto.setId(shop.getId());
        dto.setNome(shop.getNome());
        dto.setNumeroContribuite(shop.getNumeroContribuite());
        dto.setContacto(shop.getContacto());
        dto.setEmail(shop.getEmail());
        dto.setCaixaPostal(shop.getCaixaPostal());
        dto.setDatacriacao(shop.getDatacriacao());
        dto.setDataAtualizacao(shop.getDataAtualizacao());
        dto.setShopType(shop.getShopType());
        dto.setShopTypes(shop.getShopType().getDescricao());

        if (shop.getLocation() != null) {
            dto.setIdlocation(shop.getLocation().getId());
            dto.setNomelocation(shop.getLocation().getNome());
        }

        dto.setLogoUrl(buscarLogoBase64(shop));
        return dto;
    }
}