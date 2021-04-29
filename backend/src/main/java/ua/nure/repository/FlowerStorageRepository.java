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
            value = "SELECT con.contract_id as id, con.date as date , con.price as price, service.name as serviceName," +
                    " cl_cp.name as cleaningCompanyName, cust_cp.name as customerCompanyName, room.room_id as roomId, " +
                    "service.cleaning_service_id as cleaningServiceId " +
                    "FROM contract con, cleaning_service service, room, cleaning_company cl_cp, customer_company cust_cp " +
                    "WHERE cust_cp.email = ?1 AND con.room_id = room.room_id and con.cleaning_service_id = service.cleaning_service_id " +
                    "and service.cleaning_company_id = cl_cp.cleaning_company_id and room.customer_company_id = cust_cp.customer_company_id",
            nativeQuery = true
    )
    Set<FlowerStorageInfoDto> getAllStoragesByFlower(Long flowerId);

    @Query(
            value = "SELECT con.contract_id as id, con.date as date , con.price as price, service.name as serviceName," +
                    " cl_cp.name as cleaningCompanyName, cust_cp.name as customerCompanyName, room.room_id as roomId, " +
                    "service.cleaning_service_id as cleaningServiceId " +
                    "FROM contract con, cleaning_service service, room, cleaning_company cl_cp, customer_company cust_cp " +
                    "WHERE cl_cp.email = ?1 AND con.room_id = room.room_id and con.cleaning_service_id = service.cleaning_service_id " +
                    "and service.cleaning_company_id = cl_cp.cleaning_company_id and room.customer_company_id = cust_cp.customer_company_id",
            nativeQuery = true
    )
    Set<FlowerStorageInfoDto> getAllStoragesByRoom(Long roomId);

}
