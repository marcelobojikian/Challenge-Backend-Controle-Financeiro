package br.com.alura.challenge.finance.backend.rest.controller;

import java.time.Month;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.challenge.finance.backend.rest.dto.summary.GroupCategory;
import br.com.alura.challenge.finance.backend.rest.dto.summary.HeaderDTO;
import br.com.alura.challenge.finance.backend.rest.dto.summary.SummaryDTO;
import br.com.alura.challenge.finance.backend.service.SummaryService;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

	private SummaryService service;

	public SummaryController(SummaryService service) {
		this.service = service;
	}

	@GetMapping("/{year}/{month}")
	public ResponseEntity<?> findByYearAndMonth(@PathVariable int year, @PathVariable Month month) {

		HeaderDTO header = service.findHeaderByMonth(year, month);
		List<GroupCategory> groupCategoria = service.findGroupCategoryByMonth(year, month);

		SummaryDTO summary = new SummaryDTO(header, groupCategoria);

		return ResponseEntity.ok(summary);

	}

}
