package com.irond.citas_api.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.irond.citas_api.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

	@Query(value = "SELECT * FROM citas WHERE id_cliente = :idDocumento", nativeQuery = true)
	List<Cita> getCitasPorCliente(Long idDocumento);
	
	@Query(value = "SELECT * FROM citas WHERE id_responsable = :idDocumento", nativeQuery = true)
	List<Cita> getCitasPorResponsable(Long idDocumento);
	
	@Query(value = "SELECT * FROM citas WHERE id_cliente = :idCliente AND id_responsable = :idResponsable "
			+ "AND dtm_cita = :dtmFechaCita", nativeQuery = true)
	List<Cita> getCitaPorDetalle(Long idCliente, Long idResponsable, Date dtmFechaCita);
	
}
