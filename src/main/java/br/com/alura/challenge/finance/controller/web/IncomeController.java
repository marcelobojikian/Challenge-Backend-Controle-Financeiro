package br.com.alura.challenge.finance.controller.web;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.service.IncomeService;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

	private IncomeService service;

	private ModelMapper modelMapper;

	public IncomeController(IncomeService service, ModelMapper modelMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public IncomeDTO create(@RequestBody @Valid IncomeDTO dto) {
		Income entity = modelMapper.map(dto, Income.class);
		entity = service.create(entity);
		return modelMapper.map(entity, IncomeDTO.class);
	}

}
