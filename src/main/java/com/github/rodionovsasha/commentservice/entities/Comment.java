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
@Entity
public class Comment implements Serializable {
    private static final long serialVersionUID = 28116440335967L;

    @Id @GeneratedValue @Getter
    private long id;

    @NotBlank @Getter @Setter
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @JoinColumn @ManyToOne @JsonIgnore
    @Getter @Setter
    private User user;

    @JoinColumn @ManyToOne @JsonIgnore
    @Getter @Setter
    private Topic topic;

    public Date getDate() {
        return (Date) date.clone();
    }
}
