package br.com.alura.challenge.finance.backend.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.alura.challenge.finance.backend.model.Expenditure;
import br.com.alura.challenge.finance.backend.model.Expenditure.Categoria;
import br.com.alura.challenge.finance.backend.rest.dto.finance.ExpenditureDTO;
import br.com.alura.challenge.finance.backend.rest.dto.finance.IncomeDTO;
import br.com.alura.challenge.finance.backend.model.Income;

@Tag("configuration")
@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

	static ApplicationConfig config;

	@BeforeAll
	public static void setupAll() {
		config = new ApplicationConfig();
	}

	@Test
	public void shouldConvertInheritanceIncomeToEntity() {

		ModelMapper modelMapper = config.modelMapper();

		IncomeDTO finance = new IncomeDTO(1l, "test", BigDecimal.ONE, toDate("01/01/2022"));

		Income result = modelMapper.map(finance, Income.class);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(null);
		assertThat(result.getDescricao()).isEqualTo("test");
		assertThat(result.getData()).isEqualTo(toDate("01/01/2022"));
		assertThat(result.getValor()).isEqualTo(BigDecimal.ONE);

	}

	@Test
	public void shouldConvertInheritanceExpenditureToEntity() {

		ModelMapper modelMapper = config.modelMapper();

		ExpenditureDTO finance = new ExpenditureDTO(1l, "test", BigDecimal.ONE, toDate("01/01/2022"), Categoria.LAZER);

		Expenditure result = modelMapper.map(finance, Expenditure.class);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(null);
		assertThat(result.getDescricao()).isEqualTo("test");
		assertThat(result.getData()).isEqualTo(toDate("01/01/2022"));
		assertThat(result.getValor()).isEqualTo(BigDecimal.ONE);
		assertThat(result.getCategoria()).isEqualTo(Categoria.LAZER);

	}

	@Test
	public void shouldConvertInheritanceIncomeToDTO() {

		ModelMapper modelMapper = config.modelMapper();

		Income finance = new Income(1l, "test", BigDecimal.ONE, toDate("01/01/2022"));

		IncomeDTO result = modelMapper.map(finance, IncomeDTO.class);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1l);
		assertThat(result.getDescricao()).isEqualTo("test");
		assertThat(result.getData()).isEqualTo(toDate("01/01/2022"));
		assertThat(result.getValor()).isEqualTo(BigDecimal.ONE);

	}

	@Test
	public void shouldConvertInheritanceExpenditureToDTO() {

		ModelMapper modelMapper = config.modelMapper();

		Expenditure finance = new Expenditure(1l, "test", BigDecimal.ONE, toDate("01/01/2022"), Categoria.LAZER);

		ExpenditureDTO result = modelMapper.map(finance, ExpenditureDTO.class);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1l);
		assertThat(result.getDescricao()).isEqualTo("test");
		assertThat(result.getData()).isEqualTo(toDate("01/01/2022"));
		assertThat(result.getValor()).isEqualTo(BigDecimal.ONE);
		assertThat(result.getCategoria()).isEqualTo(Categoria.LAZER);

	}

	@Test
	public void shouldSendFieldFromDTOToEntity() {

		ModelMapper modelMapper = config.modelMapper();

		ExpenditureDTO dto = new ExpenditureDTO(999l, "update", BigDecimal.TEN, toDate("12/12/2022"), Categoria.LAZER);
		Expenditure finance = new Expenditure(1l, "test", BigDecimal.ONE, toDate("01/01/2022"));

		modelMapper.map(dto, finance);

		assertThat(finance).isNotNull();
		assertThat(finance.getId()).isEqualTo(1l);
		assertThat(finance.getDescricao()).isEqualTo("update");
		assertThat(finance.getData()).isEqualTo(toDate("12/12/2022"));
		assertThat(finance.getValor()).isEqualTo(BigDecimal.TEN);
		assertThat(finance.getCategoria()).isEqualTo(Categoria.LAZER);

	}

	public static LocalDate toDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

}
