package ua.nure.service;

import ua.nure.dto.FlowerDto;

import java.util.List;
import java.util.Set;

public interface FlowerService {

    FlowerDto create(FlowerDto flowerDto);

    FlowerDto update(FlowerDto flowerDto);

    void delete(FlowerDto flowerDto);

    List<FlowerDto> findAll();

    FlowerDto findById(Long id);

}
