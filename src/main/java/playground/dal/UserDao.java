package playground.dal;

import org.springframework.data.repository.CrudRepository;

import playground.logic.EntityComponents.UserEntity;
import playground.logic.EntityComponents.UserId;

public interface UserDao extends CrudRepository<UserEntity, UserId>{

}
