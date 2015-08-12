package org.sopt.linkbox.custom.adapters.listViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.adapters.imageViewAdapter.RoundedImageView;
import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-07-10.
 */
public class LinkEditorListAdapter extends BaseAdapter {
    private ArrayList<UsrListData> source = null;
    private LayoutInflater layoutInflater = null;
    private Context context = null;

    public LinkEditorListAdapter(Context context, ArrayList<UsrListData> source) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
        this.context = context;

        synchronized (Glide.class){
            if(!Glide.isSetup()){
                File file = Glide.getPhotoCacheDir(context);
                int size = 1024*1024*1024;
                DiskCache cache = DiskLruCacheWrapper.get(file, size);
                GlideBuilder builder = new GlideBuilder(context);
                builder.setDiskCache(cache);
                Glide.setup(builder);
            }
        }
    }

    public void setSource(ArrayList<UsrListData> source) {
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
            view = layoutInflater.inflate(R.layout.layout_editor_list_box_editor, viewGroup, false);
        }
        UsrListData usrListData = (UsrListData) getItem(i);
        RoundedImageView ivProfile = ViewHolder.get(view, R.id.IV_profile_link_editor);
        TextView tvName = ViewHolder.get(view, R.id.TV_editor_name_link_editor);
        Glide.with(context).load(usrListData.usrProfile).into(ivProfile);
        tvName.setText(usrListData.usrName);
        return view;
    }
}
