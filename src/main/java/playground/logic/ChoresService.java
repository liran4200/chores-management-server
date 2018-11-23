package playground.logic;

import java.util.List;

import playground.logic.ChoreAlreadyExistsException;

public interface ChoresService {
	
	public void cleanup();
	
	public ChoreEntity createNewChore(ChoreEntity chore, String userPlayground, String email) throws ChoreAlreadyExistsException;
	
	public void updateChore(ChoreEntity chore, String userPlayground, String email, String playground, String id) throws ChoreNotFoundException;
	
	public ChoreEntity getChoreById(String userPlayground, String email, String playground, String id) throws ChoreNotFoundException;
	
	public List<ChoreEntity> getAllChores(String userPlayground, String email, int page, int size);
	
	public List<ChoreEntity> getAllNearChores(String userPlaygeound, String email, double x, double y, double distance, int page, int size);
	
	public List<ChoreEntity> searchChore(String userPlaygeound, String email, String attributeName, String value, int page, int size) throws NoSuchAttributeException;
}
