package com.ideias_inovadora.config;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class STNCurrencySerializer extends JsonSerializer<BigDecimal>{

	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "ST"));
        symbols.setCurrencySymbol("Db");
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat formatoMoeda = new DecimalFormat("Db ##,##0.00", symbols);
        gen.writeString(formatoMoeda.format(value));
    }
}
