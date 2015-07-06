package org.sopt.linkbox.custom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sopt.linkbox.custom.data.LinkItBoxListData;
import org.sopt.linkbox.custom.helper.ViewHolder;
import org.sopt.linkbox.R;

import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-06-30.
 *
 */
public class LinkItBoxListAdapter extends BaseAdapter {

    private ArrayList<LinkItBoxListData> source = null;
    private LayoutInflater layoutInflater = null;

    public LinkItBoxListAdapter(Context context, ArrayList<LinkItBoxListData> source) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<LinkItBoxListData> source) {
        this.source = source;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (source != null) ? source.size() : 0;
    }
    @Override
    public Object getItem(int i) {
        return (source != null && i < source.size() && i >= 0) ?
                source.get(i) : null;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_box_list_link_it, viewGroup, false);
        }
        LinkItBoxListData linkItBoxListData = (LinkItBoxListData)getItem(i);
        TextView tvBoxName = ViewHolder.get(view, R.id.TV_box_name_link_it);
        tvBoxName.setText(linkItBoxListData.boxName);
        return view;
    }
}
