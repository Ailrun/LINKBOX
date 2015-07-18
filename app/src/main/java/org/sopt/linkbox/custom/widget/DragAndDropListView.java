package org.sopt.linkbox.custom.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Junyoung on 2015-07-18.
 *
 */
public class DragAndDropListView extends ListView {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DragAndDropListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DragAndDropListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DragAndDropListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragAndDropListView(Context context) {
        super(context);
    }
}
