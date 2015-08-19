package org.sopt.linkbox.custom.adapters.listViewAdapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.constant.MainStrings;
import org.sopt.linkbox.custom.data.mainData.AlarmListData;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.helper.ViewHolder;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;

import java.io.File;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sy on 2015-08-12.
 *
 */
public class InvitedBoxListAdapter extends BaseAdapter {
    private static final String TAG = "TEST/" + InvitedBoxListAdapter.class.getName() + " : ";
    private ArrayList<AlarmListData> source = null;
    private LayoutInflater layoutInflater = null;
    private Context context = null;
    private AlarmListData alarmListData = null;
    private BoxListWrapper boxListWrapper = null;

    public InvitedBoxListAdapter(Context context, ArrayList<AlarmListData> source) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
        this.context = context;
        boxListWrapper = new BoxListWrapper();
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

    public void setSource(ArrayList<AlarmListData> source) {
        this.source = source;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (source != null) ? source.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (source != null && position < source.size() && position >= 0) ?
                source.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_box_list_inivted_box, viewGroup, false);
        }

        alarmListData = (AlarmListData) getItem(position);
        //final ImageView ivBoximage = ViewHolder.get(view, R.id.IV_invited_box_thumbnail);
        TextView tvBoxName = ViewHolder.get(view, R.id.TV_box_name_invited_box);
        TextView tvUsrName = ViewHolder.get(view, R.id.TV_usr_name_invited_box);
        TextView tvMessage = ViewHolder.get(view, R.id.TV_message_invited_box);
        TextView tvDate = ViewHolder.get(view, R.id.TV_date_invited_box);
        Button bAgree = ViewHolder.get(view, R.id.B_invited_box_accept);
        Button bDisagree = ViewHolder.get(view, R.id.B_invited_box_reject);

        tvBoxName.setText(alarmListData.alarmBoxName);
        tvUsrName.setText(alarmListData.alarmSetUsrName);
        tvMessage.setText(alarmListData.alarmMessage);
        tvDate.setText(alarmListData.alarmDate);

        LinearLayout LL_invited_box_header = ViewHolder.get(view, R.id.LL_invited_box_header);
        final LinearLayout LL_invited_box_expandable = ViewHolder.get(view, R.id.LL_invited_box_expandable);

        LL_invited_box_expandable.setVisibility(View.GONE);
        LL_invited_box_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LL_invited_box_expandable.getVisibility() == View.GONE) {
                    expand(LL_invited_box_expandable);

                } else {
                    collapse(LL_invited_box_expandable);
                }
            }
        });

        bAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Agree!");
                boxListWrapper.accept(alarmListData, LinkBoxController.boxListSource.size(), new BoxAcceptCallback(alarmListData));
            }
        });
        bDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Disagree!");
                boxListWrapper.decline(alarmListData, new BoxDeclineCallback());
            }
        });

        return view;
    }

    private void expand(final View v) {
        //set Visible
        v.setVisibility(View.VISIBLE);


        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, v.getMeasuredHeight(), v);
        mAnimator.start();
    }
    private void collapse(final View v) {
        int finalHeight = v.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, v);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end, final View v) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private class BoxAcceptCallback implements Callback<MainServerData<BoxListData>> {
        private AlarmListData alarmListData = null;
        public BoxAcceptCallback(AlarmListData alarmListData) {
            this.alarmListData = alarmListData;
        }
        @Override
        public void success(MainServerData<BoxListData> wrappedBoxListData, Response response) {
            if (wrappedBoxListData.result) {
                LinkBoxController.invitedBoxListSource.remove(alarmListData);
                LinkBoxController.notifyInvitedDataSetChanged();
                LinkBoxController.boxListSource.add(wrappedBoxListData.object);
                LinkBoxController.notifyBoxDataSetChanged();
                LinkBoxController.currentBox = wrappedBoxListData.object;
                Intent intent = new Intent(context, LinkBoxActivity.class);
                intent.putExtra(MainStrings.inBox, true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
            else {
                Log.d(TAG, "Fail to accept");
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, "Fail to accept at all");
        }
    }
    private class BoxDeclineCallback implements Callback<MainServerData<Object>> {
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            if (wrappedObject.result) {
                LinkBoxController.invitedBoxListSource.remove(alarmListData);
                LinkBoxController.notifyInvitedDataSetChanged();
            }
            else {
                Log.d(TAG, "Fail to decline");
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, "Fail to decline at all");
        }
    }
}
