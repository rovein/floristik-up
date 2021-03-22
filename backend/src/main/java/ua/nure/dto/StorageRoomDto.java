package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class StorageRoomDto {

    private Long id;

    private String city;

    private String street;

    private String house;

    private Integer maxCapacity;

    private SmartSystemDto smartDevice;

}
