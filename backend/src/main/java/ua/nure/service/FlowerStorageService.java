package ua.nure.service;

import ua.nure.dto.FlowerStorageInfo;
import ua.nure.dto.FlowerStorageRequestDto;
import ua.nure.dto.FlowerStorageResponseDto;

import java.util.Set;

public interface FlowerStorageService {

    FlowerStorageResponseDto create(FlowerStorageRequestDto flowerStorageRequestDto)
            throws Exception;

    FlowerStorageResponseDto update(FlowerStorageRequestDto flowerStorageRequestDto)
            throws Exception;

    void delete(Long id);

    FlowerStorageResponseDto findById(Long id);

    Set<FlowerStorageInfo> getAllStoragesByRoom(Long roomId);
}
