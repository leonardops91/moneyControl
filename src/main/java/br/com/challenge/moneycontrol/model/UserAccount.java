package br.com.challenge.moneycontrol.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class UserAccount implements UserDetails {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Profile> profile = new ArrayList<>();

    public UserAccount(){}
    public UserAccount(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserAccount(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return id.equals(that.id) && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    public Long getId() {
        return id;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.profile;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
