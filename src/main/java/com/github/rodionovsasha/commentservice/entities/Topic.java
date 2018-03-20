package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
public class Topic implements Serializable {
    private static final long serialVersionUID = 28116440335967L;

    @Id @GeneratedValue
    private int id;

    @NotBlank
    private String title;

    @Setter
    private boolean archived = false;

    @Temporal(TemporalType.TIMESTAMP)
    private final Date date = new Date();

    @JsonIgnore
    @JoinColumn @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    public Topic(String title, User owner) {
        this.title = title;
        this.owner = owner;
    }

    public Date getDate() {
        return (Date)date.clone();
    }
}
