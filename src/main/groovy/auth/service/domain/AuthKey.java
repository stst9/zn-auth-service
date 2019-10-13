package auth.service.domain;

import auth.service.beans.CryptUtilsBean;
import auth.service.utils.domain.DefaultEntity;
import auth.service.utils.gp.CryptUtils;
import org.apache.commons.codec.binary.Base64;

import javax.persistence.*;

@Entity
@Table(name = "auth_keys")
public class AuthKey extends DefaultEntity {

    @ManyToOne
    @JoinColumn(name = "ak_user_id",nullable = false)
    protected AuthUser user;

    @Column(name = "ak_value",nullable = false)
    protected byte[] value;

    @Column(name = "ak_id")
    public String getId() {
        return id;
    }

    public AuthUser getUser() {
        return user;
    }

    public byte[] getValue() {
        return value;
    }

    public String getValueBase64(){
        return Base64.encodeBase64String(value);
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public void setUser(AuthUser user) {
        this.user = user;
    }
}
