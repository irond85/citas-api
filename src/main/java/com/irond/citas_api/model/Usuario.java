package com.irond.citas_api.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "Usuarios")
public class Usuario {

	@Id
	@NotNull(message = "El documento no puede ser nulo")
	private Long idDocumento;

	@NotNull(message = "El nombre no puede ser nulo")
	private String strNombre;

	@NotNull(message = "El telefono no puede ser nulo")
	private String strTelefono;

	private String strCorreo;

	@NotNull(message = "El rol no puede ser nulo")
	private Integer idRolUsuario;

	@Basic
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtmCreacionUsuario;

	@OneToMany(mappedBy = "usuarioCliente")
	private List<Cita> listCitas;

	@OneToMany(mappedBy = "usuarioResponsable")
	private List<Cita> listServicios;

}
