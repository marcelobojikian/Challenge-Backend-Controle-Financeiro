package br.com.alura.challenge.finance.test.integration.converter;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.alura.challenge.finance.backend.model.FinanceEntity;

public class ListFinanceConverter implements ArgumentConverter {

	Logger log = LoggerFactory.getLogger(FinanceConverter.class);

	private FinanceConverter financeConverter = new FinanceConverter();

	@Override
	public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {

		log.debug("Converte List to finances: {}", source);
		String[] parts = checkSource(source);

		try {

			List<FinanceEntity> result = new ArrayList<>();

			for (String finance : parts) {
				FinanceEntity entity = (FinanceEntity) financeConverter.convert(finance, context);
				result.add(entity);
			}

			return result;

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

		return ((String) source).split(";");

	}

}
