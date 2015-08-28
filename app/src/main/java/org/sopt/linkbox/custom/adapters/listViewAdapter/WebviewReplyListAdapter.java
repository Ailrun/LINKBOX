package org.sopt.linkbox.custom.adapters.listViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.helper.ViewHolder;
import org.sopt.linkbox.custom.widget.RoundedImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sy on 2015-08-28.
 */
public class WebviewReplyListAdapter extends BaseAdapter {
    private ArrayList<UsrListData> source = null;
    private LayoutInflater layoutInflater = null;
    private Context context = null;

    public WebviewReplyListAdapter(Context context, ArrayList<UsrListData> source) {
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
            view = layoutInflater.inflate(R.layout.layout_reply_list_webview, viewGroup, false);
        }
        UsrListData usrListData = (UsrListData) getItem(i);
        RoundedImageView ivProfile = ViewHolder.get(view, R.id.IV_profile_reply_webview);
        TextView tvName = ViewHolder.get(view, R.id.TV_name_reply_webview);
        TextView tvContents = ViewHolder.get(view, R.id.TV_contents_reply_webview);
        Glide.with(context).load(usrListData.usrProfile).into(ivProfile);
        tvName.setText(usrListData.usrName);
        //TODO 댓글 내용은 어디다 저장하고 어떻게 불러오는가
        return view;
    }

}
