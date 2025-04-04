package com.irond.citas_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.irond.citas_api.model.Movimiento;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

	@Query(value = "SELECT * FROM movimientos WHERE id_usuario = :idUsuario", nativeQuery = true)
	List<Movimiento> getMovimientosPorUsuario(Long idUsuario);

	@Query(value = "SELECT * FROM movimientos WHERE id_cita = :idCita", nativeQuery = true)
	List<Movimiento> getMovimientosPorCita(Long idCita);

}
