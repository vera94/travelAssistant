package repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import model.UserEntity;

public interface IUserRepository extends CrudRepository<UserEntity, Long> {

	@Query("SELECT u FROM UserEntity u WHERE u.email = ?1")
	UserEntity fingByEmail(String email);

}