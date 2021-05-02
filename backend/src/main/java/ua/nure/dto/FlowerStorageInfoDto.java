package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FlowerStorageInfoDto {

    public FlowerStorageInfoDto(FlowerStorageInfo flowerStorageInfo) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        this.setId(flowerStorageInfo.getId())
                .setStartDate(flowerStorageInfo.getStartDate())
                .setAmount(flowerStorageInfo.getAmount())
                .setFlowerName(flowerStorageInfo.getFlowerName())
                .setFlowerColor(flowerStorageInfo.getFlowerColor())
                .setFlowerShelfLife(flowerStorageInfo.getFlowerShelfLife())
                .setFlowerId(flowerStorageInfo.getFlowerId())
                .setStorageRoomId(flowerStorageInfo.getStorageRoomId())
                .setMinTemperature(flowerStorageInfo.getMinTemperature())
                .setMaxTemperature(flowerStorageInfo.getMaxTemperature())
                .setCity(flowerStorageInfo.getCity())
                .setStreet(flowerStorageInfo.getStreet())
                .setHouse(flowerStorageInfo.getHouse())
                .setMaxCapacity(flowerStorageInfo.getMaxCapacity())
                .setTemperature(flowerStorageInfo.getTemperature())
                .setHumidity(flowerStorageInfo.getHumidity())
                .setFormattedDate(formatter.format(flowerStorageInfo.getStartDate()));
    }

    private Long id;

    private Date startDate;

    private Integer amount;

    private String flowerName;

    private String flowerColor;

    private String flowerShelfLife;

    private Long flowerId;

    private Long storageRoomId;

    private String formattedDate;

    private Long minTemperature;

    private Long maxTemperature;

    private String city;

    private String street;

    private String house;

    private Long maxCapacity;

    private Long temperature;

    private Long humidity;

    private Integer actualCapacity;

}
