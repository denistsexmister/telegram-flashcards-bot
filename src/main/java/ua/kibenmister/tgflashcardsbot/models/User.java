package ua.kibenmister.tgflashcardsbot.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

}
