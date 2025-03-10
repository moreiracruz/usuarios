package br.com.moreiracruz.usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
	@Schema(description = "id")
	private Long id;

	@Schema(description = "nome")
	@Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres")
	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@Schema(description = "senha")
	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
	private String senha;
}
