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
    private int bedsDouble;
    private int bedsSingle;
    private int number;
    private int roomClass;
    private int status;
    private boolean children;
    private boolean minibar;
    private boolean pets;

    public Room(){
    }

    public Room(int bedsDouble, int bedsSingle, int number, int roomClass, int status, boolean children, boolean pets, boolean minibar) {
        this.bedsDouble = bedsDouble;
        this.bedsSingle = bedsSingle;
        this.number = number;
        this.roomClass = roomClass;
        this.status = status;
        this.children = children;
        this.pets = pets;
        this.minibar = minibar;
    }
}
