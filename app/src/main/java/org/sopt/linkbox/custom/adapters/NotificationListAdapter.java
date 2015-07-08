package org.sopt.linkbox.custom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.LinkBoxListData;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.util.ArrayList;

public class NotificationListAdapter extends BaseExpandableListAdapter {

    private ArrayList<LinkBoxListData> group = null;
    private ArrayList<ArrayList<LinkBoxListData>> source = null;
    private LayoutInflater layoutInflater = null;

    public NotificationListAdapter(Context context, ArrayList<LinkBoxListData> group,
                                   ArrayList<ArrayList<LinkBoxListData>> source) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.group = group;
        this.source = source;
    }

    public void setGroup(ArrayList<LinkBoxListData> group) {
        this.group = group;
        notifyDataSetChanged();
    }

    public void setSource(ArrayList<ArrayList<LinkBoxListData>> source) {
        this.source = source;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }
    @Override
    public int getChildrenCount(int i) {
        return source.get(i).size();
    }
    @Override
    public LinkBoxListData getGroup(int i) {
        return group.get(i);
    }
    @Override
    public LinkBoxListData getChild(int i, int i2) {
        return source.get(i).get(i2);
    }
    @Override
    public long getGroupId(int i) {
        return i;
    }
    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_notification_list_header, viewGroup, false);
        }
        LinkBoxListData linkBoxListData = getGroup(i);
        TextView tvGroupNotification = ViewHolder.get(view, R.id.TV_group_notification_user_setting);
        CheckBox cbGroupNotification = ViewHolder.get(view, R.id.CB_group_notification_user_setting);
        tvGroupNotification.setText(linkBoxListData.cbname);
        cbGroupNotification.setChecked(LinkBoxController.defaultAlarm);
        cbGroupNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LinkBoxController.defaultAlarm = b;
            }
        });
        return view;
    }
    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_notification_list_item, viewGroup, false);
        }
        LinkBoxListData linkBoxListData = getChild(i, i2);
        TextView tvChildNotification = ViewHolder.get(view, R.id.TV_child_notification_user_setting);
        CheckBox cbChildNotification = ViewHolder.get(view, R.id.CB_child_notification_user_setting);
        tvChildNotification.setText(linkBoxListData.cbname);
        cbChildNotification.setChecked(LinkBoxController.defaultAlarm);
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}