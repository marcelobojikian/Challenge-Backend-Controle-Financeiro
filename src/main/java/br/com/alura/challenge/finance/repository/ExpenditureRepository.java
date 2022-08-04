package br.com.alura.challenge.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.challenge.finance.model.Expenditure;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {

}
