package jm.task.core.jdbc.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(includeFieldNames=true)
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name="name", unique=true, length=100)
    private String name;

    @Column (name="lastName", unique=true, length=100)
    private String lastName;

    @Column (name="age", nullable=false)
    private Byte age;

    public User(String name, String lastName, Byte age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }
}
