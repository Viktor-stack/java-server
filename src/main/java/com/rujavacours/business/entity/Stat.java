package com.rujavacours.business.entity;

import com.rujavacours.auth.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "STAT", schema = "tasklist", catalog = "postgres")
public class Stat {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "completed_total", nullable = false, updatable = false)
    private Long completedTotal;
    @Basic
    @Column(name = "uncompleted_total", nullable = false, updatable = false)
    private Long uncompletedTotal;
//    @Basic
//    @Column(name = "user_id", updatable = false)
//    private Long userId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)
    private User user;
}
