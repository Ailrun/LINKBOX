package org.sopt.linkbox.custom.adapters.swapeListViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-07-02.
 *
 */
public class LinkBoxUrlListAdapter extends BaseSwipeAdapter {
    private static final String TAG = "TEST/" + LinkBoxUrlListAdapter.class.getName() + " : ";
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
    public int getSwipeLayoutResourceId(int i) {
        return R.id.SL_swape_link_box;
    }

    @Override
    public View generateView(int i, ViewGroup viewGroup) {
        return layoutInflater.from(context).inflate(R.layout.layout_url_list_link_box, viewGroup, false);
    }

    @Override
    public void fillValues(int i, View view) {
        UrlListData urlListData = (UrlListData)getItem(i);
        TextView tvUrlTitle = ViewHolder.get(view, R.id.TV_url_name_link_box);
        TextView tvUrlAddress = ViewHolder.get(view, R.id.TV_url_address_link_box);
        TextView tvUrlWriter = ViewHolder.get(view, R.id.TV_url_writer_link_box);
        TextView tvUrlDate = ViewHolder.get(view, R.id.TV_url_date_link_box);

        ImageView ivUrlThumb = ViewHolder.get(view, R.id.IV_thumb_link_box);

        ImageButton ibDelete = ViewHolder.get(view, R.id.IB_delete_link_box);
        ImageButton ibEdit = ViewHolder.get(view, R.id.IB_edit_link_box);
        ImageButton ibShare = ViewHolder.get(view, R.id.IB_share_link_box);

        tvUrlTitle.setText(urlListData.urlTitle);
        tvUrlAddress.setText(urlListData.url);
        tvUrlWriter.setText(urlListData.urlWriterUsrName);
        tvUrlDate.setText(urlListData.urlDate);

        Glide.with(context).load(urlListData.urlThumbnail).into(ivUrlThumb);

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Delete!");
                //TODO : delete url check popup
                //Must include which url push this button
            }
        });
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit!");
                //TODO : goto url edit popup
            }
        });
        ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : goto share popup
            }
        });
    }
}
