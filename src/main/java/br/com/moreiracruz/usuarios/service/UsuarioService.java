package br.com.moreiracruz.usuarios.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.dto.UsuarioMapper;
import br.com.moreiracruz.usuarios.exception.ResourceNotFoundException;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioMapper mapper;

	public List<UsuarioDTO> findAll() {
		return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
	}

	public UsuarioDTO findById(Long id) {
		Usuario usuario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
		return mapper.toDTO(usuario);
	}

	public UsuarioDTO findByNome(String nome) {
		Usuario usuario = repository.findByNome(nome)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
		return mapper.toDTO(usuario);
	}

	public UsuarioDTO save(UsuarioDTO usuarioDTO) {
		Usuario usuario = mapper.toEntity(usuarioDTO);
		usuario = repository.save(usuario);
		return mapper.toDTO(usuario);
	}

	public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
		Usuario usuario = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
		usuario.setNome(usuarioDTO.getNome());
		usuario.setSenha(usuarioDTO.getSenha());
		usuario = repository.save(usuario);
		return mapper.toDTO(usuario);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
}