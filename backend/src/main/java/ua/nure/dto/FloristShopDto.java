package ua.nure.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ua.nure.entity.value.UserRole;
import ua.nure.validation.annotation.UniqueEmail;
import ua.nure.validation.annotation.UniquePhoneNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FloristShopDto {
    private Long id;

    @NotEmpty(message = "Phone number can`t be empty")
    @UniquePhoneNumber
    private String phoneNumber;

    @NotEmpty(message = "Email can`t be empty")
    @Email(message = "Invalid email")
    @UniqueEmail
    protected String email;

    @NotEmpty(message = "Password can`t be empty")
    protected String password;

    @NotEmpty(message = "Name can`t be empty")
    private String name;

    private Boolean isLocked;

    private Date creationDate;

    private String country;

    private UserRole role;

    public FloristShopDto isLocked(boolean isLocked) {
        this.isLocked = isLocked;
        return this;
    }

    public Boolean isLocked() {
        return isLocked;
    }
}
