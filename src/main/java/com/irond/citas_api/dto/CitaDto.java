package com.irond.citas_api.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CitaDto {

	private Long idCita;
	private Date dtmCita;
	private Long idCliente;
	private Long idResponsable;
	private String strObservaciones;
	private Integer intEstado;
	private Integer intCambiosCita;
	private Date dtmCreacionCita;

}
