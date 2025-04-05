package com.irond.citas_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.irond.citas_api.dto.CitaDto;
import com.irond.citas_api.dto.UsuarioDto;
import com.irond.citas_api.service.interfaz.CitaServicio;
import com.irond.citas_api.service.interfaz.UsuarioServicio;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping("/api/v1/usuarios")
@Slf4j
public class UsuarioController {

	@Autowired
	private UsuarioServicio usuarioServicio;

	@Autowired
	private CitaServicio citaServicio;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	private List<UsuarioDto> getAllUsuarios() throws Exception {
		return usuarioServicio.obtenerUsuarios();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	private ResponseEntity<String> createUsuario(@Valid @RequestBody UsuarioDto usuarioDto) throws Exception {
		try {
			UsuarioDto usuario = usuarioServicio.crearUsuario(usuarioDto);
			if (usuario == null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");
			}
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(value = "/{id}/citas")
	@ResponseStatus(HttpStatus.OK)
	private List<CitaDto> getCitasPorUsuario(@PathVariable Long id) throws Exception {
		return citaServicio.getCitasPorUsuario(id);
	}

}
