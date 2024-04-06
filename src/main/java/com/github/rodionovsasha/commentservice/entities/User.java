package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 28116440335967L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter @NotBlank
    private String name;

    @Setter
    private int age;

    @Setter
    private boolean active = true;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Topic> topics = new ArrayList<>();

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
