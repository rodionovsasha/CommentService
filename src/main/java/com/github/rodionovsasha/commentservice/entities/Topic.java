package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Entity
public class Topic implements Serializable {
    private static final long serialVersionUID = 28116440335967L;

    @Id @GeneratedValue @Getter
    private long id;

    @NotBlank @Getter @Setter
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @JoinColumn @ManyToOne @JsonIgnore
    @Getter @Setter
    private User starter;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter
    private List<Comment> comments = new LinkedList<>();

    public Topic(String title, User starter) {
        this.title = title;
        this.starter = starter;
    }

    public Date getDate() {
        return (Date) date.clone();
    }
}
