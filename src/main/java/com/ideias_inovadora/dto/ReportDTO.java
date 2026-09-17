package com.ideias_inovadora.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReportDTO {

    // ====================================================
    // FILTROS COMUNS
    // ====================================================

    public static class Filter implements Serializable {

        private static final long serialVersionUID = 1L;

        private LocalDate from;
        private LocalDate to;

        private Long shopId;
        private Long boxId;
        private Long employeeId;
        private Long customerId;
        private Long productId;

        public LocalDate getFrom() {
            return from;
        }

        public void setFrom(LocalDate from) {
            this.from = from;
        }

        public LocalDate getTo() {
            return to;
        }

        public void setTo(LocalDate to) {
            this.to = to;
        }

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }

        public Long getBoxId() {
            return boxId;
        }

        public void setBoxId(Long boxId) {
            this.boxId = boxId;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
    }


    // ====================================================
    // RESUMO
    // ====================================================

    public static class Summary implements Serializable {

        private static final long serialVersionUID = 1L;

        private long salesCount;
        private long itemsCount;

        private BigDecimal subtotal = BigDecimal.ZERO;
        private BigDecimal tax = BigDecimal.ZERO;
        private BigDecimal discount = BigDecimal.ZERO;
        private BigDecimal total = BigDecimal.ZERO;
        private BigDecimal averageTicket = BigDecimal.ZERO;

        public long getSalesCount() {
            return salesCount;
        }

        public void setSalesCount(long salesCount) {
            this.salesCount = salesCount;
        }

        public long getItemsCount() {
            return itemsCount;
        }

        public void setItemsCount(long itemsCount) {
            this.itemsCount = itemsCount;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }

        public BigDecimal getTax() {
            return tax;
        }

        public void setTax(BigDecimal tax) {
            this.tax = tax;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public BigDecimal getAverageTicket() {
            return averageTicket;
        }

        public void setAverageTicket(BigDecimal averageTicket) {
            this.averageTicket = averageTicket;
        }
    }


    // ====================================================
    // VENDAS POR PERÍODO
    // ====================================================

    public static class SalesRow implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long saleId;
        private String numeroFactura;
        private String serie;
        private LocalDateTime dataVenda;

        private String cliente;
        private String nifCliente;

        private Long boxId;
        private String operador;

        private int quantidadeItens;

        private BigDecimal subtotal = BigDecimal.ZERO;
        private BigDecimal imposto = BigDecimal.ZERO;
        private BigDecimal desconto = BigDecimal.ZERO;
        private BigDecimal total = BigDecimal.ZERO;

        private String metodoPagamento;


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

        public String getNifCliente() {
            return nifCliente;
        }

        public void setNifCliente(String nifCliente) {
            this.nifCliente = nifCliente;
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

        public int getQuantidadeItens() {
            return quantidadeItens;
        }

        public void setQuantidadeItens(int quantidadeItens) {
            this.quantidadeItens = quantidadeItens;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }

        public BigDecimal getImposto() {
            return imposto;
        }

        public void setImposto(BigDecimal imposto) {
            this.imposto = imposto;
        }

        public BigDecimal getDesconto() {
            return desconto;
        }

        public void setDesconto(BigDecimal desconto) {
            this.desconto = desconto;
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
    }


    // ====================================================
    // EVOLUÇÃO
    // ====================================================

    public static class DailySales implements Serializable {

        private static final long serialVersionUID = 1L;

        private LocalDate data;
        private long vendas;
        private BigDecimal total = BigDecimal.ZERO;

        public LocalDate getData() {
            return data;
        }

        public void setData(LocalDate data) {
            this.data = data;
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
    }


    public static class WeeklySales implements Serializable {

        private static final long serialVersionUID = 1L;

        private String semana;
        private LocalDate inicio;
        private LocalDate fim;
        private long vendas;
        private BigDecimal total = BigDecimal.ZERO;

        public String getSemana() {
            return semana;
        }

        public void setSemana(String semana) {
            this.semana = semana;
        }

        public LocalDate getInicio() {
            return inicio;
        }

        public void setInicio(LocalDate inicio) {
            this.inicio = inicio;
        }

        public LocalDate getFim() {
            return fim;
        }

        public void setFim(LocalDate fim) {
            this.fim = fim;
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
    }


    public static class MonthlySales implements Serializable {

        private static final long serialVersionUID = 1L;

        private int ano;
        private int mes;
        private long vendas;
        private BigDecimal total = BigDecimal.ZERO;

        public int getAno() {
            return ano;
        }

        public void setAno(int ano) {
            this.ano = ano;
        }

        public int getMes() {
            return mes;
        }

        public void setMes(int mes) {
            this.mes = mes;
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
    }


    public static class SalesPeriodReport implements Serializable {

        private static final long serialVersionUID = 1L;

        private Summary summary;
        private List<DailySales> daily;
        private List<WeeklySales> weekly;
        private List<MonthlySales> monthly;
        private List<SalesRow> rows;

        public Summary getSummary() {
            return summary;
        }

        public void setSummary(Summary summary) {
            this.summary = summary;
        }

        public List<DailySales> getDaily() {
            return daily;
        }

        public void setDaily(List<DailySales> daily) {
            this.daily = daily;
        }

        public List<WeeklySales> getWeekly() {
            return weekly;
        }

        public void setWeekly(List<WeeklySales> weekly) {
            this.weekly = weekly;
        }

        public List<MonthlySales> getMonthly() {
            return monthly;
        }

        public void setMonthly(List<MonthlySales> monthly) {
            this.monthly = monthly;
        }

        public List<SalesRow> getRows() {
            return rows;
        }

        public void setRows(List<SalesRow> rows) {
            this.rows = rows;
        }
    }


    // ====================================================
    // PAGAMENTOS
    // ====================================================

    public static class PaymentRow implements Serializable {

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


    public static class PaymentSummary implements Serializable {

        private static final long serialVersionUID = 1L;

        private long salesCount;
        private BigDecimal total = BigDecimal.ZERO;
        private BigDecimal averageTicket = BigDecimal.ZERO;
        private String mainMethod;

        public long getSalesCount() {
            return salesCount;
        }

        public void setSalesCount(long salesCount) {
            this.salesCount = salesCount;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public BigDecimal getAverageTicket() {
            return averageTicket;
        }

        public void setAverageTicket(BigDecimal averageTicket) {
            this.averageTicket = averageTicket;
        }

        public String getMainMethod() {
            return mainMethod;
        }

        public void setMainMethod(String mainMethod) {
            this.mainMethod = mainMethod;
        }
    }


    public static class PaymentReport implements Serializable {

        private static final long serialVersionUID = 1L;

        private PaymentSummary summary;
        private List<PaymentRow> rows;

        public PaymentSummary getSummary() {
            return summary;
        }

        public void setSummary(PaymentSummary summary) {
            this.summary = summary;
        }

        public List<PaymentRow> getRows() {
            return rows;
        }

        public void setRows(List<PaymentRow> rows) {
            this.rows = rows;
        }
    }


    // ====================================================
    // PRODUTOS MAIS VENDIDOS
    // ====================================================

    public static class TopProductRow implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;
        private String nome;
        private long quantidade;
        private BigDecimal subtotal = BigDecimal.ZERO;
        private BigDecimal total = BigDecimal.ZERO;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
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

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }


    public static class TopProductsSummary implements Serializable {

        private static final long serialVersionUID = 1L;

        private long productsCount;
        private long itemsCount;
        private BigDecimal subtotal = BigDecimal.ZERO;
        private BigDecimal total = BigDecimal.ZERO;

        public long getProductsCount() {
            return productsCount;
        }

        public void setProductsCount(long productsCount) {
            this.productsCount = productsCount;
        }

        public long getItemsCount() {
            return itemsCount;
        }

        public void setItemsCount(long itemsCount) {
            this.itemsCount = itemsCount;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }


    public static class TopProductsReport implements Serializable {

        private static final long serialVersionUID = 1L;

        private TopProductsSummary summary;
        private List<TopProductRow> rows;

        public TopProductsSummary getSummary() {
            return summary;
        }

        public void setSummary(TopProductsSummary summary) {
            this.summary = summary;
        }

        public List<TopProductRow> getRows() {
            return rows;
        }

        public void setRows(List<TopProductRow> rows) {
            this.rows = rows;
        }
    }


    // ====================================================
    // STOCK
    // ====================================================

    public static class StockRow implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long productId;
        private String produto;
        private String codigoBarra;

        private int quantidade;
        private int minimo;

        private BigDecimal preco = BigDecimal.ZERO;
        private BigDecimal valorInventario = BigDecimal.ZERO;

        private boolean baixo;
        private boolean semStock;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProduto() {
            return produto;
        }

        public void setProduto(String produto) {
            this.produto = produto;
        }

        public String getCodigoBarra() {
            return codigoBarra;
        }

        public void setCodigoBarra(String codigoBarra) {
            this.codigoBarra = codigoBarra;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public int getMinimo() {
            return minimo;
        }

        public void setMinimo(int minimo) {
            this.minimo = minimo;
        }

        public BigDecimal getPreco() {
            return preco;
        }

        public void setPreco(BigDecimal preco) {
            this.preco = preco;
        }

        public BigDecimal getValorInventario() {
            return valorInventario;
        }

        public void setValorInventario(BigDecimal valorInventario) {
            this.valorInventario = valorInventario;
        }

        public boolean isBaixo() {
            return baixo;
        }

        public void setBaixo(boolean baixo) {
            this.baixo = baixo;
        }

        public boolean isSemStock() {
            return semStock;
        }

        public void setSemStock(boolean semStock) {
            this.semStock = semStock;
        }
    }


    public static class StockSummary implements Serializable {

        private static final long serialVersionUID = 1L;

        private long productsCount;
        private long stockBaixo;
        private long semStock;
        private BigDecimal valorInventario = BigDecimal.ZERO;

        public long getProductsCount() {
            return productsCount;
        }

        public void setProductsCount(long productsCount) {
            this.productsCount = productsCount;
        }

        public long getStockBaixo() {
            return stockBaixo;
        }

        public void setStockBaixo(long stockBaixo) {
            this.stockBaixo = stockBaixo;
        }

        public long getSemStock() {
            return semStock;
        }

        public void setSemStock(long semStock) {
            this.semStock = semStock;
        }

        public BigDecimal getValorInventario() {
            return valorInventario;
        }

        public void setValorInventario(BigDecimal valorInventario) {
            this.valorInventario = valorInventario;
        }
    }


    public static class StockReport implements Serializable {

        private static final long serialVersionUID = 1L;

        private StockSummary summary;
        private List<StockRow> rows;

        public StockSummary getSummary() {
            return summary;
        }

        public void setSummary(StockSummary summary) {
            this.summary = summary;
        }

        public List<StockRow> getRows() {
            return rows;
        }

        public void setRows(List<StockRow> rows) {
            this.rows = rows;
        }
    }


    // ====================================================
    // MOVIMENTOS DE STOCK
    // ====================================================

    public static class StockMovementRow implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;
        private LocalDateTime data;

        private Long productId;
        private String produto;

        private String tipo;
        private int quantidade;

        private String motivo;

        private Long employeeId;
        private String funcionario;

        private Long shopId;
        private String loja;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public LocalDateTime getData() {
            return data;
        }

        public void setData(LocalDateTime data) {
            this.data = data;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProduto() {
            return produto;
        }

        public void setProduto(String produto) {
            this.produto = produto;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public String getMotivo() {
            return motivo;
        }

        public void setMotivo(String motivo) {
            this.motivo = motivo;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public String getFuncionario() {
            return funcionario;
        }

        public void setFuncionario(String funcionario) {
            this.funcionario = funcionario;
        }

        public Long getShopId() {
            return shopId;
        }

        public void setShopId(Long shopId) {
            this.shopId = shopId;
        }

        public String getLoja() {
            return loja;
        }

        public void setLoja(String loja) {
            this.loja = loja;
        }
    }


    public static class StockMovementReport implements Serializable {

        private static final long serialVersionUID = 1L;

        private long movementsCount;
        private long totalQuantity;

        private List<StockMovementRow> rows;

        public long getMovementsCount() {
            return movementsCount;
        }

        public void setMovementsCount(long movementsCount) {
            this.movementsCount = movementsCount;
        }

        public long getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(long totalQuantity) {
            this.totalQuantity = totalQuantity;
        }

        public List<StockMovementRow> getRows() {
            return rows;
        }

        public void setRows(List<StockMovementRow> rows) {
            this.rows = rows;
        }
    }


    // ====================================================
    // FUNCIONÁRIOS
    // ====================================================

    public static class EmployeeRow implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;
        private String nome;

        private long vendas;
        private BigDecimal total = BigDecimal.ZERO;
        private BigDecimal ticketMedio = BigDecimal.ZERO;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
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

        public BigDecimal getTicketMedio() {
            return ticketMedio;
        }

        public void setTicketMedio(BigDecimal ticketMedio) {
            this.ticketMedio = ticketMedio;
        }
    }


    public static class EmployeeReport implements Serializable {

        private static final long serialVersionUID = 1L;

        private Summary summary;
        private List<EmployeeRow> rows;

        public Summary getSummary() {
            return summary;
        }

        public void setSummary(Summary summary) {
            this.summary = summary;
        }

        public List<EmployeeRow> getRows() {
            return rows;
        }

        public void setRows(List<EmployeeRow> rows) {
            this.rows = rows;
        }
    }


    // ====================================================
    // CAIXAS
    // ====================================================

    public static class BoxRow implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;

        private String status;

        private LocalDateTime openingDate;
        private LocalDateTime closingDate;

        private String openedBy;
        private String closedBy;

        private BigDecimal openingValue = BigDecimal.ZERO;
        private BigDecimal salesTotal = BigDecimal.ZERO;
        private BigDecimal cashReceived = BigDecimal.ZERO;
        private BigDecimal changeGiven = BigDecimal.ZERO;

        private BigDecimal expectedClosingValue = BigDecimal.ZERO;
        private BigDecimal closingValue = BigDecimal.ZERO;
        private BigDecimal difference = BigDecimal.ZERO;

        private long salesCount;

        private String shop;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getOpeningDate() {
            return openingDate;
        }

        public void setOpeningDate(LocalDateTime openingDate) {
            this.openingDate = openingDate;
        }

        public LocalDateTime getClosingDate() {
            return closingDate;
        }

        public void setClosingDate(LocalDateTime closingDate) {
            this.closingDate = closingDate;
        }

        public String getOpenedBy() {
            return openedBy;
        }

        public void setOpenedBy(String openedBy) {
            this.openedBy = openedBy;
        }

        public String getClosedBy() {
            return closedBy;
        }

        public void setClosedBy(String closedBy) {
            this.closedBy = closedBy;
        }

        public BigDecimal getOpeningValue() {
            return openingValue;
        }

        public void setOpeningValue(BigDecimal openingValue) {
            this.openingValue = openingValue;
        }

        public BigDecimal getSalesTotal() {
            return salesTotal;
        }

        public void setSalesTotal(BigDecimal salesTotal) {
            this.salesTotal = salesTotal;
        }

        public BigDecimal getCashReceived() {
            return cashReceived;
        }

        public void setCashReceived(BigDecimal cashReceived) {
            this.cashReceived = cashReceived;
        }

        public BigDecimal getChangeGiven() {
            return changeGiven;
        }

        public void setChangeGiven(BigDecimal changeGiven) {
            this.changeGiven = changeGiven;
        }

        public BigDecimal getExpectedClosingValue() {
            return expectedClosingValue;
        }

        public void setExpectedClosingValue(BigDecimal expectedClosingValue) {
            this.expectedClosingValue = expectedClosingValue;
        }

        public BigDecimal getClosingValue() {
            return closingValue;
        }

        public void setClosingValue(BigDecimal closingValue) {
            this.closingValue = closingValue;
        }

        public BigDecimal getDifference() {
            return difference;
        }

        public void setDifference(BigDecimal difference) {
            this.difference = difference;
        }

        public long getSalesCount() {
            return salesCount;
        }

        public void setSalesCount(long salesCount) {
            this.salesCount = salesCount;
        }

        public String getShop() {
            return shop;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }
    }


    public static class BoxReport implements Serializable {

        private static final long serialVersionUID = 1L;

        private long boxesCount;
        private BigDecimal totalSales = BigDecimal.ZERO;

        private List<BoxRow> rows;

        public long getBoxesCount() {
            return boxesCount;
        }

        public void setBoxesCount(long boxesCount) {
            this.boxesCount = boxesCount;
        }

        public BigDecimal getTotalSales() {
            return totalSales;
        }

        public void setTotalSales(BigDecimal totalSales) {
            this.totalSales = totalSales;
        }

        public List<BoxRow> getRows() {
            return rows;
        }

        public void setRows(List<BoxRow> rows) {
            this.rows = rows;
        }
    }


    // ====================================================
    // CLIENTES
    // ====================================================

    public static class CustomerRow implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long id;
        private String nome;
        private String nif;

        private long compras;

        private BigDecimal total = BigDecimal.ZERO;
        private BigDecimal ticketMedio = BigDecimal.ZERO;

        private LocalDateTime primeiraCompra;
        private LocalDateTime ultimaCompra;

        private BigDecimal frequenciaMediaDias = BigDecimal.ZERO;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getNif() {
            return nif;
        }

        public void setNif(String nif) {
            this.nif = nif;
        }

        public long getCompras() {
            return compras;
        }

        public void setCompras(long compras) {
            this.compras = compras;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public BigDecimal getTicketMedio() {
            return ticketMedio;
        }

        public void setTicketMedio(BigDecimal ticketMedio) {
            this.ticketMedio = ticketMedio;
        }

        public LocalDateTime getPrimeiraCompra() {
            return primeiraCompra;
        }

        public void setPrimeiraCompra(LocalDateTime primeiraCompra) {
            this.primeiraCompra = primeiraCompra;
        }

        public LocalDateTime getUltimaCompra() {
            return ultimaCompra;
        }

        public void setUltimaCompra(LocalDateTime ultimaCompra) {
            this.ultimaCompra = ultimaCompra;
        }

        public BigDecimal getFrequenciaMediaDias() {
            return frequenciaMediaDias;
        }

        public void setFrequenciaMediaDias(BigDecimal frequenciaMediaDias) {
            this.frequenciaMediaDias = frequenciaMediaDias;
        }
    }


    public static class CustomerReport implements Serializable {

        private static final long serialVersionUID = 1L;

        private long customersCount;
        private BigDecimal total = BigDecimal.ZERO;

        private List<CustomerRow> rows;

        public long getCustomersCount() {
            return customersCount;
        }

        public void setCustomersCount(long customersCount) {
            this.customersCount = customersCount;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }

        public List<CustomerRow> getRows() {
            return rows;
        }

        public void setRows(List<CustomerRow> rows) {
            this.rows = rows;
        }
    }


    // ====================================================
    // FINANCEIRO MENSAL
    // ====================================================

    public static class MonthlySaleRow implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long documentoNumero;
        private String documentoSerie;
        private LocalDate documentoData;
        private String nifConsumidor;

        private BigDecimal totalValorItens;
        private BigDecimal taxAplicavelItens;

        private Integer codigoIsento;
        private Integer quantItens;

        private String descItens;

        private String numeroDocumentoOrigem;
        private LocalDate dataDocumentoOrigem;

        private String tipoDocumento;


        public Long getDocumentoNumero() {
            return documentoNumero;
        }

        public void setDocumentoNumero(Long documentoNumero) {
            this.documentoNumero = documentoNumero;
        }

        public String getDocumentoSerie() {
            return documentoSerie;
        }

        public void setDocumentoSerie(String documentoSerie) {
            this.documentoSerie = documentoSerie;
        }

        public LocalDate getDocumentoData() {
            return documentoData;
        }

        public void setDocumentoData(LocalDate documentoData) {
            this.documentoData = documentoData;
        }

        public String getNifConsumidor() {
            return nifConsumidor;
        }

        public void setNifConsumidor(String nifConsumidor) {
            this.nifConsumidor = nifConsumidor;
        }

        public BigDecimal getTotalValorItens() {
            return totalValorItens;
        }

        public void setTotalValorItens(BigDecimal totalValorItens) {
            this.totalValorItens = totalValorItens;
        }

        public BigDecimal getTaxAplicavelItens() {
            return taxAplicavelItens;
        }

        public void setTaxAplicavelItens(BigDecimal taxAplicavelItens) {
            this.taxAplicavelItens = taxAplicavelItens;
        }

        public Integer getCodigoIsento() {
            return codigoIsento;
        }

        public void setCodigoIsento(Integer codigoIsento) {
            this.codigoIsento = codigoIsento;
        }

        public Integer getQuantItens() {
            return quantItens;
        }

        public void setQuantItens(Integer quantItens) {
            this.quantItens = quantItens;
        }

        public String getDescItens() {
            return descItens;
        }

        public void setDescItens(String descItens) {
            this.descItens = descItens;
        }

        public String getNumeroDocumentoOrigem() {
            return numeroDocumentoOrigem;
        }

        public void setNumeroDocumentoOrigem(String numeroDocumentoOrigem) {
            this.numeroDocumentoOrigem = numeroDocumentoOrigem;
        }

        public LocalDate getDataDocumentoOrigem() {
            return dataDocumentoOrigem;
        }

        public void setDataDocumentoOrigem(LocalDate dataDocumentoOrigem) {
            this.dataDocumentoOrigem = dataDocumentoOrigem;
        }

        public String getTipoDocumento() {
            return tipoDocumento;
        }

        public void setTipoDocumento(String tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
        }
    }


    public static class MonthlyReport implements Serializable {

        private static final long serialVersionUID = 1L;

        private Summary summary;
        private List<MonthlySaleRow> rows;

        public Summary getSummary() {
            return summary;
        }

        public void setSummary(Summary summary) {
            this.summary = summary;
        }

        public List<MonthlySaleRow> getRows() {
            return rows;
        }

        public void setRows(List<MonthlySaleRow> rows) {
            this.rows = rows;
        }
    }
}