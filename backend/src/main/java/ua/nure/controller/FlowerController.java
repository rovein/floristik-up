package ua.nure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nure.dto.FlowerDto;
import ua.nure.service.FlowerService;

import javax.validation.Valid;

import java.util.List;

import static ua.nure.validation.BindingResultValidator.errorBody;

@CrossOrigin
@RestController
@RequestMapping("/flowers")
@Api(tags = "Flowers")
public class FlowerController {

    @Autowired
    private FlowerService flowerService;

    @GetMapping
    @ApiOperation(value = "Returns a list of all flowers", nickname = "getAllFlowers")
    public ResponseEntity<List<FlowerDto>> getAllFlowers() {
        return ResponseEntity.ok(flowerService.findAll());
    }

    @PostMapping
    @ApiOperation(value = "Adds new flower type", nickname = "addFlower")
    public ResponseEntity<?> addFlower(
            @Valid @RequestBody FlowerDto FlowerDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }
        return ResponseEntity.ok(flowerService.create(FlowerDto));
    }

    @PutMapping
    @ApiOperation(value = "Updates the flower type", nickname = "updateFlower")
    public ResponseEntity<?> updateFlower(
            @Valid @RequestBody FlowerDto FlowerDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(errorBody(bindingResult));
        }

        FlowerDto updatedFlower = flowerService.update(FlowerDto);

        if (updatedFlower == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedFlower);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes flower type by ID", nickname = "deleteFlower")
    public ResponseEntity<Void> deleteFlower(@PathVariable Long id) {
        FlowerDto flower = flowerService.findById(id);

        if (flower == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        flowerService.delete(flower);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds flower by ID", nickname = "getFlowerId")
    public ResponseEntity<?> getFlowerById(@PathVariable Long id) {
        FlowerDto flowerDto = flowerService.findById(id);
        if (flowerDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(flowerDto);
    }

}
