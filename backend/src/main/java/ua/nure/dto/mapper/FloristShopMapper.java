package ua.nure.dto.mapper;

import ua.nure.dto.FloristShopDto;
import ua.nure.entity.FloristShop;
import ua.nure.entity.value.UserRole;

public class FloristShopMapper {

    public static FloristShopDto toFloristShopDto(FloristShop floristShop) {
        return new FloristShopDto()
                .setId(floristShop.getId())
                .setPhoneNumber(floristShop.getPhoneNumber())
                .setEmail(floristShop.getEmail())
                .setPassword(floristShop.getPassword())
                .setName(floristShop.getName())
                .setCreationDate(floristShop.getCreationDate())
                .setCountry(floristShop.getCountry())
                .setRole(UserRole.USER);
    }

}
