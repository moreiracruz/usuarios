package br.com.moreiracruz.usuarios.service;

import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario criar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Integer id, Usuario usuarioDetails) {
        Usuario usuario = buscarPorId(id);
        usuario.setLogin(usuarioDetails.getLogin());
        usuario.setPassword(usuarioDetails.getPassword());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletar(Integer id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
