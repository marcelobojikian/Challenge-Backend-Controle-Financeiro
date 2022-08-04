package br.com.alura.challenge.finance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.challenge.finance.model.Income;

public interface IncomeRepository extends JpaRepository<Income, Long> {

	public List<Income> findAllByDescricaoContainingIgnoreCaseAndDataBetween(String descricao, LocalDate dateStart,
			LocalDate dateEnd);

}
