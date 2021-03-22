package ua.nure.dto.mapper;

import ua.nure.dto.SmartSystemDto;
import ua.nure.entity.SmartSystem;

public class SmartSystemMapper {

    public static SmartSystemDto toSmartSystemDto(SmartSystem smartSystem) {

        if (smartSystem == null) {
            return new SmartSystemDto();
        }

        return new SmartSystemDto()
                .setId(smartSystem.getId())
                .setAirQuality(smartSystem.getAirQuality())
                .setHumidity(smartSystem.getHumidity())
                .setSatisfactionFactor(smartSystem.getSatisfactionFactor())
                .setTemperature(smartSystem.getTemperature());
    }

    public static SmartSystem toSmartSystem(SmartSystemDto smartSystemDto) {

        if (smartSystemDto == null) {
            return new SmartSystem();
        }

        return new SmartSystem()
                .setAirQuality(smartSystemDto.getAirQuality())
                .setHumidity(smartSystemDto.getHumidity())
                .setSatisfactionFactor(smartSystemDto.getSatisfactionFactor())
                .setTemperature(smartSystemDto.getTemperature());
    }
}
