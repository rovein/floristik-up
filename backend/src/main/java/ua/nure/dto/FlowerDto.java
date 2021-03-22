package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FlowerDto {
    private Long id;

    private String name;

    private String color;

    private Integer shelfLife;

    private Integer minTemperature;

    private Integer maxTemperature;
}
