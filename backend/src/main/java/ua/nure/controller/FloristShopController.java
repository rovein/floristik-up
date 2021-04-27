package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.dto.FloristShopDto;
import ua.nure.dto.StorageRoomDto;
import ua.nure.service.FloristShopService;

import javax.validation.Valid;
import java.util.List;

import static ua.nure.validation.BindingResultValidator.errorBody;

@CrossOrigin
@RestController
@RequestMapping("/florist-shops")
@Api(tags = "Florist Shop")
public class FloristShopController {

    @Autowired
    private FloristShopService floristShopService;

    @GetMapping
    @ApiOperation(value = "Returns a list of all florist shops", nickname = "getAllFloristShops")
    public ResponseEntity<List<FloristShopDto>> getAllFloristShops() {
        return ResponseEntity.ok(floristShopService.findAll());
    }

    @GetMapping("/{email}")
    @ApiOperation(value = "Finds florist shop by email", nickname = "getFloristShopByEmail")
    public ResponseEntity<?> getFloristShopByEmail(@PathVariable String email) {
        FloristShopDto floristShop = floristShopService.findByEmail(email);
        if (floristShop == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(floristShop);
    }

    @PostMapping
    @ApiOperation(value = "Adds new florist shop", nickname = "addFloristShop")
    public ResponseEntity<?> addFloristShop(
            @Valid @RequestBody FloristShopDto floristShopDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }
        return ResponseEntity.ok(floristShopService.create(floristShopDto));
    }

    @PutMapping
    @ApiOperation(value = "Updates the florist shop", nickname = "updateFloristShop")
    public ResponseEntity<?> updateFloristShop(
            @Valid @RequestBody FloristShopDto floristShopDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        FloristShopDto floristShop = floristShopService.findByEmail(floristShopDto.getEmail());
        if (floristShop == null) {
            return ResponseEntity.notFound().build();
        }

        String password = floristShopDto.getPassword();
        if (password == null || password.isEmpty()) {
            floristShopDto.setPassword(floristShop.getPassword());
        }
        FloristShopDto updatedShop = floristShopService.update(floristShopDto);

        return ResponseEntity.ok(updatedShop);
    }

    @DeleteMapping("/{email}")
    @ApiOperation(value = "Deletes florist shop by email", nickname = "deleteFloristShop")
    public ResponseEntity<Void> deleteFloristShop(@PathVariable String email) {
        FloristShopDto floristShop = floristShopService.findByEmail(email);

        if (floristShop == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        floristShopService.delete(floristShop);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{email}/rooms")
    @ApiOperation(value = "Returns all florist shop storage rooms ", nickname = "getAllStorageRooms")
    public ResponseEntity<?> getAllRooms(@PathVariable String email) {
        FloristShopDto floristShop = floristShopService.findByEmail(email);
        if (floristShop == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(floristShopService.findAllStorageRooms(email));
    }

    @PostMapping("/{email}/rooms")
    @ApiOperation(value = "Adds new storage room for florist shop", nickname = "addStorageRoom")
    public ResponseEntity<?> addRoom(
            @PathVariable String email,
            @Valid @RequestBody StorageRoomDto storageRoomDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        FloristShopDto floristShop = floristShopService.findByEmail(email);

        if (floristShop == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                floristShopService.addStorageRoom(storageRoomDto, email));
    }

    @PutMapping("/{email}/rooms")
    @ApiOperation(value = "Updates storage room of florist shop (room id must be present)", nickname = "updateStorageRoom")
    public ResponseEntity<?> updateRoom(
            @PathVariable String email,
            @Valid @RequestBody StorageRoomDto storageRoomDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        StorageRoomDto updatedRoom = floristShopService.updateRoom(
                storageRoomDto, email);

        if (updatedRoom == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/rooms/{id}")
    @ApiOperation(value = "Deletes storage room by ID", nickname = "deleteStorageRoom")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        StorageRoomDto room = floristShopService.findStorageRoomById(id);

        if (room == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        floristShopService.deleteStorageRoom(room);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/rooms/{id}")
    @ApiOperation(value = "Finds storage room by id", nickname = "getStorageRoomById")
    public ResponseEntity<?> getStorageRoomById(@PathVariable Long id) {
        StorageRoomDto storageRoomDto = floristShopService.findStorageRoomById(id);
        if (storageRoomDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(storageRoomDto);
    }
    
}
