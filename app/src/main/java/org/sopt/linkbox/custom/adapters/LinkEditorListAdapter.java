package org.sopt.linkbox.custom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-07-10.
 */
public class LinkEditorListAdapter extends BaseAdapter {
    private ArrayList<String> source = null;
    private LayoutInflater layoutInflater = null;

    public LinkEditorListAdapter(Context context, ArrayList<String> source) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
    }

    public void setSource(ArrayList<String> source) {
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
            view = layoutInflater.inflate(R.layout.layout_editor_list_link_editor, viewGroup, false);
        }
        ImageView ivProfile = ViewHolder.get(view, R.id.IV_profile_link_editor);
        TextView tvName = ViewHolder.get(view, R.id.TV_editor_name_link_editor);

        return view;
    }
}
