package br.com.alura.challenge.finance.parameter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.model.Income;

public class FinanceConverter implements ArgumentConverter {

	@Override
	public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {

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
