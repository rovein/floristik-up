package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.dto.FloristShopDto;
import ua.nure.service.FloristShopService;

@RestController
@CrossOrigin
@RequestMapping(path="/admin")
@Api(tags = "Admin")
public class AdminController {

    @Autowired
    private FloristShopService floristShopService;

    @PostMapping("/lock-user/{email}")
    @ApiOperation(
            value = "Locks/unlocks user access to the system",
            nickname = "lockUser"
    )
    public ResponseEntity<?> lockUser(@PathVariable String email) {
        FloristShopDto shop = floristShopService.findByEmail(email);

        if (shop == null) {
            return ResponseEntity.notFound().build();
        }
        boolean reverseLocked = !shop.isLocked();
        shop.isLocked(reverseLocked);
        FloristShopDto updated = floristShopService.update(shop);

        return ResponseEntity.ok(updated);
    }


}
