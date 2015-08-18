package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.dialog.DeleteDialog;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-08-18.
 */
public class DeleteDialogActivity extends AppCompatActivity {
    private DeleteDialog deleteDialog = null;
    private UrlListWrapper urlListWrapper = null;
    private UrlListData urlListData = null;
    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        index = getIntent().getIntExtra("Index", 0);
        urlListWrapper = new UrlListWrapper();
        urlListData = LinkBoxController.urlListSource.get(index);

        deleteDialog = new DeleteDialog(this);
        deleteDialog.setOclDelete(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlListWrapper.remove(urlListData, new UrlRemoveCallback(urlListData));
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setOclCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                LinkBoxController.resetUrlDataSet();
                finish();
            }
        });
        deleteDialog.show();
    }

    private class UrlRemoveCallback implements Callback<MainServerData<Object>> {
        private UrlListData urlListData = null;
        public UrlRemoveCallback(UrlListData urlListData) {
            this.urlListData = urlListData;
        }
        @Override
        public void success(MainServerData<Object> objectMainServerData, Response response) {
            LinkBoxController.urlListSource.remove(urlListData);
            LinkBoxController.notifyUrlDataSetChanged();
            LinkBoxController.resetUrlDataSet();
            finish();
        }
        @Override
        public void failure(RetrofitError error) {
            LinkBoxController.resetUrlDataSet();
            finish();
        }
    }
}
