package br.com.moreiracruz.usuarios.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.moreiracruz.usuarios.dto.UsuarioDTO;
import br.com.moreiracruz.usuarios.dto.UsuarioMapper;
import br.com.moreiracruz.usuarios.exception.ResourceNotFoundException;
import br.com.moreiracruz.usuarios.model.Usuario;
import br.com.moreiracruz.usuarios.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceUnitTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_deveRetornarListaVaiza() {
        when(repository.findAll()).thenReturn(List.of());
        assertNotNull(service.findAll());
    }

    @Test
    void findById_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void findByNome_deveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findByNome("Nome")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findByNome("Nome"));
    }

    @Test
    void save_deveRetornarUsuarioDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        Usuario usuario = new Usuario();
        when(mapper.toEntity(dto)).thenReturn(usuario);
        when(repository.save(usuario)).thenReturn(usuario);
        when(mapper.toDTO(usuario)).thenReturn(dto);

        assertEquals(dto, service.save(dto));
    }

    @Test
    void update_deveLancarExcecaoQuandoUsuarioNaoExiste() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Atualizado");
        dto.setSenha("novaSenha");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, dto));
    }

    @Test
    void delete_deveChamarRepositoryDelete() {
        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}