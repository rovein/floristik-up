package ua.nure.dto.mapper;

import ua.nure.dto.FlowerStorageResponseDto;
import ua.nure.entity.FlowerStorage;

public class FlowerStorageMapper {

    public static FlowerStorageResponseDto toFlowerStorageResponseDto(
            FlowerStorage flowerStorage) {
        return new FlowerStorageResponseDto()
                .setId(flowerStorage.getId())
                .setStartDate(flowerStorage.getStartDate())
                .setAmount(flowerStorage.getAmount())
                .setFlowerId(flowerStorage.getFlower().getId())
                .setStorageRoomId(flowerStorage.getStorageRoom().getId());
    }
}
