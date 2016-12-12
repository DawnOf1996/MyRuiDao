package com.lyp.myruidao.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lyp.myruidao.R;

import java.util.Map;


public class CourseDetailFragment extends BaseFragment {

    private TextView tvCourseDetail;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_course_detail;
    }

    @Override
    protected void initViews(View view) {
        tvCourseDetail = findView(R.id.tv_course_detail);
    }

    @Override
    protected void initData() {
        Intent intent = getActivity().getIntent();
        Map<String, Object> courseBean = (Map<String, Object>) intent.getSerializableExtra("course_bean");

        String detail = "课程描述：\n\n\t\t本微课程是睿道在线课程";

        String title = intent.getStringExtra("course_title");
        if (title != null) {
            detail += ("“"+title+"”中的一个完整单元的知识片段");
        }

        if (courseBean != null) {
            detail += ("，主要讲述了"+courseBean.get("title")+"方面的内容");
        }

        detail += ("。通过本课程的学习去反复练习，掌握本视频的知识点。");

        tvCourseDetail.setText(detail);
    }

    @Override
    protected void initEvents(View view) {

    }

}
