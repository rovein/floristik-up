package ua.nure.floristikup.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FlowerStorageResponseDto {

    private Long id;

    private Date startDate;

    private Integer amount;

    private Long storageRoomId;

    private Long flowerId;

    private String error;
}
