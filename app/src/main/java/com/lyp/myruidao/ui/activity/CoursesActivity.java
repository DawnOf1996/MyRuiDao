package com.lyp.myruidao.ui.activity;


import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lyp.myruidao.R;
import com.lyp.myruidao.adapter.CoursesListAdapter;
import com.lyp.myruidao.bean.CourseList;
import com.lyp.myruidao.util.Constant;
import com.lyp.myruidao.util.SPUtils;
import com.lyp.networkhelper.activity.BaseActivity;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class CoursesActivity extends BaseActivity {

    private Toolbar coursesToolbar;
    private TextView coursesTitle;
    private ExpandableListView coursesListView;

    private List<CourseList> mData = new ArrayList<>();
    private CoursesListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_courses;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();

        String courseName = intent.getStringExtra("course_name");
        if (courseName != null) {
            coursesTitle.setText(courseName);
        }

        int courseId = intent.getIntExtra("course_id", -1);

        Map<String, String> map = new HashMap();
        map.put("Cookie", (String) SPUtils.get(this, "jsessionid", ""));

        if (courseId != -1) {
            OkHttpUtils
                    .post()
                    .url(Constant.URL_COURSE_LIST)
                    .headers(map)
                    .addParams("courseid", String.valueOf(courseId))
                    .build()
                    .execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(Response response, int id) throws Exception {
                            return response.body().string();
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Logger.e(e.getMessage());
                        }

                        @Override
                        public void onResponse(Object response, int id) {
                            String json = (String) response;
                            Logger.json(json);

                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                JSONArray courseList = jsonObject.getJSONArray("courseList");

                                for (int i = 0; i < courseList.length(); i++) {
                                    JSONObject object = courseList.getJSONObject(i);

                                    CourseList bean = new CourseList(
                                            object.getString("id"),
                                            object.getInt("parentId"),
                                            object.getString("title"),
                                            object.getInt("time"),
                                            object.getString("courseId"),
                                            object.getString("url")
                                    );

                                    JSONArray list = object.getJSONArray("mList");
                                    List<Map<String, Object>> mList = new ArrayList<>();
                                    for (int j = 0; j < list.length(); j++) {
                                        Map<String, Object> map = new HashMap<>();
                                        JSONObject o = list.getJSONObject(j);
                                        map.put("id", o.getString("id"));
                                        map.put("parentId", o.getString("parentId"));
                                        map.put("title", o.getString("title"));
                                        map.put("time", o.getInt("time"));
                                        map.put("courseId", o.getString("courseId"));
                                        map.put("url", o.getString("url"));
                                        mList.add(map);
                                    }
                                    bean.setmList(mList);

                                    mData.add(bean);
                                }

                                mAdapter = new CoursesListAdapter(CoursesActivity.this, mData);
                                coursesListView.setAdapter(mAdapter);

                            } catch (JSONException e) {
                                Logger.e(e.getMessage());
                            }

                        }
                    });
        }
    }

    @Override
    protected void initViews() {
        coursesToolbar = findView(R.id.courses_toolbar);
        setToolbarWithBack(coursesToolbar, 0);
        coursesTitle = findView(R.id.courses_title);
        coursesListView = findView(R.id.courses_list_view);
    }

    @Override
    protected void initEvents() {
        coursesListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Intent intent = new Intent(CoursesActivity.this, StudyActivity.class);
                intent.putExtra("course_bean", (Serializable) mData.get(groupPosition).getmList().get(childPosition));
                intent.putExtra("course_title", coursesTitle.getText().toString());
                startActivity(intent);
                return false;
            }
        });
    }
}
