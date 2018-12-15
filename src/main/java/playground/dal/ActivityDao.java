package playground.dal;

import org.springframework.data.repository.CrudRepository;

import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.ActivityId;

public interface ActivityDao extends CrudRepository<ElementEntity, ActivityId> {

}
