package org.sopt.linkbox.custom.helper.tagHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sopt.linkbox.R;

/**
 * Created by MinGu on 2015-08-30.
 */
public class TagLayout extends LinearLayout {

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        TextView tvTag = (TextView)findViewById(R.id.TV_tag);
        if (selected) {
            tvTag.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0);
        } else {
            tvTag.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }
}
