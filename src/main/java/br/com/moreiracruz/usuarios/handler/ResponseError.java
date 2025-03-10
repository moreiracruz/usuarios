package br.com.moreiracruz.usuarios.handler;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseError {
    private LocalDateTime timestamp;
    private String status;
    private int statusCode;
    private String error;
}
