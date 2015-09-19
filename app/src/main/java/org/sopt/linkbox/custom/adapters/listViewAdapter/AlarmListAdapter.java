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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.alarmPage.AlarmActivity;
import org.sopt.linkbox.activity.mainPage.boxListPage.WebviewActivity;
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.constant.AlarmType;
import org.sopt.linkbox.constant.MainStrings;
import org.sopt.linkbox.custom.data.mainData.AlarmListData;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.helper.ViewHolder;
import org.sopt.linkbox.custom.network.main.alarm.AlarmListWrapper;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sy on 2015-09-03.
 */
public class AlarmListAdapter extends BaseAdapter {
    private static final String TAG = "TEST/" + AlarmListAdapter.class.getName() + " : ";


    private ArrayList<AlarmListData> source = null;
    private LayoutInflater layoutInflater = null;
    private Context context = null;
    private AlarmListData alarmListData = null;
    private AlarmListWrapper alarmListWrapper = null;
    private BoxListWrapper boxListWrapper = null;

    public AlarmListAdapter(Context context, ArrayList<AlarmListData> source) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.source = source;
        this.context = context;

        notifyDataSetChanged();

        alarmListWrapper = new AlarmListWrapper();
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
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
        tvDate.setText(alarmListData.alarmDate);

        final LinearLayout LL_invited_box_header = ViewHolder.get(view, R.id.LL_invited_box_header);
        final LinearLayout LL_invited_box_expandable = ViewHolder.get(view, R.id.LL_invited_box_expandable);

        LL_invited_box_expandable.setVisibility(View.GONE);

            switch (alarmListData.alarmType)
            {
                case AlarmType.typeBox:
                    tvMessage.setText(alarmListData.alarmMessage);
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
                    break;

                case AlarmType.typeUrl:
                    tvMessage.setText(alarmListData.alarmSetUsrName + "님이 '" + alarmListData.alarmBoxName + "'박스에 " + alarmListData.alarmUrlTitle + "을 추가했습니다.");
                    if(alarmListData.alarmRead == 0)
                        LL_invited_box_header.setBackgroundResource(R.color.indigo200);
                    else
                        LL_invited_box_header.setBackgroundResource(R.color.real_white);

                    LL_invited_box_header.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LL_invited_box_header.setBackgroundResource(R.color.real_white);
                            alarmListWrapper.read(alarmListData, new ReadCallback());
                        }
                    });
                break;

                case AlarmType.typeGood:
                    tvMessage.setText(alarmListData.alarmSetUsrName + "님이'" + alarmListData.alarmUrlTitle + "'에 좋아요를 눌렀습니다.");
                    if(alarmListData.alarmRead == 0)
                        LL_invited_box_header.setBackgroundResource(R.color.indigo200);
                    else
                        LL_invited_box_header.setBackgroundResource(R.color.real_white);

                    LL_invited_box_header.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LL_invited_box_header.setBackgroundResource(R.color.real_white);
                            alarmListWrapper.read(alarmListData, new ReadCallback());
                        }
                    });
                    break;
            }




        bAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Agree!");
                boxListWrapper.accept((AlarmListData)getItem(position), LinkBoxController.boxListSource.size(), new BoxAcceptCallback((AlarmListData)getItem(position)));
            }
        });
        bDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Disagree!");
                boxListWrapper.decline((AlarmListData)getItem(position), new BoxDeclineCallback());
            }
        });

        return view;
    }
    //<editor-fold desc="Maybe Divided to helper class" defaultstate="collapsed">
    //TODO : DIVIDE?
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
    //</editor-fold>


    private class BoxAcceptCallback implements Callback<MainServerData<BoxListData>> {
        private AlarmListData alarmListData = null;
        public BoxAcceptCallback(AlarmListData alarmListData) {
            this.alarmListData = alarmListData;
        }
        @Override
        public void success(MainServerData<BoxListData> wrappedBoxListData, Response response) {
            if (wrappedBoxListData.result) {
                LinkBoxController.alarmBoxListSource.remove(alarmListData);
                LinkBoxController.notifyAlarmDataSetChanged();
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
                Toast.makeText(context, "박스 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, "Fail to accept at all");
            Toast.makeText(context, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private class BoxDeclineCallback implements Callback<MainServerData<Object>> {
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            if (wrappedObject.result) {
                LinkBoxController.alarmBoxListSource.remove(alarmListData);
                LinkBoxController.notifyAlarmDataSetChanged();
            }
            else {
                Log.d(TAG, "Fail to decline");
                Toast.makeText(context, "Object.result가 false입니다..", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, "Fail to decline at all");
            Toast.makeText(context, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class ReadCallback implements Callback<MainServerData<Object>> {
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            if (wrappedObject.result) {
                alarmListData.alarmRead = 1;
                Intent intent = new Intent(context, WebviewActivity.class);
                int  key = LinkBoxController.linkBoxUrlListAdapter.getItemPostionAsKey(alarmListData.alarmUrlKey);
                if(key <0)
                {
                    Toast.makeText(context, "url키가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra(MainStrings.position, key);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
            else {
                Log.d(TAG, "Fail to go to url");
                Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, "Fail to decline at all");
            Toast.makeText(context, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
