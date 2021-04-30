package ua.nure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.nure.dto.FloristShopDto;
import ua.nure.dto.SmartSystemDto;
import ua.nure.dto.StorageRoomDto;
import ua.nure.dto.mapper.FloristShopMapper;
import ua.nure.dto.mapper.FlowerMapper;
import ua.nure.dto.mapper.FlowerStorageMapper;
import ua.nure.dto.mapper.StorageRoomMapper;
import ua.nure.entity.*;
import ua.nure.entity.value.UserRole;
import ua.nure.repository.FloristShopRepository;
import ua.nure.repository.FlowerStorageRepository;
import ua.nure.repository.StorageRoomRepository;
import ua.nure.service.FloristShopService;
import ua.nure.service.RoleService;

import java.util.*;

@Service
public class FloristShopServiceImpl implements FloristShopService {

    @Autowired
    private FloristShopRepository floristShopRepository;

    @Autowired
    private StorageRoomRepository storageRoomRepository;

    @Autowired
    private FlowerStorageRepository flowerStorageRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public FloristShopDto create(FloristShopDto floristShopDto) {
        FloristShop floristShop = toFloristShop(floristShopDto,
                new FloristShop());

        return FloristShopMapper
                .toFloristShopDto(floristShopRepository.save(floristShop));
    }

    @Override
    public FloristShopDto update(FloristShopDto floristShopDto) {
        Optional<FloristShop> floristShop = floristShopRepository
                .findByEmail(floristShopDto.getEmail());

        if (floristShop.isPresent()) {
            FloristShop floristShopModel = toFloristShop(floristShopDto,
                    floristShop.get());

            return FloristShopMapper.toFloristShopDto(
                    floristShopRepository.save(floristShopModel));
        }

        return null;
    }

    @Override
    public void delete(FloristShopDto floristShopDto) {
        floristShopRepository.deleteById(floristShopDto.getId());
    }

    @Override
    public List<FloristShopDto> findAll() {
        List<FloristShop> floristShops = floristShopRepository.findAll();
        List<FloristShopDto> floristShopsDto = new ArrayList<>();

        for (FloristShop floristShop : floristShops) {
            floristShopsDto
                    .add(FloristShopMapper.toFloristShopDto(floristShop));
        }

        return floristShopsDto;
    }

    @Override
    public FloristShopDto findByPhoneNumber(String phoneNumber) {
        Optional<FloristShop> floristShop = floristShopRepository
                .findByPhoneNumber(phoneNumber);
        return floristShop.map(FloristShopMapper::toFloristShopDto)
                .orElse(null);
    }

    @Override
    public FloristShopDto findByEmail(String email) {
        Optional<FloristShop> floristShop = floristShopRepository
                .findByEmail(email);
        return floristShop.map(FloristShopMapper::toFloristShopDto)
                .orElse(null);
    }

    @Override
    public FloristShopDto findById(Long id) {
        Optional<FloristShop> floristShop = floristShopRepository.findById(id);
        return floristShop.map(FloristShopMapper::toFloristShopDto)
                .orElse(null);
    }

    @Override
    public Set<StorageRoomDto> findAllStorageRooms(String email) {
        Optional<FloristShop> company = floristShopRepository
                .findByEmail(email);
        if (company.isPresent()) {
            Set<StorageRoom> storageRooms = company.get().getStorageRooms();
            Set<StorageRoomDto> roomsDto = new HashSet<>();

            for (StorageRoom storageRoom : storageRooms) {
                roomsDto.add(StorageRoomMapper.toStorageRoomDto(storageRoom));
            }

            return roomsDto;
        }
        return Collections.emptySet();
    }

    @Override
    public StorageRoomDto addStorageRoom(StorageRoomDto storageRoomDto,
            String email) {
        Optional<FloristShop> floristShop = floristShopRepository
                .findByEmail(email);

        return saveRoom(storageRoomDto, floristShop);
    }

    @Override
    public StorageRoomDto updateRoom(StorageRoomDto storageRoomDto,
            String email) {

        Optional<FloristShop> company = floristShopRepository
                .findByEmail(email);

        if (company.isPresent()) {
            Optional<StorageRoom> optionalRoom = storageRoomRepository
                    .findById(storageRoomDto.getId());

            if (optionalRoom.isPresent()) {
                return saveRoom(storageRoomDto, company);
            }
        }

        return null;
    }

