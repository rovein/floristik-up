package ua.nure.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FloristShop extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "florist_user_id")
    private Long id;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private Date creationDate;

    @OneToMany(mappedBy = "floristShop", fetch = FetchType.EAGER)
    Set<StorageRoom> storageRooms;

    @Override
    public String toString() {
        return "FloristShop{" +
                "id=" + id +
                ", \nname='" + name + '\'' +
                ", \ncreationDate=" + creationDate +
                ", \nemail='" + email + '\'' +
                ", \npassword='" + password + "'}";
    }
}
