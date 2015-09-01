package org.sopt.linkbox.custom.helper.tagHelper;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import org.sopt.linkbox.R;

/**
 * Created by MinGu on 2015-08-30.
 */
public class TagCompletionView extends TokenCompleteTextView<IndividualTag> {

    public TagCompletionView(Context context) {
        super(context);
    }

    public TagCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(IndividualTag individualTag) {

        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.layout_individual_tag, (ViewGroup)TagCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.TV_tag)).setText(individualTag.getTagName());

        return view;
    }

    @Override
    protected IndividualTag defaultObject(String completionText) {
        return new IndividualTag(completionText);
    }
}
