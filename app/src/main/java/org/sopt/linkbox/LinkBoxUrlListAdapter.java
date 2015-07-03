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
class LinkBoxUrlListAdapter extends BaseAdapter {

    private ArrayList<LinkBoxUrlListData> source = null;
    private LayoutInflater layoutInflater = null;

    public LinkBoxUrlListAdapter(Context context, ArrayList<LinkBoxUrlListData> source) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<LinkBoxUrlListData> source) {
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
        LinkBoxUrlListData linkBoxUrlListData = (LinkBoxUrlListData)getItem(i);
        TextView tvUrlTitle = ViewHolder.get(view, R.id.TV_url_title_link_box);
        TextView tvUrlWriterDate = ViewHolder.get(view, R.id.TV_url_writer_date_link_box);
        tvUrlTitle.setText(linkBoxUrlListData.urlTitle);
        tvUrlWriterDate.setText(linkBoxUrlListData.urlWriter);
        return view;
    }
}

class LinkBoxUrlListData {
    String url;
    String urlTitle;
    String urlWriter;
    String urlDate;
    String urlImage;
}
