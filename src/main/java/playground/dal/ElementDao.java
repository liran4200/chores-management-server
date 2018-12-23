package playground.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.query.Param;

import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.ElementId;

@RepositoryRestResource
public interface ElementDao extends PagingAndSortingRepository<ElementEntity, ElementId> {
	
	public List<ElementEntity> findAllByNameLike(@Param("value") String value, Pageable pageable);

	public List<ElementEntity> findAllByTypeLike(@Param("value") String value, Pageable pageable);
	
	public List<ElementEntity> findAllByXBetweenAndYBetween(@Param("xBottom") double xBottom,
															@Param("xTop") double xTop,
															@Param("yBottom") double yBottom,
															@Param("yTop") double yTop,
															Pageable pageable);
}
