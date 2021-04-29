package ua.nure.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import ua.nure.entity.Flower;
import ua.nure.entity.StorageRoom;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@Data
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"flower", "storageRoom"})
@ToString(exclude = {"flower", "storageRoom"})
public class FlowerStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flower_storage_id")
    private Long id;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flower_id")
    private Flower flower;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "storage_room_id")
    private StorageRoom storageRoom;


}
