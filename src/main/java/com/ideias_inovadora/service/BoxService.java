package com.ideias_inovadora.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.BoxRequestDTO;
import com.ideias_inovadora.dto.BoxResponseDTO;
import com.ideias_inovadora.model.Box;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.model.StatusCaixa;
import com.ideias_inovadora.repository.BoxRepository;
import com.ideias_inovadora.repository.EmployeeRepository;
import com.ideias_inovadora.repository.ShopRepository;
import com.ideias_inovadora.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class BoxService {

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ShopRepository shopRepository;

    /**
     * ====================================================
     * ABRIR CAIXA
     * ====================================================
     *
     * A loja é determinada pelo shopId recebido no contexto
     * da operação.
     *
     * Isto permite que o mesmo Admin Master tenha uma caixa
     * diferente em cada loja.
     *
     * Exemplo:
     *
     * Admin 1 -> Loja A -> Caixa #10
     * Admin 1 -> Loja B -> Caixa #11
     */
    @Transactional
    @CacheEvict(value = "box", allEntries = true)
    public BoxResponseDTO create(
            Box box,
            Long shopId) {

        // ====================================================
        // VALIDAR LOJA
        // ====================================================

        if (shopId == null || shopId == 0) {
            throw new IllegalArgumentException(
                    "É necessário seleccionar uma loja para abrir o caixa."
            );
        }

        // ====================================================
        // PROCURAR LOJA
        // ====================================================

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Loja não encontrada: " + shopId
                        )
                );

        // ====================================================
        // ASSOCIAR LOJA
        // ====================================================

        box.setShop(shop);

        // ====================================================
        // DATA DE ABERTURA
        // ====================================================

        box.setDataAbertura(
                LocalDateTime.now()
        );

        // ====================================================
        // ESTADO INICIAL
        // ====================================================

        box.setStatusCaixa(
                StatusCaixa.ABERTO
        );

        // ====================================================
        // VALOR DO DIA
        // ====================================================

        if (box.getValorDia() == null) {
            box.setValorDia(
                    BigDecimal.ZERO
            );
        }

        // ====================================================
        // VALOR FINAL
        // ====================================================

        box.setValorFinal(null);

        // ====================================================
        // GUARDAR
        // ====================================================

        Box savedBox =
                boxRepository.save(box);

        // ====================================================
        // DEVOLVER DTO SEGURO
        // ====================================================

        return boxResponseDTO(
                savedBox
        );
    }

    /**
     * ====================================================
     * PROCURAR CAIXA ABERTO DA LOJA
     * ====================================================
     *
     * A pesquisa é feita por:
     *
     *    Loja + Estado ABERTO
     *
     * O employeeId continua na assinatura porque o Controller
     * ainda o envia, mas não é utilizado para identificar
     * a sessão.
     *
     * Isto é necessário para o Admin Master, pois o mesmo
     * funcionário pode abrir caixas em lojas diferentes.
     */
    public BoxResponseDTO findActiveSession(Long employeeId, Long shopId) {

        // Sem loja seleccionada não existe contexto operacional.
        if (shopId == null || shopId == 0) {
            return null;
        }

        // Procura caixas ABERTOS da loja seleccionada.
        List<Box> sessions =
                boxRepository.findActiveSessionsByShop(
                        shopId,
                        StatusCaixa.ABERTO
                );

        // Não existe caixa aberto nesta loja.
        if (sessions.isEmpty()) {
            return null;
        }

        // O Repository ordena pelo ID descendente,
        // portanto o primeiro é o mais recente.
        return boxResponseDTO(sessions.get(0));
    }

    /**
     * ====================================================
     * FECHAR CAIXA
     * ====================================================
     */
    @Transactional
    @CacheEvict(value = "box", allEntries = true)
    public BoxResponseDTO fecharCaixa(BoxRequestDTO dto) {

        // Validação básica do DTO.
        if (dto == null || dto.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Dados do fecho do caixa inválidos."
            );
        }

        // Procura o caixa.
        Box box = boxRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Caixa não encontrado."
                        )
                );

        // Não permite fechar um caixa que já esteja fechado.
        if (box.getStatusCaixa() == StatusCaixa.FECHADO) {
            throw new IllegalStateException(
                    "Este caixa já está fechado."
            );
        }

        // Valida o valor contado.
        if (dto.getValor() == null) {
            throw new IllegalArgumentException(
                    "O valor contado do caixa é obrigatório."
            );
        }

        // Valor total contado fisicamente na gaveta.
        BigDecimal valorTotalContado = dto.getValor();

        // Garante que o valor inicial nunca seja null.
        BigDecimal valorInicial =
                box.getValorInicial() != null
                        ? box.getValorInicial()
                        : BigDecimal.ZERO;

        // Calcula o total correspondente às vendas do dia.
        BigDecimal valorVendasDoDia =
                valorTotalContado.subtract(valorInicial);

        // Procurar o funcionário que está a efectuar o fecho.
        Employee closureEmployee =
                employeeRepository.findById(dto.getEmployeeId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Funcionário não encontrado."
                                )
                        );

        // Guarda quem fechou o caixa.
        box.setEmployeeClosure(closureEmployee);

        // Guarda o valor final contado.
        box.setValorFinal(valorTotalContado);

        // Guarda o valor das vendas calculado.
        box.setValorDia(valorVendasDoDia);

        // Guarda a data/hora do fecho.
        box.setDataFecho(LocalDateTime.now());

        // Muda o estado para FECHADO.
        box.setStatusCaixa(StatusCaixa.FECHADO);

        // Persiste as alterações.
        boxRepository.save(box);

        // Devolve o DTO seguro.
        return boxResponseDTO(box);
    }

    /**
     * ====================================================
     * LISTAR TODOS OS CAIXAS
     * ====================================================
     *
     * Usado principalmente para gestão administrativa.
     */
    public List<BoxResponseDTO> listarTodos() {

        return boxRepository.findAll()
                .stream()
                .map(this::boxResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * ====================================================
     * LISTAR CAIXAS DE UMA LOJA
     * ====================================================
     */
    public List<BoxResponseDTO> listarPorLoja(Long shopId) {

        // Sem loja não existe filtro válido.
        if (shopId == null || shopId == 0) {
            return List.of();
        }

        // Agora a pesquisa é feita directamente pelo Box.shop.
        return boxRepository
                .findAllByShopIdOrderByIdDesc(shopId)
                .stream()
                .map(this::boxResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * ====================================================
     * CONVERTER BOX PARA BoxResponseDTO
     * ====================================================
     *
     * A informação do operador e da loja é obtida
     * separadamente:
     *
     * employeeOpened -> quem abriu
     * shop           -> em que loja
     */
    private BoxResponseDTO boxResponseDTO(Box box) {

        BoxResponseDTO res = new BoxResponseDTO();

        // ID do caixa.
        res.setId(box.getId());

        // Data de abertura.
        res.setDataAbertura(box.getDataAbertura());

        // Data de fecho.
        res.setDataFecho(box.getDataFecho());

        // Valor inicial.
        res.setValorInicial(
                box.getValorInicial() != null
                        ? box.getValorInicial()
                        : BigDecimal.ZERO
        );

        // ====================================================
        // DADOS DA LOJA
        // ====================================================

        // A loja agora vem directamente do Box.
        if (box.getShop() != null) {

            Shop shop = box.getShop();

            // Nome da loja.
            res.setShopNome(shop.getNome());

            // NIF da loja.
            res.setShopNif(shop.getNumeroContribuite());

            // Endereço da loja.
            if (shop.getLocation() != null) {

                res.setShopEndereco(
                        shop.getLocation().getNome()
                );

            } else {

                res.setShopEndereco(
                        "Endereço não configurado"
                );
            }
        } else {

            // Segurança para caixas antigos
            // que ainda não tenham shop associado.
            res.setShopNome("Loja não configurada");
            res.setShopNif("---");
            res.setShopEndereco("Endereço não configurado");
        }

        // ====================================================
        // FUNCIONÁRIO QUE ABRIU
        // ====================================================

        if (box.getEmployeeOpened() != null) {

            res.setNomeOperadorAbertura(
                    box.getEmployeeOpened().getNome()
            );

        } else {

            res.setNomeOperadorAbertura("N/A");
        }

        // ====================================================
        // FUNCIONÁRIO QUE FECHOU
        // ====================================================

        if (box.getEmployeeClosure() != null) {

            res.setNomeOperadorFecho(
                    box.getEmployeeClosure().getNome()
            );

        } else {

            res.setNomeOperadorFecho(
                    "SESSÃO ACTIVA"
            );
        }

        // ====================================================
        // VENDAS DO DIA
        // ====================================================

        BigDecimal vendasCalculadas =
                transactionRepository.sumVendasByBoxId(
                        box.getId()
                );

        // Evita null.
        if (vendasCalculadas == null) {
            vendasCalculadas = BigDecimal.ZERO;
        }

        // Mostra o valor real das vendas.
        res.setValorDia(vendasCalculadas);

        // ====================================================
        // VALOR FINAL DA GAVETA
        // ====================================================

        if (box.getValorFinal() != null) {

            // Caixa já fechado:
            // utiliza o valor contado e gravado.
            res.setValorFinal(
                    box.getValorFinal()
            );

        } else {

            // Caixa ainda aberto:
            // mostra a previsão:
            //
            // Fundo inicial + vendas.
            res.setValorFinal(
                    res.getValorInicial()
                            .add(vendasCalculadas)
            );
        }

        // ====================================================
        // ESTADO
        // ====================================================

        if (box.getStatusCaixa() != null) {

            res.setStatusCaixa(
                    box.getStatusCaixa().getDescricao()
            );

        } else {

            res.setStatusCaixa(
                    "DESCONHECIDO"
            );
        }

        return res;
    }
    
    
}