package com.irond.citas_api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irond.citas_api.model.Cita;
import com.irond.citas_api.model.Movimiento;
import com.irond.citas_api.repository.MovimientoRepository;
import com.irond.citas_api.service.interfaz.MovimientoServicio;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MovimientoServicioImpl implements MovimientoServicio {

	@Autowired
	private MovimientoRepository mvtoRepository;

	@Override
	public List<Movimiento> getMovimientosPorUsuario(Long idUsuario) {
		return mvtoRepository.getMovimientosPorUsuario(idUsuario);
	}

	@Override
	public List<Movimiento> getMovimientoPorCita(Long idCita) {
		return mvtoRepository.getMovimientosPorCita(idCita);
	}

	@Override
	public void crearMovimiento(Cita cita, String strDetalle) {
		Movimiento mvto = new Movimiento();
		mvto.setIdCita(cita.getIdCita());
		mvto.setIdUsuario(cita.getUsuarioCliente().getIdDocumento());
		mvto.setStrDescripcion(strDetalle + cita.getStrObservaciones());
		mvtoRepository.save(mvto);
	}

}
