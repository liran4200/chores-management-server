package playground.dal;

import org.springframework.data.repository.CrudRepository;

import playground.logic.EntityComponents.ElementEntity;

public interface ElementDao extends CrudRepository<ElementEntity, String> {

}
