package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Entity @Getter @Setter
public class Topic implements Serializable {
    private static final long serialVersionUID = 28116440335967L;

    @Id @GeneratedValue
    private long id;
    @NotBlank
    private String name;
    private DateTime date = new DateTime();

    @JoinColumn @ManyToOne @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new LinkedList<>();
}
