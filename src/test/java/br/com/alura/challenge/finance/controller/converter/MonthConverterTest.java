package br.com.alura.challenge.finance.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.TypeDescriptor;

@ExtendWith(MockitoExtension.class)
class MonthConverterTest {

	MonthConverter converter = new MonthConverter();

	@Mock
	Object source;

	@Mock
	TypeDescriptor targetType;

	@Test
	void shoulConvertMonthObject() {

		MonthMock mock = new MonthMock(Month.class.getFields()[0]);

		assertThat(converter.convert(Month.DECEMBER, mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert(Month.JANUARY, mock, targetType)).isInstanceOf(Month.class);

	}

	@Test
	void shoulConvertStringObject() {

		StringMock mock = new StringMock(String.class.getFields()[0]);

		assertThat(converter.convert("DECEMBER", mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert("DECEMBER", mock, targetType)).isEqualTo(Month.DECEMBER);

		assertThat(converter.convert("december", mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert("december", mock, targetType)).isEqualTo(Month.DECEMBER);

		assertThat(converter.convert("12", mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert("12", mock, targetType)).isEqualTo(Month.DECEMBER);

		assertThat(converter.convert("JANUARY", mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert("JANUARY", mock, targetType)).isEqualTo(Month.JANUARY);

		assertThat(converter.convert("january", mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert("january", mock, targetType)).isEqualTo(Month.JANUARY);

		assertThat(converter.convert("1", mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert("1", mock, targetType)).isEqualTo(Month.JANUARY);

	}

	@Test
	public void shoulConvertNumberObject() {

		NumberMock mock = new NumberMock(Integer.class.getFields()[0]);

		assertThat(converter.convert(Integer.valueOf(1), mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert(Integer.valueOf(1), mock, targetType)).isEqualTo(Month.JANUARY);

		mock = new NumberMock(Double.class.getFields()[0]);

		assertThat(converter.convert(Double.valueOf(3.33), mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert(Double.valueOf(3.33), mock, targetType)).isEqualTo(Month.MARCH);

		mock = new NumberMock(BigDecimal.class.getFields()[0]);

		assertThat(converter.convert(BigDecimal.valueOf(4), mock, targetType)).isInstanceOf(Month.class);
		assertThat(converter.convert(BigDecimal.valueOf(4), mock, targetType)).isEqualTo(Month.APRIL);

	}

	class MonthMock extends TypeDescriptor {
		private static final long serialVersionUID = 1L;

		public MonthMock(Field field) {
			super(field);
		}

		@Override
		public Class<?> getType() {
			return Month.class;
		}

	}

	class StringMock extends TypeDescriptor {
		private static final long serialVersionUID = 1L;

		public StringMock(Field field) {
			super(field);
		}

		@Override
		public Class<?> getType() {
			return String.class;
		}

	}

	class NumberMock extends TypeDescriptor {
		private static final long serialVersionUID = 1L;

		public NumberMock(Field field) {
			super(field);
		}

		@Override
		public Class<?> getType() {
			return Number.class;
		}

	}

}
