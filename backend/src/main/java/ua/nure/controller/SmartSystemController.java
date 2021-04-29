package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.dto.SmartSystemDto;
import ua.nure.dto.StorageRoomDto;
import ua.nure.entity.user.FlowerStorage;
import ua.nure.service.FloristShopService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/device")
@Api(tags = "Smart Device")
public class SmartSystemController {

    @Autowired
    private FloristShopService floristShopService;

    @PostMapping()
    @ApiOperation(
            value = "Update smart device characteristics, endpoint for Arduino",
            nickname = "updateSmartDevice"
    )
    public ResponseEntity<?> updateSmartDevice(
            @RequestBody SmartSystemDto smartDeviceDto) {
        StorageRoomDto storageRoomDto =
                floristShopService.findStorageRoomById(smartDeviceDto.getId());

        if (storageRoomDto == null) {
            return ResponseEntity.notFound().build();
        }

        List<FlowerStorage> flowerStorages = floristShopService
                .updateSmartDevice(smartDeviceDto);

        return ResponseEntity.ok(flowerStorages);
    }

}
