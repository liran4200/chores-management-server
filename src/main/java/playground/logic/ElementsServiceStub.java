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

import playground.logic.EntityComponents.ElementEntity;
import playground.logic.exceptions.ElementAlreadyExistsException;
import playground.logic.exceptions.ElementNotFoundException;
import playground.logic.exceptions.NoSuchAttributeException;
import playground.logic.services.ElementsService;


@Service
public class ElementsServiceStub implements ElementsService {
	
	private Map<String, ElementEntity> elements;
	
	@PostConstruct
	public void init() {
		this.elements = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	public void cleanup() {
		this.elements.clear();
	}

	@Override
	public ElementEntity createNewElement(ElementEntity element, String userPlayground, String email) throws ElementAlreadyExistsException {
		this.elements.values()
				   	.stream()
				   	.forEach(ch -> {
						if (ch.equals(element)) {
							throw new ElementAlreadyExistsException("element " + element.getName() + " is already exists");
						}
				   	});
		this.elements.put(element.getIdAndPlayground().toString(), element);
		return this.elements.get(element.getIdAndPlayground().toString());
	}

	@Override
	public void updateElement(ElementEntity element, String userPlayground, String email, String playground, String id) throws ElementNotFoundException {
		ElementEntity existingElement = this.elements.get(id);
		if (existingElement == null) {
			throw new ElementNotFoundException("no element found for id " + id);
		}
		
		boolean dirty = false;
		if (element.getExpirationDate() != null && !existingElement.getExpirationDate().equals(element.getExpirationDate())) {
			existingElement.setExpirationDate(element.getExpirationDate());
			dirty = true;
		}
		
		if (element.getName() != null && !existingElement.getName().equals(element.getName())) {
			existingElement.setName(element.getName());
			dirty = true;
		}
		
		if (element.getType() != null && !existingElement.getType().equals(element.getType())) {
			existingElement.setType(element.getType());
			dirty = true;
		}
		
		if(element.getX() != null && !existingElement.getX().equals(element.getX())) {
			existingElement.setX(element.getX());
			dirty = true;
		}
		
		if(element.getY() != null && !existingElement.getY().equals(element.getY())) {
			existingElement.setY(element.getY());
			dirty = true;
		}
		
		if (dirty) {
			this.elements.put(id, existingElement);
		}
	}

	@Override
	public ElementEntity getElementById(String userPlayground, String email, String playground, String id) throws ElementNotFoundException {
		ElementEntity elementToReturn = this.elements.get(id);
		if (elementToReturn == null) {
			throw new ElementNotFoundException("no element found for id " + id);
		} else {
			return elementToReturn;
		}
	}

	@Override
	public List<ElementEntity> getAllElements(String userPlayground, String email, int page, int size) {
		Collection<ElementEntity> copy;
		synchronized (this.elements) {
			copy = new ArrayList<>(this.elements.values());
		}		
		return 
				copy
				.stream()
				.skip(page * size)
				.limit(size)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<ElementEntity> getAllNearElements(String userPlaygeound, String email, double x, double y, double distance, int page, int size) {
		Collection<ElementEntity> copy;
		synchronized (this.elements) {
			copy = new ArrayList<>(this.elements.values());
		}
		return copy
				.stream()
				.filter(element -> element.calculateDistance(x, y) < distance)
				.skip(page * size)
				.limit(size)
				.collect(Collectors.toList());
	}

	@Override
	public List<ElementEntity> searchElement(String userplayground, String email, String attributeName, String value, int page, int size) throws NoSuchAttributeException {
		Collection<ElementEntity> copy;
		synchronized (this.elements) {
			copy = new ArrayList<>(this.elements.values());
		}
		
		if (attributeName.equals("name")) {
			return copy
					.stream()
					.filter(element -> element.getName().equals(value))
					.skip(page * size)
					.limit(size)
					.collect(Collectors.toList());
		} else if (attributeName.equals("type")) {
			return copy
					.stream()
					.filter(element -> element.getType().equals(value))
					.skip(page * size)
					.limit(size)
					.collect(Collectors.toList());
		} else {
			throw new NoSuchAttributeException("no " + attributeName + " attribute in elements");
		}
	}
	
	

}
