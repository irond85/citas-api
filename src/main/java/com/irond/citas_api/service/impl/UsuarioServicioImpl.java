package com.irond.citas_api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irond.citas_api.dto.UsuarioDto;
import com.irond.citas_api.model.Usuario;
import com.irond.citas_api.repository.UsuarioRepository;
import com.irond.citas_api.service.interfaz.UsuarioServicio;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioServicioImpl implements UsuarioServicio {

	@Autowired
	private UsuarioRepository repository;

	@Override
	public List<UsuarioDto> obtenerUsuarios() throws Exception {
		List<UsuarioDto> listUsuariosDto = new ArrayList<UsuarioDto>();
		try {
			List<Usuario> listUsuarios = repository.findAll();

			for (Usuario usuario : listUsuarios) {
				UsuarioDto usuarioReturn = new UsuarioDto();
				usuarioReturn.setIdDocumento(usuario.getIdDocumento());
				usuarioReturn.setStrNombre(usuario.getStrNombre());
				usuarioReturn.setStrTelefono(usuario.getStrTelefono());
				usuarioReturn.setStrCorreo(usuario.getStrCorreo());
				usuarioReturn.setIdRolUsuario(usuario.getIdRolUsuario());
				usuarioReturn.setIntNroCitas(usuario.getListCitas().size());

				usuarioReturn.setIntNroServicios(
						usuarioReturn.getIdRolUsuario() == 0 ? usuario.getListServicios().size() : null);

				listUsuariosDto.add(usuarioReturn);
			}
		} catch (Exception e) {
			log.error("Error ejecutando servicio obtenerUsuarios() ", e);
			throw new Exception();
		}
		return listUsuariosDto;
	}

	@Override
	public UsuarioDto crearUsuario(UsuarioDto usuarioDto) throws Exception {
		try {
			Usuario usuarioExistente = this.obtenerUsuarioPorId(usuarioDto.getIdDocumento());
			if (usuarioExistente != null) {
				log.error("Usuario ya existente " + usuarioDto.getIdDocumento());
				return null;
			}

			Usuario usuarioNuevo = new Usuario();
			usuarioNuevo.setIdDocumento(usuarioDto.getIdDocumento());
			usuarioNuevo.setStrNombre(usuarioDto.getStrNombre());
			usuarioNuevo.setStrTelefono(usuarioDto.getStrTelefono());
			usuarioNuevo.setStrCorreo(usuarioDto.getStrCorreo());
			usuarioNuevo.setIdRolUsuario(usuarioDto.getIdRolUsuario());

			repository.save(usuarioNuevo);
			return usuarioDto;
		} catch (Exception e) {
			log.error("Error ejecutando servicio crearUsuario() " + usuarioDto.getIdDocumento(), e);
			throw new Exception();
		}

	}

	@Override
	public UsuarioDto obtenerUsuarioDtoPorId(Long idUsuario) {
		try {
			Optional<Usuario> usuarioExistente = repository.findById(idUsuario);

			if (usuarioExistente.isPresent()) {
				UsuarioDto usuarioReturn = new UsuarioDto();
				Usuario usuario = usuarioExistente.get();

				usuarioReturn.setIdDocumento(usuario.getIdDocumento());
				usuarioReturn.setStrNombre(usuario.getStrNombre());
				usuarioReturn.setStrTelefono(usuario.getStrTelefono());
				usuarioReturn.setStrCorreo(usuario.getStrCorreo());
				usuarioReturn.setIdRolUsuario(usuario.getIdRolUsuario());
				usuarioReturn.setIntNroCitas(usuario.getListCitas().size());

				usuarioReturn.setIntNroServicios(
						usuarioReturn.getIdRolUsuario() == 0 ? usuario.getListServicios().size() : null);
				return usuarioReturn;
			}
		} catch (Exception e) {
			log.error("Error ejecutando servicio obtenerUsuarioDtoPorId() " + idUsuario, e);
		}
		return null;
	}

	@Override
	public Usuario obtenerUsuarioPorId(Long idUsuario) {
		try {
			Optional<Usuario> usuarioExistente = repository.findById(idUsuario);
			if (usuarioExistente.isPresent()) {
				return usuarioExistente.get();
			}
		} catch (Exception e) {
			log.error("Error ejecutando servicio obtenerUsuarioPorId() " + idUsuario, e);
		}
		return null;

	}

}
