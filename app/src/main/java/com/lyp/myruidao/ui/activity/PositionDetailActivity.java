package com.lyp.myruidao.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.lyp.myruidao.R;
import com.lyp.myruidao.adapter.TabViewPagerAdapter;
import com.lyp.myruidao.ui.fragment.PositionInfoFragment;
import com.lyp.myruidao.ui.fragment.PositionStageFragment;
import com.lyp.networkhelper.activity.BaseActivity;

public class PositionDetailActivity extends BaseActivity {

    private Toolbar posiDetailToolbar;
    private ViewPager posiDetailViewPager;
    private TabLayout posiDetailTab;
    private TabViewPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_position_detail;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        posiDetailToolbar = findView(R.id.posi_detail_toolbar);
        setToolbarWithBack(posiDetailToolbar, 0);

        posiDetailViewPager = findView(R.id.posi_detail_view_pager);
        posiDetailTab = findView(R.id.posi_detail_tab);

        mAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragments(new PositionInfoFragment(), "岗位信息");
        mAdapter.addFragments(new PositionStageFragment(), "阶段学习");
        posiDetailViewPager.setOffscreenPageLimit(2);
        posiDetailViewPager.setAdapter(mAdapter);
        posiDetailTab.addTab(posiDetailTab.newTab().setText("岗位信息"));
        posiDetailTab.addTab(posiDetailTab.newTab().setText("阶段学习"));
        posiDetailTab.setupWithViewPager(posiDetailViewPager);
    }

    @Override
    protected void initEvents() {

    }
}
