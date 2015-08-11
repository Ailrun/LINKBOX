package org.sopt.linkbox.custom.adapters.cardViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.helper.ViewHolder;

import java.util.ArrayList;

/**
 * Created by MinGu on 2015-08-02.
 */
public class BoxEditInvitedBoxListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater = null;
    private ArrayList<BoxListData> source = null;
    private Context context = null;

    public BoxEditInvitedBoxListAdapter(Context context, ArrayList<BoxListData> source) {
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
            view = layoutInflater.inflate(R.layout.layout_invited_box_list_link_box, viewGroup, false);
        }
        final BoxListData boxListData = (BoxListData) getItem(i);
        TextView tvBoxName = ViewHolder.get(view, R.id.TV_invited_box_name);
        ImageView tvBoxImage = ViewHolder.get(view, R.id.IV_invited_box_thumbnail);  // TODO : Unfinished. Needs to import data
        tvBoxName.setText(boxListData.boxName);
        tvBoxImage.setImageBitmap(null);    // TODO : Unfinished

        ImageView agreeBtn = (ImageView) view.findViewById(R.id.agree_btn);
        ImageView disagreeBtn = (ImageView) view.findViewById(R.id.disagree_btn);

        agreeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("RESPONSE", "agreed");
            }
        });
        disagreeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("RESPONSE", "disagreed");
            }
        });

        return view;
    }
}
