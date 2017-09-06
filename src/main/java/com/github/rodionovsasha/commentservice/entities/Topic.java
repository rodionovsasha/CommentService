package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Entity @Getter @Setter
public class Topic {
    @Id @GeneratedValue
    private long id;
    @NotBlank
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @JoinColumn @ManyToOne @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new LinkedList<>();

    public Topic(final String name, final User user) {
        this.name = name;
        this.user = user;
    }
}
