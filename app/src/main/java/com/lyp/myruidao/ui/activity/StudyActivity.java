package com.lyp.myruidao.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lyp.myruidao.R;
import com.lyp.myruidao.adapter.TabViewPagerAdapter;
import com.lyp.myruidao.ui.fragment.CourseDetailFragment;
import com.lyp.myruidao.ui.fragment.CourseIntroFragment;
import com.lyp.myruidao.ui.fragment.CourseQRFragment;
import com.lyp.networkhelper.activity.BaseActivity;
import com.orhanobut.logger.Logger;

import java.util.Map;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


public class StudyActivity extends BaseActivity {

    private Toolbar studyToolbar;
    private TextView studyTitle;
    private VideoView studyVideoView;
    private ViewPager studyViewPager;
    private TabLayout studyTabLayout;
    private ProgressBar studyProgressBar;

    private TabViewPagerAdapter tabViewPagerAdapter;

    private MediaController mMediaController;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_study;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Map<String, Object> courseBean = (Map<String, Object>) intent.getSerializableExtra("course_bean");
        if (courseBean != null) {
            studyTitle.setText((CharSequence) courseBean.get("title"));

            studyProgressBar.setVisibility(View.GONE);
//            mMediaController = new MediaController(this);
//            studyVideoView.setMediaController(mMediaController);
            studyVideoView.setVideoPath((String) courseBean.get("url"));
            studyVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
            studyVideoView.requestFocus();
            studyVideoView.start();
        }
    }

    @Override
    protected void initViews() {
        Vitamio.isInitialized(this);

        studyToolbar = findView(R.id.study_toolbar);
        setToolbarWithBack(studyToolbar, 0);

        studyTitle = findView(R.id.study_title);
        studyVideoView = findView(R.id.study_video);
        studyProgressBar = findView(R.id.study_progressBar);
        studyProgressBar.setVisibility(View.VISIBLE);

        studyViewPager = findView(R.id.study_view_pager);
        studyTabLayout = findView(R.id.study_tab_layout);

        studyViewPager.setOffscreenPageLimit(3);
        tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.addFragments(new CourseIntroFragment(), "课程简介");
        tabViewPagerAdapter.addFragments(new CourseDetailFragment(), "课程详细");
        tabViewPagerAdapter.addFragments(new CourseQRFragment(), "扫码播放");
        studyViewPager.setAdapter(tabViewPagerAdapter);

        studyTabLayout.addTab(studyTabLayout.newTab().setText("课程简介"));
        studyTabLayout.addTab(studyTabLayout.newTab().setText("课程详细"));
        studyTabLayout.addTab(studyTabLayout.newTab().setText("扫码播放"));
        studyTabLayout.setupWithViewPager(studyViewPager);
    }

    @Override
    protected void initEvents() {

    }
}
