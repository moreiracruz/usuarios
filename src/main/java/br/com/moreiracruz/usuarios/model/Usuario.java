package br.com.moreiracruz.usuarios.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O nome é obrigatório")
	@Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
	private String nome;

	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
	private String senha;

	@CreationTimestamp
	private LocalDateTime criacao;

	@UpdateTimestamp
	private LocalDateTime edicao;
}
