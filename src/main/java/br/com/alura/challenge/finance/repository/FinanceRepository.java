package br.com.alura.challenge.finance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface FinanceRepository<T> extends JpaRepository<T, Long> {

	public List<T> findAllByDescricaoContainingIgnoreCaseAndDataBetween(String descricao, LocalDate dateStart,
			LocalDate dateEnd);

}
