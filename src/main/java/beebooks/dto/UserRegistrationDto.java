package beebooks.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRegistrationDto {
    private String username;
    private String email;
    private String phone;
    private String password;
    private String address;

    public UserRegistrationDto(String username, String email, String password, String address, String phone) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
    }

}
