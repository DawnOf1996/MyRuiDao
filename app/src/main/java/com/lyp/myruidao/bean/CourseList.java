package com.lyp.myruidao.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by lyp on 2016/12/11.
 */

public class CourseList implements Serializable {

    private String id;
    private int parentId;
    private String title;
    private int time;
    private String courseId;
    private String url;
    private List<Map<String, Object>> mList;

    public CourseList(String id, int parentId, String title, int time, String courseId, String url) {
        this.id = id;
        this.parentId = parentId;
        this.title = title;
        this.time = time;
        this.courseId = courseId;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Map<String, Object>> getmList() {
        return mList;
    }

    public void setmList(List<Map<String, Object>> mList) {
        this.mList = mList;
    }
}
