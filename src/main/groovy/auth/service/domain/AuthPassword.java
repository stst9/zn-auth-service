package auth.service.domain;


import auth.service.utils.domain.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class AuthPassword extends DefaultEntity {

    @OneToOne(mappedBy = "password")
    private AuthUser user;

    @Column(name = "ap_value")
    private String value;
    @Column(name = "ap_id")
    public String getId(){
        return id;
    }

    public AuthUser getUser() {
        return user;
    }

    public String getValue() {
        return value;
    }
}
