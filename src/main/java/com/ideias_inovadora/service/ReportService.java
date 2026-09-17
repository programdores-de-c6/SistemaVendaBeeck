package com.ideias_inovadora.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideias_inovadora.dto.ReportDTO;
import com.ideias_inovadora.model.Box;
import com.ideias_inovadora.model.Customer;
import com.ideias_inovadora.model.Sale;
import com.ideias_inovadora.model.SaleItem;
import com.ideias_inovadora.model.Stock;
import com.ideias_inovadora.model.StockMovement;
import com.ideias_inovadora.model.Transaction;
import com.ideias_inovadora.repository.BoxRepository;
import com.ideias_inovadora.repository.ReportRepository;
import com.ideias_inovadora.repository.SaleItemRepository;
import com.ideias_inovadora.repository.StockMovementRepository;
import com.ideias_inovadora.repository.StockRepository;

import jakarta.transaction.Transactional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private BoxRepository boxRepository;


    // ====================================================
    // 1. VENDAS POR PERÍODO
    // ====================================================

    @Transactional
    public ReportDTO.SalesPeriodReport vendasPorPeriodo(
            ReportDTO.Filter filter) {

        validarDatas(filter);

        List<Sale> vendas =
                buscarVendas(filter);


        ReportDTO.SalesPeriodReport report =
                new ReportDTO.SalesPeriodReport();

        ReportDTO.Summary summary =
                new ReportDTO.Summary();


        List<ReportDTO.SalesRow> rows =
                new ArrayList<>();


        Map<LocalDate, ReportDTO.DailySales>
                dailyMap =
                new LinkedHashMap<>();

        Map<String, ReportDTO.WeeklySales>
                weeklyMap =
                new LinkedHashMap<>();

        Map<String, ReportDTO.MonthlySales>
                monthlyMap =
                new LinkedHashMap<>();


        BigDecimal subtotalTotal =
                BigDecimal.ZERO;

        BigDecimal impostoTotal =
                BigDecimal.ZERO;

        BigDecimal descontoTotal =
                BigDecimal.ZERO;

        BigDecimal totalTotal =
                BigDecimal.ZERO;

        long quantidadeTotalItens =
                0;

        long vendasValidas =
                0;


        for (Sale sale : vendas) {

            if (sale == null) {
                continue;
            }


            Transaction transaction =
                    sale.getTransaction();

            if (transaction == null) {
                continue;
            }


            BigDecimal subtotal =
                    valor(transaction.getSubTotal());

            BigDecimal imposto =
                    valor(transaction.getTotalImposto());

            BigDecimal desconto =
                    valor(transaction.getDiscountValue());

            BigDecimal total =
                    valor(transaction.getTotalGeral());


            subtotalTotal =
                    subtotalTotal.add(subtotal);

            impostoTotal =
                    impostoTotal.add(imposto);

            descontoTotal =
                    descontoTotal.add(desconto);

            totalTotal =
                    totalTotal.add(total);


            List<SaleItem> itens =
                    saleItemRepository.findBySale_Id(
                            sale.getId()
                    );


            if (itens == null) {
                itens = List.of();
            }


            int quantidadeItens =
                    itens.stream()
                            .filter(Objects::nonNull)
                            .mapToInt(SaleItem::getQuantidade)
                            .sum();


            quantidadeTotalItens +=
                    quantidadeItens;

            vendasValidas++;


            // ==================================================
            // CAIXA
            // ==================================================

            Box box =
                    transaction.getBox();

            Long boxId =
                    box != null
                            ? box.getId()
                            : null;


            String operador =
                    box != null
                            && box.getEmployeeOpened() != null
                            ? box.getEmployeeOpened().getNome()
                            : "N/D";


            // ==================================================
            // CLIENTE
            // ==================================================

            String cliente =
                    transaction.getCustomer() != null
                            ? transaction.getCustomer().getNome()
                            : sale.getNomeclienteInformal();


            if (cliente == null
                    || cliente.isBlank()) {

                cliente =
                        "Venda ao Público";
            }


            String nifCliente =
                    transaction.getCustomer() != null
                            ? transaction.getCustomer()
                                    .getNumeroContribuinte()
                            : null;


            // ==================================================
            // PAGAMENTO
            // ==================================================

            String metodoPagamento =
                    transaction.getPaymentMethod() != null
                            ? transaction.getPaymentMethod().toString()
                            : null;


            // ==================================================
            // LINHA
            // ==================================================

            ReportDTO.SalesRow row =
                    new ReportDTO.SalesRow();

            row.setSaleId(
                    sale.getId()
            );

            row.setNumeroFactura(
                    sale.getNumeroFactura()
            );

            row.setSerie(
                    sale.getSeries() != null
                            ? sale.getSeries().getSerie()
                            : null
            );

            row.setDataVenda(
                    sale.getDatavenda()
            );

            row.setCliente(
                    cliente
            );

            row.setNifCliente(
                    nifCliente
            );

            row.setBoxId(
                    boxId
            );

            row.setOperador(
                    operador
            );

            row.setQuantidadeItens(
                    quantidadeItens
            );

            row.setSubtotal(
                    arredondar(subtotal)
            );

            row.setImposto(
                    arredondar(imposto)
            );

            row.setDesconto(
                    arredondar(desconto)
            );

            row.setTotal(
                    arredondar(total)
            );

            row.setMetodoPagamento(
                    metodoPagamento
            );


            rows.add(row);


            // ==================================================
            // DIÁRIO
            // ==================================================

            if (sale.getDatavenda() != null) {

                LocalDate data =
                        sale.getDatavenda()
                                .toLocalDate();


                ReportDTO.DailySales daily =
                        dailyMap.computeIfAbsent(
                                data,
                                key -> {

                                    ReportDTO.DailySales d =
                                            new ReportDTO.DailySales();

                                    d.setData(key);
                                    d.setVendas(0);
                                    d.setTotal(
                                            BigDecimal.ZERO
                                    );

                                    return d;
                                }
                        );


                daily.setVendas(
                        daily.getVendas() + 1
                );


                daily.setTotal(
                        daily.getTotal()
                                .add(total)
                );
            }


            // ==================================================
            // SEMANAL
            // ==================================================

            if (sale.getDatavenda() != null) {

                LocalDate data =
                        sale.getDatavenda()
                                .toLocalDate();


                WeekFields weekFields =
                        WeekFields.ISO;


                int week =
                        data.get(
                                weekFields.weekOfWeekBasedYear()
                        );


                int weekYear =
                        data.get(
                                weekFields.weekBasedYear()
                        );


                String key =
                        String.format(
                                "%04d-W%02d",
                                weekYear,
                                week
                        );


                ReportDTO.WeeklySales weekly =
                        weeklyMap.computeIfAbsent(
                                key,
                                k -> {

                                    ReportDTO.WeeklySales w =
                                            new ReportDTO.WeeklySales();

                                    LocalDate inicio =
                                            data.with(
                                                    DayOfWeek.MONDAY
                                            );

                                    LocalDate fim =
                                            inicio.plusDays(6);

                                    w.setSemana(k);
                                    w.setInicio(inicio);
                                    w.setFim(fim);
                                    w.setVendas(0);
                                    w.setTotal(
                                            BigDecimal.ZERO
                                    );

                                    return w;
                                }
                        );


                weekly.setVendas(
                        weekly.getVendas() + 1
                );

                weekly.setTotal(
                        weekly.getTotal()
                                .add(total)
                );
            }


            // ==================================================
            // MENSAL
            // ==================================================

            if (sale.getDatavenda() != null) {

                LocalDate data =
                        sale.getDatavenda()
                                .toLocalDate();


                String key =
                        String.format(
                                "%04d-%02d",
                                data.getYear(),
                                data.getMonthValue()
                        );


                ReportDTO.MonthlySales monthly =
                        monthlyMap.computeIfAbsent(
                                key,
                                k -> {

                                    ReportDTO.MonthlySales m =
                                            new ReportDTO.MonthlySales();

                                    m.setAno(
                                            data.getYear()
                                    );

                                    m.setMes(
                                            data.getMonthValue()
                                    );

                                    m.setVendas(0);
                                    m.setTotal(
                                            BigDecimal.ZERO
                                    );

                                    return m;
                                }
                        );


                monthly.setVendas(
                        monthly.getVendas() + 1
                );

                monthly.setTotal(
                        monthly.getTotal()
                                .add(total)
                );
            }
        }


        rows.sort(
                Comparator.comparing(
                        ReportDTO.SalesRow::getDataVenda,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );


        List<ReportDTO.DailySales> daily =
                new ArrayList<>(
                        dailyMap.values()
                );


        daily.sort(
                Comparator.comparing(
                        ReportDTO.DailySales::getData
                )
        );


        List<ReportDTO.WeeklySales> weekly =
                new ArrayList<>(
                        weeklyMap.values()
                );


        weekly.sort(
                Comparator.comparing(
                        ReportDTO.WeeklySales::getInicio
                )
        );


        List<ReportDTO.MonthlySales> monthly =
                new ArrayList<>(
                        monthlyMap.values()
                );


        monthly.sort(
                Comparator.comparing(
                        ReportDTO.MonthlySales::getAno
                ).thenComparing(
                        ReportDTO.MonthlySales::getMes
                )
        );


        summary.setSalesCount(
                vendasValidas
        );

        summary.setItemsCount(
                quantidadeTotalItens
        );

        summary.setSubtotal(
                arredondar(subtotalTotal)
        );

        summary.setTax(
                arredondar(impostoTotal)
        );

        summary.setDiscount(
                arredondar(descontoTotal)
        );

        summary.setTotal(
                arredondar(totalTotal)
        );


        summary.setAverageTicket(
                vendasValidas == 0
                        ? BigDecimal.ZERO
                        : arredondar(
                                totalTotal.divide(
                                        BigDecimal.valueOf(
                                                vendasValidas
                                        ),
                                        2,
                                        RoundingMode.HALF_UP
                                )
                        )
        );


        report.setSummary(
                summary
        );

        report.setDaily(
                daily
        );

        report.setWeekly(
                weekly
        );

        report.setMonthly(
                monthly
        );

        report.setRows(
                rows
        );


        return report;
    }


    // ====================================================
    // 2. RESUMO
    // ====================================================

    @Transactional
    public ReportDTO.Summary resumo(
            ReportDTO.Filter filter) {

        ReportDTO.SalesPeriodReport report =
                vendasPorPeriodo(filter);

        return report.getSummary();
    }


    // ====================================================
    // 3. PAGAMENTOS
    // ====================================================

    @Transactional
    public ReportDTO.PaymentReport pagamentos(
            ReportDTO.Filter filter) {

        validarDatas(filter);

        List<Sale> vendas =
                buscarVendas(filter);


        Map<String, ReportDTO.PaymentRow> map =
                new LinkedHashMap<>();


        BigDecimal totalGeral =
                BigDecimal.ZERO;

        long vendasValidas =
                0;


        for (Sale sale : vendas) {

            if (sale == null
                    || sale.getTransaction() == null) {

                continue;
            }


            Transaction transaction =
                    sale.getTransaction();


            String metodo =
                    transaction.getPaymentMethod() != null
                            ? transaction.getPaymentMethod().getDescricao()
                            : "NÃO INFORMADO";


            BigDecimal total =
                    valor(
                            transaction.getTotalGeral()
                    );


            ReportDTO.PaymentRow row =
                    map.computeIfAbsent(
                            metodo,
                            key -> {

                                ReportDTO.PaymentRow r =
                                        new ReportDTO.PaymentRow();

                                r.setMetodo(key);
                                r.setVendas(0);
                                r.setTotal(
                                        BigDecimal.ZERO
                                );

                                return r;
                            }
                    );


            row.setVendas(
                    row.getVendas() + 1
            );


            row.setTotal(
                    row.getTotal()
                            .add(total)
            );


            totalGeral =
                    totalGeral.add(total);

            vendasValidas++;
        }


        List<ReportDTO.PaymentRow> rows =
                new ArrayList<>(
                        map.values()
                );


        rows.sort(
                Comparator.comparing(
                        ReportDTO.PaymentRow::getTotal
                ).reversed()
        );


        for (ReportDTO.PaymentRow row : rows) {

            BigDecimal percentagem =
                    totalGeral.signum() == 0
                            ? BigDecimal.ZERO
                            : row.getTotal()
                                    .multiply(
                                            BigDecimal.valueOf(100)
                                    )
                                    .divide(
                                            totalGeral,
                                            2,
                                            RoundingMode.HALF_UP
                                    );


            row.setTotal(
                    arredondar(
                            row.getTotal()
                    )
            );

            row.setPercentagem(
                    percentagem
            );
        }


        ReportDTO.PaymentSummary summary =
                new ReportDTO.PaymentSummary();


        summary.setSalesCount(
                vendasValidas
        );

        summary.setTotal(
                arredondar(totalGeral)
        );

        summary.setAverageTicket(
                vendasValidas == 0
                        ? BigDecimal.ZERO
                        : arredondar(
                                totalGeral.divide(
                                        BigDecimal.valueOf(
                                                vendasValidas
                                        ),
                                        2,
                                        RoundingMode.HALF_UP
                                )
                        )
        );


        summary.setMainMethod(
                rows.isEmpty()
                        ? null
                        : rows.get(0).getMetodo()
        );


        ReportDTO.PaymentReport report =
                new ReportDTO.PaymentReport();

        report.setSummary(
                summary
        );

        report.setRows(
                rows
        );


        return report;
    }


    // ====================================================
    // 4. PRODUTOS MAIS VENDIDOS
    // ====================================================

    @Transactional
    public ReportDTO.TopProductsReport produtosMaisVendidos(
            ReportDTO.Filter filter) {

        validarDatas(filter);

        List<Sale> vendas =
                buscarVendas(filter);


        Map<Long, ReportDTO.TopProductRow> map =
                new LinkedHashMap<>();


        long itemsCount =
                0;

        BigDecimal subtotalTotal =
                BigDecimal.ZERO;

        BigDecimal totalTotal =
                BigDecimal.ZERO;


        for (Sale sale : vendas) {

            if (sale == null) {
                continue;
            }


            Transaction transaction =
                    sale.getTransaction();

            if (transaction == null) {
                continue;
            }


            List<SaleItem> itens =
                    saleItemRepository.findBySale_Id(
                            sale.getId()
                    );


            if (itens == null) {
                continue;
            }


            for (SaleItem item : itens) {

                if (item == null
                        || item.getProduct() == null) {

                    continue;
                }


                Long productId =
                        item.getProduct().getId();


                ReportDTO.TopProductRow row =
                        map.computeIfAbsent(
                                productId,
                                key -> {

                                    ReportDTO.TopProductRow r =
                                            new ReportDTO.TopProductRow();

                                    r.setId(
                                            item.getProduct().getId()
                                    );

                                    r.setNome(
                                            item.getProduct().getNome()
                                    );

                                    r.setQuantidade(0);
                                    r.setSubtotal(
                                            BigDecimal.ZERO
                                    );

                                    r.setTotal(
                                            BigDecimal.ZERO
                                    );

                                    return r;
                                }
                        );


                int quantity =
                        item.getQuantidade();


                BigDecimal subtotal =
                        valor(
                                item.getSubTotal()
                        );


                BigDecimal itemTotal =
                        subtotal;


                row.setQuantidade(
                        row.getQuantidade()
                                + quantity
                );


                row.setSubtotal(
                        row.getSubtotal()
                                .add(subtotal)
                );


                row.setTotal(
                        row.getTotal()
                                .add(itemTotal)
                );


                itemsCount +=
                        quantity;

                subtotalTotal =
                        subtotalTotal.add(
                                subtotal
                        );

                totalTotal =
                        totalTotal.add(
                                itemTotal
                        );
            }
        }


        List<ReportDTO.TopProductRow> rows =
                new ArrayList<>(
                        map.values()
                );


        rows.forEach(row -> {

            row.setSubtotal(
                    arredondar(
                            row.getSubtotal()
                    )
            );

            row.setTotal(
                    arredondar(
                            row.getTotal()
                    )
            );
        });


        rows.sort(
                Comparator.comparing(
                        ReportDTO.TopProductRow::getQuantidade
                ).reversed()
        );


        ReportDTO.TopProductsSummary summary =
                new ReportDTO.TopProductsSummary();


        summary.setProductsCount(
                rows.size()
        );

        summary.setItemsCount(
                itemsCount
        );

        summary.setSubtotal(
                arredondar(
                        subtotalTotal
                )
        );

        summary.setTotal(
                arredondar(
                        totalTotal
                )
        );


        ReportDTO.TopProductsReport report =
                new ReportDTO.TopProductsReport();


        report.setSummary(
                summary
        );

        report.setRows(
                rows
        );


        return report;
    }


    // ====================================================
    // 5. STOCK ACTUAL
    // ====================================================

    @Transactional
    public ReportDTO.StockReport stockAtual(
            ReportDTO.Filter filter) {

        List<Stock> stocks;


        if (filter != null
                && filter.getShopId() != null
                && filter.getShopId() > 0) {

            stocks =
                    stockRepository.findByShopId(
                            filter.getShopId()
                    );

        } else {

            stocks =
                    stockRepository.findAll();
        }


        List<ReportDTO.StockRow> rows =
                new ArrayList<>();


        BigDecimal valorInventario =
                BigDecimal.ZERO;

        long stockBaixo =
                0;

        long semStock =
                0;


        for (Stock stock : stocks) {

            if (stock == null
                    || stock.getProduct() == null) {

                continue;
            }


            if (
                    filter != null
                    && filter.getProductId() != null
                    && filter.getProductId() > 0
                    && stock.getProduct().getId()
                            != filter.getProductId()
            ) {

                continue;
            }


            int quantidade =
                    stock.getQuantidade();


            int minimo =
                    stock.getQuantidadeMinima();


            BigDecimal preco =
                    valor(
                            stock.getPrecoUnitario()
                    );


            BigDecimal valorTotal =
                    preco.multiply(
                            BigDecimal.valueOf(
                                    quantidade
                            )
                    );


            boolean sem =
                    quantidade == 0;


            boolean baixo =
                    quantidade <= minimo;


            if (baixo) {
                stockBaixo++;
            }

            if (sem) {
                semStock++;
            }


            valorInventario =
                    valorInventario.add(
                            valorTotal
                    );


            ReportDTO.StockRow row =
                    new ReportDTO.StockRow();


            row.setProductId(
                    stock.getProduct().getId()
            );

            row.setProduto(
                    stock.getProduct().getNome()
            );

            row.setCodigoBarra(
                    stock.getProduct().getCodigobarra()
            );

            row.setQuantidade(
                    quantidade
            );

            row.setMinimo(
                    minimo
            );

            row.setPreco(
                    arredondar(preco)
            );

            row.setValorInventario(
                    arredondar(valorTotal)
            );

            row.setBaixo(
                    baixo
            );

            row.setSemStock(
                    sem
            );


            rows.add(row);
        }


        rows.sort(
                Comparator.comparing(
                        ReportDTO.StockRow::getProduto,
                        Comparator.nullsLast(
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
        );


        ReportDTO.StockSummary summary =
                new ReportDTO.StockSummary();


        summary.setProductsCount(
                rows.size()
        );

        summary.setStockBaixo(
                stockBaixo
        );

        summary.setSemStock(
                semStock
        );

        summary.setValorInventario(
                arredondar(
                        valorInventario
                )
        );


        ReportDTO.StockReport report =
                new ReportDTO.StockReport();


        report.setSummary(
                summary
        );

        report.setRows(
                rows
        );


        return report;
    }


    // ====================================================
    // 6. MOVIMENTOS DE STOCK
    // ====================================================

 // ====================================================
 // 6. MOVIMENTOS DE STOCK
 // ====================================================

 @Transactional
 public ReportDTO.StockMovementReport movimentosStock(
         ReportDTO.Filter filter) {

     validarDatas(filter);

     // ====================================================
     // PREPARAR PERÍODO
     // ====================================================

     LocalDateTime from =
             filter != null
                     && filter.getFrom() != null
                     ? filter.getFrom().atStartOfDay()
                     : null;

     LocalDateTime to =
             filter != null
                     && filter.getTo() != null
                     ? filter.getTo().plusDays(1).atStartOfDay()
                     : null;


     // ====================================================
     // BUSCAR MOVIMENTOS
     // ====================================================

     List<StockMovement> movements;


     boolean hasShop =
             filter != null
                     && filter.getShopId() != null
                     && filter.getShopId() > 0;


     if (hasShop) {

         if (from != null && to != null) {

             movements =
                     stockMovementRepository.findByShopAndDateRange(
                             filter.getShopId(),
                             from,
                             to
                     );

         } else {

             movements =
                     stockMovementRepository.findAllByShop(
                             filter.getShopId()
                     );
         }

     } else {

         if (from != null && to != null) {

             movements =
                     stockMovementRepository.findByDateRange(
                             from,
                             to
                     );

         } else {

             movements =
                     stockMovementRepository.findAllGlobal();
         }
     }


     // ====================================================
     // PROTEGER CONTRA NULL
     // ====================================================

     if (movements == null) {
         movements = new ArrayList<>();
     }


     // ====================================================
     // MONTAR RESULTADO
     // ====================================================

     List<ReportDTO.StockMovementRow> rows =
             new ArrayList<>();


     long totalQuantity = 0;


     for (StockMovement movement : movements) {

         if (movement == null) {
             continue;
         }


         // ==================================================
         // FILTRO POR PRODUTO
         // ==================================================

         if (
                 filter != null
                 && filter.getProductId() != null
                 && filter.getProductId() > 0
                 && (
                     movement.getProduct() == null
                     || movement.getProduct().getId()
                             != filter.getProductId()
                 )
         ) {
             continue;
         }


         // ==================================================
         // FILTRO POR FUNCIONÁRIO
         // ==================================================

         if (
                 filter != null
                 && filter.getEmployeeId() != null
                 && filter.getEmployeeId() > 0
                 && (
                     movement.getEmployee() == null
                     || movement.getEmployee().getId()
                             != filter.getEmployeeId()
                 )
         ) {
             continue;
         }


         // ==================================================
         // DTO
         // ==================================================

         ReportDTO.StockMovementRow row =
                 new ReportDTO.StockMovementRow();


         // ID
         row.setId(
                 movement.getId()
         );


         // DATA
         row.setData(
                 movement.getData()
         );


         // ==================================================
         // PRODUTO
         // ==================================================

         if (movement.getProduct() != null) {

             row.setProductId(
                     movement.getProduct().getId()
             );

             row.setProduto(
                     movement.getProduct().getNome()
             );
         }


         // ==================================================
         // TIPO
         // ==================================================

         row.setTipo(
                 movement.getMovementType() != null
                         ? movement.getMovementType().getDescricao()
                         : "N/D"
         );


         // ==================================================
         // QUANTIDADE
         // ==================================================

         row.setQuantidade(
                 movement.getQuantidade()
         );


         // ==================================================
         // MOTIVO
         // ==================================================

         row.setMotivo(
                 movement.getMotivo()
         );


         // ==================================================
         // FUNCIONÁRIO
         // ==================================================

         if (movement.getEmployee() != null) {

             row.setEmployeeId(
                     movement.getEmployee().getId()
             );

             row.setFuncionario(
                     movement.getEmployee().getNome()
             );

         }


         // ==================================================
         // LOJA
         // ==================================================

         if (movement.getShop() != null) {

             row.setShopId(
                     movement.getShop().getId()
             );

             row.setLoja(
                     movement.getShop().getNome()
             );

         }


         // ==================================================
         // ADICIONAR
         // ==================================================

         rows.add(row);


         totalQuantity +=
                 movement.getQuantidade();
     }


     // ====================================================
     // ORDENAR POR DATA DESCENDENTE
     // ====================================================

     rows.sort(
             Comparator.comparing(
                     ReportDTO.StockMovementRow::getData,
                     Comparator.nullsLast(
                             Comparator.reverseOrder()
                     )
             )
     );


     // ====================================================
     // RESULTADO FINAL
     // ====================================================

     ReportDTO.StockMovementReport report =
             new ReportDTO.StockMovementReport();


     report.setMovementsCount(
             rows.size()
     );


     report.setTotalQuantity(
             totalQuantity
     );


     report.setRows(
             rows
     );


     return report;
 }


    // ====================================================
    // 7. VENDAS POR FUNCIONÁRIO
    // ====================================================

    @Transactional
    public ReportDTO.EmployeeReport vendasPorFuncionario(
            ReportDTO.Filter filter) {

        validarDatas(filter);

        List<Sale> vendas =
                buscarVendas(filter);


        Map<Long, ReportDTO.EmployeeRow> map =
                new LinkedHashMap<>();


        for (Sale sale : vendas) {

            if (sale == null
                    || sale.getTransaction() == null) {

                continue;
            }


            Transaction transaction =
                    sale.getTransaction();

            Box box =
                    transaction.getBox();


            if (box == null
                    || box.getEmployeeOpened() == null) {

                continue;
            }


            Long employeeId =
                    box.getEmployeeOpened().getId();


            if (
                    filter != null
                    && filter.getEmployeeId() != null
                    && filter.getEmployeeId() > 0
                    && employeeId != filter.getEmployeeId()
            ) {
                continue;
            }


            BigDecimal total =
                    valor(
                            transaction.getTotalGeral()
                    );


            ReportDTO.EmployeeRow row =
                    map.computeIfAbsent(
                            employeeId,
                            key -> {

                                ReportDTO.EmployeeRow r =
                                        new ReportDTO.EmployeeRow();

                                r.setId(
                                        key
                                );

                                r.setNome(
                                        box.getEmployeeOpened()
                                                .getNome()
                                );

                                r.setVendas(0);

                                r.setTotal(
                                        BigDecimal.ZERO
                                );

                                return r;
                            }
                    );


            row.setVendas(
                    row.getVendas() + 1
            );


            row.setTotal(
                    row.getTotal()
                            .add(total)
            );
        }


        List<ReportDTO.EmployeeRow> rows =
                new ArrayList<>(
                        map.values()
                );


        rows.forEach(row -> {

            row.setTotal(
                    arredondar(
                            row.getTotal()
                    )
            );

            row.setTicketMedio(
                    row.getVendas() == 0
                            ? BigDecimal.ZERO
                            : arredondar(
                                    row.getTotal().divide(
                                            BigDecimal.valueOf(
                                                    row.getVendas()
                                            ),
                                            2,
                                            RoundingMode.HALF_UP
                                    )
                            )
            );
        });


        rows.sort(
                Comparator.comparing(
                        ReportDTO.EmployeeRow::getTotal
                ).reversed()
        );


        ReportDTO.Summary summary =
                resumo(filter);


        ReportDTO.EmployeeReport report =
                new ReportDTO.EmployeeReport();


        report.setSummary(
                summary
        );

        report.setRows(
                rows
        );


        return report;
    }


    // ====================================================
    // 8. VENDAS POR CAIXA
    // ====================================================

    @Transactional
    public ReportDTO.BoxReport vendasPorCaixa(
            ReportDTO.Filter filter) {

        validarDatas(filter);


        List<Box> boxes;


        if (
                filter != null
                && filter.getShopId() != null
                && filter.getShopId() > 0
        ) {

            boxes =
                    reportRepository.findBoxesByShop(
                            filter.getShopId()
                    );

        } else {

            boxes =
                    reportRepository.findAllBoxes();
        }


        List<ReportDTO.BoxRow> rows =
                new ArrayList<>();


        BigDecimal totalSales =
                BigDecimal.ZERO;


        for (Box box : boxes) {

            if (box == null) {
                continue;
            }


            if (
                    filter != null
                    && filter.getBoxId() != null
                    && filter.getBoxId() > 0
                    && box.getId() != filter.getBoxId()
            ) {
                continue;
            }


            // Filtro de data pela abertura do caixa
            if (
                    filter != null
                    && filter.getFrom() != null
                    && box.getDataAbertura() != null
                    && box.getDataAbertura()
                            .toLocalDate()
                            .isBefore(
                                    filter.getFrom()
                            )
            ) {
                continue;
            }


            if (
                    filter != null
                    && filter.getTo() != null
                    && box.getDataAbertura() != null
                    && box.getDataAbertura()
                            .toLocalDate()
                            .isAfter(
                                    filter.getTo()
                            )
            ) {
                continue;
            }


            List<Sale> sales =
                    reportRepository.findSalesByBoxAsc(
                            box.getId()
                    );


            BigDecimal salesTotal =
                    BigDecimal.ZERO;

            BigDecimal cashReceived =
                    BigDecimal.ZERO;

            BigDecimal changeGiven =
                    BigDecimal.ZERO;

            long salesCount =
                    0;


            for (Sale sale : sales) {

                if (sale == null
                        || sale.getTransaction() == null) {
                    continue;
                }


                Transaction transaction =
                        sale.getTransaction();


                // Apenas venda
                if (
                        transaction.getTransactionType() != null
                        && !"VENDA".equals(
                                transaction.getTransactionType()
                                        .toString()
                        )
                ) {
                    continue;
                }


                BigDecimal total =
                        valor(
                                transaction.getTotalGeral()
                        );


                salesTotal =
                        salesTotal.add(total);


                salesCount++;


                if (
                        transaction.getPaymentMethod() != null
                        && "DINHEIRO".equals(
                                transaction.getPaymentMethod()
                                        .getDescricao()
                        )
                ) {

                    cashReceived =
                            cashReceived.add(
                                    valor(
                                            transaction.getValorRecebido()
                                    )
                            );

                    changeGiven =
                            changeGiven.add(
                                    valor(
                                            transaction.getTroco()
                                    )
                            );
                }
            }


            BigDecimal openingValue =
                    valor(
                            box.getValorInicial()
                    );


            BigDecimal expected =
                    openingValue.add(
                            salesTotal
                    );


            BigDecimal closing =
                    valor(
                            box.getValorFinal()
                    );


            BigDecimal difference =
                    closing.subtract(
                            expected
                    );


            ReportDTO.BoxRow row =
                    new ReportDTO.BoxRow();


            row.setId(
                    box.getId()
            );

            row.setStatus(
                    box.getStatusCaixa() != null
                            ? box.getStatusCaixa().getDescricao()
                            : "N/D"
            );

            row.setOpeningDate(
                    box.getDataAbertura()
            );

            row.setClosingDate(
                    box.getDataFecho()
            );


            row.setOpenedBy(
                    box.getEmployeeOpened() != null
                            ? box.getEmployeeOpened().getNome()
                            : "N/D"
            );


            row.setClosedBy(
                    box.getEmployeeClosure() != null
                            ? box.getEmployeeClosure().getNome()
                            : "SESSÃO ACTIVA"
            );


            row.setOpeningValue(
                    arredondar(
                            openingValue
                    )
            );

            row.setSalesTotal(
                    arredondar(
                            salesTotal
                    )
            );

            row.setCashReceived(
                    arredondar(
                            cashReceived
                    )
            );

            row.setChangeGiven(
                    arredondar(
                            changeGiven
                    )
            );

            row.setExpectedClosingValue(
                    arredondar(
                            expected
                    )
            );

            row.setClosingValue(
                    arredondar(
                            closing
                    )
            );

            row.setDifference(
                    arredondar(
                            difference
                    )
            );

            row.setSalesCount(
                    salesCount
            );


            row.setShop(
                    box.getShop() != null
                            ? box.getShop().getNome()
                            : "N/D"
            );


            rows.add(row);

            totalSales =
                    totalSales.add(
                            salesTotal
                    );
        }


        ReportDTO.BoxReport report =
                new ReportDTO.BoxReport();


        report.setBoxesCount(
                rows.size()
        );

        report.setTotalSales(
                arredondar(
                        totalSales
                )
        );

        report.setRows(
                rows
        );


        return report;
    }


    // ====================================================
    // 9. CLIENTES
    // ====================================================

    @Transactional
    public ReportDTO.CustomerReport clientes(
            ReportDTO.Filter filter) {

        validarDatas(filter);

        List<Sale> vendas =
                buscarVendas(filter);


        Map<Long, List<Transaction>> map =
                new LinkedHashMap<>();


        for (Sale sale : vendas) {

            if (
                    sale == null
                    || sale.getTransaction() == null
                    || sale.getTransaction().getCustomer() == null
            ) {
                continue;
            }


            Transaction transaction =
                    sale.getTransaction();


            Customer customer =
                    transaction.getCustomer();


            if (
                    filter != null
                    && filter.getCustomerId() != null
                    && filter.getCustomerId() > 0
                    && customer.getId()
                            != filter.getCustomerId()
            ) {
                continue;
            }


            map.computeIfAbsent(
                    customer.getId(),
                    key -> new ArrayList<>()
            ).add(transaction);
        }


        List<ReportDTO.CustomerRow> rows =
                new ArrayList<>();


        BigDecimal totalGlobal =
                BigDecimal.ZERO;


        for (
                Map.Entry<Long, List<Transaction>> entry
                : map.entrySet()
        ) {

            List<Transaction> transactions =
                    entry.getValue();


            transactions.sort(
                    Comparator.comparing(
                            Transaction::getDatatrasacao,
                            Comparator.nullsLast(
                                    Comparator.naturalOrder()
                            )
                    )
            );


            if (transactions.isEmpty()) {
                continue;
            }


            Customer customer =
                    transactions.get(0)
                            .getCustomer();


            BigDecimal total =
                    BigDecimal.ZERO;


            for (Transaction transaction
                    : transactions) {

                total =
                        total.add(
                                valor(
                                        transaction.getTotalGeral()
                                )
                        );
            }


            long compras =
                    transactions.size();


            BigDecimal ticketMedio =
                    total.divide(
                            BigDecimal.valueOf(
                                    compras
                            ),
                            2,
                            RoundingMode.HALF_UP
                    );


            BigDecimal frequencia =
                    BigDecimal.ZERO;


            List<LocalDateTime> dates =
                    transactions.stream()
                            .map(Transaction::getDatatrasacao)
                            .filter(Objects::nonNull)
                            .toList();


            if (dates.size() > 1) {

                long totalDays =
                        0;

                int intervals =
                        0;


                for (
                        int i = 1;
                        i < dates.size();
                        i++
                ) {

                    totalDays +=
                            Duration.between(
                                    dates.get(i - 1),
                                    dates.get(i)
                            ).toDays();

                    intervals++;
                }


                if (intervals > 0) {

                    frequencia =
                            BigDecimal.valueOf(
                                    totalDays
                            )
                            .divide(
                                    BigDecimal.valueOf(
                                            intervals
                                    ),
                                    2,
                                    RoundingMode.HALF_UP
                            );
                }
            }


            ReportDTO.CustomerRow row =
                    new ReportDTO.CustomerRow();


            row.setId(
                    customer.getId()
            );

            row.setNome(
                    customer.getNome()
            );

            row.setNif(
                    customer.getNumeroContribuinte()
            );

            row.setCompras(
                    compras
            );

            row.setTotal(
                    arredondar(
                            total
                    )
            );

            row.setTicketMedio(
                    arredondar(
                            ticketMedio
                    )
            );

            row.setPrimeiraCompra(
                    dates.isEmpty()
                            ? null
                            : dates.get(0)
            );

            row.setUltimaCompra(
                    dates.isEmpty()
                            ? null
                            : dates.get(
                                    dates.size() - 1
                            )
            );

            row.setFrequenciaMediaDias(
                    frequencia
            );


            rows.add(row);


            totalGlobal =
                    totalGlobal.add(total);
        }


        rows.sort(
                Comparator.comparing(
                        ReportDTO.CustomerRow::getTotal
                ).reversed()
        );


        ReportDTO.CustomerReport report =
                new ReportDTO.CustomerReport();


        report.setCustomersCount(
                rows.size()
        );

        report.setTotal(
                arredondar(
                        totalGlobal
                )
        );

        report.setRows(
                rows
        );


        return report;
    }


    // ====================================================
    // 10. RECEITA MENSAL / FINANÇAS
    // ====================================================

    @Transactional
    public ReportDTO.MonthlyReport receitaMensalFinanceira(
            int year,
            int month,
            Long shopId) {

        if (
                month < 1
                || month > 12
        ) {

            throw new IllegalArgumentException(
                    "Mês inválido."
            );
        }


        LocalDate from =
                LocalDate.of(
                        year,
                        month,
                        1
                );


        LocalDate to =
                from.withDayOfMonth(
                        from.lengthOfMonth()
                );


        ReportDTO.Filter filter =
                new ReportDTO.Filter();


        filter.setFrom(from);
        filter.setTo(to);
        filter.setShopId(shopId);


        List<Sale> vendas =
                buscarVendas(filter);


        List<ReportDTO.MonthlySaleRow> rows =
                new ArrayList<>();


        for (Sale sale : vendas) {

            if (
                    sale == null
                    || sale.getTransaction() == null
            ) {
                continue;
            }


            Transaction transaction =
                    sale.getTransaction();


            List<SaleItem> itens =
                    saleItemRepository.findBySale_Id(
                            sale.getId()
                    );


            BigDecimal valorItens =
                    BigDecimal.ZERO;


            int quantidade =
                    0;


            String descricao =
                    "";


            if (itens != null) {

                valorItens =
                        itens.stream()
                                .filter(
                                        Objects::nonNull
                                )
                                .map(
                                        item ->
                                                valor(
                                                        item.getSubTotal()
                                                )
                                )
                                .reduce(
                                        BigDecimal.ZERO,
                                        BigDecimal::add
                                );


                quantidade =
                        itens.stream()
                                .filter(
                                        Objects::nonNull
                                )
                                .mapToInt(
                                        SaleItem::getQuantidade
                                )
                                .sum();


                descricao =
                        itens.stream()
                                .filter(
                                        Objects::nonNull
                                )
                                .filter(
                                        item ->
                                                item.getProduct()
                                                        != null
                                )
                                .map(
                                        item ->
                                                item.getProduct()
                                                        .getNome()
                                )
                                .filter(
                                        Objects::nonNull
                                )
                                .collect(
                                        Collectors.joining("; ")
                                );
            }


            ReportDTO.MonthlySaleRow row =
                    new ReportDTO.MonthlySaleRow();


            row.setDocumentoNumero(
                    sale.getId()
            );


            row.setDocumentoSerie(
                    sale.getSeries() != null
                            ? sale.getSeries()
                                    .getSerie()
                            : null
            );


            row.setDocumentoData(
                    sale.getDatavenda() != null
                            ? sale.getDatavenda()
                                    .toLocalDate()
                            : null
            );


            row.setNifConsumidor(
                    transaction.getCustomer() != null
                            ? transaction.getCustomer()
                                    .getNumeroContribuinte()
                            : null
            );


            row.setTotalValorItens(
                    arredondar(
                            valorItens
                    )
            );


            row.setTaxAplicavelItens(
                    arredondar(
                            transaction.getTotalImposto()
                    )
            );


            row.setCodigoIsento(
                    null
            );


            row.setQuantItens(
                    quantidade
            );


            row.setDescItens(
                    descricao
            );


            row.setNumeroDocumentoOrigem(
                    null
            );


            row.setDataDocumentoOrigem(
                    null
            );


            row.setTipoDocumento(
                    null
            );


            rows.add(row);
        }


        ReportDTO.MonthlyReport report =
                new ReportDTO.MonthlyReport();


        report.setSummary(
                resumo(filter)
        );

        report.setRows(
                rows
        );


        return report;
    }


    // ====================================================
    // BUSCAR VENDAS
    // ====================================================

    private List<Sale> buscarVendas(
            ReportDTO.Filter filter) {

        if (filter == null) {
            filter = new ReportDTO.Filter();
        }


        List<Sale> vendas;


        if (
                filter.getShopId() != null
                && filter.getShopId() > 0
        ) {

            if (
                    filter.getBoxId() != null
                    && filter.getBoxId() > 0
            ) {

                vendas =
                        reportRepository
                                .findSalesByShopAndBox(
                                        filter.getShopId(),
                                        filter.getBoxId()
                                );

            } else if (
                    filter.getEmployeeId() != null
                    && filter.getEmployeeId() > 0
            ) {

                vendas =
                        reportRepository
                                .findSalesByShopAndEmployee(
                                        filter.getShopId(),
                                        filter.getEmployeeId()
                                );

            } else {

                vendas =
                        reportRepository
                                .findSalesByShop(
                                        filter.getShopId()
                                );
            }

        } else {

            if (
                    filter.getBoxId() != null
                    && filter.getBoxId() > 0
            ) {

                vendas =
                        reportRepository
                                .findSalesByBox(
                                        filter.getBoxId()
                                );

            } else {

                vendas =
                        reportRepository
                                .findAllSales();
            }
        }


        LocalDateTime from =
                filter.getFrom() != null
                        ? filter.getFrom()
                                .atStartOfDay()
                        : null;


        LocalDateTime toExclusive =
                filter.getTo() != null
                        ? filter.getTo()
                                .plusDays(1)
                                .atStartOfDay()
                        : null;


        final LocalDateTime finalFrom =
                from;

        final LocalDateTime finalTo =
                toExclusive;


        return vendas.stream()
                .filter(
                        sale -> {

                            if (
                                    sale == null
                                    || sale.getDatavenda() == null
                            ) {
                                return false;
                            }


                            LocalDateTime data =
                                    sale.getDatavenda();


                            if (
                                    finalFrom != null
                                    && data.isBefore(
                                            finalFrom
                                    )
                            ) {
                                return false;
                            }


                            if (
                                    finalTo != null
                                    && !data.isBefore(
                                            finalTo
                                    )
                            ) {
                                return false;
                            }


                            return true;
                        }
                )
                .toList();
    }


    // ====================================================
    // VALIDAR DATAS
    // ====================================================

    private void validarDatas(
            ReportDTO.Filter filter) {

        if (filter == null) {
            return;
        }


        if (
                filter.getFrom() != null
                && filter.getTo() != null
                && filter.getFrom()
                        .isAfter(
                                filter.getTo()
                        )
        ) {

            throw new IllegalArgumentException(
                    "A data inicial não pode ser superior à data final."
            );
        }
    }


    // ====================================================
    // VALOR SEGURO
    // ====================================================

    private BigDecimal valor(
            BigDecimal value) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }


    // ====================================================
    // ARREDONDAR
    // ====================================================

    private BigDecimal arredondar(
            BigDecimal value) {

        return valor(value)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }
}