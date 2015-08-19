package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.dialog.DeleteDialog;
import org.sopt.linkbox.custom.dialog.EditDialog;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;

import java.util.Objects;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-08-18.
 */
public class EditDialogActivity extends AppCompatActivity {
    private EditDialog editDialog = null;
    private UrlListWrapper urlListWrapper = null;
    private UrlListData urlListData = null;
    private int index = 0;
    private String urlName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        index = getIntent().getIntExtra("Index", 0);
        urlListWrapper = new UrlListWrapper();
        urlListData = LinkBoxController.urlListSource.get(index);

        editDialog = new EditDialog(this);
        editDialog.setOclEdit(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlName = editDialog.getName();
                urlListWrapper.edit(urlListData, urlName, new UrlEditCallback(urlListData));
                editDialog.dismiss();
            }
        });
        editDialog.setOclCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
                finish();
            }
        });
        editDialog.show();

    }

    private class UrlEditCallback implements Callback<MainServerData<Object>> {
        private UrlListData urlListData = null;
        public UrlEditCallback(UrlListData urlListData) {
            this.urlListData = urlListData;
        }
        @Override
        public void success(MainServerData<Object> objectsMainServerData, Response response) {
            urlListData.urlTitle = urlName;
            LinkBoxController.notifyUrlDataSetChanged();
            finish();
        }
        @Override
        public void failure(RetrofitError error) {
            LinkBoxController.resetUrlDataSet();
            finish();
        }
    }
}
