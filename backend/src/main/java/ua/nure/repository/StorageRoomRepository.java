package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.StorageRoom;

@Repository
public interface StorageRoomRepository extends CrudRepository<StorageRoom, Long> {

}
