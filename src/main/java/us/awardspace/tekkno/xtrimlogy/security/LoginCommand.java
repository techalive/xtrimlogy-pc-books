package us.awardspace.tekkno.xtrimlogy.security;

import lombok.Data;

@Data
public class LoginCommand {
    private String username;
    private String password;
}
