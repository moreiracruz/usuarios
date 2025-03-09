package br.com.moreiracruz.usuarios.handler;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ResponseError {
    private Date timestamp;
    private String status;
    private int statusCode;
    private String error;
}
