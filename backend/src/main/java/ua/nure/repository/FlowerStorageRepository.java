package ua.nure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.dto.FlowerStorageInfoDto;
import ua.nure.entity.user.FlowerStorage;

import java.util.Set;

@Repository
public interface FlowerStorageRepository extends CrudRepository<FlowerStorage, Long> {

    @Query(
            value = "SELECT flower_storage.flower_storage_id as id, flower_storage.amount as amount, flower_storage.start_date as startDate, flower.name as flowerName, "
                    + "flower.color as flowerColor, flower.shelf_life as flowerShelfLife, flower.flower_id as flowerId, "
                    + "flower_storage.storage_room_id as storageRoomId "
                    + "FROM flower_storage "
                    + "JOIN flower ON flower_storage.flower_id = flower.flower_id "
                    + "JOIN storage_room room ON flower_storage.storage_room_id = room.storage_room_id "
                    + "WHERE room.storage_room_id = ?1 AND flower_storage.storage_room_id = room.storage_room_id AND flower.flower_id = flower_storage.flower_id",
            nativeQuery = true
    )
    Set<FlowerStorageInfoDto> getAllStoragesByRoom(Long roomId);

}
