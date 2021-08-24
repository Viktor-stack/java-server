package com.rujavacours.business.service;

import com.rujavacours.business.entity.Stat;
import com.rujavacours.business.repo.StatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatService {
    private final StatRepo statRepo;

    @Autowired
    public StatService(StatRepo statRepo) {
        this.statRepo = statRepo;
    }

    public Stat findStat(String email) {
        return statRepo.findByUserEmail(email);
    }

}
