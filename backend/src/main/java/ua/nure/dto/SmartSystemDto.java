package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SmartSystemDto {

    private Long id;

    private Double temperature;

    private Double humidity;

    private Double airQuality;

    private Double satisfactionFactor;

}
