package org.sopt.linkbox.custom.adapters.listViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junyoung on 2015-07-02.
 *
 */
public class LinkBoxBoxListAdapter extends BaseAdapter {
    private List<BoxListData> favorite = null;
    private ArrayList<BoxListData> source = null;
    private LayoutInflater layoutInflater = null;

    public LinkBoxBoxListAdapter(Context context, ArrayList<BoxListData> source) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<BoxListData> source) {
        this.source = source;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        setFavorite();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (favorite != null) ? favorite.size() : 0;
    }
    @Override
    public Object getItem(int i) {
        return (favorite != null && i < favorite.size() && i >= 0) ?
                favorite.get(i) : null;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {    // TODO : Read about view
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_box_list_drawer, viewGroup, false);
        }
        BoxListData boxListData = (BoxListData) getItem(i);
        TextView tvBoxName = ViewHolder.get(view, R.id.TV_box_name_link_box);
        tvBoxName.setText(boxListData.boxName);
        return view;
    }

    private void setFavorite() {
        favorite.clear();
        for (BoxListData b : source) {
            if (b.boxFavorite == 1) {
                favorite.add(b);
            }
        }
    }
}
