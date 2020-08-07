package repository;

import org.springframework.data.repository.CrudRepository;

import model.DirectionsRequest;

public interface IDirectionsRequestRepository extends CrudRepository<DirectionsRequest, Long> {

}
