package com.ideias_inovadora.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.ProFormaDTO;
import com.ideias_inovadora.model.Customer;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.Product;
import com.ideias_inovadora.model.ProForma;
import com.ideias_inovadora.model.ProformaInvoiceItem;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.repository.EmployeeRepository;
import com.ideias_inovadora.repository.ProductRepository;
import com.ideias_inovadora.repository.ProFormaRepository;
import com.ideias_inovadora.repository.ShopRepository;

import jakarta.transaction.Transactional;

@Service
public class ProFormaService {

    @Autowired
    private ProFormaRepository proFormaRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerService customerService;

    // ============================================================
    // CRIAR PRO FORMA
    // ============================================================

    @Transactional
    public ProFormaDTO.Response criar(ProFormaDTO.Request dto) {

        // --------------------------------------------------------
        // VALIDAR
        // --------------------------------------------------------

        if (dto == null
                || dto.getItems() == null
                || dto.getItems().isEmpty()) {

            throw new IllegalArgumentException(
                    "A pro forma deve conter pelo menos um produto."
            );
        }

        // --------------------------------------------------------
        // UTILIZADOR AUTENTICADO
        // --------------------------------------------------------

        Employee employee =
                obterFuncionarioAutenticado();

        // --------------------------------------------------------
        // LOJA
        // --------------------------------------------------------

        Shop shop =
                resolverLojaDaOperacao(
                        dto.getShopId(),
                        employee
                );

        // --------------------------------------------------------
        // CLIENTE
        // --------------------------------------------------------

        /*
         * Usamos exactamente a mesma lógica da venda:
         *
         * customer     -> cliente registado / consumidor padrão
         * nomeInformal -> nome introduzido manualmente
         */

        var dadosCliente =
                customerService.processarClienteParaVenda(
                        dto.getCustomerId(),
                        dto.getCustomerName(),
                        dto.getCustomerNif()
                );

        Customer customer =
                (Customer) dadosCliente.get("customer");

        String nomeInformal =
                (String) dadosCliente.get("nomeInformal");

        // --------------------------------------------------------
        // GARANTIR NOME INFORMAL
        // --------------------------------------------------------

        /*
         * Se o frontend enviou um nome e o CustomerService
         * devolveu esse nome como informal, mantemos.
         *
         * Se por alguma razão o CustomerService não o devolveu,
         * mas temos um nome diferente de "Venda ao Público",
         * preservamos o nome como informal.
         */

        String nomeRecebido =
                dto.getCustomerName() != null
                        ? dto.getCustomerName().trim()
                        : "";

        boolean possuiNomeInformal =
                !nomeRecebido.isEmpty()
                        && !"Venda ao Público".equalsIgnoreCase(
                                nomeRecebido
                        );

        if (possuiNomeInformal
                && (nomeInformal == null
                        || nomeInformal.trim().isEmpty())) {

            nomeInformal =
                    nomeRecebido;
        }

        if (nomeInformal != null
                && !nomeInformal.trim().isEmpty()) {

            nomeInformal =
                    nomeInformal.trim();
        }

        // ========================================================
        // IMPORTANTE
        // ========================================================
        //
        // NÃO fazemos:
        //
        // customer = null;
        //
        // A venda não faz isso.
        // Mantemos customer + nomeInformal separados.
        //
        // ========================================================


        // --------------------------------------------------------
        // VALORES
        // --------------------------------------------------------

        BigDecimal subtotal =
                BigDecimal.ZERO;

        BigDecimal imposto =
                BigDecimal.ZERO;

        // --------------------------------------------------------
        // NOVA PRO FORMA
        // --------------------------------------------------------

        ProForma proForma =
                new ProForma();

        proForma.setShop(
                shop
        );

        proForma.setEmployee(
                employee
        );

        // Mesmo comportamento da venda
        proForma.setCustomer(
                customer
        );

        // Nome informal separado
        proForma.setNomeclienteInformal(
                nomeInformal
        );

        // --------------------------------------------------------
        // NÚMERO DA PRO FORMA
        // --------------------------------------------------------

        proForma.setNumeroproforma(
                gerarNumeroProForma(
                        shop.getId()
                )
        );

        proForma.setDataproforma(
                LocalDateTime.now()
        );

        proForma.setEstado(
                "ABERTA"
        );

        // --------------------------------------------------------
        // PRODUTOS
        // --------------------------------------------------------

        for (ProFormaDTO.Item input :
                dto.getItems()) {

            validarItem(input);

            Product product =
                    productRepository.findById(
                            input.getProductId()
                    ).orElseThrow(
                            () -> new RuntimeException(
                                    "Produto não encontrado ID: "
                                            + input.getProductId()
                            )
                    );

            BigDecimal taxRate =
                    input.getTaxRate() == null
                            ? BigDecimal.ZERO
                            : input.getTaxRate();

            BigDecimal itemSubtotal =
                    input.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            input.getQuantity()
                                    )
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );

