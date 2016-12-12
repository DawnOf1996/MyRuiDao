package com.lyp.myruidao.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.lyp.myruidao.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.Map;


public class CourseQRFragment extends BaseFragment {

    private ImageView qrImg;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_course_qr;
    }

    @Override
    protected void initViews(View view) {
        qrImg = findView(R.id.course_qr_img);
    }

    @Override
    protected void initData() {
        Intent intent = getActivity().getIntent();
        Map<String, Object> courseBean = (Map<String, Object>) intent.getSerializableExtra("course_bean");
        if (courseBean != null) {
            Bitmap qrBitmap = CodeUtils.createImage((String)courseBean.get("url"), 256, 256, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            qrImg.setImageBitmap(qrBitmap);
        }
    }

    @Override
    protected void initEvents(View view) {

    }
}
