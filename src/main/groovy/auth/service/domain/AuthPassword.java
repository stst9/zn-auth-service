package auth.service.domain;


import auth.service.utils.domain.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class AuthPassword extends DefaultEntity {

    @JoinColumn(name = "ap_user")
    @OneToOne()
    protected AuthUser user;

    @Column(name = "ap_value")
    protected String value;

    public AuthPassword(){

    }

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

    public void setValue(String value) {
        this.value = value;
    }
}
