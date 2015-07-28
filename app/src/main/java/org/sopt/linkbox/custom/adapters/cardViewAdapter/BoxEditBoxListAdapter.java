package org.sopt.linkbox.custom.adapters.cardViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
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

    public BoxEditBoxListAdapter(Context context, ArrayList<BoxListData> source) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
        this.context = context;
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
        BoxListData boxListData = (BoxListData) getItem(i);
        TextView tvBoxName = ViewHolder.get(view, R.id.info_text1);
        ImageView tvBoxImage = ViewHolder.get(view, R.id.some_image1);  // TODO : Unfinished. Needs to import data
        tvBoxName.setText(boxListData.boxName);
        tvBoxImage.setImageBitmap(null);    // TODO : Unfinished

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.e(TAG, "");
            }
        });
        return view;
    }
}