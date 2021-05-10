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
public class SmartDevice {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("temperature")
    @Expose
    private Double temperature;

    @SerializedName("humidity")
    @Expose
    private Double humidity;

    @SerializedName("satisfactionFactor")
    @Expose
    private Double satisfactionFactor;

    @SerializedName("airQuality")
    @Expose
    private Double airQuality;

}
