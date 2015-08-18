package org.sopt.linkbox.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.sopt.linkbox.R;

/**
 * Created by Junyoung on 2015-08-18.
 */
public class EditDialog extends Dialog {

    private TextView tvTitle = null;
    private EditText etName = null;
    private Button bEdit = null;
    private Button bCancel = null;
    private String title = null;
    private String name = null;
    private View.OnClickListener oclEdit = null;
    private View.OnClickListener oclCancel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindow();
        initView();
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

    public void setTitle(String title) {
        this.title = title;
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }
    public void setName(String name) {
        this.name = name;
        if (etName != null) {
            etName.setText(name);
        }
    }
    public String getName() {
        setName(etName.getText().toString());
        return name;
    }
    public void setOclEdit(View.OnClickListener oclEdit) {
        this.oclEdit = oclEdit;
        if (bEdit != null) {
            bEdit.setOnClickListener(oclEdit);
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
        setContentView(R.layout.layout_edit_dialog_link_box);
    }
    private void initView() {
        tvTitle = (TextView) findViewById(R.id.TV_title_edit_dialog);
        etName = (EditText) findViewById(R.id.ET_name_edit_dialog);
        bEdit = (Button) findViewById(R.id.B_edit_link_apply_edit_dialog);
        bCancel = (Button) findViewById(R.id.B_edit_link_cancel_edit_dialog);
        if (title != null) {
            tvTitle.setText(title);
        }
        if (name != null) {
            etName.setText(name);
        }
        bEdit.setOnClickListener(oclEdit);
        bCancel.setOnClickListener(oclCancel);
    }
}
