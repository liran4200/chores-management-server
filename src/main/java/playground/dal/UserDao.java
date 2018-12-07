package playground.dal;

import org.springframework.data.repository.CrudRepository;

import playground.logic.UserEntity;
import playground.logic.UserId;

public interface UserDao extends CrudRepository<UserEntity, UserId>{

}
