package org.sopt.linkbox.custom.adapters.swapeListViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.urlListingPage.DeleteDialogActivity;
import org.sopt.linkbox.activity.mainPage.urlListingPage.EditDialogActivity;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.activity.mainPage.urlListingPage.SearchActivity;
import org.sopt.linkbox.custom.data.mainData.url.TagListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.helper.ViewHolder;
import org.sopt.linkbox.custom.helper.DateCalculator;
import org.sopt.linkbox.custom.helper.tagHelper.IndividualTag;
import org.sopt.linkbox.custom.helper.tagHelper.TagCompletionView;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;
import org.sopt.linkbox.custom.widget.SoundSearcher;
import org.sopt.linkbox.debugging.RetrofitDebug;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-07-02.
 *
 */
public class LinkBoxUrlListAdapter extends BaseSwipeAdapter implements TagCompletionView.TokenListener<IndividualTag>, TagCompletionView.OnClickListener {
    private static final String TAG = "TEST/" + LinkBoxUrlListAdapter.class.getName() + " : ";
    private ArrayList<UrlListData> source = null;
    private LayoutInflater layoutInflater = null;
    private Context context = null;
    private UrlListData urlListData = null;
    private UrlListWrapper urlListWrapper = null;
    private BoxListData boxListData = null;

