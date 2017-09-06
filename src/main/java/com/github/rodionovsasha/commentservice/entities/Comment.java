package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Entity @Getter
public class Comment {
    @Id @GeneratedValue
    private long id;
    @NotBlank
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @JoinColumn @ManyToOne @JsonIgnore
    private User user;

    @JoinColumn @ManyToOne @JsonIgnore
    private Topic topic;

    public Comment(final String content, final User user, final Topic topic) {
        this.content = content;
        this.user = user;
        this.topic = topic;
    }
}