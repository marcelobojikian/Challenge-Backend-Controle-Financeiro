package br.com.alura.challenge.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.challenge.finance.model.Income;

public interface IncomeRepository extends JpaRepository<Income, Long> {

}
