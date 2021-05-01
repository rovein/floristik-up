package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FlowerStorageInfoDto {

    public static FlowerStorageInfoDto get(FlowerStorageInfo flowerStorageInfo) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = formatter.format(flowerStorageInfo.getStartDate());

        return new FlowerStorageInfoDto()
                .setId(flowerStorageInfo.getId())
                .setStartDate(flowerStorageInfo.getStartDate())
                .setAmount(flowerStorageInfo.getAmount())
                .setFlowerName(flowerStorageInfo.getFlowerName())
                .setFlowerColor(flowerStorageInfo.getFlowerColor())
                .setFlowerShelfLife(flowerStorageInfo.getFlowerShelfLife())
                .setFlowerId(flowerStorageInfo.getFlowerId())
                .setStorageRoomId(flowerStorageInfo.getStorageRoomId())
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
    
}
