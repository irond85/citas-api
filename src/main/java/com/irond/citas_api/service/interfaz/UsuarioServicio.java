package com.irond.citas_api.service.interfaz;

import java.util.List;

import com.irond.citas_api.dto.UsuarioDto;
import com.irond.citas_api.model.Usuario;

public interface UsuarioServicio {

	List<UsuarioDto> obtenerUsuarios() throws Exception;

	void crearUsuario(UsuarioDto usuarioDto) throws Exception;

	UsuarioDto obtenerUsuarioDtoPorId(Long idUsuario);
	
	Usuario obtenerUsuarioPorId(Long idUsuario);

}
