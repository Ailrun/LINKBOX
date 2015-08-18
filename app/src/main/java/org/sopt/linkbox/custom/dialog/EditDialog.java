package org.sopt.linkbox.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import org.sopt.linkbox.R;

/**
 * Created by Junyoung on 2015-08-18.
 */
public class EditDialog extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindow();
    }

    public EditDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }
    public EditDialog(Context context, View.OnClickListener oclEdit, View.OnClickListener oclCancel) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }
    public EditDialog(Context context, String title, String content, View.OnClickListener oclEdit, View.OnClickListener oclCancel) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    private void initWindow() {
        WindowManager.LayoutParams lpDialog = new WindowManager.LayoutParams();
        lpDialog.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpDialog.dimAmount = 0.7f;
        getWindow().setAttributes(lpDialog);
        setContentView(R.layout.layout_delete_dialog_link_box);
    }
}
