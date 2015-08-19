package org.sopt.linkbox.custom.adapters.cardViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.constant.MainStrings;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.helper.BoxImageSaveLoad;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.util.ArrayList;

/**
 * Created by Junyoung on 2015-07-10.
 *
 */
public class BoxEditBoxListAdapter extends BaseAdapter {
    private static final String TAG = "TEST/" + BoxEditBoxListAdapter.class.getName() + " : ";

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_box_list_link_box, viewGroup, false);
        }
        final BoxListData boxListData = (BoxListData) getItem(i);
        Log.e("Loaded Image Number", boxListData.toString());
        Bitmap boxImage = boxImageSaveLoader.loadProfileImage(i);
        TextView tvBoxName = ViewHolder.get(view, R.id.TV_box_title);
        ImageView tvBoxImage = ViewHolder.get(view, R.id.IV_box_image);  // TODO : Unfinished. Needs to import data
        tvBoxName.setText(boxListData.boxName);
        tvBoxImage.setImageBitmap(boxImage);    // TODO : Unfinished

        final ImageView favoriteBtn = (ImageView) view.findViewById(R.id.IV_favorite_btn);
        final BitmapDrawable bookmark = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_box_bookmark);
        final BitmapDrawable bookmarkSelected = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_box_bookmark_selected);

        if(boxListData.isFavorite == 1 && bookmark != null){
            favoriteBtn.setImageBitmap(bookmark.getBitmap());
        }
        else if(boxListData.isFavorite == 0 && bookmarkSelected != null){
            favoriteBtn.setImageBitmap(bookmarkSelected.getBitmap());
        }

        ImageView modifyBtn = (ImageView) view.findViewById(R.id.IV_modify_btn);
        ImageView deleteBtn = (ImageView) view.findViewById(R.id.IV_delete_btn);

        favoriteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(boxListData.isFavorite == 1 && bookmark != null){
                    boxListData.isFavorite = 0;
                    favoriteBtn.setImageBitmap(bookmark.getBitmap());
                }
                else if (bookmarkSelected != null){
                    boxListData.isFavorite = 1;
                    favoriteBtn.setImageBitmap(bookmarkSelected.getBitmap());
                }
            }
        });
        modifyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoxEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("boxIndex", boxListData.boxIndex);
                context.startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.e("RESPONSE", "Delete");
                Intent intent = new Intent(context, BoxDeleteDialogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("boxIndex", i);
                context.startActivity(intent);
            }
        });

        tvBoxImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LinkBoxController.currentBox = boxListData;
                Intent intent = new Intent(context, LinkBoxActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(MainStrings.inBox, true);
                Log.d(TAG, "current Box : " + LinkBoxController.currentBox.toString());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
