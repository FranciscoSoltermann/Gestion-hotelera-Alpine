package org.TPDesarrollo.mappers;

import org.TPDesarrollo.entity.Usuario;
import org.TPDesarrollo.dto.UsuarioDTO;
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