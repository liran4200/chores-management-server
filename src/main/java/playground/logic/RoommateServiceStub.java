package playground.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import playground.logic.EntityComponents.RoommateEntity;
import playground.logic.exceptions.InValidConfirmationCodeException;
import playground.logic.exceptions.RoommateAlreadyExistsException;
import playground.logic.exceptions.RoommateNotFoundException;
import playground.logic.services.RoommateService;

@Service
public class RoommateServiceStub implements RoommateService{
	
	private AtomicLong codeGen;
	private Map<String,RoommateEntity> roommates;
	
	@PostConstruct
	public void init() {
		//thread safe
		this.roommates = Collections.synchronizedMap(new HashMap<>());
		this.codeGen   = new AtomicLong(1000);
	}

	@Override
	public void cleanup() {
		this.roommates.clear();
	}

	@Override
	public long createRoommate(RoommateEntity roommate)  throws RoommateAlreadyExistsException{
		String key = roommate.getPlayground()+roommate.getEmail();
		if(this.roommates.containsKey(key))
			throw new RoommateAlreadyExistsException("Roommate-"+roommate.getEmail()+" already exists");
		roommate.setConfirmCode(codeGen.getAndIncrement());
		this.roommates.put(key, roommate);
		return roommate.getConfirmCode();
	}

	@Override
	public RoommateEntity getCustomRoommate(String email, String playground) throws RoommateNotFoundException {
		String key = playground+email;
		RoommateEntity roommate = this.roommates.get(key);
		if(roommate == null)
			throw new RoommateNotFoundException("Roommate -"+email+" not found");
		
		return roommate;
	}
	
	
	@Override
	public RoommateEntity getConfirmRoommate(String email, String playground, long code)
			throws RoommateNotFoundException, InValidConfirmationCodeException {
		
		RoommateEntity roommate = getCustomRoommate(email, playground);
		if(code != roommate.getConfirmCode())
			throw new InValidConfirmationCodeException("confirmation code not matching");
		roommate.setIsActive(true);
		return roommate;
	}

	@Override
	public void updateRoommate(String email,String playground,RoommateEntity roommate)
			throws RoommateNotFoundException{
		String key = playground+email;
		if(!this.roommates.containsKey(key))
			throw new RoommateNotFoundException("Roommate not exists");
		
		RoommateEntity existing = this.roommates.get(key);
		boolean dirty = false;
		
		if( roommate.getRoommateName()!=null && 
				!roommate.getRoommateName().equals(existing.getRoommateName())) {
			dirty = true;
			existing.setRoommateName(roommate.getRoommateName());
		}
		
		if( roommate.getAvatar()!=null && 
				!roommate.getAvatar().equals(existing.getAvatar())) {
			dirty = true;
			existing.setAvatar(roommate.getAvatar());
		}
		
		if( roommate.getRole()!= null &&
				!roommate.getRole().equals(roommate.getRole())) {
			dirty = true;
			existing.setRole(roommate.getRole());
		}
		
		if(dirty) {
			this.roommates.put(key, existing);
		}
		
	}
	
	
}
