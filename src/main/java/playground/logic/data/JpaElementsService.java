package playground.logic.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import playground.dal.ElementDao;
import playground.dal.NumberGenerator;
import playground.dal.NumberGeneratorDao;
import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.ElementUniqueId;
import playground.logic.exceptions.ElementAlreadyExistsException;
import playground.logic.exceptions.ElementNotFoundException;
import playground.logic.exceptions.NoSuchAttributeException;
import playground.logic.services.ElementsService;

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
	public void cleanup() {
		this.elements.deleteAll();
	}

	@Override
	@Transactional
	public ElementEntity createNewElement(ElementEntity element, String userPlayground, String email)
			throws ElementAlreadyExistsException {
		if (!this.elements.existsById(element.getIdAndPlayground())) {
			NumberGenerator temp = this.numberGenerator.save(new NumberGenerator());
			String number = "" + temp.getNextNumber();
			//set new id to element
			element.setIdAndPlayground(new ElementUniqueId(number));
			this.numberGenerator.delete(temp);
			return this.elements.save(element);
		} else {
			throw new ElementAlreadyExistsException("element " + element.getName() + " is already exists");
		}
	}

	@Override
	@Transactional
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
	public ElementEntity getElementById(String userPlayground, String email, String playground, String id) throws ElementNotFoundException {
		ElementUniqueId uniqueId = new ElementUniqueId(id, playground);
		Optional<ElementEntity> op = this.elements.findById(uniqueId);
		if (op.isPresent()) {
			return op.get();
		} else {
			throw new ElementNotFoundException("no element found for id " + id + " in playground " + playground);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> getAllElements(String userPlayground, String email, int page, int size) {
		
		return getAllValuesFromDao()
				.stream()
				.skip(page * size)
				.limit(size)
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> getAllNearElements(String userPlaygeound, String email, double x, double y, double distance, int page, int size) {
		
		return getAllValuesFromDao()
				.stream()
				.filter(element -> element.calculateDistance(x, y) < distance)
				.skip(page * size)
				.limit(size)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> searchElement(String userPlaygeound, String email, String attributeName, String value,
			int page, int size) throws NoSuchAttributeException {
		List<ElementEntity> allElements = getAllValuesFromDao();
		
		if (attributeName.equals("name")) {
			return allElements
					.stream()
					.filter(element -> element.getName().equals(value))
					.skip(page * size)
					.limit(size)
					.collect(Collectors.toList());
		} else if (attributeName.equals("type")) {
			return allElements
					.stream()
					.filter(element -> element.getType().equals(value))
					.skip(page * size)
					.limit(size)
					.collect(Collectors.toList());
		} else {
			throw new NoSuchAttributeException("no " + attributeName + " attribute in elements");
		}
	}
	
	/**
	 * @return all elements from DB as List
	 */
	private List<ElementEntity> getAllValuesFromDao() {
		List<ElementEntity> allList = new ArrayList<>();
		this.elements.findAll()
			.forEach(allList::add);
		
		return allList;
	}

}
