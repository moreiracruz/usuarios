package br.com.moreiracruz.usuarios.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuário", description = "API para gerenciamento de usuários")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@GetMapping
	@Operation(summary = "Listar todos os usuários")
	public ResponseEntity<List<UsuarioDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar usuário por ID")
	public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@GetMapping("/nome/{nome}")
	@Operation(summary = "Buscar usuário por nome")
	public ResponseEntity<UsuarioDTO> findByNome(@PathVariable String nome) {
		return ResponseEntity.ok(service.findByNome(nome));
	}

	@PostMapping
	@Operation(summary = "Criar um novo usuário")
	public ResponseEntity<UsuarioDTO> save(@Valid @RequestBody UsuarioDTO usuarioDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuarioDTO));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar um usuário existente")
	public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
		return ResponseEntity.ok(service.update(id, usuarioDTO));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir um usuário")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
