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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
public class Topic implements Serializable {
    @Serial
    private static final long serialVersionUID = 28116440335967L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
