package br.com.alura.challenge.finance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.challenge.finance.controller.dto.summary.GroupCategory;
import br.com.alura.challenge.finance.model.Expenditure;

public interface ExpenditureRepository extends FinanceRepository<Expenditure> {

	@Query("SELECT new br.com.alura.challenge.finance.controller.dto.summary.GroupCategory(e.categoria, sum(e.valor)) "
			+ "FROM Expenditure e WHERE e.data BETWEEN :start AND :end GROUP BY e.categoria ORDER BY e.categoria")
	public List<GroupCategory> findGroupCategoryBetweenDate(@Param("start") LocalDate start,
			@Param("end") LocalDate end);

}
