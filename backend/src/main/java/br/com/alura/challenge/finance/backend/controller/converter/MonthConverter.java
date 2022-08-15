package br.com.alura.challenge.finance.backend.controller.converter;

import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

public class MonthConverter implements GenericConverter {

	static final ConvertiblePair[] pairs = new ConvertiblePair[] { new ConvertiblePair(Number.class, Month.class),
			new ConvertiblePair(String.class, Month.class) };

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return new HashSet<>(Arrays.asList(pairs));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

		if (sourceType.getType() == Month.class) {
			return source;
		}

		if (sourceType.getType() == String.class) {
			String value = (String) source;

			if (StringUtils.isNumeric(value)) {
				return Month.of(NumberUtils.toInt(value));
			}

			return Month.valueOf(value.toUpperCase());
		} else {
			Number number = (Number) source;
			return Month.of(number.intValue());
		}

	}

}
