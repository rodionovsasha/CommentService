package com.github.rodionovsasha.commentservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Entity @Getter @Setter
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
}
