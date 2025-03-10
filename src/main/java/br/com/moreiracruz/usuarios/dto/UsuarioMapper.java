package br.com.moreiracruz.usuarios.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.moreiracruz.usuarios.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDTO(Usuario usuario);
    Usuario toEntity(UsuarioDTO usuarioDTO);
}