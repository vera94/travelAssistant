package repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import model.Landmark;
import model.LandmarkType;

public interface ILandmarkRepository extends CrudRepository<Landmark, Long> {

//	@Query("SELECT l FROM Landmark l WHERE l.id = ?1")
//	Landmark fingById(long id);
//	
//	@Query("SELECT l FROM Landsmark l")
//	List<Landmark> getAllLandmarks();

//	@Query("SELECT l FROM Landmark l WHERE (select l2.types from landark l2 where   in (?1)")
	@Query("SELECT distinct l2 FROM Landmark l2 JOIN l2.types t WHERE t.type in (?1)")
	List<Landmark> getAllLandmarksByType(Collection<String> types);

}
