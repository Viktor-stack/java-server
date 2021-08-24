package com.rujavacours.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@DynamicUpdate
@Entity
@Table(name = "ACTIVITY", schema = "tasklist", catalog = "postgres")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column
    private boolean activated;

    @NotBlank
    @Column(updatable = false)
    private String uuid;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
