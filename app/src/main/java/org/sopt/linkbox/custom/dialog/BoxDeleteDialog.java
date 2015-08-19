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
 * Created by sy on 2015-08-18.
 */
public class BoxDeleteDialog extends Dialog {

    private Button bDelete = null;
    private Button bCancel = null;
    private View.OnClickListener oclDelete = null;
    private View.OnClickListener oclCancel = null;

    public BoxDeleteDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindow();
        initView();
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
        setContentView(R.layout.layout_box_delete_dialog_link_box);
    }
    private void initView() {

        bDelete = (Button) findViewById(R.id.B_delete_box_delete_dialog);
        bCancel = (Button) findViewById(R.id.B_cancel_box_delete_dialog);

        bDelete.setOnClickListener(oclDelete);
        bCancel.setOnClickListener(oclCancel);
    }
}
