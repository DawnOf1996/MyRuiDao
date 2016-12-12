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
import com.lyp.myruidao.bean.PositionStage;
import com.lyp.myruidao.bean.PositionStageChild;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyp on 2016/12/9.
 */

public class PositionStageAdapter implements ExpandableListAdapter {

    private Context mContext;
    private List<PositionStage> mData;

    private String[] parentNames;
    private String[][] childNames;

    public PositionStageAdapter(Context context, List<PositionStage> data) {
        this.mContext = context;
        this.mData = data;

        this.parentNames = new String[mData.size()];
        this.childNames = new String[mData.size()][];

        for (int i = 0; i < mData.size(); i++) {
            this.parentNames[i] = mData.get(i).getParentStageName() + " - " + mData.get(i).getStageName();

            List<PositionStageChild> childList = mData.get(i).getChildStageResList();
            if (childList!= null && childList.size() != 0) {
                String[] cNames = new String[childList.size()];

                for (int j = 0; j < childList.size(); j++) {
                    cNames[j] = childList.get(j).getResName();
                    // Logger.d(i + "\n" + childList.get(j).toString());
                }
                childNames[i] = cNames;
            }
        }

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return parentNames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (childNames[groupPosition] == null) {
            return 0;
        }
        return childNames[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentNames[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childNames[groupPosition][childPosition];
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
//        ImageView logo = new ImageView(mContext);
//        logo.setImageResource(logos[groupPosition]);
//        logo.setPadding(36, 15, 0, 0);
//        ll.addView(logo);
        TextView textView = getTextView();
        textView.setTextColor(Color.parseColor("#454545"));
        textView.setText(getGroup(groupPosition).toString());
        ll.addView(textView);
        return ll;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getTextView();
        textView.setTextSize(14);
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;
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