package playground.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;


@Service
public class ChoresServiceStub implements ChoresService {
	
	private Map<String, ChoreEntity> chores;
	
	@PostConstruct
	public void init() {
		this.chores = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	public void cleanup() {
		this.chores.clear();
	}

	@Override
	public ChoreEntity createNewChore(ChoreEntity chore, String userPlayground, String email) throws ChoreAlreadyExistsException {
		//never get the same id.... have to check what is the unique key for chores
		if (this.chores.containsKey(chore.getId())) {
			throw new ChoreAlreadyExistsException("chore " + chore.getName() + " with id " + chore.getId() + " is already exists");
		}
		this.chores.put(chore.getId(), chore);
		return this.chores.get(chore.getId());
	}

	@Override
	public void updateChore(ChoreEntity chore, String userPlayground, String email, String playground, String id) throws ChoreNotFoundException {
		ChoreEntity existingChore = this.chores.get(id);
		if (existingChore == null) {
			throw new ChoreNotFoundException("no chore found for id " + id);
		}
		
		boolean dirty = false;
		if (chore.getExpirationDate() != null && !existingChore.getExpirationDate().equals(chore.getExpirationDate())) {
			existingChore.setExpirationDate(chore.getExpirationDate());
			dirty = true;
		}
		
		if (chore.getName() != null && !existingChore.getName().equals(chore.getName())) {
			existingChore.setName(chore.getName());
			dirty = true;
		}
		
		if (chore.getType() != null && !existingChore.getType().equals(chore.getType())) {
			existingChore.setType(chore.getType());
			dirty = true;
		}
		
		if(chore.getX() != null && !existingChore.getX().equals(chore.getX())) {
			existingChore.setX(chore.getX());
			dirty = true;
		}
		
		if(chore.getY() != null && !existingChore.getY().equals(chore.getY())) {
			existingChore.setY(chore.getY());
			dirty = true;
		}
		
		if (dirty) {
			this.chores.put(id, existingChore);
		}
	}

	@Override
	public ChoreEntity getChoreById(String userPlayground, String email, String playground, String id) throws ChoreNotFoundException {
		ChoreEntity choreToReturn = this.chores.get(id);
		if (choreToReturn == null) {
			throw new ChoreNotFoundException("no chore found for id " + id);
		} else {
			return choreToReturn;
		}
	}

	@Override
	public List<ChoreEntity> getAllChores(String userPlayground, String email, int page, int size) {
		Collection<ChoreEntity> copy;
		synchronized (this.chores) {
			copy = new ArrayList<>(this.chores.values());
		}		
		return 
				copy
				.stream()
				.skip(page * size)
				.limit(size)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ChoreEntity> getAllNearChores(String userPlaygeound, String email, double x, double y, double distance, int page, int size) {
		Collection<ChoreEntity> copy;
		synchronized (this.chores) {
			copy = new ArrayList<>(this.chores.values());
		}
		return copy
				.stream()
				.filter(chore -> chore.calculateDistance(x, y) < distance)
				.skip(page * size)
				.limit(size)
				.collect(Collectors.toList());
	}

	@Override
	public List<ChoreEntity> searchChore(String userplayground, String email, String attributeName, String value, int page, int size) throws NoSuchAttributeException {
		Collection<ChoreEntity> copy;
		synchronized (this.chores) {
			copy = new ArrayList<>(this.chores.values());
		}
		
		if (attributeName.equals("name")) {
			return copy
					.stream()
					.filter(chore -> chore.getName().equals(value))
					.skip(page * size)
					.limit(size)
					.collect(Collectors.toList());
		} else if (attributeName.equals("type")) {
			return copy
					.stream()
					.filter(chore -> chore.getType().equals(value))
					.skip(page * size)
					.limit(size)
					.collect(Collectors.toList());
		} else {
			throw new NoSuchAttributeException("no " + attributeName + " attribute in chores");
		}
	}
	
	

}
