package playground.logic.data;

import java.util.List;

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
		if (!this.elements.existsById(element.getIdAndPlayground().toString())) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional(readOnly=true)
	public ElementEntity getElementById(String userPlayground, String email, String playground, String id)
			throws ElementNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> getAllElements(String userPlayground, String email, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> getAllNearElements(String userPlaygeound, String email, double x, double y,
			double distance, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> searchElement(String userPlaygeound, String email, String attributeName, String value,
			int page, int size) throws NoSuchAttributeException {
		// TODO Auto-generated method stub
		return null;
	}

}
