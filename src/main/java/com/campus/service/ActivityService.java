package com.campus.service;

import com.campus.dao.ActivityDao;
import com.campus.entity.Activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityService {

    private final ActivityDao activityDao = new ActivityDao();

    public List<Activity> findActivities(String keyword, String categoryId, String status) {
        return activityDao.findActivities(keyword, categoryId, status);
    }

    public Map<String, Object> findActivitiesPaginated(String keyword, String categoryId, String status, int page, int pageSize) {
        int total = activityDao.countActivities(keyword, categoryId, status);
        int offset = (page - 1) * pageSize;
        List<Activity> list = activityDao.findActivitiesPaginated(keyword, categoryId, status, offset, pageSize);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", totalPages);
        return result;
    }

    public Activity findById(int activityId) {
        return activityDao.findById(activityId);
    }

    public void save(Activity activity) {
        if (activity.getActivityId() != null && activity.getActivityId() > 0) {
            activityDao.update(activity);
        } else {
            activityDao.insert(activity);
        }
    }

    public void delete(int activityId) {
        activityDao.delete(activityId);
    }

    public int countAll() {
        return activityDao.countAll();
    }

    public int countOpen() {
        return activityDao.countOpen();
    }
}
