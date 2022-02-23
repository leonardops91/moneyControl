package br.com.challenge.moneycontrol.form;

import br.com.challenge.moneycontrol.model.UserAccount;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserForm {
    @NotNull @NotEmpty
    private String name;
    @NotNull @NotEmpty
    private String email;
    @NotNull @NotEmpty
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserAccount convert(){
        String passEncoded = new BCryptPasswordEncoder().encode(this.password);
        return new UserAccount(this.name, this.email,
                passEncoded);
    }
}