            subtotal =
                    subtotal.add(
                            itemSubtotal
                    );

            imposto =
                    imposto.add(
                            itemSubtotal.multiply(
                                    taxRate
                            )
                    );

            ProformaInvoiceItem item =
                    new ProformaInvoiceItem();

            item.setProForma(
                    proForma
            );

            item.setProduct(
                    product
            );

            item.setQuantidade(
                    input.getQuantity()
            );

            item.setPrecoUnitario(
                    input.getUnitPrice()
            );

            item.setSubTotal(
                    itemSubtotal
            );

            item.setTaxaImposto(
                    taxRate
            );

            proForma.getItens().add(
                    item
            );
        }

        // --------------------------------------------------------
        // TOTAIS
        // --------------------------------------------------------

        subtotal =
                arredondar(
                        subtotal
                );

        imposto =
                arredondar(
                        imposto
                );

        BigDecimal desconto =
                calcularDesconto(
                        subtotal.add(imposto),
                        dto.getDiscountAmount(),
                        dto.getDiscountType()
                );

        proForma.setTotalGeral(
                subtotal
                        .add(imposto)
                        .subtract(desconto)
                        .max(BigDecimal.ZERO)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        )
        );

        // --------------------------------------------------------
        // GUARDAR
        // --------------------------------------------------------

        ProForma saved =
                proFormaRepository.save(
                        proForma
                );

        return toResponse(
                saved
        );
    }

    // ============================================================
    // GERAR NÚMERO DA PRO FORMA
    // ============================================================

    /**
     * Gera o próximo número da Pro Forma por loja e por ano.
     *
     * Exemplos:
     *
     * 1/2026
     * 2/2026
     * 3/2026
     *
     * Ao mudar para 2027:
     *
     * 1/2027
     * 2/2027
     */

    private String gerarNumeroProForma(
            Long shopId
    ) {

        int ano =
                Year.now().getValue();

        Integer ultimoNumero =
                proFormaRepository.findUltimoNumeroProForma(
                        shopId,
                        ano
                );

        int proximoNumero =
                (ultimoNumero == null
                        ? 0
                        : ultimoNumero) + 1;

        return proximoNumero + "/" + ano;
    }

    // ============================================================
    // OBTER
    // ============================================================

    @Transactional
    public ProFormaDTO.Response obter(
            Long id
    ) {

        ProForma proForma =
                proFormaRepository.findById(
                        id
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Pro forma não encontrada."
                        )
                );

        validarAcessoLoja(
                proForma
        );

        return toResponse(
                proForma
        );
    }

    // ============================================================
    // LISTAR
    // ============================================================

    @Transactional
    public List<ProFormaDTO.Response> listar(
            Long shopId
    ) {

        Employee logado =
                obterFuncionarioAutenticado();

        Long lojaObrigatoria =
                logado.getShop() == null
                        ? null
                        : logado.getShop().getId();

        Long lojaSolicitada =
                (shopId == null || shopId == 0)
                        ? null
                        : shopId;

        // --------------------------------------------------------
        // VALIDAR ACESSO À LOJA
        // --------------------------------------------------------

        if (lojaObrigatoria != null
                && lojaSolicitada != null
                && !lojaObrigatoria.equals(
                        lojaSolicitada
                )) {

            throw new SecurityException(
                    "O funcionário não tem acesso a esta loja."
            );
        }

        Long lojaEfetiva =
                lojaObrigatoria != null
                        ? lojaObrigatoria
                        : lojaSolicitada;

        // --------------------------------------------------------
        // CARREGAR PRO FORMAS
        // --------------------------------------------------------

        List<ProForma> propostas =
                lojaEfetiva == null
                        ? proFormaRepository
                                .findAllByOrderByDataproformaDesc()
                        : proFormaRepository
                                .findByShopIdOrderByDataproformaDesc(
                                        lojaEfetiva
                                );

        // --------------------------------------------------------
        // CONVERTER PARA DTO
        // --------------------------------------------------------

        List<ProFormaDTO.Response> result =
                new ArrayList<>(
                        propostas.size()
                );

        for (ProForma proposta :
                propostas) {

            result.add(
                    toResponse(
                            proposta
                    )
            );
        }

        return result;
    }

    // ============================================================
    // PREPARAR CONVERSÃO
    // ============================================================

    @Transactional
    public ProFormaDTO.Response prepararConversao(
            Long id
    ) {

        ProForma proForma =
                proFormaRepository.findById(
                        id
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Pro forma não encontrada."
                        )
                );

        validarAcessoLoja(
                proForma
        );

        if (!"ABERTA".equals(
                proForma.getEstado()
        )) {

            throw new IllegalStateException(
                    "Esta Pro Forma já foi convertida ou está cancelada."
            );
        }

        return toResponse(
                proForma
        );
    }

    // ============================================================
    // CANCELAR
    // ============================================================

    @Transactional
    public ProFormaDTO.Response cancelar(
            Long id
    ) {

        ProForma proposta =
                proFormaRepository.findById(
                        id
                ).orElseThrow(
                        () -> new RuntimeException(
                                "Pro forma não encontrada."
                        )
                );

        validarAcessoLoja(
                proposta
        );

        if ("CONVERTIDA".equals(
                proposta.getEstado()
        )) {

            throw new IllegalStateException(
                    "Uma pro forma convertida não pode ser cancelada."
            );
        }

        proposta.setEstado(
                "CANCELADA"
        );

        return toResponse(
                proFormaRepository.save(
                        proposta
                )
        );
    }

    // ============================================================
    // AUTENTICAÇÃO
    // ============================================================

    private Employee obterFuncionarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new SecurityException(
                    "Utilizador não autenticado."
            );
        }

        Object principal =
                authentication.getPrincipal();

        if (principal instanceof Employee employee) {
            return employee;
        }

        String identificador =
                principal instanceof UserDetails userDetails
                        ? userDetails.getUsername()
                        : authentication.getName();

        return employeeRepository
                .findByEmailOrUserUsername(
                        identificador,
                        identificador
                )
                .orElseThrow(
                        () -> new SecurityException(
                                "Funcionário autenticado não encontrado."
                        )
                );
    }

    // ============================================================
    // RESOLVER LOJA
    // ============================================================

    private Shop resolverLojaDaOperacao(
            Long shopId,
            Employee logado
    ) {

        if (logado.getShop() != null) {

            if (shopId != null
                    && shopId != 0
                    && !Long.valueOf(
                            logado.getShop().getId()
                    ).equals(shopId)) {

                throw new SecurityException(
                        "O funcionário não tem acesso a esta loja."
                );
            }

            return logado.getShop();
        }

        if (shopId == null
                || shopId == 0) {

            throw new IllegalArgumentException(
                    "A loja é obrigatória para este utilizador."
            );
        }

        return shopRepository
                .findById(
                        shopId
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "Loja não encontrada."
                        )
                );
    }

    // ============================================================
    // VALIDAR ACESSO
    // ============================================================

    private void validarAcessoLoja(
            ProForma proForma
    ) {

        Employee logado =
                obterFuncionarioAutenticado();

        if (logado.getShop() != null
                && (
                        proForma.getShop() == null
                                || !Long.valueOf(
                                        logado.getShop().getId()
                                ).equals(
                                        proForma.getShop().getId()
                                )
                )
        ) {

            throw new SecurityException(
                    "O funcionário não tem acesso a esta pro forma."
            );
        }
    }

    // ============================================================
    // VALIDAR ITEM
    // ============================================================

    private void validarItem(
            ProFormaDTO.Item input
    ) {

        if (input == null
                || input.getProductId() == null
                || input.getQuantity() <= 0
                || input.getUnitPrice() == null
                || input.getUnitPrice()
                        .compareTo(
                                BigDecimal.ZERO
                        ) < 0) {

            throw new IllegalArgumentException(
                    "Produto, quantidade e preço devem ser válidos."
            );
        }
    }

    // ============================================================
    // SUBTOTAL
    // ============================================================

    private BigDecimal calcularSubtotal(
            ProForma p
    ) {

        return arredondar(
                p.getItens()
                        .stream()
                        .map(
                                ProformaInvoiceItem::getSubTotal
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        )
        );
    }

    // ============================================================
    // IMPOSTO
    // ============================================================

    private BigDecimal calcularImposto(
            ProForma p
    ) {

        return arredondar(
                p.getItens()
                        .stream()
                        .map(
                                i ->
                                        i.getSubTotal()
                                                .multiply(
                                                        i.getTaxaImposto() == null
                                                                ? BigDecimal.ZERO
                                                                : i.getTaxaImposto()
                                                )
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        )
        );
    }

    // ============================================================
    // DESCONTO
    // ============================================================

    private BigDecimal calcularDesconto(
            BigDecimal base,
            BigDecimal valor,
            com.ideias_inovadora.model.DiscountType tipo
    ) {

        if (valor == null
                || valor.signum() <= 0) {

            return BigDecimal.ZERO;
        }

        if (tipo ==
                com.ideias_inovadora.model.DiscountType.PERCENTAGE) {

            return base
                    .multiply(valor)
                    .divide(
                            BigDecimal.valueOf(100),
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        return valor.min(
                base
        );
    }

    // ============================================================
    // ARREDONDAMENTO
    // ============================================================

    private BigDecimal arredondar(
            BigDecimal valor
    ) {

        return valor.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    // ============================================================
    // RESPONSE
    // ============================================================

    private ProFormaDTO.Response toResponse(
            ProForma p
    ) {

        BigDecimal subtotal =
                calcularSubtotal(
                        p
                );

        BigDecimal imposto =
                calcularImposto(
                        p
                );

        BigDecimal bruto =
                subtotal.add(
                        imposto
                );

        BigDecimal desconto =
                bruto
                        .subtract(
                                p.getTotalGeral()
                        )
                        .max(
                                BigDecimal.ZERO
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        ProFormaDTO.Response r =
                new ProFormaDTO.Response();

        // --------------------------------------------------------
        // DADOS PRINCIPAIS
        // --------------------------------------------------------

        r.setId(
                p.getId()
        );

        r.setNumeroProforma(
                p.getNumeroproforma()
        );

        r.setDataProforma(
                p.getDataproforma()
        );

        r.setShopId(
                p.getShop() == null
                        ? null
                        : p.getShop().getId()
        );

        r.setStatus(
                p.getEstado()
        );

        r.setSubtotal(
                subtotal
        );

        r.setTotalImposto(
                imposto
        );

        r.setDiscountValue(
                desconto
        );

        r.setTotalGeral(
                p.getTotalGeral()
        );

        // ========================================================
        // CLIENTE
        //
        // MESMA LÓGICA DO buildSaleResponse()
        // ========================================================

        String nomeClienteInformal =
                p.getNomeclienteInformal();

        String nomeCliente;

        if (nomeClienteInformal != null
                && !nomeClienteInformal.isBlank()) {

            // CLIENTE INFORMAL
            nomeCliente =
                    nomeClienteInformal.trim();

        } else if (p.getCustomer() != null) {

            // CLIENTE REGISTADO / CONSUMIDOR FINAL
            nomeCliente =
                    p.getCustomer().getNome();

        } else {

            // SEM CLIENTE
            nomeCliente =
                    "Venda ao Público";
        }

        r.setCustomerName(
                nomeCliente
        );

        // --------------------------------------------------------
        // CUSTOMER ID
        // --------------------------------------------------------

        if (p.getCustomer() != null) {

            r.setCustomerId(
                    p.getCustomer().getId()
            );

        } else {

            r.setCustomerId(
                    null
            );
        }

        // --------------------------------------------------------
        // NIF
        // --------------------------------------------------------

        if (p.getCustomer() != null) {

            r.setCustomerNif(
                    p.getCustomer()
                            .getNumeroContribuinte()
            );

        } else {

            r.setCustomerNif(
                    "999999999"
            );
        }

        // ========================================================
        // ITENS
        // ========================================================

        List<ProFormaDTO.ItemResponse> items =
                new ArrayList<>(
                        p.getItens().size()
                );

        for (ProformaInvoiceItem i :
                p.getItens()) {

            ProFormaDTO.ItemResponse x =
                    new ProFormaDTO.ItemResponse();

            x.setProductId(
                    i.getProduct().getId()
            );

            x.setProductName(
                    i.getProduct().getNome()
            );

            x.setQuantity(
                    i.getQuantidade()
            );

            x.setUnitPrice(
                    i.getPrecoUnitario()
            );

            x.setTaxRate(
                    i.getTaxaImposto()
            );

            x.setSubtotal(
                    i.getSubTotal()
            );

            items.add(
                    x
            );
        }

        r.setItems(
                items
        );

        return r;
    }
}