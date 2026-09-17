package com.ideias_inovadora.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ideias_inovadora.dto.ReportDTO;
import com.ideias_inovadora.service.ReportService;

@RestController
@RequestMapping("api/sales-system/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;


    // ====================================================
    // RESUMO
    // ====================================================

    @GetMapping("/summary")
    public ReportDTO.Summary summary(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to,

            @RequestParam(required = false)
            Long shopId,

            @RequestParam(required = false)
            Long boxId,

            @RequestParam(required = false)
            Long employeeId,

            @RequestParam(required = false)
            Long customerId,

            @RequestParam(required = false)
            Long productId) {


        ReportDTO.Filter filter =
                criarFiltro(
                        from,
                        to,
                        shopId,
                        boxId,
                        employeeId,
                        customerId,
                        productId
                );


        return reportService.resumo(
                filter
        );
    }


    // ====================================================
    // VENDAS POR PERÍODO
    // ====================================================

    @GetMapping("/sales-by-period")
    public ReportDTO.SalesPeriodReport salesByPeriod(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to,

            @RequestParam(required = false)
            Long shopId,

            @RequestParam(required = false)
            Long boxId,

            @RequestParam(required = false)
            Long employeeId) {


        ReportDTO.Filter filter =
                criarFiltro(
                        from,
                        to,
                        shopId,
                        boxId,
                        employeeId,
                        null,
                        null
                );


        return reportService.vendasPorPeriodo(
                filter
        );
    }


    // ====================================================
    // PAGAMENTOS
    // ====================================================

    @GetMapping("/payment-methods")
    public ReportDTO.PaymentReport paymentMethods(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to,

            @RequestParam(required = false)
            Long shopId,

            @RequestParam(required = false)
            Long boxId,

            @RequestParam(required = false)
            Long employeeId) {


        ReportDTO.Filter filter =
                criarFiltro(
                        from,
                        to,
                        shopId,
                        boxId,
                        employeeId,
                        null,
                        null
                );


        return reportService.pagamentos(
                filter
        );
    }


    // ====================================================
    // PRODUTOS
    // ====================================================

    @GetMapping("/top-products")
    public ReportDTO.TopProductsReport topProducts(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to,

            @RequestParam(required = false)
            Long shopId,

            @RequestParam(required = false)
            Long productId) {


        ReportDTO.Filter filter =
                criarFiltro(
                        from,
                        to,
                        shopId,
                        null,
                        null,
                        null,
                        productId
                );


        return reportService.produtosMaisVendidos(
                filter
        );
    }


    // ====================================================
    // STOCK
    // ====================================================

    @GetMapping("/stock")
    public ReportDTO.StockReport stock(

            @RequestParam(required = false)
            Long shopId,

            @RequestParam(required = false)
            Long productId) {


        ReportDTO.Filter filter =
                criarFiltro(
                        null,
                        null,
                        shopId,
                        null,
                        null,
                        null,
                        productId
                );


        return reportService.stockAtual(
                filter
        );
    }


    // ====================================================
    // MOVIMENTOS
    // ====================================================

    @GetMapping("/stock-movements")
    public ReportDTO.StockMovementReport stockMovements(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to,

            @RequestParam(required = false)
            Long shopId,

            @RequestParam(required = false)
            Long employeeId,

            @RequestParam(required = false)
            Long productId) {


        ReportDTO.Filter filter =
                criarFiltro(
                        from,
                        to,
                        shopId,
                        null,
                        employeeId,
                        null,
                        productId
                );


        return reportService.movimentosStock(
                filter
        );
    }


    // ====================================================
    // FUNCIONÁRIOS
    // ====================================================

    @GetMapping("/by-employee")
    public ReportDTO.EmployeeReport byEmployee(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to,

            @RequestParam(required = false)
            Long shopId,

            @RequestParam(required = false)
            Long employeeId) {


        ReportDTO.Filter filter =
                criarFiltro(
                        from,
                        to,
                        shopId,
                        null,
                        employeeId,
                        null,
                        null
                );


        return reportService.vendasPorFuncionario(
                filter
        );
    }


    // ====================================================
    // CAIXAS
    // ====================================================

    @GetMapping("/by-box")
    public ReportDTO.BoxReport byBox(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to,

            @RequestParam(required = false)
            Long shopId,

            @RequestParam(required = false)
            Long boxId) {


        ReportDTO.Filter filter =
                criarFiltro(
                        from,
                        to,
                        shopId,
                        boxId,
                        null,
                        null,
                        null
                );


        return reportService.vendasPorCaixa(
                filter
        );
    }


    // ====================================================
    // CLIENTES
    // ====================================================

    @GetMapping("/customers")
    public ReportDTO.CustomerReport customers(

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to,

            @RequestParam(required = false)
            Long shopId,

            @RequestParam(required = false)
            Long customerId) {


        ReportDTO.Filter filter =
                criarFiltro(
                        from,
                        to,
                        shopId,
                        null,
                        null,
                        customerId,
                        null
                );


        return reportService.clientes(
                filter
        );
    }


    // ====================================================
    // FINANCEIRO MENSAL
    // ====================================================

    @GetMapping("/monthly-financial")
    public ReportDTO.MonthlyReport monthlyFinancial(

            @RequestParam
            int year,

            @RequestParam
            int month,

            @RequestParam(required = false)
            Long shopId) {


        return reportService.receitaMensalFinanceira(
                year,
                month,
                shopId
        );
    }


    // ====================================================
    // CRIAR FILTRO
    // ====================================================

    private ReportDTO.Filter criarFiltro(

            LocalDate from,
            LocalDate to,
            Long shopId,
            Long boxId,
            Long employeeId,
            Long customerId,
            Long productId) {


        ReportDTO.Filter filter =
                new ReportDTO.Filter();


        filter.setFrom(from);
        filter.setTo(to);
        filter.setShopId(shopId);
        filter.setBoxId(boxId);
        filter.setEmployeeId(employeeId);
        filter.setCustomerId(customerId);
        filter.setProductId(productId);


        return filter;
    }
}