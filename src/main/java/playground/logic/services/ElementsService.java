package playground.logic.services;

import java.util.List;

import playground.logic.EntityComponents.ElementEntity;
import playground.logic.exceptions.ElementAlreadyExistsException;
import playground.logic.exceptions.ElementNotFoundException;
import playground.logic.exceptions.NoSuchAttributeException;


/**
 * service interface for Elements component
 * @author yuriv
 */
public interface ElementsService {
	
	public void cleanup();
	
	public ElementEntity createNewElement(ElementEntity element, String userPlayground, String email) throws ElementAlreadyExistsException;
	
	public void updateElement(ElementEntity element, String userPlayground, String email, String playground, String id) throws ElementNotFoundException;
	
	public ElementEntity getElementById(String userPlayground, String email, String playground, String id) throws ElementNotFoundException;
	
	public List<ElementEntity> getAllElements(String userPlayground, String email, int page, int size);
	
	public List<ElementEntity> getAllNearElements(String userPlaygeound, String email, double x, double y, double distance, int page, int size);
	
	public List<ElementEntity> searchElement(String userPlaygeound, String email, String attributeName, String value, int page, int size) throws NoSuchAttributeException;
}
