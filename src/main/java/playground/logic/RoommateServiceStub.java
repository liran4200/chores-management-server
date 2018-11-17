package playground.logic;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class RoommateServiceStub implements RoommateService{
	
	//temp
	private static final String CONFIRMATION_CODE = "1234";
	
	private Map<String,RoommateEntity> roommates;
	
	@PostConstruct
	public void init() {
		this.roommates = new HashMap<>();
	}

	@Override
	public void cleanup() {
		this.roommates.clear();
	}

	@Override
	public void createRoommate(RoommateEntity roommate)  throws RoommateAlreadyExistsException{
		if(this.roommates.containsKey(roommate.getEmail()))
			throw new RoommateAlreadyExistsException("Roommate-"+roommate.getEmail()+" already exists");
		this.roommates.put(roommate.getEmail(), roommate);
	}

	@Override
	public RoommateEntity getCustomRoommate(String email, String playground) throws RoommateNotFoundException {
		RoommateEntity roommate = this.roommates.get(email);
		if(roommate == null)
			throw new RoommateNotFoundException("Roommate -"+email+" not found");
		
		return roommate;
	}
	
	
	@Override
	public RoommateEntity getConfirmRoommate(String email, String playground, String code)
			throws RoommateNotFoundException, InValidConfirmationCodeException {
		if(!code.equals(CONFIRMATION_CODE))
			throw new InValidConfirmationCodeException("confirm code not matching");
		
		return getCustomRoommate(email, playground);
	}

	@Override
	public void updateRoommate(String email,String playground,RoommateEntity roommate) throws RoommateNotFoundException {
		if(!this.roommates.containsKey(email))
			throw new RoommateNotFoundException("Roommate not exists");
		
		// if email field has been updated
		if(!email.equals(roommate.getEmail())) {
			//remove old roommate and replace old email to new email.
			this.roommates.remove(email);
			email = roommate.getEmail();
		}
		
		this.roommates.put(email, roommate);
	}
	
	
}
