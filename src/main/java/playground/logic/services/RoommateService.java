package playground.logic.services;

import playground.logic.EntityComponents.RoommateEntity;
import playground.logic.exceptions.InValidConfirmationCodeException;
import playground.logic.exceptions.RoommateAlreadyExistsException;
import playground.logic.exceptions.RoommateNotFoundException;

public interface RoommateService {
	
	public void cleanup();
	
	public long createRoommate(RoommateEntity roommate) 
			throws RoommateAlreadyExistsException;
	
	public RoommateEntity getCustomRoommate(String email,String playground)
			throws RoommateNotFoundException;
	
	public RoommateEntity getConfirmRoommate(String email, String playground, long code)
			throws RoommateNotFoundException, InValidConfirmationCodeException;
	
	public void updateRoommate(String email, String playground, RoommateEntity roommate) 
			throws RoommateNotFoundException;
	
}