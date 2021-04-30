package ua.nure.service;

import ua.nure.dto.FloristShopDto;
import ua.nure.dto.SmartSystemDto;
import ua.nure.dto.StorageRoomDto;
import ua.nure.entity.user.FlowerStorage;

import java.util.List;
import java.util.Set;

public interface FloristShopService {

    FloristShopDto create(FloristShopDto floristShopDto);

    FloristShopDto update(FloristShopDto floristShopDto);

    void delete(FloristShopDto floristShopDto);

    List<FloristShopDto> findAll();

    FloristShopDto findByPhoneNumber(String phoneNumber);

    FloristShopDto findByEmail(String email);

    FloristShopDto findById(Long id);

    Set<StorageRoomDto> findAllStorageRooms(String email);

    StorageRoomDto addStorageRoom(StorageRoomDto storageRoomDto, String email);

    StorageRoomDto updateRoom(StorageRoomDto storageRoomDto, String email);

    StorageRoomDto findStorageRoomById(Long id);

    void deleteStorageRoom(StorageRoomDto room);

    List<FlowerStorage> updateSmartDevice(SmartSystemDto smartDeviceDto);
}
