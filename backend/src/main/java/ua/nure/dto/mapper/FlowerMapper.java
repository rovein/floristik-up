package ua.nure.dto.mapper;

import ua.nure.dto.FlowerDto;
import ua.nure.entity.Flower;

public class FlowerMapper {

    public static FlowerDto toFlowerDto(Flower flower) {
        return new FlowerDto()
                .setId(flower.getId())
                .setName(flower.getName())
                .setColor(flower.getColor())
                .setMinTemperature(flower.getMinTemperature())
                .setMaxTemperature(flower.getMaxTemperature())
                .setShelfLife(flower.getShelfLife());

    }

    public static Flower toFlower(FlowerDto flowerDto) {
        return new Flower()
                .setId(flowerDto.getId() == null ? 0 : flowerDto.getId())
                .setName(flowerDto.getName())
                .setColor(flowerDto.getColor())
                .setMinTemperature(flowerDto.getMinTemperature())
                .setMaxTemperature(flowerDto.getMaxTemperature())
                .setShelfLife(flowerDto.getShelfLife());

    }
}
