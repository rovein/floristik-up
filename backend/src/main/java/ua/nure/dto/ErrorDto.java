package ua.nure.dto;

import lombok.Data;

@Data
public class ErrorDto {

    public ErrorDto(String error) {
        this.error = error;
    }
    private String error;
}
