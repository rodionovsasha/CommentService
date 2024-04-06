package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 28116440335967L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Setter
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
