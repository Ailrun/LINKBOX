package org.sopt.linkbox.custom.adapters.cardViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.boxListPage.BoxDeleteDialogActivity;
import org.sopt.linkbox.activity.mainPage.boxListPage.BoxEditActivity;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.helper.BoxImageSaveLoad;
import org.sopt.linkbox.custom.helper.ViewHolder;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-07-10.
 *
 */
public class BoxEditBoxListAdapter extends BaseAdapter {
    private static final String TAG = "TEST/" + BoxEditBoxListAdapter.class.getName() + " : ";
    private static BitmapDrawable bookmark = null;
    private static BitmapDrawable bookmarkSelected = null;

    private LayoutInflater layoutInflater = null;
    private ArrayList<BoxListData> source = null;
    private Context context = null;
    private BoxListWrapper boxListWrapper = null;
    private BoxImageSaveLoad boxImageSaveLoader = null;

    public BoxEditBoxListAdapter(Context context, ArrayList<BoxListData> source) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
        this.context = context;
        boxListWrapper = new BoxListWrapper();
        boxImageSaveLoader = new BoxImageSaveLoad(context.getApplicationContext());

        if (bookmark == null) {
            bookmark = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_box_bookmark);
        }
        if (bookmarkSelected == null) {
            bookmarkSelected = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_box_bookmark_selected);
        }
    }

    public void setSource(ArrayList<BoxListData> source) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_box_list_link_box, viewGroup, false);
        }
        BoxListData boxListData = (BoxListData) getItem(i);
        Log.e("Loaded Image Number", boxListData.toString());
        Bitmap boxImage = boxImageSaveLoader.loadProfileImage(i);
        if (boxImage == null) {
            boxImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.splash);
        }
        TextView tvBoxName = ViewHolder.get(view, R.id.TV_box_title);
        ImageView tvBoxImage = ViewHolder.get(view, R.id.IV_box_image);  // TODO : Unfinished. Needs to import data
        tvBoxName.setText(boxListData.boxName);
        tvBoxImage.setImageBitmap(boxImage);    // TODO : Unfinished

        final ImageView ivFavorite = (ImageView) view.findViewById(R.id.IV_favorite_btn);

        if(boxListData.boxFavorite == 0 && bookmark != null){
            ivFavorite.setImageBitmap(bookmark.getBitmap());
        }
        else if(boxListData.boxFavorite == 1 && bookmarkSelected != null){
            ivFavorite.setImageBitmap(bookmarkSelected.getBitmap());
        }

        ImageView modifyBtn = (ImageView) view.findViewById(R.id.IV_modify_btn);
        ImageView deleteBtn = (ImageView) view.findViewById(R.id.IV_delete_btn);

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BoxListData)getItem(i)).boxFavorite = 1 - ((BoxListData)getItem(i)).boxFavorite;
                boxListWrapper.favorite(((BoxListData)getItem(i)), new BoxFavoriteCallback(((BoxListData)getItem(i)), ivFavorite));
                ivFavorite.setEnabled(false);
            }
        });
        modifyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoxEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "BoxIndex : " + i);
                intent.putExtra("boxIndex", i);
                context.startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoxDeleteDialogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "BoxIndex : " + i);
                intent.putExtra("boxIndex", i);
                context.startActivity(intent);
            }
        });

        return view;
    }

    private class BoxFavoriteCallback implements Callback<MainServerData<Object>> {
        BoxListData boxListData = null;
        ImageView ivFavorite = null;
        public BoxFavoriteCallback(BoxListData boxListData, ImageView ivFavorite) {
            this.boxListData = boxListData;
            this.ivFavorite = ivFavorite;
        }
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            if (wrappedObject.result) {

            }
            else {
                boxListData.boxFavorite = 1 - boxListData.boxFavorite;
            }
            LinkBoxController.notifyBoxDataSetChanged();
            ivFavorite.setImageDrawable((boxListData.boxFavorite==0 ? bookmark : bookmarkSelected));
            ivFavorite.setEnabled(true);
        }
        @Override
        public void failure(RetrofitError error) {
            boxListData.boxFavorite = 1 - boxListData.boxFavorite;
            LinkBoxController.notifyBoxDataSetChanged();
            ivFavorite.setImageDrawable((boxListData.boxFavorite==0 ? bookmark : bookmarkSelected));
            ivFavorite.setEnabled(true);
        }
    }
}
