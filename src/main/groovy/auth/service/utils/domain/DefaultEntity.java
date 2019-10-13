package auth.service.utils.domain;

import auth.service.utils.gp.IdGenerator;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class DefaultEntity {
    @Id
    protected String id= IdGenerator.generateId();

}
