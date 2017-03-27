package has.Room;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity(name = "ROOM")
@Scope("session")
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Min(0)
    private int bedsDouble;

    @NotNull
    @Min(0)
    private int bedsSingle;

    @NotNull
    @Min(0)
    private int number;

    @NotNull
    @Min(0)
    @Max(2)
    private int roomClass;

    @NotNull
    @Min(0)
    @Max(3)
    private int status;

    private boolean children;

    private boolean minibar;

    private boolean pets;

    public Room() {
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
