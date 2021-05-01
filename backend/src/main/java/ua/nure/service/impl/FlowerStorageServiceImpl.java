package ua.nure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.dto.FlowerStorageInfo;
import ua.nure.dto.FlowerStorageRequestDto;
import ua.nure.dto.FlowerStorageResponseDto;
import ua.nure.dto.mapper.FlowerStorageMapper;
import ua.nure.entity.*;
import ua.nure.entity.user.FlowerStorage;
import ua.nure.repository.*;
import ua.nure.service.FlowerStorageService;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class FlowerStorageServiceImpl implements FlowerStorageService {

    @Autowired
    private FlowerStorageRepository flowerStorageRepository;

    @Autowired
    private StorageRoomRepository storageRoomRepository;

    @Autowired
    private FlowerRepository flowerRepository;

    @Autowired
    private FloristShopRepository floristShopRepository;

    @Override
    public FlowerStorageResponseDto create(FlowerStorageRequestDto
            flowerStorageRequestDto) throws Exception {
        FlowerStorage flowerStorage = new FlowerStorage();
        flowerStorage.setStartDate(new Date());

        Optional<StorageRoom> roomById =
                storageRoomRepository.findById(flowerStorageRequestDto.getStorageRoomId());
        Optional<Flower> flowerById =
                flowerRepository.findById(flowerStorageRequestDto.getFlowerId());

        if (roomById.isPresent() && flowerById.isPresent()) {
            StorageRoom storageRoom = roomById.get();
            Flower flower = flowerById.get();

            flowerStorage.setStorageRoom(storageRoom);
            flowerStorage.setFlower(flower);
            flowerStorage.setAmount(flowerStorageRequestDto.getAmount());

            SmartSystem smartSystem = storageRoom.getSmartSystem();

            String cause = "";

            Double actualTemperature = smartSystem.getTemperature();
            Integer minTemperature = flower.getMinTemperature();
            Integer maxTemperature = flower.getMaxTemperature();

            if (actualTemperature < minTemperature ||
                    actualTemperature > maxTemperature) {
                cause = "Незадовільний мікроклімат для зберігання. "
                        + "Температура в приміщенні: " + actualTemperature +
                        " °С, діапазон зберігання квітки: " + minTemperature
                        + "-" + maxTemperature + "°С.";
            }

            int actualAmount = storageRoom.getFlowerStorages().stream()
                    .mapToInt(FlowerStorage::getAmount).sum();
            int newAmount = actualAmount + flowerStorage.getAmount();
            int maxAmount = storageRoom.getMaxCapacity();

            if (newAmount > maxAmount) {
                cause = "Недостатня місткість приміщення. "
                        + "Максимальна: " + maxAmount + ", теперішня: "
                        + actualAmount + ", очікувана: " + newAmount;
            }

            if (!cause.isEmpty()) {
                throw new Exception(cause);
            }

            return FlowerStorageMapper.toFlowerStorageResponseDto(
                    flowerStorageRepository.save(flowerStorage)
            );
        }

        return null;
    }

    @Override
    public FlowerStorageResponseDto update(
            FlowerStorageRequestDto flowerStorageRequestDto) throws Exception {
        Optional<FlowerStorage> contractById = flowerStorageRepository.findById(
                flowerStorageRequestDto.getId());

        if (contractById.isPresent()) {
            FlowerStorage flowerStorage = contractById.get();

            int actualAmount = flowerStorage.getStorageRoom().getFlowerStorages()
                    .stream().mapToInt(FlowerStorage::getAmount).sum();
            int newAmount = flowerStorageRequestDto.getAmount() - flowerStorage.getAmount() + actualAmount;
            int maxAmount = flowerStorage.getStorageRoom().getMaxCapacity();

            if (newAmount > maxAmount) {
               String cause = "Недостатня місткість приміщення. "
                        + "Максимальна: " + maxAmount + ", теперішня: "
                        + actualAmount + ", очікувана: " + newAmount;
               throw new Exception(cause);
            }

            flowerStorage.setAmount(flowerStorageRequestDto.getAmount());
            return FlowerStorageMapper.toFlowerStorageResponseDto(
                    flowerStorageRepository.save(flowerStorage));
        }

        return null;

    }

    @Override
    public void delete(Long id) {
        flowerStorageRepository.deleteById(id);
    }

    @Override
    public FlowerStorageResponseDto findById(Long id) {
        Optional<FlowerStorage> contract = flowerStorageRepository.findById(id);
        return contract.map(FlowerStorageMapper::toFlowerStorageResponseDto).orElse(null);
    }

    @Override
    public Set<FlowerStorageInfo> getAllStoragesByRoom(Long roomId) {
        Optional<StorageRoom> storageRoom = storageRoomRepository.findById(roomId);
        if (storageRoom.isPresent()) {
            return flowerStorageRepository.getAllStoragesByRoom(roomId);
        }
        return Collections.emptySet();
    }
}
