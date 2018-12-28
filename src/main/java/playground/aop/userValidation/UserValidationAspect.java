package playground.aop.userValidation;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import playground.aop.logger.LoggerAspect;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.UserEntity;
import playground.logic.services.ElementNotFoundException;
import playground.logic.services.UserNotActiveException;
import playground.logic.services.UserNotFoundException;
import playground.logic.services.UserService;
import playground.utils.PlaygroundConstants;


/**
 * Aspect for validating if user exists and confirmed, filtering return values for managers and users 
 * @author yuriv
 *
 */
@Component
@Aspect
public class UserValidationAspect {
	
	private UserService users;
	private Log log = LogFactory.getLog(LoggerAspect.class);
	
	@Autowired
	private void setUsers(UserService users) {
		this.users = users;
	}

	@SuppressWarnings("unchecked")
	@Around("@annotation(playground.aop.userValidation.PlaygroundUserValidation) && args (userPlayground, email,..)")
	public Object validateActiveUser(ProceedingJoinPoint jp, String userPlayground, String email) throws Throwable {
		try {
			log.info("******************** User Validation ********************");
			 UserEntity user = users.getCustomUser(email, userPlayground); //this will throw exception if user is not exist or not active
			 Object rv = jp.proceed();
			 Date now = new Date();
			 
			 //filter not expired elements for not manager users
			 if (!user.getRole().equals(PlaygroundConstants.USER_ATTRIBUTE_ROLE_MANAGER)) {
				 //if this method return a list of elements - return only elements which are not expired
				 if (rv instanceof List<?>) {
					 if (((List<?>) rv).get(0) instanceof ElementEntity) {
						 List<ElementEntity> elementsToReturn = (List<ElementEntity>) rv;
						 return elementsToReturn.stream()
								 .filter(e -> e.getExpirationDate().compareTo(now) > 0).collect(Collectors.toList());
					 } 
				 } else if (rv instanceof ElementEntity) { //if method returns one elementEntity - return null if expired
					 if (((ElementEntity) rv).getExpirationDate().compareTo(now) > 0) {
						 return rv;
					 } else {
						 throw new ElementNotFoundException("no element found");
					 }
				 }
			 }
			 return rv;
		} catch (UserNotFoundException | UserNotActiveException e) {
			throw e;
		}	
	}
	
	
}
