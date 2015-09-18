package org.sopt.linkbox.custom.adapters.cardViewAdapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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
import org.sopt.linkbox.activity.mainPage.boxListPage.BoxMenuActivity;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.helper.BoxImageSaveLoad;
import org.sopt.linkbox.custom.helper.CardViewHolder;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private BoxListData boxListData = null;
    private CardViewHolder vhContentsHolder = null;
    // private ProgressBar pbImageStatus = null;

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
        // final CardViewHolder vhContentsHolder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_box_list_link_box, viewGroup, false);

            vhContentsHolder = new CardViewHolder();
            vhContentsHolder.tvBoxName = (TextView) view.findViewById(R.id.TV_box_title);
            vhContentsHolder.ivBoxImage = (ImageView) view.findViewById(R.id.IV_box_image);
            vhContentsHolder.ivMenuBtn = (ImageView) view.findViewById(R.id.IV_menu_btn);
            // vhContentsHolder.ivShareButton = (ImageView) view.findViewById(R.id.IV_share_button);
            // vhContentsHolder.ivModifyBtn = (ImageView) view.findViewById(R.id.IV_modify_button);
            // vhContentsHolder.ivDeleteBtn = (ImageView) view.findViewById(R.id.IV_delete_button);
            // vhContentsHolder.pbImageStatus = (ProgressBar) view.findViewById(R.id.PB_image_loading_status);

            view.setTag(vhContentsHolder);
        }
        else {
            vhContentsHolder = (CardViewHolder) view.getTag();
        }


        boxListData = (BoxListData) getItem(i);
        // Bitmap boxImage = boxImageSaveLoader.loadProfileImage(i);

        // ** Using AsyncTask to perform UI operations on a separate thread
        /*
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("boxImageDir", Context.MODE_PRIVATE);
        File file = new File(directory, boxListData.boxKey + ".png");

        vhContentsHolder.currentImagePath = file.toString();
        */
        vhContentsHolder.ivBoxImage.setTag(boxListData.boxKey);
        new LoadBoxImage(vhContentsHolder.ivBoxImage).execute();
        /*
        if (boxImage == null) {
            boxImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.box_default);
        }
        */
        vhContentsHolder.tvBoxName.setText(boxListData.boxName);
        // vhContentsHolder.ivBoxImage.setImageBitmap(boxImage);
        vhContentsHolder.ivMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoxMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "BoxIndex : " + i);
                intent.putExtra("boxIndex", i);
                context.startActivity(intent);
            }
        });
        /*
        if(boxListData.boxFavorite == 0 && bookmark != null){
            vhContentsHolder.ivShareButton.setImageBitmap(bookmark.getBitmap());
        }
        else if(boxListData.boxFavorite == 1 && bookmarkSelected != null){
            vhContentsHolder.ivShareButton.setImageBitmap(bookmarkSelected.getBitmap());
        }
        */
        /*
        vhContentsHolder.ivShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BoxListData)getItem(i)).boxFavorite = 1 - ((BoxListData)getItem(i)).boxFavorite;
                boxListWrapper.favorite(((BoxListData)getItem(i)), new BoxFavoriteCallback(((BoxListData)getItem(i)), vhContentsHolder.ivShareButton));
                vhContentsHolder.ivShareButton.setEnabled(false);
            }
        });

        vhContentsHolder.ivShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        vhContentsHolder.ivModifyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoxEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "BoxIndex : " + i);
                intent.putExtra("boxIndex", i);
                context.startActivity(intent);
            }
        });
        vhContentsHolder.ivDeleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoxDeleteDialogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "BoxIndex : " + i);
                intent.putExtra("boxIndex", i);
                context.startActivity(intent);
            }
        });
        */
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

    class LoadBoxImage extends AsyncTask<Object, Void, Bitmap>{

        private ImageView currentImage;
        private String currentPath;
        private File file;
        Bitmap temporaryImage = null;

        public LoadBoxImage(ImageView currentImage) {
            this.currentImage = currentImage;
            this.currentPath = currentImage.getTag().toString();
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            //currentImage = (ImageView) params[0];
            Bitmap bitmap = null;

            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("boxImageDir", Context.MODE_PRIVATE);
            // file = new File(directory, boxListData.boxKey + ".png");
            file = new File(directory, currentPath + ".png");
            Log.e("Load destination", file.toString());
            try {
                temporaryImage = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return temporaryImage;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // vhContentsHolder.pbImageStatus.setVisibility(ProgressBar.INVISIBLE);
            if(!currentImage.getTag().toString().equals(currentPath)) {
                return;
            }
            if(bitmap != null && currentImage != null){
                currentImage.setVisibility(View.VISIBLE);
                currentImage.setImageBitmap(bitmap);
            }
            else{
                // currentImage.setVisibility(View.GONE);
            }
        }
    }
}
