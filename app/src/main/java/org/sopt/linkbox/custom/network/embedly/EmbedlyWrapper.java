package org.sopt.linkbox.custom.network.embedly;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.networkData.EmbedlyData;

import java.util.HashMap;

import retrofit.Callback;

/**
 * Created by Junyoung on 2015-07-14.
 */
public class EmbedlyWrapper {
    private static EmbedlyInterface embedlyInterface = null;

    private static void fillInterface() {
        if (embedlyInterface == null) {
            embedlyInterface = LinkBoxController.getApplication().getLinkNetworkEmbedlyInterface();
        }
    }
    public void getThumbAsync(String url, Callback<EmbedlyData> callback) {
        fillInterface();
        HashMap<String, String> parameter = new HashMap<>();
        parameter.put("url", url);
        parameter.put("format", "json");
        embedlyInterface.getThumbAsync(parameter, callback);
    }
}
