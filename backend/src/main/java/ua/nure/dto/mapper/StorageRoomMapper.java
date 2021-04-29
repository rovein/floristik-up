package ua.nure.dto.mapper;

import ua.nure.dto.StorageRoomDto;
import ua.nure.entity.StorageRoom;

public class StorageRoomMapper {

    public static StorageRoomDto toStorageRoomDto(StorageRoom storageRoom) {
        return new StorageRoomDto()
                .setId(storageRoom.getId())
                .setCity(storageRoom.getCity())
                .setStreet(storageRoom.getStreet())
                .setHouse(storageRoom.getHouse())
                .setActualCapacity(storageRoom.getActualCapacity())
                .setMaxCapacity(storageRoom.getMaxCapacity())
                .setSmartDevice(SmartSystemMapper.toSmartSystemDto(storageRoom.getSmartSystem()));
    }

    public static StorageRoom toStorageRoom(StorageRoomDto storageRoomDto) {
        return new StorageRoom()
                .setId(storageRoomDto.getId() == null ? 0 : storageRoomDto.getId())
                .setCity(storageRoomDto.getCity())
                .setStreet(storageRoomDto.getStreet())
                .setHouse(storageRoomDto.getHouse())
                .setMaxCapacity(storageRoomDto.getMaxCapacity())
                .setSmartSystem(SmartSystemMapper.toSmartSystem(storageRoomDto.getSmartDevice()));

    }

}
