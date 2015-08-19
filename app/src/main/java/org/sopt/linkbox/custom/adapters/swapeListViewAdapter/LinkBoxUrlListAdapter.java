package org.sopt.linkbox.custom.adapters.swapeListViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.urlListingPage.DeleteDialogActivity;
import org.sopt.linkbox.activity.mainPage.urlListingPage.EditDialogActivity;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.helper.ViewHolder;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;

import java.io.File;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-07-02.
 *
 */
public class LinkBoxUrlListAdapter extends BaseSwipeAdapter {
    private static final String TAG = "TEST/" + LinkBoxUrlListAdapter.class.getName() + " : ";
    private ArrayList<UrlListData> source = null;
    private LayoutInflater layoutInflater = null;
    private Context context = null;
    private UrlListData urlListData = null;
    private UrlListWrapper urlListWrapper = null;

    public LinkBoxUrlListAdapter(Context context, ArrayList<UrlListData> source) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
        this.context = context;
        urlListWrapper = new UrlListWrapper();
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
        return LayoutInflater.from(context).inflate(R.layout.layout_url_list_link_box, viewGroup, false);
    }
    @Override
    public void fillValues(int i, View view) {
        urlListData = (UrlListData)getItem(i);
        fillMainValues(i, view);
        fillHiddenValues(i, view);
    }

    private void fillMainValues(int i, View view) {
        TextView tvUrlTitle = ViewHolder.get(view, R.id.TV_url_name_link_box);
        TextView tvUrlAddress = ViewHolder.get(view, R.id.TV_url_address_link_box);
        TextView tvUrlWriter = ViewHolder.get(view, R.id.TV_url_writer_link_box);
        TextView tvUrlDate = ViewHolder.get(view, R.id.TV_url_date_link_box);

        ImageView ivUrlThumb = ViewHolder.get(view, R.id.IV_thumb_link_box);
        final ImageView ivLike = ViewHolder.get(view, R.id.IV_like_link_box);

        tvUrlTitle.setText(urlListData.urlTitle);
        tvUrlAddress.setText(urlListData.url);
        tvUrlWriter.setText(urlListData.urlWriterUsrName);
        tvUrlDate.setText(urlListData.urlDate);

        Glide.with(context).load(urlListData.urlThumbnail).into(ivUrlThumb);
        ivLike.setImageResource(urlListData.good == 0 ? R.drawable.mainpage_bookmark_unchecked : R.drawable.mainpage_bookmark_checked);
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlListWrapper.like(urlListData, (1-urlListData.good), new UrlLikeCallback(urlListData, ivLike));
            }
        });
    }
    private void fillHiddenValues(final int i, View view) {
        ImageButton ibDelete = ViewHolder.get(view, R.id.IB_delete_link_box);
        ibDelete.setScaleX(0.45f);
        ibDelete.setScaleY(0.45f);
        ImageButton ibEdit = ViewHolder.get(view, R.id.IB_edit_link_box);
        ibEdit.setScaleX(0.35f);
        ibEdit.setScaleY(0.35f);
        ImageButton ibShare = ViewHolder.get(view, R.id.IB_share_link_box);
        ibShare.setScaleX(0.42f);
        ibShare.setScaleY(0.42f);

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Delete!");
                Intent intent = new Intent(context, DeleteDialogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Index", i);
                context.startActivity(intent);
            }
        });
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit!");
                Intent intent = new Intent(context, EditDialogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Index", i);
                context.startActivity(intent);
            }
        });
        ibShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra(Intent.EXTRA_SUBJECT, urlListData.urlTitle);
                intent.putExtra(Intent.EXTRA_TEXT, urlListData.url);
                intent.putExtra(Intent.EXTRA_TITLE, urlListData.urlTitle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private class UrlLikeCallback implements Callback<MainServerData<Object>> {
        UrlListData urlListData = null;
        ImageView ivLike = null;

        public UrlLikeCallback(UrlListData urlListData, ImageView ivLike) {
            this.urlListData = urlListData;
            this.ivLike = ivLike;
        }
        @Override
        public void success(MainServerData<Object> objectMainServerData, Response response) {
            urlListData.good = (1-urlListData.good);
            ivLike.setImageResource(urlListData.good == 0 ? R.drawable.mainpage_bookmark_unchecked : R.drawable.mainpage_bookmark_checked);
        }
        @Override
        public void failure(RetrofitError error) {
        }
    }
}
