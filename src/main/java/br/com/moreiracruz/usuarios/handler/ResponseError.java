package br.com.moreiracruz.usuarios.handler;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseError {
    private Date timestamp;
    private String status;
    private int statusCode;
    private String error;
}
