package has.Room;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String bedsDouble;
    private String bedsSingle;
    private String number;
    private String roomClass;
    private String status;
    private boolean children;
    private boolean pets;

    public Room(){
    }

    public Room(String bedsDouble, String bedsSingle, String number, String roomClass, String status, boolean children, boolean pets) {
        this.bedsDouble = bedsDouble;
        this.bedsSingle = bedsSingle;
        this.number = number;
        this.roomClass = roomClass;
        this.status = status;
        this.children = children;
        this.pets = pets;
    }
}
