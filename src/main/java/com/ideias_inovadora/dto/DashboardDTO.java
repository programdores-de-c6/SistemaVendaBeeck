package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ideias_inovadora.model.PaymentMethod;

/**
 * ====================================================
 * DASHBOARD DTO
 * ====================================================
 *
 * Resposta única para o Dashboard.
 *
 * O Dashboard trabalha com 2 contextos:
 * - LOJA: informação da unidade seleccionada;
 * - GLOBAL: informação consolidada + detalhe por loja.
 *
 * Para utilizador normal, o Service pode devolver apenas
 * os campos operacionais permitidos.
 */
public class DashboardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String scope;
    private Long shopId;
    private String shopName;

    private Summary summary;

    private List<DailyPoint> salesByDay;
    private List<PaymentPoint> payments;
    private List<TopProduct> topProducts;
    private List<RecentSale> recentSales;

    private List<OpenBox> openBoxes;
    private List<YesterdayClosing> yesterdayClosings;

    private List<ShopDashboard> shops;

    public DashboardDTO() {
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public List<DailyPoint> getSalesByDay() {
        return salesByDay;
    }

    public void setSalesByDay(List<DailyPoint> salesByDay) {
        this.salesByDay = salesByDay;
    }

    public List<PaymentPoint> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentPoint> payments) {
        this.payments = payments;
    }

    public List<TopProduct> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(List<TopProduct> topProducts) {
        this.topProducts = topProducts;
    }

    public List<RecentSale> getRecentSales() {
        return recentSales;
    }

    public void setRecentSales(List<RecentSale> recentSales) {
        this.recentSales = recentSales;
    }

    public List<OpenBox> getOpenBoxes() {
        return openBoxes;
    }

    public void setOpenBoxes(List<OpenBox> openBoxes) {
        this.openBoxes = openBoxes;
    }

    public List<YesterdayClosing> getYesterdayClosings() {
        return yesterdayClosings;
    }

    public void setYesterdayClosings(List<YesterdayClosing> yesterdayClosings) {
        this.yesterdayClosings = yesterdayClosings;
    }

    public List<ShopDashboard> getShops() {
        return shops;
    }

    public void setShops(List<ShopDashboard> shops) {
        this.shops = shops;
    }

    // ====================================================
    // RESUMO
    // ====================================================

    public static class Summary implements Serializable {

        private static final long serialVersionUID = 1L;

        private long salesToday;
        private BigDecimal totalToday = BigDecimal.ZERO;

        private long salesMonth;
        private BigDecimal totalMonth = BigDecimal.ZERO;

        private BigDecimal averageTicket = BigDecimal.ZERO;

        private long itemsSold;

        private long openBoxes;

        private long lowStock;
        private long outOfStock;

        public long getSalesToday() {
            return salesToday;
        }

        public void setSalesToday(long salesToday) {
            this.salesToday = salesToday;
        }

        public BigDecimal getTotalToday() {
            return totalToday;
        }

        public void setTotalToday(BigDecimal totalToday) {
            this.totalToday = totalToday;
        }

        public long getSalesMonth() {
            return salesMonth;
        }

        public void setSalesMonth(long salesMonth) {
            this.salesMonth = salesMonth;
        }

        public BigDecimal getTotalMonth() {
            return totalMonth;
        }

        public void setTotalMonth(BigDecimal totalMonth) {
            this.totalMonth = totalMonth;
        }

        public BigDecimal getAverageTicket() {
            return averageTicket;
        }

        public void setAverageTicket(BigDecimal averageTicket) {
            this.averageTicket = averageTicket;
        }

        public long getItemsSold() {
            return itemsSold;
        }

        public void setItemsSold(long itemsSold) {
            this.itemsSold = itemsSold;
        }

        public long getOpenBoxes() {
            return openBoxes;
        }

        public void setOpenBoxes(long openBoxes) {
            this.openBoxes = openBoxes;
        }

        public long getLowStock() {
            return lowStock;
        }

        public void setLowStock(long lowStock) {
            this.lowStock = lowStock;
        }

        public long getOutOfStock() {
            return outOfStock;
        }

        public void setOutOfStock(long outOfStock) {
            this.outOfStock = outOfStock;
        }
    }

    // ====================================================
    // GRÁFICO DE VENDAS POR DIA
    // ====================================================

    public static class DailyPoint implements Serializable {

        private static final long serialVersionUID = 1L;

        private LocalDate date;
        private long sales;
        private BigDecimal total = BigDecimal.ZERO;

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public long getSales() {
            return sales;
        }

        public void setSales(long sales) {
            this.sales = sales;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }

    // ====================================================
    // MÉTODOS DE PAGAMENTO
    // ====================================================

    public static class PaymentPoint implements Serializable {

        private static final long serialVersionUID = 1L;

        private String metodo;
        private long vendas;
        private BigDecimal total = BigDecimal.ZERO;
        private BigDecimal percentagem = BigDecimal.ZERO;

        public String getMetodo() {
            return metodo;
        }

        public void setMetodo(String metodo) {
            this.metodo = metodo;
        }

        public long getVendas() {
            return vendas;
        }

        public void setVendas(long vendas) {
            this.vendas = vendas;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public BigDecimal getPercentagem() {
            return percentagem;
        }

        public void setPercentagem(BigDecimal percentagem) {
            this.percentagem = percentagem;
        }
    }

    // ====================================================
    // PRODUTOS MAIS VENDIDOS
    // ====================================================

    public static class TopProduct implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long productId;
        private String nome;
        private long quantidade;
        private BigDecimal total = BigDecimal.ZERO;
        private String shopName;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public long getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(long quantidade) {
            this.quantidade = quantidade;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }
    }

    // ====================================================
    // ÚLTIMAS VENDAS
    // ====================================================

    public static class RecentSale implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long saleId;
        private String numeroFactura;
        private String serie;
        private LocalDateTime dataVenda;

        private String cliente;

        private BigDecimal total = BigDecimal.ZERO;
        private String metodoPagamento;

        private String shopName;
        private Long boxId;
        private String operador;

        public Long getSaleId() {
            return saleId;
        }

        public void setSaleId(Long saleId) {
            this.saleId = saleId;
        }

        public String getNumeroFactura() {
            return numeroFactura;
        }

        public void setNumeroFactura(String numeroFactura) {
            this.numeroFactura = numeroFactura;
        }

        public String getSerie() {
            return serie;
        }

        public void setSerie(String serie) {
            this.serie = serie;
        }

        public LocalDateTime getDataVenda() {
            return dataVenda;
        }

        public void setDataVenda(LocalDateTime dataVenda) {
            this.dataVenda = dataVenda;
        }

        public String getCliente() {
            return cliente;
        }

        public void setCliente(String cliente) {
            this.cliente = cliente;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public String getMetodoPagamento() {
            return metodoPagamento;
        }

        public void setMetodoPagamento(String metodoPagamento) {
            this.metodoPagamento = metodoPagamento;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public Long getBoxId() {
            return boxId;
        }

        public void setBoxId(Long boxId) {
            this.boxId = boxId;
        }

        public String getOperador() {
            return operador;
        }

        public void setOperador(String operador) {
            this.operador = operador;
        }
    }

    // ====================================================
    // CAIXAS ABERTOS
    // ====================================================

    public static class OpenBox implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long boxId;
        private String shopName;

        private String openedBy;
        private LocalDateTime openingDate;

        private BigDecimal openingValue = BigDecimal.ZERO;

        public Long getBoxId() {
            return boxId;
        }

        public void setBoxId(Long boxId) {
            this.boxId = boxId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getOpenedBy() {
            return openedBy;
        }

        public void setOpenedBy(String openedBy) {
            this.openedBy = openedBy;
        }

        public LocalDateTime getOpeningDate() {
            return openingDate;
        }

        public void setOpeningDate(LocalDateTime openingDate) {
            this.openingDate = openingDate;
        }

        public BigDecimal getOpeningValue() {
            return openingValue;
        }

        public void setOpeningValue(BigDecimal openingValue) {
            this.openingValue = openingValue;
        }
    }

    // ====================================================
    // FECHO DO DIA ANTERIOR
    // ====================================================

    public static class YesterdayClosing implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long boxId;
        private String shopName;

        private LocalDateTime closingDate;
        private BigDecimal closingValue = BigDecimal.ZERO;

        private String closedBy;

        public Long getBoxId() {
            return boxId;
        }

        public void setBoxId(Long boxId) {
            this.boxId = boxId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public LocalDateTime getClosingDate() {
            return closingDate;
        }

        public void setClosingDate(LocalDateTime closingDate) {
            this.closingDate = closingDate;
        }

        public BigDecimal getClosingValue() {
            return closingValue;
        }

        public void setClosingValue(BigDecimal closingValue) {
            this.closingValue = closingValue;
        }

        public String getClosedBy() {
            return closedBy;
        }

        public void setClosedBy(String closedBy) {
            this.closedBy = closedBy;
        }
    }

    // ====================================================
    // RESUMO POR LOJA
    // ====================================================

    public static class ShopDashboard implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long shopId;
        private String shopName;

        private BigDecimal todayTotal = BigDecimal.ZERO;
        private long todaySales;

        private BigDecimal monthTotal = BigDecimal.ZERO;
        private long monthSales;

        private long openBoxes;
        private long lowStock;
        private long outOfStock;

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public BigDecimal getTodayTotal() {
            return todayTotal;
        }

        public void setTodayTotal(BigDecimal todayTotal) {
            this.todayTotal = todayTotal;
        }

        public long getTodaySales() {
            return todaySales;
        }

        public void setTodaySales(long todaySales) {
            this.todaySales = todaySales;
        }

        public BigDecimal getMonthTotal() {
            return monthTotal;
        }

        public void setMonthTotal(BigDecimal monthTotal) {
            this.monthTotal = monthTotal;
        }

        public long getMonthSales() {
            return monthSales;
        }

        public void setMonthSales(long monthSales) {
            this.monthSales = monthSales;
        }

        public long getOpenBoxes() {
            return openBoxes;
        }

        public void setOpenBoxes(long openBoxes) {
            this.openBoxes = openBoxes;
        }

        public long getLowStock() {
            return lowStock;
        }

        public void setLowStock(long lowStock) {
            this.lowStock = lowStock;
        }

        public long getOutOfStock() {
            return outOfStock;
        }

        public void setOutOfStock(long outOfStock) {
            this.outOfStock = outOfStock;
        }
    }
}
