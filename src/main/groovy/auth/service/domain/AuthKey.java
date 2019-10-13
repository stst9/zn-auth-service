package auth.service.domain;

import auth.service.utils.domain.DefaultEntity;
import org.apache.commons.codec.binary.Base64;

import javax.persistence.*;

@Entity
@Table(name = "auth_keys")
public class AuthKey extends DefaultEntity {

    @ManyToOne
    @JoinColumn(name = "ak_user_id",nullable = false)
    private AuthUser user;

    @Column(name = "ak_value",nullable = false)
    private byte[] value;

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
}
