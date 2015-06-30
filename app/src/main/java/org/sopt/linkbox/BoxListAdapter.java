package org.sopt.linkbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-06-30.
 *
 */
public class BoxListAdapter extends BaseAdapter {

    private ArrayList<BoxListData> source = null;
    private LayoutInflater layoutInflater = null;

    public BoxListAdapter(Context context, ArrayList<BoxListData> source) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<BoxListData> source) {
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
        BoxListViewHolder boxListViewHolder;

        if (view == null) {
            boxListViewHolder = new BoxListViewHolder();
            view = layoutInflater.inflate(R.layout.box_list, viewGroup, false);

            boxListViewHolder.tv_boxname = (TextView) view.findViewById(R.id.TV_boxname);

            view.setTag(boxListViewHolder);
        }
        else {
            boxListViewHolder = (BoxListViewHolder) view.getTag();
        }

        BoxListData boxListData = (BoxListData)getItem(i);

        boxListViewHolder.tv_boxname.setText(boxListData.box_name);

        return view;
    }
}

class BoxListData {
    String box_name;
}

class BoxListViewHolder {
    TextView tv_boxname;
}
