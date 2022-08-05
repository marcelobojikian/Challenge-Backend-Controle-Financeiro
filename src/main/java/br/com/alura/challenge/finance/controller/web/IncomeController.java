package br.com.alura.challenge.finance.controller.web;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.alura.challenge.finance.controller.dto.IncomeDTO;
import br.com.alura.challenge.finance.model.Income;
import br.com.alura.challenge.finance.service.IncomeService;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

	@Autowired
	private IncomeService service;

	@Autowired
	private ModelMapper modelMapper;

	public IncomeController(IncomeService service, ModelMapper modelMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public List<IncomeDTO> findAll() {
		List<Income> entities = service.findAll();
		return modelMapper.map(entities, new TypeToken<List<IncomeDTO>>() {
		}.getType());
	}

	@GetMapping("/{id}")
	public IncomeDTO findById(@PathVariable Long id) {
		Income entity = service.findById(id);
		return modelMapper.map(entity, IncomeDTO.class);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public IncomeDTO create(@RequestBody @Valid IncomeDTO dto) {
		Income entity = modelMapper.map(dto, Income.class);
		entity = service.save(entity);
		return modelMapper.map(entity, IncomeDTO.class);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public IncomeDTO update(@PathVariable Long id, @RequestBody @Valid IncomeDTO dto) {
		Income entity = modelMapper.map(dto, Income.class);
		entity = service.update(id, entity);
		return modelMapper.map(entity, IncomeDTO.class);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}

}
