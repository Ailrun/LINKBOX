package org.sopt.linkbox.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.sopt.linkbox.R;

/**
 * Created by Junyoung on 2015-08-18.
 */
public class DeleteDialog extends Dialog {

    private TextView tvTitle = null;
    private TextView tvContents = null;
    private Button bDelete = null;
    private Button bCancel = null;
    private String title = null;
    private String contents = null;
    private View.OnClickListener oclDelete = null;
    private View.OnClickListener oclCancel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindow();
        initView();
    }

    public DeleteDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }
    public DeleteDialog(Context context, String title) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.title = title;
    }
    public DeleteDialog(Context context, View.OnClickListener oclDelete, View.OnClickListener oclCancel) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.oclDelete = oclDelete;
        this.oclCancel = oclCancel;
    }
    public DeleteDialog(Context context, String title, String contents, View.OnClickListener oclDelete, View.OnClickListener oclCancel) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.title = title;
        this.contents = contents;
        this.oclDelete = oclDelete;
        this.oclCancel = oclCancel;
    }

    public void setTitle(String title) {
        this.title = title;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }
    public void setContents(String contents) {
        this.contents = contents;
        if (tvContents != null) {
            tvContents.setText(contents);
        }
    }
    public void setOclDelete(View.OnClickListener oclDelete) {
        this.oclDelete = oclDelete;
        if (bDelete != null) {
            bDelete.setOnClickListener(oclDelete);
        }
    }
    public void setOclCancel(View.OnClickListener oclCancel) {
        this.oclCancel = oclCancel;
        if (bCancel != null) {
            bCancel.setOnClickListener(oclCancel);
        }
    }

    private void initWindow() {
        WindowManager.LayoutParams lpDialog = new WindowManager.LayoutParams();
        lpDialog.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpDialog.dimAmount = 0.7f;
        getWindow().setAttributes(lpDialog);
        setContentView(R.layout.layout_delete_dialog_link_box);
    }
    private void initView() {
        tvTitle = (TextView) findViewById(R.id.TV_title_delete_dialog);
        tvContents = (TextView) findViewById(R.id.TV_contents_delete_dialog);
        bDelete = (Button) findViewById(R.id.B_delete_delete_dialog);
        bCancel = (Button) findViewById(R.id.B_cancel_delete_dialog);
        if (title != null) {
            tvTitle.setText(title);
        }
        if (contents != null) {
            tvContents.setText(contents);
        }
        bDelete.setOnClickListener(oclDelete);
        bCancel.setOnClickListener(oclCancel);
    }
}
