package com.ideias_inovadora.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ideias_inovadora.dto.DashboardDTO;
import com.ideias_inovadora.dto.ReportDTO;
import com.ideias_inovadora.dto.ReportDTO.DailySales;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.repository.ShopRepository;

/**
 * ====================================================
 * DASHBOARD SERVICE
 * ====================================================
 *
 * O Dashboard não recebe o nível de acesso do frontend.
 * O nível de acesso e a loja do utilizador são determinados
 * no backend a partir da sessão autenticada.
 *
 * Regras:
 *
 * ADMIN
 *   - X-Store-ID presente -> dashboard da loja seleccionada;
 *   - sem X-Store-ID         -> dashboard global.
 *
 * MANAGER
 *   - sempre a sua loja.
 *
 * USER / OPERADOR
 *   - sempre a sua loja;
 *   - vendas limitadas ao próprio funcionário;
 *   - sem caixas e stock de gestão na resposta.
 */
@Service
public class DashboardService {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ShopRepository shopRepository;


    // ====================================================
    // ENTRADA PRINCIPAL
    // ====================================================

    @Transactional(readOnly = true)
    public DashboardDTO dashboard(
            Long storeHeader
    ) {

        Employee logado =
                obterFuncionarioAutenticado();

        String role =
                obterRole(logado);

        // ------------------------------------------------
        // ADMIN
        // ------------------------------------------------

        if ("ROLE_ADMIN".equals(role)) {

            if (storeHeader != null
                    && storeHeader > 0) {

                validarLojaExistente(
                        storeHeader
                );

                return buildShopDashboard(
                        storeHeader,
                        null,
                        false
                );
            }

            return buildGlobalDashboard();
        }

        // ------------------------------------------------
        // GERENTE / OPERADOR
        // ------------------------------------------------

        if (logado.getShop() == null) {

            throw new SecurityException(
                    "O utilizador não possui uma loja associada."
            );
        }

        Long shopId =
                logado.getShop().getId();

        boolean operator =
                "ROLE_USER".equals(role)
                || "USER".equals(role)
                || "ROLE_OPERATOR".equals(role)
                || "OPERATOR".equals(role);

        return buildShopDashboard(
                shopId,
                operator
                        ? logado.getId()
                        : null,
                operator
        );
    }


    // ====================================================
    // DASHBOARD DA LOJA
    // ====================================================

