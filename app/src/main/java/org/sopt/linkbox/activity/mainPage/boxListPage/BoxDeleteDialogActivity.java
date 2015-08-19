package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.dialog.BoxDeleteDialog;
import org.sopt.linkbox.custom.dialog.DeleteDialog;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sy on 2015-08-18.
 */
public class BoxDeleteDialogActivity extends AppCompatActivity {
    private BoxDeleteDialog boxDeleteDialog = null;
    private BoxListWrapper boxListWrapper = null;
    private BoxListData boxListData = null;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        index = getIntent().getIntExtra("boxIndex", 0);
        boxListWrapper = new BoxListWrapper();
        boxListData = LinkBoxController.boxListSource.get(index);

        boxDeleteDialog = new BoxDeleteDialog(this);
        boxDeleteDialog.setOclDelete(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boxListWrapper.remove(boxListData, new BoxRemoveCallback(boxListData));
                boxDeleteDialog.dismiss();
            }
        });
        boxDeleteDialog.setOclCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boxDeleteDialog.dismiss();
                finish();
            }
        });
        boxDeleteDialog.show();
    }

    private class BoxRemoveCallback implements Callback<MainServerData<Object>> {
        private BoxListData boxListData = null;

        public BoxRemoveCallback(BoxListData boxListData) {
            this.boxListData = boxListData;
        }

        @Override
        public void success(MainServerData<Object> objectMainServerData, Response response) {
            LinkBoxController.boxListSource.remove(boxListData);
            LinkBoxController.notifyBoxDataSetChanged();
            finish();
        }

        @Override
        public void failure(RetrofitError error) {
            finish();
        }
    }
}
