package co.jordan.boldauthentic.dto;

import co.jordan.boldauthentic.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    private String firstname;
    private String lastname;
    private String login;
    private String password;
}
