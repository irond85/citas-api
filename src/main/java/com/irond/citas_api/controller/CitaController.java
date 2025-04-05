package com.irond.citas_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.irond.citas_api.dto.CitaDto;
import com.irond.citas_api.service.interfaz.CitaServicio;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping("/api/v1/citas")
@Slf4j
public class CitaController {

	@Autowired
	private CitaServicio citaServicio;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	private List<CitaDto> getCitas() throws Exception {
		return citaServicio.getCitas();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	private ResponseEntity<String> createCita(@Valid @RequestBody CitaDto citaDto) throws Exception {
		try {
			String response = citaServicio.crearCita(citaDto);
			if (!response.equalsIgnoreCase("OK")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	private CitaDto getCitaPorId(@PathVariable Long id) throws Exception {
		return citaServicio.obtenerCitaPorId(id);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	private void cancelarCita(@PathVariable Long id) throws Exception {
		citaServicio.cancelarCita(id);
	}

	@PatchMapping(value = "/{id}")
	private ResponseEntity<String> updateCita(@Valid @RequestBody CitaDto citaDto, @PathVariable Long id)
			throws Exception {
		try {
			String response = citaServicio.actualizarCita(citaDto, id);
			if (!response.equalsIgnoreCase("OK")) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
