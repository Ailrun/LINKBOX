package org.sopt.linkbox.custom.adapters.cardViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.boxListPage.BoxEditActivity;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.helper.BoxImageSaveLoad;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-07-10.
 */
public class BoxEditBoxListAdapter extends BaseAdapter {
    private static final String TAG = "THIS IS A TEST/";
    private LayoutInflater layoutInflater = null;
    private ArrayList<BoxListData> source = null;
    private Context context = null;
    private BoxImageSaveLoad boxImageSaveLoader = null;

    public BoxEditBoxListAdapter(Context context, ArrayList<BoxListData> source) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
        this.context = context;
        boxImageSaveLoader = new BoxImageSaveLoad(context.getApplicationContext());
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_box_list_link_box, viewGroup, false);
        }
        final BoxListData boxListData = (BoxListData) getItem(i);
        Bitmap boxImage = boxImageSaveLoader.loadProfileImage(i);
        TextView tvBoxName = ViewHolder.get(view, R.id.TV_box_title);
        ImageView tvBoxImage = ViewHolder.get(view, R.id.IV_box_image);  // TODO : Unfinished. Needs to import data
        tvBoxName.setText(boxListData.boxName);
        tvBoxImage.setImageBitmap(boxImage);    // TODO : Unfinished

        final ImageView favoriteBtn = (ImageView) view.findViewById(R.id.IV_favorite_btn);

        if(boxListData.isFavorite == 1){
            favoriteBtn.setImageBitmap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_box_bookmark_selected)).getBitmap());
        }
        else if(boxListData.isFavorite == 0){
            favoriteBtn.setImageBitmap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_box_bookmark)).getBitmap());
        }

        ImageView modifyBtn = (ImageView) view.findViewById(R.id.IV_modify_btn);
        ImageView deleteBtn = (ImageView) view.findViewById(R.id.IV_delete_btn);


        // LinkBoxController.currentBox = boxListData;
        // TODO : Give via Intent
        // TODO : Fill Controller currentBox
        /*
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d(TAG, "");
            }
        });
        */
        favoriteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(boxListData.isFavorite == 1){
                    LinkBoxController.currentBox =  boxListData;
                    LinkBoxController.currentBox.isFavorite = 0;
                    favoriteBtn.setImageBitmap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_box_bookmark)).getBitmap());
                }
                else{
                    LinkBoxController.currentBox = boxListData;
                    LinkBoxController.currentBox.isFavorite = 1;
                    favoriteBtn.setImageBitmap(((BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_box_bookmark_selected)).getBitmap());
                }
            }
        });
        modifyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LinkBoxController.currentBox = boxListData;
                Intent intent = new Intent(context, BoxEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.e("RESPONSE", "Delete");
            }
        });

        tvBoxImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LinkBoxController.currentBox =  boxListData;
            }
        });

        return view;
    }
}
