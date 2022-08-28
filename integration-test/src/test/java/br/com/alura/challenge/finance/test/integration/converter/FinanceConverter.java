package br.com.alura.challenge.finance.test.integration.converter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.alura.challenge.finance.backend.model.Expenditure;
import br.com.alura.challenge.finance.backend.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.backend.model.Income;

public class FinanceConverter implements ArgumentConverter {

	Logger log = LoggerFactory.getLogger(FinanceConverter.class);

	@Override
	public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {

		log.debug("Converte to finance: {}", source);
		String[] parts = checkSource(source);

		try {

			String instance = parts[0].toLowerCase();

			Long id = null;
			if (!parts[1].trim().isEmpty()) {
				id = Long.parseLong(parts[1]);
			}

			String description = parts[2].trim();
			BigDecimal amount = new BigDecimal(parts[3].trim());
			LocalDate date = parse(parts[4].trim());

			if (instance.equals("expenditure")) {
				if (parts.length == 6) {
					Categoria category = Categoria.valueOf(parts[5].trim());
					return new Expenditure(id, description, amount, date, category);
				}
				return new Expenditure(id, description, amount, date);
			}

			return new Income(id, description, amount, date);

		} catch (Exception e) {
			throw new ArgumentConversionException("Failed to convert", e);
		}

	}

	private String[] checkSource(Object source) {

		if (source == null) {
			throw new ArgumentConversionException("Cannot convert null source object");
		}

		if (!source.getClass().equals(String.class)) {
			throw new ArgumentConversionException("The argument should be a string: " + source);
		}

		String sourceString = (String) source;
		if (sourceString.trim().isEmpty()) {
			throw new ArgumentConversionException("Cannot convert an empty source string");
		}

		String[] parts = ((String) source).split(":");

		if (parts.length < 5) {
			throw new ArgumentConversionException("Cannot convert object, less than 4 field");
		}

		String instance = parts[0].toLowerCase();

		if (!instance.equals("income") && !instance.equals("expenditure")) {
			throw new ArgumentConversionException("Cannot convert an type of finance: " + instance);
		}

		return parts;

	}

	LocalDate parse(String data) {
		return LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
