package com.irond.citas_api.service.interfaz;

import java.util.Date;
import java.util.List;

import com.irond.citas_api.dto.CitaDto;

public interface CitaServicio {

	List<CitaDto> getCitasPorUsuario(Long idDocumento);

	List<CitaDto> getCitas();

	String crearCita(CitaDto citaDto);

	CitaDto obtenerCitaPorInfo(Long idCliente, Long idResponsable, Date dtmFechaCita, Integer intEstado);

	String actualizarCita(CitaDto citaDto, Long idCita);

	CitaDto obtenerCitaPorId(Long idCita);

	void cancelarCita(Long idCita);

}
