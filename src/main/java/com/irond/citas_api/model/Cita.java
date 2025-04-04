package com.irond.citas_api.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "Citas")
public class Cita {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCita;

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "La fecha de la cita no puede ser nula")
	private Date dtmCita;

	@Basic
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtmCreacionReserva;

	@ManyToOne
	@JoinColumn(name = "id_cliente")
	@NotNull(message = "El cliente no puede ser null")
	private Usuario usuarioCliente;

	@ManyToOne
	@JoinColumn(name = "id_responsable")
	@NotNull(message = "El responsable no puede ser null")
	private Usuario usuarioResponsable;

	private String strObservaciones;
	private Integer intEstado;
	private Integer intCambiosCita;

}
