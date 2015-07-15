package org.sopt.linkbox.custom.adapters.dragAndDropListAdapter;

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
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-07-02.
 *
 */
public class LinkBoxUrlListAdapter extends BaseAdapter {

    private ArrayList<UrlListData> source = null;
    private LayoutInflater layoutInflater = null;
    private Context context = null;

    public LinkBoxUrlListAdapter(Context context, ArrayList<UrlListData> source) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public void setSource(ArrayList<UrlListData> source) {
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
            view = layoutInflater.inflate(R.layout.layout_url_list_link_box, viewGroup, false);
        }
        UrlListData urlListData = (UrlListData)getItem(i);
        TextView tvUrlTitle = ViewHolder.get(view, R.id.TV_url_name_link_box);
        TextView tvUrlWriterDate = ViewHolder.get(view, R.id.TV_url_writer_date_link_box);

        ImageView ivUrlThumb = ViewHolder.get(view, R.id.IV_thumb_link_box);
        tvUrlTitle.setText(urlListData.urlname);
        tvUrlWriterDate.setText(urlListData.urlwriter);
        Glide.with(context).load(urlListData.urlthumb).into(ivUrlThumb);

        return view;
    }
}
