package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topic> topics = new LinkedList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new LinkedList<>();

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
