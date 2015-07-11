package org.sopt.linkbox.custom.network.LinkNetwork;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.LinkUrlListData;
import org.sopt.linkbox.custom.data.tempData.EmbedlyResult;
import org.sopt.linkbox.custom.network.LinkNetworkInterface.EmbedlyInterface;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-07-11.
 */
public class Embedly {
    private static String TAG = "TEST/" + Embedly.class.getName() + " : ";
    private static EmbedlyInterface embedlyInterface;

    public static void getThumbUrlFromEmbedlyAsync(final LinkUrlListData data, final ImageView iv) {
        embedlyInterface = LinkBoxController.getApplication().getLinkNetworkEmbedlyInterface();
        if (TextUtils.isEmpty(data.address)) {
            Log.d(TAG, "data is empty.");
            return;
        }

        HashMap<String, String> parameter = new HashMap<>();

        parameter.put("url", data.address);
        parameter.put("format", "json");
        embedlyInterface.getDataAsync(parameter, new Callback<EmbedlyResult>() {
            @Override
            public void success(EmbedlyResult res, Response response) {
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
