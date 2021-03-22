package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.FloristShop;

import java.util.List;
import java.util.Optional;

@Repository
public interface FloristShopRepository extends CrudRepository<FloristShop, Long> {

    Optional<FloristShop> findByPhoneNumber(String phoneNumber);

    Optional<FloristShop> findByEmail(String email);

    List<FloristShop> findAll();

}
