package ua.nure.floristikup.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FloristShop {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("userRole")
    @Expose
    private String userRole;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("country")
    @Expose
    private String country;

    private static FloristShop mInstance;


    public FloristShop(String id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static FloristShop getInstance() {
        if(mInstance == null) {
            mInstance = new FloristShop();
        }
        return mInstance;
    }
}