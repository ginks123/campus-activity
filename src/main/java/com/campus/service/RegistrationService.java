package com.campus.service;

import com.campus.dao.RegistrationDao;
import com.campus.entity.ActivityStats;
import com.campus.entity.Registration;

import java.util.List;

public class RegistrationService {

    private final RegistrationDao registrationDao = new RegistrationDao();

    public String register(int userId, int activityId) {
        return registrationDao.register(userId, activityId);
    }

    public String cancel(int userId, int activityId) {
        return registrationDao.cancel(userId, activityId);
    }

    public List<Registration> findByUser(int userId) {
        return registrationDao.findByUser(userId);
    }

    public List<Registration> findAll(String auditStatus) {
        return registrationDao.findAll(auditStatus);
    }

    public void audit(int registrationId, String status, int auditor) {
        registrationDao.audit(registrationId, status, auditor);
    }

    public int countByStatus(String status) {
        return registrationDao.countByStatus(status);
    }

    public int countAll() {
        return registrationDao.countAll();
    }

    public List<ActivityStats> getStatistics() {
        return registrationDao.getStatistics();
    }
}
