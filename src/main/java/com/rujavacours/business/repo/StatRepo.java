package com.rujavacours.business.repo;

import com.rujavacours.business.entity.Stat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepo extends CrudRepository<Stat, Long> {
    Stat findByUserEmail(String email);
}
