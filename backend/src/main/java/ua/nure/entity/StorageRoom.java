package ua.nure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = {"floristShop"})
@EqualsAndHashCode(exclude = {"floristShop"})
@Accessors(chain = true)
public class StorageRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "storage_room_id")
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house")
    private String house;

    @Column(name = "max_capacity")
    private Integer maxCapacity;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "florist_shop_id")
    protected FloristShop floristShop;

    @OneToOne(mappedBy = "storageRoom", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SmartSystem smartSystem;

    @JsonIgnore
    @OneToMany(mappedBy = "storageRoom", fetch = FetchType.EAGER)
    Set<FlowerStorage> flowerStorages;

    public void addStorage(FlowerStorage flowerStorage) {
        flowerStorages.add(flowerStorage);
    }

    public void removeStorage(FlowerStorage flowerStorage) {
        flowerStorages.remove(flowerStorage);
    }

}
