package com.lyp.myruidao.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyp.myruidao.R;
import com.lyp.myruidao.bean.CourseList;

import java.util.List;

/**
 * Created by lyp on 2016/12/9.
 */

public class CoursesListAdapter implements ExpandableListAdapter {

    private Context mContext;
    private List<CourseList> mData;

    public CoursesListAdapter(Context context, List<CourseList> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mData.get(groupPosition).getmList() == null) {
            return 0;
        }
        return mData.get(groupPosition).getmList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition).getTitle();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getmList().get(childPosition).get("title");
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = getTextView();
        textView.setTextColor(Color.parseColor("#454545"));
        textView.setText(getGroup(groupPosition).toString());
        ll.addView(textView);
        return ll;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ImageView logo = new ImageView(mContext);
        logo.setImageResource(R.drawable.ic_video_16);
        logo.setPadding(52, 20, 0, 0);
        ll.addView(logo);
        TextView textView = getTextView();
        textView.setTextSize(14);
        textView.setPadding(8, 8, 16, 8);
        textView.setText(getChild(groupPosition, childPosition).toString());
        ll.addView(textView);
        return ll;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    private TextView getTextView() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64);
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(52, 8, 52, 8);
        textView.setTextSize(16);
        return textView;
    }
}