    @Override
    public void deleteStorageRoom(StorageRoomDto room) {
        storageRoomRepository.deleteById(room.getId());
    }

    @Override
    public StorageRoomDto findStorageRoomById(Long id) {
        Optional<StorageRoom> room = storageRoomRepository.findById(id);
        return room.map(StorageRoomMapper::toStorageRoomDto).orElse(null);
    }

    public List<FlowerStorage> updateSmartDevice(
            SmartSystemDto smartDeviceDto) {
        Optional<StorageRoom> storageRoomOptional = storageRoomRepository
                .findById(smartDeviceDto.getId());

        if (storageRoomOptional.isPresent()) {
            StorageRoom storageRoom = storageRoomOptional.get();
            SmartSystem smartDevice = storageRoom.getSmartSystem();

            ArrayList<FlowerStorage> updatedStorages = new ArrayList<>();

            storageRoom.getFlowerStorages().forEach(flowerStorage -> {
                Flower flower = flowerStorage.getFlower();

                Integer minTemperature = flower.getMinTemperature();
                Integer maxTemperature = flower.getMaxTemperature();
                Double currentTemperature = smartDeviceDto.getTemperature();

                if (currentTemperature < minTemperature
                        || currentTemperature > maxTemperature) {
                    Set<StorageRoom> storageRooms = storageRoom.getFloristShop()
                            .getStorageRooms();

                    storageRooms.stream().filter(room -> {
                        Double temperature = room.getSmartSystem()
                                .getTemperature();

                        return temperature > minTemperature
                                && temperature < maxTemperature;
                    }).filter(room -> {
                        int actualAmount = room.getFlowerStorages().stream()
                                .mapToInt(FlowerStorage::getAmount).sum();
                        int newAmount =
                                actualAmount + flowerStorage.getAmount();
                        int maxAmount = room.getMaxCapacity();
                        return newAmount < maxAmount;
                    }).findAny().ifPresent(newStorageRoom -> {
                        flowerStorage.setStorageRoom(newStorageRoom);
                        updatedStorages.add(flowerStorageRepository
                                .save(flowerStorage));
                    });
                }
            });

            smartDevice.setAirQuality(smartDeviceDto.getAirQuality())
                    .setTemperature(smartDeviceDto.getTemperature())
                    .setHumidity(smartDeviceDto.getHumidity())
                    .setSatisfactionFactor(
                            smartDeviceDto.getSatisfactionFactor());
            storageRoom.setSmartSystem(smartDevice);

            storageRoomRepository.save(storageRoom);
            return updatedStorages;
        }

        return null;
    }

    private StorageRoomDto saveRoom(StorageRoomDto storageRoomDto,
            Optional<FloristShop> company) {
        StorageRoom storageRoom = StorageRoomMapper
                .toStorageRoom(storageRoomDto);
        SmartSystem smartSystem = storageRoom.getSmartSystem();

        if (smartSystem != null) {
            smartSystem.setStorageRoom(storageRoom);
            smartSystem.setId(storageRoom.getId());
            setIndicators(smartSystem);

        }
        company.ifPresent(storageRoom::setFloristShop);

        return StorageRoomMapper
                .toStorageRoomDto(storageRoomRepository.save(storageRoom));
    }

    private void setIndicators(SmartSystem smartSystem) {
        if (smartSystem.getSatisfactionFactor() == null) {
            smartSystem.setSatisfactionFactor(0.0);
        }

        if (smartSystem.getAirQuality() == null) {
            smartSystem.setAirQuality(0.0);
        }

        if (smartSystem.getHumidity() == null) {
            smartSystem.setHumidity(0.0);
        }

        if (smartSystem.getTemperature() == null) {
            smartSystem.setTemperature(0.0);
        }
    }

    private FloristShop toFloristShop(FloristShopDto floristShopDto,
            FloristShop floristShop) {
        floristShop.setEmail(floristShopDto.getEmail());
        floristShop.setPhoneNumber(floristShopDto.getPhoneNumber());
        floristShop.setPassword(
                bCryptPasswordEncoder.encode(floristShopDto.getPassword()));
        floristShop.setName(floristShopDto.getName());
        floristShop.setCreationDate(floristShopDto.getCreationDate());
        floristShop.setRole(roleService.findByName(UserRole.USER));

        return floristShop;
    }

}
