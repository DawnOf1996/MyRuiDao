package com.lyp.myruidao.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lyp.myruidao.R;
import com.lyp.myruidao.adapter.PositionAdapter;
import com.lyp.myruidao.bean.Position;
import com.lyp.myruidao.ui.activity.PositionDetailActivity;
import com.lyp.myruidao.util.Constant;
import com.lyp.myruidao.util.SPUtils;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class PositionCoursesFragment extends BaseFragment {

    private RecyclerView rcPosition;
    private PositionAdapter mAdapter;
    private List<Position> mData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_position_courses;
    }

    @Override
    protected void initViews(View view) {
        rcPosition = findView(R.id.rc_position);
        rcPosition.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void initData() {

        Map<String, String> map = new HashMap();
        map.put("Cookie", (String) SPUtils.get(getContext(), "jsessionid", ""));

        OkHttpUtils
                .post()
                .url(Constant.URL_POSITION_LIST)
                .headers(map)
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
                        mData.clear();
                        String json = (String) response;
                        Logger.json(json);

                        try {
                            JSONObject object = new JSONObject(json);
                            JSONArray indexPositionList = object.getJSONArray("indexPositionList");
                            if (indexPositionList == null || indexPositionList.length()==0) {
                                toast("请重新登录");
                                return;
                            }
                            for (int i = 0; i < indexPositionList.length(); i++) {
                                JSONObject positionObject = indexPositionList.getJSONObject(i);
                                Position position = new Position();
                                position.setVideoNums(positionObject.getInt("videoNums"));
                                position.setStudy(positionObject.getInt("isStudy"));
                                position.setCourseNums(positionObject.getInt("courseNums"));
                                position.setPostName(positionObject.getString("postName"));
                                position.setPostDesc(positionObject.getString("postDesc"));
                                position.setPostUrl(positionObject.getString("postUrl"));
                                position.setCourseHours(positionObject.getInt("courseHours"));
                                position.setPersonNums(positionObject.getInt("personNums"));
                                position.setStudyDays(positionObject.getString("studyDays"));
                                position.setTestNums(positionObject.getInt("testNums"));
                                position.setPostId(positionObject.getInt("postId"));
                                mData.add(position);
                            }
                            mAdapter = new PositionAdapter(getContext(), mData);
                            Logger.d("获得岗位数据"+mData.size()+"条");
                            rcPosition.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new PositionAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getActivity(), PositionDetailActivity.class);
                                    intent.putExtra("bean_position", mData.get(position));
                                    startActivity(intent);
                                }

                                @Override
                                public void onItemLongClick(View view, int position) {}
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });


    }

    @Override
    protected void initEvents(View view) {
    }

}
