package ua.nure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@SelectBeforeUpdate
@NoArgsConstructor
@Data
@Table(name = "smart_system")
@ToString(exclude = {"storageRoom"})
@EqualsAndHashCode(exclude = {"storageRoom"})
@Accessors(chain = true)
public class SmartSystem {

    @Id
    @Column(name = "storage_room_id")
    private Long id;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "air_quality")
    private Double airQuality;

    @Column(name = "satisfaction_factor")
    private Double satisfactionFactor;

    @OneToOne
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "storage_room_id")
    private StorageRoom storageRoom;

}
