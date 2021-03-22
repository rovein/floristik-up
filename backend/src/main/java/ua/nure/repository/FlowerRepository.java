package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.Flower;

@Repository
public interface FlowerRepository extends CrudRepository<Flower, Long> {

}
