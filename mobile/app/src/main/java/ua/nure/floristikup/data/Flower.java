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
public class Flower {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("shelfLife")
    @Expose
    private Integer shelfLife;

    @SerializedName("minTemperature")
    @Expose
    private Integer minTemperature;

    @SerializedName("maxTemperature")
    @Expose
    private Integer maxTemperature;

}