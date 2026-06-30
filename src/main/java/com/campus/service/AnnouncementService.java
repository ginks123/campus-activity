package com.campus.service;

import com.campus.dao.AnnouncementDao;
import com.campus.entity.Announcement;

import java.util.List;

public class AnnouncementService {

    private final AnnouncementDao announcementDao = new AnnouncementDao();

    public List<Announcement> findAll() {
        return announcementDao.findAll();
    }

    public void add(String title, String content, int publisher) {
        announcementDao.insert(title, content, publisher);
    }

    public void delete(int announcementId) {
        announcementDao.delete(announcementId);
    }

    public int countAll() {
        return announcementDao.countAll();
    }
}
