package has.Room;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Size(min = 0)
    @Pattern(regexp = "[0-9]")
    private int bedsDouble;
    @NotNull
    @Size(min = 0)
    @Pattern(regexp = "[0-9]")
    private int bedsSingle;
    @NotNull
    @Size(min = 1)
    @Pattern(regexp = "[0-9]")
    private int number;
    @NotNull
    @Pattern(regexp = "[0-9]")
    @Size(min = 0, max = 5)
    private int roomClass;
    @NotNull
    @Size(min = 0, max = 3)
    @Pattern(regexp = "[0-9]")
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
