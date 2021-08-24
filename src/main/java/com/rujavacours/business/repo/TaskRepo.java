package com.rujavacours.business.repo;

import com.rujavacours.business.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t where " +

            "(:title is null or :title='' or lower(t.title) like lower(concat('%', :title,'%'))) and" +

            "(:completed is null or t.completed=:completed) and " +  // учитываем, что параметр может быть null или пустым

            "(:priorityId is null or t.priority.id=:priorityId) and " +

            "(:categoryId is null or t.category.id=:categoryId) and " +
            "(" +
            "(cast(:dateFrom as timestamp) is null or t.taskDate>=:dateFrom) and " +

            "(cast(:dateTo as timestamp) is null or t.taskDate<=:dateTo)" +

            ") and " +

            "(t.user.email=:email)" // показывать задачи только определенного пользователя, а не все
    )
    Page<Task> find(@Param("title") String title,
                    @Param("completed") Integer completed,
                    @Param("categoryId") Long categoryId,
                    @Param("priorityId") Long priorityId,
                    @Param("email") String email,
                    @Param("dateFrom") Date dateFrom,
                    @Param("dateTo") Date dateTo,
                    Pageable pageable);

    List<Task> findByUserEmailOrderByIdAsc(String email);
}
