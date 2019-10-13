package auth.service.domain;

import auth.service.utils.domain.DefaultEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auth_users")
public class AuthUser extends DefaultEntity {

    @Column(name = "au_email",nullable = false,length = 150,unique = true)
    private String email;


    @OneToOne(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private AuthPassword password;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<AuthKey> authKeys=new ArrayList<>();

    public AuthUser(){

    }

    public AuthUser(String email, AuthPassword password, List<AuthKey> authKeys) {
        this();
        this.email = email;
        this.password = password;
        this.authKeys = authKeys;
    }

    public String getId(){
        return id;
    }

    public String getEmail() {
        return email;
    }

    public AuthPassword getPassword() {
        return password;
    }

    public List<AuthKey> getAuthKeys() {
        return authKeys;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(AuthPassword password) {
        this.password = password;
        if (password!=null){
            password.user=this;
        }
    }
}