    private DashboardDTO buildShopDashboard(
            Long shopId,
            Long employeeId,
            boolean operator
    ) {

        validarLojaExistente(
                shopId
        );

        LocalDate today =
                LocalDate.now();

        LocalDate firstDay =
                today.withDayOfMonth(1);

        LocalDate lastDay =
                today.withDayOfMonth(
                        today.lengthOfMonth()
                );

        LocalDate yesterday =
                today.minusDays(1);

        ReportDTO.Filter todayFilter =
                filter(
                        today,
                        today,
                        shopId,
                        employeeId
                );

        ReportDTO.Filter monthFilter =
                filter(
                        firstDay,
                        lastDay,
                        shopId,
                        employeeId
                );

        ReportDTO.SalesPeriodReport todayReport =
                reportService.vendasPorPeriodo(
                        todayFilter
                );

        ReportDTO.SalesPeriodReport monthReport =
                reportService.vendasPorPeriodo(
                        monthFilter
                );

        ReportDTO.PaymentReport paymentReport =
                reportService.pagamentos(
                        monthFilter
                );

        ReportDTO.TopProductsReport productsReport =
                operator
                        ? emptyTopProductsReport()
                        : reportService.produtosMaisVendidos(
                                monthFilter
                        );

        ReportDTO.StockReport stockReport =
                operator
                        ? emptyStockReport()
                        : reportService.stockAtual(
                                filter(
                                        null,
                                        null,
                                        shopId,
                                        null
                                )
                        );

        ReportDTO.BoxReport todayBoxes =
                operator
                        ? emptyBoxReport()
                        : reportService.vendasPorCaixa(
                                filter(
                                        today,
                                        today,
                                        shopId,
                                        null
                                )
                        );

        ReportDTO.BoxReport yesterdayBoxes =
                operator
                        ? emptyBoxReport()
                        : reportService.vendasPorCaixa(
                                filter(
                                        yesterday,
                                        yesterday,
                                        shopId,
                                        null
                                )
                        );

        Shop shop =
                shopRepository.findById(
                        shopId
                ).orElseThrow(
                        () -> new IllegalArgumentException(
                                "Loja não encontrada: " + shopId
                        )
                );

        DashboardDTO dashboard =
                new DashboardDTO();

        dashboard.setScope(
                operator
                        ? "OPERATOR"
                        : "SHOP"
        );

        dashboard.setShopId(
                shopId
        );

        dashboard.setShopName(
                shop.getNome()
        );

        dashboard.setSummary(
                summary(
                        todayReport,
                        monthReport,
                        stockReport,
                        todayBoxes,
                        operator
                )
        );

        dashboard.setSalesByDay(
                dailyPoints(
                        monthReport.getDaily()
                )
        );

        dashboard.setPayments(
                paymentPoints(
                        paymentReport
                )
        );

        dashboard.setTopProducts(
                topProducts(
                        productsReport,
                        shop.getNome()
                )
        );

        // "Últimas vendas" são as últimas vendas de hoje.
        dashboard.setRecentSales(
                recentSales(
                        todayReport.getRows(),
                        shop.getNome()
                )
        );

        if (!operator) {

            dashboard.setOpenBoxes(
                    openBoxes(
                            todayBoxes
                    )
            );

            dashboard.setYesterdayClosings(
                    yesterdayClosings(
                            yesterdayBoxes
                    )
            );

        } else {

            dashboard.setOpenBoxes(
                    Collections.emptyList()
            );

            dashboard.setYesterdayClosings(
                    Collections.emptyList()
            );
        }

        dashboard.setShops(
                Collections.emptyList()
        );

        return dashboard;
    }


    // ====================================================
    // DASHBOARD GLOBAL
    // ====================================================

