package com.lyp.myruidao.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lyp.myruidao.R;
import com.lyp.myruidao.adapter.TabViewPagerAdapter;
import com.lyp.myruidao.ui.fragment.BlankFragment;
import com.lyp.myruidao.ui.fragment.PositionCoursesFragment;
import com.lyp.networkhelper.activity.BaseActivity;
import com.lyp.networkhelper.view.DefaultLoadingLayout;
import com.lyp.networkhelper.view.NetworkHelperLayout;

public class HomeActivity extends BaseActivity {

    private Toolbar homeToolbar;
    private ViewPager homeViewPager;
    private TabLayout homeTab;
    private TabViewPagerAdapter tabViewPagerAdapter;

    private DefaultLoadingLayout mLoadingLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        mLoadingLayout = (DefaultLoadingLayout) NetworkHelperLayout.createDefaultLayout(this, homeViewPager);

        homeToolbar = findView(R.id.home_toolbar);
        homeToolbar.setTitle(getResources().getString(R.string.app_name));
        homeToolbar.inflateMenu(R.menu.menu_main);

        homeViewPager = findView(R.id.home_view_pager);
        homeTab = findView(R.id.home_tab);
        homeTab.addTab(homeTab.newTab().setText("岗位课程"));
        homeTab.addTab(homeTab.newTab().setText("热门课程"));
        homeTab.addTab(homeTab.newTab().setText("团队介绍"));
        tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.addFragments(new PositionCoursesFragment(), "岗位课程");
        tabViewPagerAdapter.addFragments(new BlankFragment(), "热门课程");
        tabViewPagerAdapter.addFragments(new BlankFragment(), "团队介绍");
        homeViewPager.setOffscreenPageLimit(3);
        homeViewPager.setAdapter(tabViewPagerAdapter);
        homeTab.setupWithViewPager(homeViewPager);

    }

    @Override
    protected void initEvents() {
        homeToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_user:
                        //TODO
                        break;

                    case R.id.menu_logout:
                        finish();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        break;
                }
                return true;
            }
        });
    }
}
