package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import ua.nure.dto.FloristShopDto;
import ua.nure.dto.FlowerStorageRequestDto;
import ua.nure.dto.FlowerStorageResponseDto;
import ua.nure.service.FlowerService;
import ua.nure.service.FlowerStorageService;
import ua.nure.service.FloristShopService;

import javax.validation.Valid;

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

//    @GetMapping("/room/{id}")
//    @ApiOperation(value = "Finds all contracts for cleaning company", nickname = "getAllStoragesByRoom")
//    public ResponseEntity<?> getAllContractsByCleaningCompany(@PathVariable String id) {
//        CleaningCompanyDto cleaningCompany = flowerService.findByEmail(email);
//        if (cleaningCompany == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(flowerStorageService.getAllStoragesByRoom(email));
//    }
//
//    @GetMapping("/flower/{id}")
//    @ApiOperation(value = "Finds all contracts for customer company", nickname = "getAllStoragesByFlower")
//    public ResponseEntity<?> getAllContractsByCustomerCompany(@PathVariable String id) {
//        FloristShopDto customerCompany = floristShopService.findByEmail(email);
//        if (customerCompany == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(flowerStorageService.getAllStoragesByFlower(email));
//    }

    @PostMapping
    @ApiOperation(value = "Creates new flower storage in storage room", nickname = "createStorage")
    public ResponseEntity<?> createStorage(
            @Valid @RequestBody FlowerStorageRequestDto flowerStorageRequestDto,
            BindingResult bindingResult
    ) {
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
            return ResponseEntity.
                    status(HttpStatus.NOT_FOUND)
                    .body("Flower type does not exist");
        }

        try {
            return ResponseEntity.ok(flowerStorageService.create(flowerStorageRequestDto));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value = "Updates storage (Flower Storage ID must be present, updates only amount)", nickname = "updateStorage")
    public ResponseEntity<?> updateStorage(
            @Valid @RequestBody FlowerStorageRequestDto flowerStorageRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        FlowerStorageResponseDto updatedContract = null;

        try {
            updatedContract = flowerStorageService.update(flowerStorageRequestDto);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
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
