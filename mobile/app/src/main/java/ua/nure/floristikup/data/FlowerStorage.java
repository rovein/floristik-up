package ua.nure.floristikup.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class FlowerStorage {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("startDate")
    @Expose
    private Date startDate;

    @SerializedName("formattedDate")
    @Expose
    private String formattedDate;

    @SerializedName("flowerId")
    @Expose
    private Long flowerId;

    @SerializedName("storageRoomId")
    @Expose
    private Long storageRoomId;

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("flowerName")
    @Expose
    private String flowerName;

    @SerializedName("flowerColor")
    @Expose
    private String flowerColor;

    @SerializedName("flowerShelfLife")
    @Expose
    private String flowerShelfLife;

    @SerializedName("minTemperature")
    @Expose
    private Long minTemperature;

    @SerializedName("maxTemperature")
    @Expose
    private Long maxTemperature;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("street")
    @Expose
    private String street;

    @SerializedName("house")
    @Expose
    private String house;

    @SerializedName("actualCapacity")
    @Expose
    private Integer actualCapacity;

    @SerializedName("maxCapacity")
    @Expose
    private Long maxCapacity;

    @SerializedName("temperature")
    @Expose
    private Double temperature;

    @SerializedName("humidity")
    @Expose
    private Double humidity;

    @SerializedName("airQuality")
    @Expose
    public Double airQuality;

    @SerializedName("satisfactionFactor")
    @Expose
    public Double satisfactionFactor;

}
