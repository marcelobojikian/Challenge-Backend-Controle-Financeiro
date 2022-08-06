package br.com.alura.challenge.finance.controller.web;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.challenge.finance.controller.dto.FinanceDTO;
import br.com.alura.challenge.finance.model.Expenditure;
import br.com.alura.challenge.finance.service.ExpenditureService;

@RestController
@RequestMapping("/api/expenditures")
public class ExpenditureController {

	private FinanceController<Expenditure> service;

	private ModelMapper modelMapper;

	public ExpenditureController(ExpenditureService service, ModelMapper modelMapper) {
		this.service = new FinanceController<Expenditure>(service, modelMapper);
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public List<FinanceDTO> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public FinanceDTO findById(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FinanceDTO create(@RequestBody @Valid FinanceDTO dto) {
		Expenditure entity = modelMapper.map(dto, Expenditure.class);
		return service.create(entity);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public FinanceDTO update(@PathVariable Long id, @RequestBody @Valid FinanceDTO dto) {
		Expenditure entity = modelMapper.map(dto, Expenditure.class);
		return service.update(id, entity);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}

}