    private DashboardDTO buildGlobalDashboard() {

        LocalDate today =
                LocalDate.now();

        LocalDate firstDay =
                today.withDayOfMonth(1);

        LocalDate lastDay =
                today.withDayOfMonth(
                        today.lengthOfMonth()
                );

        LocalDate yesterday =
                today.minusDays(1);

        ReportDTO.Filter globalTodayFilter =
                filter(
                        today,
                        today,
                        null,
                        null
                );

        ReportDTO.Filter globalMonthFilter =
                filter(
                        firstDay,
                        lastDay,
                        null,
                        null
                );

        ReportDTO.SalesPeriodReport todayReport =
                reportService.vendasPorPeriodo(
                        globalTodayFilter
                );

        ReportDTO.SalesPeriodReport monthReport =
                reportService.vendasPorPeriodo(
                        globalMonthFilter
                );

        ReportDTO.PaymentReport paymentReport =
                reportService.pagamentos(
                        globalMonthFilter
                );

        ReportDTO.BoxReport globalTodayBoxes =
                reportService.vendasPorCaixa(
                        globalTodayFilter
                );

        ReportDTO.BoxReport globalYesterdayBoxes =
                reportService.vendasPorCaixa(
                        filter(
                                yesterday,
                                yesterday,
                                null,
                                null
                        )
                );

        List<Shop> shops =
                shopRepository.findAll();

        List<DashboardDTO.ShopDashboard> summaries =
                new ArrayList<>();

        // ------------------------------------------------
        // DETALHE POR LOJA
        // ------------------------------------------------

        List<DashboardDTO.TopProduct> globalTopProducts =
                new ArrayList<>();

        List<DashboardDTO.RecentSale> globalRecentSales =
                new ArrayList<>();

        for (Shop shop : shops) {

            if (shop == null) {
                continue;
            }

            try {

                Long shopId =
                        shop.getId();

                ReportDTO.SalesPeriodReport shopToday =
                        reportService.vendasPorPeriodo(
                                filter(
                                        today,
                                        today,
                                        shopId,
                                        null
                                )
                        );

                ReportDTO.SalesPeriodReport shopMonth =
                        reportService.vendasPorPeriodo(
                                filter(
                                        firstDay,
                                        lastDay,
                                        shopId,
                                        null
                                )
                        );

                ReportDTO.TopProductsReport shopProducts =
                        reportService.produtosMaisVendidos(
                                filter(
                                        firstDay,
                                        lastDay,
                                        shopId,
                                        null
                                )
                        );

                globalTopProducts.addAll(
                        topProducts(
                                shopProducts,
                                shop.getNome()
                        )
                );

                globalRecentSales.addAll(
                        recentSales(
                                shopToday.getRows(),
                                shop.getNome()
                        )
                );

                ReportDTO.StockReport shopStock =
                        reportService.stockAtual(
                                filter(
                                        null,
                                        null,
                                        shopId,
                                        null
                                )
                        );

                ReportDTO.BoxReport shopBoxes =
                        reportService.vendasPorCaixa(
                                filter(
                                        today,
                                        today,
                                        shopId,
                                        null
                                )
                        );

                DashboardDTO.ShopDashboard row =
                        new DashboardDTO.ShopDashboard();

                row.setShopId(
                        shopId
                );

                row.setShopName(
                        shop.getNome()
                );

                row.setTodayTotal(
                        money(
                                shopToday
                                        .getSummary()
                                        .getTotal()
                        )
                );

                row.setTodaySales(
                        shopToday
                                .getSummary()
                                .getSalesCount()
                );

                row.setMonthTotal(
                        money(
                                shopMonth
                                        .getSummary()
                                        .getTotal()
                        )
                );

                row.setMonthSales(
                        shopMonth
                                .getSummary()
                                .getSalesCount()
                );

                row.setOpenBoxes(
                        countOpenBoxes(
                                shopBoxes
                        )
                );

                row.setLowStock(
                        shopStock
                                .getSummary()
                                .getStockBaixo()
                );

                row.setOutOfStock(
                        shopStock
                                .getSummary()
                                .getSemStock()
                );

                summaries.add(
                        row
                );

            } catch (RuntimeException error) {

                // Uma loja com problema de dados não impede
                // o Dashboard global das restantes lojas.
            }
        }

        DashboardDTO dashboard =
                new DashboardDTO();

        dashboard.setScope(
                "GLOBAL"
        );

        dashboard.setShopId(
                null
        );

        dashboard.setShopName(
                "Visão Global — Todas as lojas"
        );

        dashboard.setSummary(
                summary(
                        todayReport,
                        monthReport,
                        emptyStockReport(),
                        emptyBoxReport(),
                        false
                )
        );

        dashboard.getSummary().setOpenBoxes(
                summaries.stream()
                        .mapToLong(
                                DashboardDTO.ShopDashboard::getOpenBoxes
                        )
                        .sum()
        );

        dashboard.getSummary().setLowStock(
                summaries.stream()
                        .mapToLong(
                                DashboardDTO.ShopDashboard::getLowStock
                        )
                        .sum()
        );

        dashboard.getSummary().setOutOfStock(
                summaries.stream()
                        .mapToLong(
                                DashboardDTO.ShopDashboard::getOutOfStock
                        )
                        .sum()
        );

        dashboard.setSalesByDay(
                dailyPoints(
                        monthReport.getDaily()
                )
        );

        dashboard.setPayments(
                paymentPoints(
                        paymentReport
                )
        );

        // No Global, produtos e vendas recentes incluem
        // explicitamente a loja de origem.
        globalTopProducts.sort(
                Comparator.comparingLong(
                        DashboardDTO.TopProduct::getQuantidade
                ).reversed()
        );

        globalRecentSales.sort(
                Comparator.comparing(
                        DashboardDTO.RecentSale::getDataVenda,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );

        dashboard.setTopProducts(
                globalTopProducts
                        .stream()
                        .limit(10)
                        .collect(
                                Collectors.toList()
                        )
        );

        dashboard.setRecentSales(
                globalRecentSales
                        .stream()
                        .limit(10)
                        .collect(
                                Collectors.toList()
                        )
        );

        dashboard.setOpenBoxes(
                openBoxes(
                        globalTodayBoxes
                )
        );

        dashboard.setYesterdayClosings(
                yesterdayClosings(
                        globalYesterdayBoxes
                )
        );

        dashboard.setShops(
                summaries.stream()
                        .sorted(
                                Comparator.comparing(
                                        DashboardDTO.ShopDashboard::getTodayTotal
                                ).reversed()
                        )
                        .collect(
                                Collectors.toList()
                        )
        );

        return dashboard;
    }


    // ====================================================
    // RESUMO
    // ====================================================

    private DashboardDTO.Summary summary(
            ReportDTO.SalesPeriodReport today,
            ReportDTO.SalesPeriodReport month,
            ReportDTO.StockReport stock,
            ReportDTO.BoxReport boxes,
            boolean operator
    ) {

        DashboardDTO.Summary summary =
                new DashboardDTO.Summary();

        ReportDTO.Summary todaySummary =
                today.getSummary();

        ReportDTO.Summary monthSummary =
                month.getSummary();

        summary.setSalesToday(
                todaySummary.getSalesCount()
        );

        summary.setTotalToday(
                money(
                        todaySummary.getTotal()
                )
        );

        summary.setSalesMonth(
                monthSummary.getSalesCount()
        );

        summary.setTotalMonth(
                money(
                        monthSummary.getTotal()
                )
        );

        long monthSales =
                monthSummary.getSalesCount();

        BigDecimal monthTotal =
                money(
                        monthSummary.getTotal()
                );

        summary.setAverageTicket(
                monthSales == 0
                        ? BigDecimal.ZERO
                        : money(
                                monthTotal.divide(
                                        BigDecimal.valueOf(
                                                monthSales
                                        ),
                                        2,
                                        RoundingMode.HALF_UP
                                )
                        )
        );

        summary.setItemsSold(
                monthSummary.getItemsCount()
        );

        if (!operator) {

            summary.setOpenBoxes(
                    countOpenBoxes(
                            boxes
                    )
            );

            summary.setLowStock(
                    stock
                            .getSummary()
                            .getStockBaixo()
            );

            summary.setOutOfStock(
                    stock
                            .getSummary()
                            .getSemStock()
            );

        }

        return summary;
    }


    // ====================================================
    // GRÁFICO DIÁRIO
    // ====================================================

    private List<DashboardDTO.DailyPoint> dailyPoints(
            List<DailySales> source
    ) {

        if (source == null) {
            return Collections.emptyList();
        }

        return source.stream()
                .filter(
                        Objects::nonNull
                )
                .map(
                        item -> {

                            DashboardDTO.DailyPoint point =
                                    new DashboardDTO.DailyPoint();

                            point.setDate(
                                    item.getData()
                            );

                            point.setSales(
                                    item.getVendas()
                            );

                            point.setTotal(
                                    money(
                                            item.getTotal()
                                    )
                            );

                            return point;
                        }
                )
                .collect(
                        Collectors.toList()
                );
    }


    // ====================================================
    // PAGAMENTOS
    // ====================================================

    private List<DashboardDTO.PaymentPoint> paymentPoints(
            ReportDTO.PaymentReport report
    ) {

        if (report == null
                || report.getRows() == null) {

            return Collections.emptyList();
        }

        return report.getRows()
                .stream()
                .filter(
                        Objects::nonNull
                )
                .map(
                        row -> {

                            DashboardDTO.PaymentPoint point =
                                    new DashboardDTO.PaymentPoint();

                            point.setMetodo(
                                    row.getMetodo()
                            );

                            point.setVendas(
                                    row.getVendas()
                            );

                            point.setTotal(
                                    money(
                                            row.getTotal()
                                    )
                            );

                            point.setPercentagem(
                                    money(
                                            row.getPercentagem()
                                    )
                            );

                            return point;
                        }
                )
                .collect(
                        Collectors.toList()
                );
    }


    // ====================================================
    // TOP PRODUTOS
    // ====================================================

    private List<DashboardDTO.TopProduct> topProducts(
            ReportDTO.TopProductsReport report,
            String shopName
    ) {

        if (report == null
                || report.getRows() == null) {

            return Collections.emptyList();
        }

        return report.getRows()
                .stream()
                .filter(
                        Objects::nonNull
                )
                .limit(5)
                .map(
                        row -> {

                            DashboardDTO.TopProduct point =
                                    new DashboardDTO.TopProduct();

                            point.setProductId(
                                    row.getId()
                            );

                            point.setNome(
                                    row.getNome()
                            );

                            point.setQuantidade(
                                    row.getQuantidade()
                            );

                            point.setTotal(
                                    money(
                                            row.getTotal()
                                    )
                            );

                            point.setShopName(
                                    shopName
                            );

                            return point;
                        }
                )
                .collect(
                        Collectors.toList()
                );
    }


    // ====================================================
    // ÚLTIMAS VENDAS
    // ====================================================

    private List<DashboardDTO.RecentSale> recentSales(
            List<ReportDTO.SalesRow> rows,
            String shopName
    ) {

        if (rows == null) {
            return Collections.emptyList();
        }

        return rows.stream()
                .filter(
                        Objects::nonNull
                )
                .limit(10)
                .map(
                        row -> {

                            DashboardDTO.RecentSale sale =
                                    new DashboardDTO.RecentSale();

                            sale.setSaleId(
                                    row.getSaleId()
                            );

                            sale.setNumeroFactura(
                                    row.getNumeroFactura()
                            );

                            sale.setSerie(
                                    row.getSerie()
                            );

                            sale.setDataVenda(
                                    row.getDataVenda()
                            );

                            sale.setCliente(
                                    row.getCliente()
                            );

                            sale.setTotal(
                                    money(
                                            row.getTotal()
                                    )
                            );

                            sale.setMetodoPagamento(
                                    row.getMetodoPagamento()
                            );

                            sale.setShopName(
                                    shopName
                            );

                            sale.setBoxId(
                                    row.getBoxId()
                            );

                            sale.setOperador(
                                    row.getOperador()
                            );

                            return sale;
                        }
                )
                .collect(
                        Collectors.toList()
                );
    }


    // ====================================================
    // CAIXAS
    // ====================================================

    private List<DashboardDTO.OpenBox> openBoxes(
            ReportDTO.BoxReport report
    ) {

        if (report == null
                || report.getRows() == null) {

            return Collections.emptyList();
        }

        return report.getRows()
                .stream()
                .filter(
                        row ->
                                row != null
                                && row.getStatus() != null
                                && row.getStatus()
                                        .toUpperCase()
                                        .contains(
                                                "ABERTO"
                                        )
                )
                .map(
                        row -> {

                            DashboardDTO.OpenBox box =
                                    new DashboardDTO.OpenBox();

                            box.setBoxId(
                                    row.getId()
                            );

                            box.setShopName(
                                    row.getShop()
                            );

                            box.setOpenedBy(
                                    row.getOpenedBy()
                            );

                            box.setOpeningDate(
                                    row.getOpeningDate()
                            );

                            box.setOpeningValue(
                                    money(
                                            row.getOpeningValue()
                                    )
                            );

                            return box;
                        }
                )
                .collect(
                        Collectors.toList()
                );
    }


    private List<DashboardDTO.YesterdayClosing> yesterdayClosings(
            ReportDTO.BoxReport report
    ) {

        if (report == null
                || report.getRows() == null) {

            return Collections.emptyList();
        }

        return report.getRows()
                .stream()
                .filter(
                        row ->
                                row != null
                                && row.getClosingDate() != null
                )
                .map(
                        row -> {

                            DashboardDTO.YesterdayClosing closing =
                                    new DashboardDTO.YesterdayClosing();

                            closing.setBoxId(
                                    row.getId()
                            );

                            closing.setShopName(
                                    row.getShop()
                            );

                            closing.setClosingDate(
                                    row.getClosingDate()
                            );

                            closing.setClosingValue(
                                    money(
                                            row.getClosingValue()
                                    )
                            );

                            closing.setClosedBy(
                                    row.getClosedBy()
                            );

                            return closing;
                        }
                )
                .collect(
                        Collectors.toList()
                );
    }


    private long countOpenBoxes(
            ReportDTO.BoxReport report
    ) {

        return openBoxes(
                report
        ).size();
    }


    // ====================================================
    // FILTRO
    // ====================================================

    private ReportDTO.Filter filter(
            LocalDate from,
            LocalDate to,
            Long shopId,
            Long employeeId
    ) {

        ReportDTO.Filter filter =
                new ReportDTO.Filter();

        filter.setFrom(from);
        filter.setTo(to);
        filter.setShopId(shopId);
        filter.setEmployeeId(employeeId);

        return filter;
    }


    private void validarLojaExistente(
            Long shopId
    ) {

        if (shopId == null
                || shopId <= 0) {

            throw new IllegalArgumentException(
                    "Loja inválida."
            );
        }

        if (!shopRepository.existsById(shopId)) {

            throw new IllegalArgumentException(
                    "Loja não encontrada: " + shopId
            );
        }
    }


    // ====================================================
    // AUTHENTICATION
    // ====================================================

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

        throw new SecurityException(
                "Não foi possível identificar o funcionário autenticado."
        );
    }


