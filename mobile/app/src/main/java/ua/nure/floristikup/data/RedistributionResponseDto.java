package ua.nure.floristikup.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class RedistributionResponseDto {

    @SerializedName("flower")
    @Expose
    Flower flower;

    @SerializedName("storageRoom")
    @Expose
    Placement storageRoom;

    @SerializedName("amount")
    @Expose
    Integer amount;
}
