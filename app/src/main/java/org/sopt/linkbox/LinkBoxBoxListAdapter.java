package org.sopt.linkbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-07-02.
 *
 */
class LinkBoxBoxListAdapter extends BaseAdapter {

    private ArrayList<LinkBoxBoxListData> source = null;
    private LayoutInflater layoutInflater = null;

    public LinkBoxBoxListAdapter(Context context, ArrayList<LinkBoxBoxListData> source) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<LinkBoxBoxListData> source) {
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
        LinkBoxBoxListViewHolder linkBoxBoxListViewHolder;

        if (view == null) {
            linkBoxBoxListViewHolder = new LinkBoxBoxListViewHolder();
            view = layoutInflater.inflate(R.layout.layout_box_list_link_it, viewGroup, false);

            linkBoxBoxListViewHolder.tvBoxName = (TextView) view.findViewById(R.id.TV_box_name_link_box);

            view.setTag(linkBoxBoxListViewHolder);
        }
        else {
            linkBoxBoxListViewHolder = (LinkBoxBoxListViewHolder) view.getTag();
        }

        LinkBoxBoxListData linkBoxBoxListData = (LinkBoxBoxListData)getItem(i);

        linkBoxBoxListViewHolder.tvBoxName.setText(linkBoxBoxListData.boxName);

        return view;
    }
}

class LinkBoxBoxListData {
    String boxName;
}

class LinkBoxBoxListViewHolder {
    TextView tvBoxName;
}
