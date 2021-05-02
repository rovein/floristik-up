package ua.nure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import ua.nure.dto.*;
import ua.nure.entity.user.FlowerStorage;
import ua.nure.service.FlowerService;
import ua.nure.service.FlowerStorageService;
import ua.nure.service.FloristShopService;

import javax.validation.Valid;

import java.util.Set;
import java.util.stream.Collectors;

import static ua.nure.validation.BindingResultValidator.errorBody;

@CrossOrigin
@RestController
@RequestMapping("/flower-storages")
@Api(tags = "Flower Storage")
public class FlowerStorageController {

    @Autowired
    private FlowerStorageService flowerStorageService;

    @Autowired
    private FlowerService flowerService;

    @Autowired
    private FloristShopService floristShopService;

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds flower storage by id", nickname = "getFlowerStorageById")
    public ResponseEntity<?> getFlowerStorageById(@PathVariable Long id) {
        FlowerStorageResponseDto flowerStorageResponseDto = flowerStorageService
                .findById(id);
        if (flowerStorageResponseDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(flowerStorageResponseDto);
    }

    @GetMapping("/storage-room/{id}")
    @ApiOperation(value = "Finds all storages for room", nickname = "getAllFlowerStoragesByRoom")
    public ResponseEntity<?> getAllFlowerStoragesByRoom(@PathVariable Long id) {
        StorageRoomDto storageRoom = floristShopService.findStorageRoomById(id);
        if (storageRoom == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(flowerStorageService.getAllStoragesByRoom(id)
                .stream()
                .map(FlowerStorageInfoDto::new)
                .map(infoDto -> infoDto.setActualCapacity(storageRoom.getActualCapacity()))
                .collect(Collectors.toSet())
        );
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "Finds flower storage info by id", nickname = "getStorageInfoById")
    public ResponseEntity<?> getStorageInfoById(@PathVariable Long id) {
        FlowerStorageResponseDto flowerStorage = flowerStorageService.findById(id);
        if (flowerStorage == null) {
            return ResponseEntity.notFound().build();
        }
        Long storageRoomId = flowerStorage.getStorageRoomId();
        StorageRoomDto storageRoom = floristShopService.findStorageRoomById(storageRoomId);

        return ResponseEntity.ok(
                new FlowerStorageInfoDto(
                        flowerStorageService.getStorageInfoByStorageId(id)
                ).setActualCapacity(storageRoom.getActualCapacity())
        );
    }

    @PostMapping
    @ApiOperation(value = "Creates new flower storage in storage room", nickname = "createStorage")
    public ResponseEntity<?> createStorage(
            @Valid @RequestBody FlowerStorageRequestDto flowerStorageRequestDto,
            BindingResult bindingResult
    ) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        if (floristShopService.findStorageRoomById(flowerStorageRequestDto.getStorageRoomId()) == null) {
            return ResponseEntity.
                    status(HttpStatus.NOT_FOUND)
                    .body("Storage room does not exist");
        }

        if (flowerService.findById(
                flowerStorageRequestDto.getFlowerId()) == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Flower type does not exist");
        }

        try {
            return ResponseEntity.ok(flowerStorageService.create(flowerStorageRequestDto));
        } catch (Exception ex) {
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorDto error = new ErrorDto(ex.getMessage());
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(error));
        }
    }

    @PutMapping
    @ApiOperation(value = "Updates storage (Flower Storage ID must be present, updates only amount)", nickname = "updateStorage")
    public ResponseEntity<?> updateStorage(
            @Valid @RequestBody FlowerStorageRequestDto flowerStorageRequestDto,
            BindingResult bindingResult
    ) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        FlowerStorageResponseDto updatedContract = null;

        try {
            updatedContract = flowerStorageService.update(flowerStorageRequestDto);
        } catch (Exception ex) {
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorDto error = new ErrorDto(ex.getMessage());
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(error));
        }

        if (updatedContract == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedContract);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes storage by ID", nickname = "deleteStorage")
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        FlowerStorageResponseDto flowerStorageResponseDto = flowerStorageService
                .findById(id);

        if (flowerStorageResponseDto == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        flowerStorageService.delete(flowerStorageResponseDto.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
