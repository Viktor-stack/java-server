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
@Table(name = "category", schema = "tasklist", catalog = "postgres")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long   id;
    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;
    @Basic
    @Column(name = "completed_count", nullable = true, updatable = false)
    private Long completedCount;
    @Basic
    @Column(name = "uncompleted_count", nullable = true, updatable = false)
    private Long uncompletedCount;
    //    @Basic
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
//    @OneToMany(mappedBy = "categoryByCategoryId")
//    private Collection<Task> tasksById;
}
