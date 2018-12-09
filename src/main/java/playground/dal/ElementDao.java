package playground.dal;

import org.springframework.data.repository.CrudRepository;

import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.ElementId;

public interface ElementDao extends CrudRepository<ElementEntity, ElementId> {

}
