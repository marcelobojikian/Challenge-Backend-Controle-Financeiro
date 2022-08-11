package br.com.alura.challenge.finance.controller.web;

import java.time.Month;

import javax.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import br.com.alura.challenge.finance.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.model.FinanceEntity;

@RestController
@RequestMapping("/api/finance")
public interface FinanceController<T extends FinanceEntity, DTO extends FinanceDTO> {

	@GetMapping
	ResponseEntity<?> all(Predicate predicate);

	@GetMapping("/{id}")
	EntityModel<DTO> one(@PathVariable Long id);

	@GetMapping("/{year}/{month}")
	ResponseEntity<?> byYearAndMonth(@PathVariable int year, @PathVariable Month month);

	@PostMapping
	ResponseEntity<?> createFinance(@RequestBody @Valid DTO dto);

	@PutMapping("/{id}")
	ResponseEntity<?> updateFinance(@PathVariable Long id, @RequestBody @Valid DTO dto);

	@DeleteMapping("/{id}")
	ResponseEntity<?> deleteFinance(@PathVariable Long id);

}
