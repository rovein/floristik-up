package ua.nure.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ua.nure.entity.user.FlowerStorage;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "flower_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "shelf_life")
    private Integer shelfLife;

    @Column(name = "min_temperature")
    private Integer minTemperature;

    @Column(name = "max_temperature")
    private Integer maxTemperature;

    @JsonIgnore
    @OneToMany(mappedBy = "flower", fetch = FetchType.EAGER)
    Set<FlowerStorage> flowerStorages;

}