    // Tag
    TagCompletionView tcvCompletionView;
    IndividualTag[] individualTags;
    ArrayAdapter<IndividualTag> tagArrayAdapter;
    // ArrayList<IndividualTag> tagCompilation;


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
        urlListWrapper.tagList(urlListData, new TagLoadingCallback(view));
        fillMainValues(i, view);
        fillHiddenValues(i, view);
    }

    private void fillMainValues(final int i, View view) {
        TextView tvUrlTitle = ViewHolder.get(view, R.id.TV_url_name_link_box);
        TextView tvUrlBox = ViewHolder.get(view, R.id.TV_url_box_link_box);
        TextView tvUrlWriter = ViewHolder.get(view, R.id.TV_url_writer_link_box);
        TextView tvUrlDate = ViewHolder.get(view, R.id.TV_url_date_link_box);
        TextView tvLikeNum = ViewHolder.get(view, R.id.TV_like_num_link_box);

        ImageView ivUrlThumb = ViewHolder.get(view, R.id.IV_thumb_link_box);
        final ImageView ivLike = ViewHolder.get(view, R.id.IV_like_link_box);

        tvUrlTitle.setText(urlListData.urlTitle);

        for(int j = 0; j < LinkBoxController.boxListSource.size(); j++){
            if(LinkBoxController.boxListSource.get(j).boxKey == urlListData.boxKey){
                boxListData = LinkBoxController.boxListSource.get(j);
            }
        }
        if(boxListData != null){
            tvUrlBox.setText(boxListData.boxName);
        }
        else{
            tvUrlBox.setText("-");
        }

        tvUrlWriter.setText(urlListData.urlWriterUsrName);

        final String urlDate = DateCalculator.compareDates(urlListData.urlDate);
        Log.e("Compared time", urlDate);
        tvUrlDate.setText(urlDate);
        tvLikeNum.setText(Integer.toString(urlListData.likedNum));

        if(urlListData.urlThumbnail != null) {
            Glide.with(context).load(urlListData.urlThumbnail).asBitmap().into(ivUrlThumb);
        }
        else{
            ivUrlThumb.setImageResource(R.drawable.mainpage_image);
        }
        ivLike.setImageResource(urlListData.liked == 0 ? R.drawable.mainpage_bookmark_unchecked : R.drawable.mainpage_bookmark_checked);
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlListWrapper.like((UrlListData) getItem(i), (1 - ((UrlListData) getItem(i)).liked), new UrlLikeCallback((UrlListData) getItem(i), ivLike));
                Log.e(TAG, "this is " + i);
                Log.e(TAG, "I'm " + getItem(i).toString());
                ivLike.setEnabled(false);
            }
        });
    }
    private void fillHiddenValues(final int i, View view) {
        ImageButton ibDelete = ViewHolder.get(view, R.id.IB_delete_link_box);
        ibDelete.setScaleX(0.35f);
        ibDelete.setScaleY(0.35f);
        ImageButton ibEdit = ViewHolder.get(view, R.id.IB_edit_link_box);
        ibEdit.setScaleX(0.42f);
        ibEdit.setScaleY(0.42f);
        ImageButton ibShare = ViewHolder.get(view, R.id.IB_share_link_box);
        ibShare.setScaleX(0.40f);
        ibShare.setScaleY(0.40f);

        // initTagView();
        // initTagAdapterView(view);

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
    // Filter Class

   public void filter(String charText) {
       charText = charText.toLowerCase();


       int count = getCount();
       final ArrayList<UrlListData> nlist = new ArrayList<UrlListData>(count);

       String filterableString;

       for(int i = 0; i < count; i++) {
           filterableString = source.get(i).urlTitle.toLowerCase();
           if(SoundSearcher.matchString(filterableString, charText)){
               nlist.add(source.get(i));
           }
       }

       Collections.sort(nlist, new UrlTitleCompare());
       Collections.sort(nlist, new UrlTitleLengthCompare());

       source = nlist;
       notifyDataSetChanged();

   }
    static class UrlTitleCompare implements Comparator<UrlListData>{
        @Override
        public int compare(UrlListData arg0, UrlListData arg1){
            return arg0.urlTitle.toLowerCase().compareTo(arg1.urlTitle.toLowerCase());
        }
    }
    static class UrlTitleLengthCompare implements Comparator<UrlListData>{
        @Override
        public int compare(UrlListData arg0, UrlListData arg1){
            return arg0.urlTitle.length() < arg1.urlTitle.length() ? -1 : arg0.urlTitle.length() > arg1.urlTitle.length() ? 1 : 0;
        }
    }

    public int getItemPostionAsKey(int key){
        int position;

        for(position  = 0; position < getCount(); position++){
            if(source.get(position).urlKey == key)
                return position;

        }
        return -1;

    }

    /*
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        }
        else
        {
            for (WorldPopulation wp : arraylist)
            {
                if (wp.getCountry().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
*/
    /*
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final ArrayList<UrlListData> list = source;

            int count = list.size();
            final ArrayList<UrlListData> nlist = new ArrayList<UrlListData>(count);

            String filterableString;

            for(int i = 0; i < count; i++){
                filterableString = list.get(i).urlTitle.toLowerCase();
                if(SoundSearcher.matchString(filterString, filterableString)){
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();


            return results;
        }

        @Override
        protected void publishResults(CharSequence constrint, FilterResults results){
            source = (ArrayList<UrlListData>) results.values;
            notifyDataSetChanged();
        }
*/

    @Override
    public void onTokenAdded(IndividualTag individualTag) {

    }

    @Override
    public void onTokenRemoved(IndividualTag individualTag) {

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "TCV WAS CLICKED", Toast.LENGTH_LONG);
    }

    private class UrlLikeCallback implements Callback<MainServerData<Object>> {
        UrlListData urlListData = null;
        ImageView ivLike = null;

        public UrlLikeCallback(UrlListData urlListData, ImageView ivLike) {
            this.urlListData = urlListData;
            this.ivLike = ivLike;
        }
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            /*
            urlListData.liked = (1-urlListData.liked);
            urlListData.likedNum += 2*urlListData.liked - 1;
            */
            Log.e(TAG, urlListData.toString());
            if(urlListData.liked == 0) {
                urlListData.liked = 1;
                urlListData.likedNum += 1;
                Log.e(TAG, "hit!!!");
            }
            else {
                urlListData.liked = 0;
                urlListData.likedNum -= 1;
            }
            Log.e(TAG, urlListData.toString());

            ivLike.setImageResource(urlListData.liked == 0 ? R.drawable.mainpage_bookmark_unchecked : R.drawable.mainpage_bookmark_checked);
            ivLike.setEnabled(true);
            LinkBoxController.notifyUrlDataSetChanged();
        }
        @Override
        public void failure(RetrofitError error) {
            ivLike.setEnabled(true);
        }
    }


    private class TagLoadingCallback implements Callback<MainServerData<List<TagListData>>>{
        View view = null;

        public TagLoadingCallback(View view){
            this.view = view;
        }

        @Override
        public void success(MainServerData<List<TagListData>> listMainServerData, Response response) {
            if(listMainServerData.result){
                if(listMainServerData.object != null) {
                    urlListData.urlTags = new ArrayList<>();
                    for(int i = 0; i < listMainServerData.object.size(); i++){
                        urlListData.urlTags.add(listMainServerData.object.get(i));
                        // LinkBoxController.notifyUrlDataSetChanged();
                        Log.e("들어는 왔다", String.valueOf(listMainServerData.object.get(i).tag));
                    }

                    // initTagView();
                    // initTagAdapterView(view);
                }
            }
            else{
            }
        }

        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }

    /*
    private void initTagView() {

        //urlListWrapper.tagList(urlListData, new TagLoadingCallback(urlListData));

        ArrayList<TagListData> currentTagData = new ArrayList<>();
        Log.e("URl TAG INDICATOR", String.valueOf(urlListData.urlTags));
        if(urlListData.urlTags != null){
            for(int i = 0; i < urlListData.urlTags.size(); i++){
                currentTagData.add(urlListData.urlTags.get(i));
            }
            individualTags = new IndividualTag[currentTagData.size()];
            Log.e("Tag Size", String.valueOf(individualTags));

            for(int i = 0; i < currentTagData.size(); i++){
                individualTags[i] = new IndividualTag(currentTagData.get(i).tag);
                Log.e("IndividualTags ".concat(String.valueOf(i)), String.valueOf(individualTags[i]));
            }
        }
        else{
            individualTags = new IndividualTag[]{};
        }

    }

    private void initTagAdapterView(View view) {
        // tagCompilation = new ArrayList<>();
        tagArrayAdapter = new FilteredArrayAdapter<IndividualTag>(context, R.layout.layout_url_list_link_box, individualTags) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater currentLayout = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = currentLayout.inflate(R.layout.layout_url_list_link_box, parent, false);
                }

                IndividualTag itCurrentTag = getItem(position);
                ((TextView) convertView.findViewById(R.id.TV_tag_name)).setText(itCurrentTag.getTagName());

                return convertView;
            }

            @Override
            protected boolean keepObject(IndividualTag individualTag, String s) {
                return true;
            }
        };

        tcvCompletionView = (TagCompletionView) view.findViewById(R.id.CCV_tag_output);
        tcvCompletionView.setAdapter(tagArrayAdapter);
        tcvCompletionView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.None);
        tcvCompletionView.setTokenListener(this);
        tcvCompletionView.setTokenLimit(3);
        // tcvCompletionView.setEnabled(false);
        tcvCompletionView.clear();

        Log.e("IndividualTags length", String.valueOf(individualTags.length));
        if(individualTags.length != 0){
            for(int i = 0; i < individualTags.length; i++){
                tcvCompletionView.addObject(individualTags[i]);
            }
        }

    }
    */
}
