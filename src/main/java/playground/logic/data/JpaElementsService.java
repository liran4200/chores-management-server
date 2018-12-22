package playground.logic.data;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.aop.logger.MyLog;
import playground.aop.userValidation.PlaygroundManagerValidation;
import playground.aop.userValidation.PlaygroundUserValidation;
import playground.dal.ElementDao;
import playground.dal.NumberGenerator;
import playground.dal.NumberGeneratorDao;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.ElementId;
import playground.logic.exceptions.ElementAlreadyExistsException;
import playground.logic.exceptions.ElementNotFoundException;
import playground.logic.exceptions.NoSuchAttributeException;
import playground.logic.services.ElementsService;

/**
 * JPA service for Elements component - using DB to store data
 * @author yuriv
 */
@Service
public class JpaElementsService implements ElementsService {
	
	private ElementDao elements;
	private NumberGeneratorDao numberGenerator;
	
	
	@Autowired
	public JpaElementsService(ElementDao elements, NumberGeneratorDao numberGenerator) {
		this.elements = elements;
		this.numberGenerator = numberGenerator;
	}
	
	@Override
	@Transactional
	@MyLog
	public void cleanup() {
		this.elements.deleteAll();
	}

	@Override
	@Transactional
	@MyLog
	@PlaygroundManagerValidation
	public ElementEntity createNewElement(ElementEntity element, String userPlayground, String email)
			throws ElementAlreadyExistsException {
		if (!this.elements.existsById(element.getElementId())) {
			NumberGenerator temp = this.numberGenerator.save(new NumberGenerator());
			String number = "" + temp.getNextNumber();
			//set new id to element
			element.setElementId(new ElementId(number));
			this.numberGenerator.delete(temp);
			return this.elements.save(element);
		} else {
			throw new ElementAlreadyExistsException("element " + element.getName() + " is already exists");
		}
	}

	@Override
	@Transactional
	@MyLog
	@PlaygroundManagerValidation
	public void updateElement(ElementEntity element, String userPlayground, String email, String playground, String id)
			throws ElementNotFoundException {
		ElementEntity existingElement = this.getElementById(userPlayground, email, playground, id);

		if (element.getExpirationDate() != null && !Objects.equals(existingElement.getExpirationDate(), element.getExpirationDate())) {
			existingElement.setExpirationDate(element.getExpirationDate());
		}
		
		if (element.getName() != null && !Objects.equals(existingElement.getName(), element.getName())) {
			existingElement.setName(element.getName());
		}
		
		if (element.getType() != null && !Objects.equals(existingElement.getType(), element.getType())) {
			existingElement.setType(element.getType());
		}
		
		if(element.getX() != null && !Objects.equals(existingElement.getX(), element.getX())) {
			existingElement.setX(element.getX());
		}
		
		if(element.getY() != null && !Objects.equals(existingElement.getY(), element.getY())) {
			existingElement.setY(element.getY());
		}
		
		this.elements.save(existingElement);
	}

	@Override
	@Transactional(readOnly=true)
	@MyLog
	@PlaygroundUserValidation
	public ElementEntity getElementById(String userPlayground, String email, String playground, String id) throws ElementNotFoundException {
		ElementId uniqueId = new ElementId(id, playground);
		Optional<ElementEntity> op = this.elements.findById(uniqueId);
		if (op.isPresent()) {
			return op.get();
		} else {
			throw new ElementNotFoundException("no element found for id " + id + " in playground " + playground);
		}
	}

	@Override
	@Transactional(readOnly=true)
	@MyLog
	@PlaygroundUserValidation
	public List<ElementEntity> getAllElements(String userPlayground, String email, int page, int size) {
		return this.elements.findAll(PageRequest.of(page, size, Direction.DESC, "creationDate")).getContent();
	}
	
	@Override
	@Transactional(readOnly=true)
	@MyLog
	@PlaygroundUserValidation
	public List<ElementEntity> getAllNearElements(String userPlaygeound, String email, double x, double y, double distance, int page, int size) {
		double xTop = x + distance;
		double xBottom = x - distance;
		double yTop = y + distance;
		double yBottom = y - distance;
		
		return this.elements.findAllByXBetweenAndYBetween(xBottom, xTop, yBottom, yTop, PageRequest.of(page, size, Direction.DESC, "creationDate"));
	}

	@Override
	@Transactional(readOnly=true)
	@MyLog
	@PlaygroundUserValidation
	public List<ElementEntity> searchElement(String userPlaygeound, String email, String attributeName, String value,
			int page, int size) throws NoSuchAttributeException {
		if (Objects.equals("name", attributeName)) {
			return this.elements.findAllByNameLike(value, PageRequest.of(page, size, Direction.DESC, "creationDate"));
		} else if (Objects.equals("type", attributeName)) {
			return this.elements.findAllByTypeLike(value, PageRequest.of(page, size, Direction.DESC, "creationDate"));
		} else {
			throw new NoSuchAttributeException("no " + attributeName + " attribute in elements");
		}
	}
	

}
