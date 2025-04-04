package com.irond.citas_api.service.interfaz;

import java.util.List;

import com.irond.citas_api.model.Cita;
import com.irond.citas_api.model.Movimiento;

public interface MovimientoServicio {

	List<Movimiento> getMovimientosPorUsuario(Long idUsuario);

	List<Movimiento> getMovimientoPorCita(Long idCita);

	void crearMovimiento(Cita cita, String strDetalle);

}
