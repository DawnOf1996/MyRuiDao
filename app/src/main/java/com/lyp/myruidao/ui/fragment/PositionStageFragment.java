package com.lyp.myruidao.ui.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lyp.myruidao.R;
import com.lyp.myruidao.adapter.PositionStageAdapter;
import com.lyp.myruidao.bean.Position;
import com.lyp.myruidao.bean.PositionStage;
import com.lyp.myruidao.bean.PositionStageChild;
import com.lyp.myruidao.ui.activity.CoursesActivity;
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

public class PositionStageFragment extends BaseFragment {

    private TextView positionStageTitle;
    private ExpandableListView positionStageListView;

    private Position position;
    private List<PositionStage> mData = new ArrayList<>();

    private PositionStageAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_position_stage;
    }

    @Override
    protected void initViews(View view) {
        positionStageTitle = findView(R.id.position_stage_title);
        positionStageListView = findView(R.id.position_stage_list_view);
    }

    @Override
    protected void initData() {
        Intent intent = getActivity().getIntent();
        this.position = (Position) intent.getSerializableExtra("bean_position");
        if (this.position != null) {
            positionStageTitle.setText(position.getPostName());
        }

        Map<String, String> map = new HashMap();
        map.put("Cookie", (String) SPUtils.get(getContext(), "jsessionid", ""));

        OkHttpUtils
                .get()
                .url(Constant.URL_POSITION_COURSES)
                .headers(map)
                .addParams("postid", String.valueOf(position.getPostId()))
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
                            JSONArray postStageResList = jsonObject.getJSONArray("postStageResList");

                            for (int i = 0; i < postStageResList.length(); i++) {
                                JSONObject object = postStageResList.getJSONObject(i);
                                JSONArray childStageList = object.getJSONArray("childStageList");

                                PositionStage positionStage = new PositionStage(object.getString("parentStageName"));

                                for (int j = 0; j < childStageList.length(); j++) {
                                    JSONObject childObject = childStageList.getJSONObject(0);
                                    positionStage.setStageName(childObject.getString("stageName"));
                                    positionStage.setStageId(childObject.getString("stageId"));
                                    positionStage.setStageProcess(childObject.getBoolean("stageProcess"));

                                    JSONArray childStageResList = childObject.getJSONArray("childStageResList");
                                    List<PositionStageChild> list = new ArrayList<>();

                                    for (int k = 0; k < childStageResList.length(); k++) {

                                        JSONObject childStageObject = childStageResList.getJSONObject(k);


                                        list.add(
                                                new PositionStageChild(
                                                        childStageObject.getString("resName"),
                                                        childStageObject.getString("resProcess"),
                                                        childStageObject.getString("examType"),
                                                        childStageObject.getInt("resId"),
                                                        childStageObject.getInt("resType")
                                                )
                                        );
                                        positionStage.setChildStageResList(list);
                                    }

                                }

                                mData.add(positionStage);
                            }


                            if (mData.size() != 0) {
                                Logger.d("解析到"+mData.size()+"条数据");
                                mAdapter = new PositionStageAdapter(getContext(), mData);
                                positionStageListView.setAdapter(mAdapter);
                            }
                        } catch (JSONException e) {
                            Logger.e(e.getMessage());
                        }

                    }
                });
    }

    @Override
    protected void initEvents(View view) {
        positionStageListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                // toast("点击了：" +mAdapter.getChild(groupPosition, childPosition));
                Intent intent = new Intent(getActivity(), CoursesActivity.class);
                intent.putExtra("course_name", (String) mAdapter.getChild(groupPosition, childPosition));
                intent.putExtra("course_id", mData.get(groupPosition).getChildStageResList().get(childPosition).getResId());
                startActivity(intent);
                return false;
            }
        });
    }
}
