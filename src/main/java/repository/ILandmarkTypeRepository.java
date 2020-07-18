package repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import model.LandmarkType;

public interface ILandmarkTypeRepository extends CrudRepository<LandmarkType, Long> {

	@Query("SELECT l FROM LandmarkType l WHERE ?1 LIKE l.path || '%'")
	List<LandmarkType> getAllTypeAncestors(String path);

	@Query("SELECT l FROM LandmarkType l WHERE l.path LIKE ?1")
	List<LandmarkType> getAllTypeDescendants(String type);

	@Query("SELECT l FROM LandmarkType l ORDER BY l.path")
	List<LandmarkType> getAllTypes();
}
