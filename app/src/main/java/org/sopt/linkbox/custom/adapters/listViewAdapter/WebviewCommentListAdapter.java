package org.sopt.linkbox.custom.adapters.listViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.boxListPage.WebviewActivity;
import org.sopt.linkbox.custom.data.mainData.url.CommentListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.helper.ViewHolder;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;
import org.sopt.linkbox.custom.widget.RoundedImageView;
import org.sopt.linkbox.debugging.RetrofitDebug;

import java.io.File;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sy on 2015-08-28.
 */
public class WebviewCommentListAdapter extends BaseAdapter {
    private static final String TAG = "TEST/" + WebviewCommentListAdapter.class.getName() + " : ";

    private ArrayList<CommentListData> source = null;
    private LayoutInflater layoutInflater = null;
    private Context context = null;
    private UrlListData urlListData = null;
    private CommentListData commentListData = null;
    private UrlListWrapper urlListWrapper = null;

    public WebviewCommentListAdapter(Context context, ArrayList<CommentListData> source, UrlListData urlListData) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
        this.context = context;
        this.urlListData = urlListData;
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

    public void setSource(ArrayList<CommentListData> source) {
        this.source = source;
        notifyDataSetChanged();
    }

    public interface NumberofCommentChange{
        public void NumberofCommentChange(int num);
    }
    private NumberofCommentChange mNumberofCommentChange;

    public void setNumberofCommentChange(NumberofCommentChange numberofCommentChange){
        this.mNumberofCommentChange = numberofCommentChange;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_comment_list_webview, viewGroup, false);
        }

        commentListData = (CommentListData) getItem(i);
        RoundedImageView ivProfile = ViewHolder.get(view, R.id.IV_profile_comment_webview);
        TextView tvName = ViewHolder.get(view, R.id.TV_name_comment_webview);
        TextView tvContents = ViewHolder.get(view, R.id.TV_contents_comment_webview);
        final ImageButton ibDelete = ViewHolder.get(view, R.id.IB_comment_delete_webview);

        Glide.with(context).load(commentListData.usrProfile).into(ivProfile);
        tvName.setText(commentListData.usrName);
        tvContents.setText(commentListData.comment);

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "I clicked! " + ((CommentListData)getItem(i)).commentKey);
                urlListWrapper.commentRemove(urlListData, (CommentListData)getItem(i), new CommentDeleteCallback(ibDelete, (CommentListData)getItem(i)));
                ibDelete.setEnabled(false);
            }
        });
        return view;
    }

    class CommentDeleteCallback implements Callback<MainServerData<Object>> {
        ImageButton ibDelete = null;
        CommentListData commentListData = null;
        public CommentDeleteCallback(ImageButton ibDelete, CommentListData commentListData) {
            this.ibDelete = ibDelete;
            this.commentListData = commentListData;
        }
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            ibDelete.setEnabled(true);

            if (wrappedObject.result) {
                LinkBoxController.commentListSource.remove(commentListData);
                LinkBoxController.notifyCommentDataSetChanged();
                if(mNumberofCommentChange != null)
                    mNumberofCommentChange.NumberofCommentChange(getCount());
            }
            else {
                Toast.makeText(context, "댓글 지우기 실패.", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(context, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            RetrofitDebug.debug(error);
            ibDelete.setEnabled(true);
        }
    }
}
