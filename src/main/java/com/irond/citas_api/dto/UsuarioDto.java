package com.irond.citas_api.dto;

import lombok.Data;

@Data
public class UsuarioDto {
	private Long idDocumento;
	private String strNombre;
	private String strTelefono;
	private String strCorreo;
	private Integer intNroCitas;
	private Integer intNroServicios;
	private Integer idRolUsuario;
}
