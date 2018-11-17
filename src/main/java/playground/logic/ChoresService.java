package playground.logic;


public interface ChoresService {
	
	public void cleanup();
	
	public void createNewChore(ChoreEntity chore, String userPlayground, String email) throws UserNotAdminException;
	
	public void updateChore(String userPlayground, String email, String playground, String id) throws UserNotAdminException, ChoreNotFoundException;
	
	public ChoreEntity getChoreById(String userPlayground, String email, String playground, String id) throws RoommateNotFoundException, ChoreNotFoundException;
	
	public ChoreEntity[] getAllChores(String userPlayground, String email) throws RoommateNotFoundException;
	
	public ChoreEntity[] getAllNearChores(String userPlaygeound, String email, double x, double y, double distance);
	
	public ChoreEntity[] searchChore(String userplayground, String email, String attributeName, String value);
}
