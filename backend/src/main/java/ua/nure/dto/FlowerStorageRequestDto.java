package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FlowerStorageRequestDto {

    private Long id;

    private Integer amount;

    private Long storageRoomId;

    private Long flowerId;

}
