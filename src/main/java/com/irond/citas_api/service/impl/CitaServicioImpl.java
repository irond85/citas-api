package com.irond.citas_api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irond.citas_api.dto.CitaDto;
import com.irond.citas_api.model.Cita;
import com.irond.citas_api.model.Usuario;
import com.irond.citas_api.repository.CitaRepository;
import com.irond.citas_api.service.interfaz.CitaServicio;
import com.irond.citas_api.service.interfaz.MovimientoServicio;
import com.irond.citas_api.service.interfaz.UsuarioServicio;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CitaServicioImpl implements CitaServicio {

	@Autowired
	private CitaRepository repository;

	@Autowired
	private UsuarioServicio usuarioService;

	@Autowired
	private MovimientoServicio mvtoService;

	@Override
	public List<CitaDto> getCitasPorUsuario(Long idDocumento) {
		try {
			Usuario usuario = usuarioService.obtenerUsuarioPorId(idDocumento);
			if (usuario != null && usuario.getIdRolUsuario() != null) {
				if (usuario.getIdRolUsuario() == 0) {
					return getCitasPorResponsable(idDocumento);
				}
				return getCitasPorCliente(idDocumento);
			}
		} catch (Exception e) {
			log.error("Error ejecutando servicio getCitasPorUsuario() " + idDocumento, e);
		}
		return null;
	}

	private List<CitaDto> getCitasPorResponsable(Long idDocumento) {
		try {
			List<Cita> listCitas = repository.getCitasPorResponsable(idDocumento);
			return convertirCitaADto(listCitas);
		} catch (Exception e) {
			log.error("Error ejecutando servicio getCitasPorResponsable() " + idDocumento, e);
			return null;
		}
	}

	private List<CitaDto> getCitasPorCliente(Long idDocumento) {
		try {
			List<Cita> listCitas = repository.getCitasPorCliente(idDocumento);
			return convertirCitaADto(listCitas);
		} catch (Exception e) {
			log.error("Error ejecutando servicio getCitasPorCliente() " + idDocumento, e);
			return null;
		}
	}

	private List<CitaDto> convertirCitaADto(List<Cita> listCitas) {
		try {
			List<CitaDto> listCitaDto = listCitas.stream().map(cita -> {
				CitaDto citaDto = new CitaDto();
				citaDto.setIdCita(cita.getIdCita());
				citaDto.setDtmCita(cita.getDtmCita());
				citaDto.setIdCliente(cita.getUsuarioCliente().getIdDocumento());
				citaDto.setIdResponsable(cita.getUsuarioResponsable().getIdDocumento());
				citaDto.setIntCambiosCita(cita.getIntCambiosCita());
				citaDto.setIntEstado(cita.getIntEstado());
				citaDto.setStrObservaciones(cita.getStrObservaciones());
				citaDto.setDtmCreacionCita(cita.getDtmCreacionReserva());
				return citaDto;
			}).collect(Collectors.toList());

			return listCitaDto;
		} catch (Exception e) {
			log.error("Error ejecutando servicio convertirCitaADto() " + listCitas, e);
			return null;
		}
	}

	@Override
	public List<CitaDto> getCitas() {
		try {
			List<Cita> listCitas = repository.findAll();
			return convertirCitaADto(listCitas);
		} catch (Exception e) {
			log.error("Error ejecutando servicio getCitas() " + e);
			return null;
		}
	}

	@Override
	public String crearCita(CitaDto citaDto) {
		try {
			Usuario usuarioCliente = usuarioService.obtenerUsuarioPorId(citaDto.getIdCliente());
			if (usuarioCliente == null) {
				String error = new String("Error: El cliente no existe " + citaDto.getIdCliente());
				log.error(error);
				return error;
			}

			Usuario usuarioResponsable = usuarioService.obtenerUsuarioPorId(citaDto.getIdResponsable());
			if (usuarioResponsable == null) {
				String error = new String("Error: El responsable no existe " + citaDto.getIdResponsable());
				log.error(error);
				return error;
			} else if (usuarioResponsable.getIdRolUsuario() != 0) {
				String error = new String("Error: Cliente sin autorizacion " + citaDto.getIdCliente());
				log.error(error);
				return error;
			}

			CitaDto citaExistente = obtenerCitaPorInfo(citaDto.getIdCliente(), citaDto.getIdResponsable(),
					citaDto.getDtmCita(), citaDto.getIntEstado());
			if (citaExistente != null) {
				String error = new String(
						"Error: Cita ya existente " + citaExistente.getIdCliente() + " " + citaExistente.getDtmCita());
				log.error(error);
				return error;
			}

			Cita nuevaCita = new Cita();
			nuevaCita.setUsuarioCliente(usuarioCliente);
			nuevaCita.setUsuarioResponsable(usuarioResponsable);
			nuevaCita.setDtmCita(citaDto.getDtmCita());
			nuevaCita.setIntCambiosCita(0);
			nuevaCita.setIntEstado(1);
			nuevaCita.setStrObservaciones(citaDto.getStrObservaciones());

			repository.save(nuevaCita);
			mvtoService.crearMovimiento(nuevaCita, "Creacion de cita. " + nuevaCita.getDtmCita());
			return new String("OK");
		} catch (Exception e) {
			log.error(
					"Error ejecutando servicio crearCita() " + citaDto.getIdCliente() + " " + citaDto.getDtmCita() + e);
			return new String("Error");
		}
	}

	@Override
	public CitaDto obtenerCitaPorInfo(Long idCliente, Long idResponsable, Date dtmFechaCita, Integer intEstado) {
		try {
			List<Cita> listCitas = repository.getCitaPorDetalle(idCliente, idResponsable, dtmFechaCita);
			return convertirCitaADto(listCitas).get(0);
		} catch (Exception e) {
			log.error("Error ejecutando servicio obtenerCitaPorInfo() " + idCliente + " " + dtmFechaCita + e);
			return null;
		}
	}

	@Override
	public String actualizarCita(CitaDto citaDto, Long idCita) {
		try {
			if (citaDto != null) {
				Cita citaExistente = obtenerCita(idCita);
				if (citaExistente != null) {
					Integer totalCambiosCitas = citaExistente.getIntCambiosCita();
					if (citaExistente.getIntEstado() == 2 || totalCambiosCitas > 1) {
						String error = new String("Cita no se puede modificar " + citaExistente.getIdCita() + " "
								+ citaExistente.getDtmCita());
						log.error(error);
						return error;
					}

					if (citaExistente.getDtmCita().compareTo(citaDto.getDtmCita()) == 0) {
						String error = new String(
								"Cita sin cambios " + citaExistente.getIdCita() + " " + citaExistente.getDtmCita());
						log.error(error);
						return error;
					}

					citaExistente.setDtmCita(citaDto.getDtmCita());
					citaExistente.setIntCambiosCita(totalCambiosCitas++);
					citaExistente.setIntEstado(1);
					citaExistente.setStrObservaciones(citaDto.getStrObservaciones());

					repository.save(citaExistente);
					mvtoService.crearMovimiento(citaExistente, "Modificacion Cita. " + citaExistente.getDtmCita());
					return new String("OK");
				}
			}
		} catch (Exception e) {
			log.error("Error ejecutando servicio obtenerCitaPorInfo() " + citaDto.getIdCliente() + " "
					+ citaDto.getDtmCita() + e);
		}
		return new String("Error: Cita sin cambios.");
	}

	private Cita obtenerCita(Long idCita) {
		try {
			Optional<Cita> citaOpt = repository.findById(idCita);
			if (citaOpt.isPresent()) {
				return citaOpt.get();
			}
		} catch (Exception e) {
			log.error("Error ejecutando servicio obtenerCitaPorId() " + idCita + e);
		}
		return null;
	}

	@Override
	public void cancelarCita(Long idCita) {
		try {
			Cita cita = obtenerCita(idCita);
			if (cita != null) {
				cita.setIntEstado(2);
				mvtoService.crearMovimiento(cita, "Cancelacion de cita. " + cita.getDtmCita());
			}
		} catch (Exception e) {
			log.error("Error ejecutando servicio cancelarCita() " + idCita + e);
		}
	}

	@Override
	public CitaDto obtenerCitaPorId(Long idCita) {
		try {
			Cita cita = obtenerCita(idCita);
			if (cita != null) {
				CitaDto citaDto = new CitaDto();
				citaDto.setIdCita(cita.getIdCita());
				citaDto.setDtmCita(cita.getDtmCita());
				citaDto.setIdCliente(cita.getUsuarioCliente().getIdDocumento());
				citaDto.setIdResponsable(cita.getUsuarioResponsable().getIdDocumento());
				citaDto.setIntCambiosCita(cita.getIntCambiosCita());
				citaDto.setIntEstado(cita.getIntEstado());
				citaDto.setStrObservaciones(cita.getStrObservaciones());
				citaDto.setDtmCreacionCita(cita.getDtmCreacionReserva());
				return citaDto;
			}
		} catch (Exception e) {
			log.error("Error ejecutando servicio obtenerCitaPorId() " + idCita + e);
		}
		return null;
	}

//	TODO: Agregar servicio para validar citas a la misma hora que la requerida
//	Esto iria en el actualizar y crear cita

}
