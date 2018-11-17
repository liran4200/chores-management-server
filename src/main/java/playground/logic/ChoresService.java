package playground.logic;


public interface ChoresService {
	
	public void cleanup();
	
	public void createNewChore(ChoreEntity chore, String userPlayground, String email);
	
	public void updateChore(String userPlayground, String email, String playground, String id);
	
	public ChoreEntity getChoreById(String userPlayground, String email, String playground, String id);
	
	public ChoreEntity[] getAllChores(String userPlayground, String email);
	
	public ChoreEntity[] getAllNearChores(String userPlaygeound, String email, double x, double y, double distance);
	
	public ChoreEntity[] searchChore(String userplayground, String email, String attributeName, String value);
}
