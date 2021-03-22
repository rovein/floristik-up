package ua.nure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.dto.FlowerDto;
import ua.nure.dto.mapper.FlowerMapper;
import ua.nure.entity.Flower;
import ua.nure.entity.value.UserRole;
import ua.nure.repository.FlowerRepository;
import ua.nure.service.FlowerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FlowerServiceImpl implements FlowerService {
    
    private final FlowerRepository flowerRepository;

    @Autowired
    public FlowerServiceImpl(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    @Override
    public FlowerDto create(FlowerDto flowerDto) {
        return FlowerMapper.toFlowerDto(flowerRepository.save(FlowerMapper.toFlower(flowerDto)));
    }

    @Override
    public FlowerDto update(FlowerDto flowerDto) {
        Optional<Flower> flowerByID = flowerRepository.findById(flowerDto.getId());

        return flowerByID.map(flower -> FlowerMapper
                .toFlowerDto(flowerRepository.save(flower))).orElse(null);
    }

    @Override
    public void delete(FlowerDto flowerDto) {
        flowerRepository.deleteById(flowerDto.getId());
    }

    @Override
    public List<FlowerDto> findAll() {
        Iterable<Flower> flowers = flowerRepository.findAll();
        List<FlowerDto> flowersDto = new ArrayList<>();

        flowers.forEach(
                flower -> flowersDto.add(FlowerMapper.toFlowerDto(flower))
        );

        return flowersDto;
    }
    
    @Override
    public FlowerDto findById(Long id) {
        Optional<Flower> flowerById = flowerRepository.findById(id);
        return flowerById.map(FlowerMapper::toFlowerDto).orElse(null);
    }
}
