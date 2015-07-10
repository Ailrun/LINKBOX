package org.sopt.linkbox.custom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.LinkBoxListData;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-07-10.
 */
public class EditBoxListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater = null;
    private ArrayList<LinkBoxListData> source = null;
    private Context context = null;

    public EditBoxListAdapter(Context context, ArrayList<LinkBoxListData> source) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
        this.context = context;
    }

    public void setSource(ArrayList<LinkBoxListData> source) {
        this.source = source;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Object getItem(int i) {
        return source.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_box_list_link_box, viewGroup, false);
        }
        LinkBoxListData linkBoxListData = (LinkBoxListData) getItem(i);
        TextView tvBoxName = ViewHolder.get(view, R.id.TV_box_name_link_box);
        tvBoxName.setText(linkBoxListData.cbname);
        return view;
    }
}
