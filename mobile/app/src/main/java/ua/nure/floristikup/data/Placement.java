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
public class Placement {

    @SerializedName("id")
    @Expose
    private Integer id;

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
    private Integer maxCapacity;

    @SerializedName("smartDevice")
    @Expose
    private SmartDevice smartDevice;
}
