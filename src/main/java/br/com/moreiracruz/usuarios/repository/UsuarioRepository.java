package br.com.moreiracruz.usuarios.repository;

import br.com.moreiracruz.usuarios.model.Usuario;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByLogin(String login);
}
