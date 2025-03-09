package br.com.moreiracruz.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.moreiracruz.usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByLogin(String login);
}
