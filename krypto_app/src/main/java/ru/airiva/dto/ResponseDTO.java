package ru.airiva.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.airiva.exception.BsException;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseDTO.class);

    private String errorMessage;

    public ResponseDTO() {
    }

    public ResponseDTO(BsException e) {
        errorMessage = e.getMessage();
    }

    public ResponseDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
