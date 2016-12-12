package com.lyp.myruidao.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lyp.myruidao.R;

import java.util.Map;

public class CourseIntroFragment extends BaseFragment {

    private TextView courseIntroName;
    private TextView courseIntroTime;
    private Button courseIntroFavBtn;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_course_intro;
    }

    @Override
    protected void initViews(View view) {
        courseIntroName = findView(R.id.course_intro_name);
        courseIntroTime = findView(R.id.course_intro_time);
        courseIntroFavBtn = findView(R.id.course_intro_fav_btn);
    }

    @Override
    protected void initData() {
        Intent intent = getActivity().getIntent();
        Map<String, Object> courseBean = (Map<String, Object>) intent.getSerializableExtra("course_bean");
        if (courseBean != null) {
            courseIntroName.setText("课程名称：" + courseBean.get("title"));
            courseIntroTime.setText("课程时长：" + courseBean.get("time"));
        }
    }

    @Override
    protected void initEvents(View view) {
        courseIntroFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("收藏");
            }
        });
    }

}
