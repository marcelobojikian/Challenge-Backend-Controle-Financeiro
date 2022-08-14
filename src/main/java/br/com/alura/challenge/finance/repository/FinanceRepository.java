package br.com.alura.challenge.finance.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import br.com.alura.challenge.finance.model.FinanceEntity;

@NoRepositoryBean
public interface FinanceRepository<T extends FinanceEntity>
		extends JpaRepository<T, Long>, QuerydslPredicateExecutor<T> {

	public List<T> findAllByDescricaoContainingIgnoreCaseAndDataBetween(String descricao, LocalDate dateStart,
			LocalDate dateEnd);

	public List<T> findAllByDataBetween(LocalDate dateStart, LocalDate dateEnd);

	@Query(value = "SELECT sum(f.valor) FROM #{#entityName} f WHERE f.data BETWEEN :start AND :end")
	public BigDecimal getAmountBetweenDate(@Param("start") LocalDate startDate, @Param("end") LocalDate endDate);

}
