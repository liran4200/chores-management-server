package playground.logic;

public interface RoommateService {
	
	public void cleanup();
	
	public void createRoommate(RoommateEntity roommate) 
			throws RoommateAlreadyExistsException;
	
	public RoommateEntity getCustomRoommate(String email,String playground)
			throws RoommateNotFoundException;
	
	public RoommateEntity getConfirmRoommate(String email, String playground, String code)
			throws RoommateNotFoundException, InValidConfirmationCodeException;
	
	public void updateRoommate(String email, String playground, RoommateEntity roommate) 
			throws RoommateNotFoundException;
	
}