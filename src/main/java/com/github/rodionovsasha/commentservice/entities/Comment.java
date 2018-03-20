package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
public class Comment implements Serializable {
    private static final long serialVersionUID = 28116440335967L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank @Setter
    private String content;

    @Setter
    private boolean archived = false;

    @Temporal(TemporalType.TIMESTAMP)
    private final Date date = new Date();

    @JsonIgnore
    @JoinColumn @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @JoinColumn @ManyToOne(fetch = FetchType.LAZY)
    private Topic topic;

    public Comment(String content, User user, Topic topic) {
        this.content = content;
        this.user = user;
        this.topic = topic;
    }

    public Date getDate() {
        return (Date)date.clone();
    }
}
