package com.github.rodionovsasha.commentservice.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
@Getter @Setter
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 28116440335967L;

    @Id @GeneratedValue
    private long id;
    @NotBlank
    private String name;
    private int age;
    private boolean enabled = true;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}