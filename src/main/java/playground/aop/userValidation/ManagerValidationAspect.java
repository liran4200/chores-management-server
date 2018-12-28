package playground.aop.userValidation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.aop.logger.LoggerAspect;
import playground.logic.EntityComponents.UserEntity;
import playground.logic.services.UserNotActiveException;
import playground.logic.services.UserNotFoundException;
import playground.logic.services.UserNotManagerException;
import playground.logic.services.UserService;


/**
 * Aspect for validating if the user is in role of manager
 * @author yuriv
 *
 */
@Component
@Aspect
public class ManagerValidationAspect {
	
	private UserService users;
	private Log log = LogFactory.getLog(LoggerAspect.class);
	
	@Autowired
	private void setUsers(UserService users) {
		this.users = users;
	}
	
	@Around("@annotation(playground.aop.userValidation.PlaygroundManagerValidation) && args (*, userPlayground, email,..)")
	public Object validateManager(ProceedingJoinPoint jp, String userPlayground, String email) throws Throwable {
		try {
			log.info("******************** Manager Validation ********************");
			 UserEntity user = users.getCustomUser(email, userPlayground);
			 if (!user.getRole().equals("manager")) {
				 throw new UserNotManagerException("user " + user.getUserName() + " is not Manager");
			 }
			 return jp.proceed();
		} catch (UserNotFoundException | UserNotActiveException e) {
			throw e;
		}	
	}

}
