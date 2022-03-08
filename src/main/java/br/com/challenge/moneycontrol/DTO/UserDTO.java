package br.com.challenge.moneycontrol.DTO;

import br.com.challenge.moneycontrol.model.UserAccount;

public class UserDTO {
    private long id;
    private String name;
    private String  email;

    public UserDTO(UserAccount user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getUsername();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
}
