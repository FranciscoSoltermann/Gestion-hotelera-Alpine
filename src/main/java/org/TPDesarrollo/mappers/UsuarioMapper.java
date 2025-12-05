package org.TPDesarrollo.mappers;

import org.TPDesarrollo.clases.Usuario;
import org.TPDesarrollo.dtos.UsuarioDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        if (usuario.getRol() == null) {
            usuario.setRol("ADMIN");
        }
        return usuario;
    }
}