    private String obterRole(
            Employee employee
    ) {

        if (employee.getAccessLevel() == null) {

            return "NO_ACCESS";
        }

        String descricao =
                employee.getAccessLevel()
                        .getDescricao();

        return descricao == null
                ? "NO_ACCESS"
                : descricao.toUpperCase();
    }


    // ====================================================
    // RELATÓRIOS VAZIOS
    // ====================================================

    private ReportDTO.StockReport emptyStockReport() {

        ReportDTO.StockSummary summary =
                new ReportDTO.StockSummary();

        summary.setProductsCount(0);
        summary.setStockBaixo(0);
        summary.setSemStock(0);
        summary.setValorInventario(
                BigDecimal.ZERO
        );

        ReportDTO.StockReport report =
                new ReportDTO.StockReport();

        report.setSummary(summary);
        report.setRows(
                Collections.emptyList()
        );

        return report;
    }


    private ReportDTO.TopProductsReport emptyTopProductsReport() {

        ReportDTO.TopProductsSummary summary =
                new ReportDTO.TopProductsSummary();

        summary.setProductsCount(0);
        summary.setItemsCount(0);
        summary.setSubtotal(
                BigDecimal.ZERO
        );
        summary.setTotal(
                BigDecimal.ZERO
        );

        ReportDTO.TopProductsReport report =
                new ReportDTO.TopProductsReport();

        report.setSummary(summary);
        report.setRows(
                Collections.emptyList()
        );

        return report;
    }


    private ReportDTO.BoxReport emptyBoxReport() {

        ReportDTO.BoxReport report =
                new ReportDTO.BoxReport();

        report.setBoxesCount(0);
        report.setTotalSales(
                BigDecimal.ZERO
        );
        report.setRows(
                Collections.emptyList()
        );

        return report;
    }


    // ====================================================
    // VALOR
    // ====================================================

    private BigDecimal money(
            BigDecimal value
    ) {

        return value == null
                ? BigDecimal.ZERO.setScale(2)
                : value.setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }
}
