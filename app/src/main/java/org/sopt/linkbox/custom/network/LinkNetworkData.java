package org.sopt.linkbox.custom.network;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.sopt.linkbox.LinkBox;
import org.sopt.linkbox.custom.data.LinkBoxUrlListData;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-07-07.
 *
 */
public class LinkNetworkData {
    public static class EmbedlyData {
        private static String TAG = "TEST/" + EmbedlyData.class.getName() + " : ";
        private static LinkNetworkInterface.EmbedlyInterface embedlyInterface;
        public static void getThumbUrlFromServerAsync(final LinkBoxUrlListData data) {
            embedlyInterface = LinkBox.getApplication().getLinkNetworkEmbedlyInterface();
            if (TextUtils.isEmpty(data.url))
                return;

            HashMap<String, String> parameter = new HashMap<>();

            String url = makeEncodedUrl(data.url);
            if (url == null) {
                parameter.put("url", url);
                parameter.put("format", "json");
                Log.d(TAG, url + " is parse of " + data.url);
                embedlyInterface.getDataAsync(parameter, new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        Gson gson = new Gson();
                        String jsonString = gson.toJson(o);
                        try {
                            JSONObject json = new JSONObject(jsonString);
                            ResultEmbedly resultEmbedly = gson.fromJson(json.toString(), ResultEmbedly.class);
                            data.urlThumb = resultEmbedly.thumbnail_url;
                            data.width = resultEmbedly.thumbnail_width;
                            data.height = resultEmbedly.thumbnail_height;
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("ERROR", "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                    }
                });
            }
        }

        private static String makeEncodedUrl(String urlString) {
            String ret = null;
            try {
                ret = java.net.URLEncoder.encode(urlString, "ISO-8859-1");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return ret;
        }

        class ResultEmbedly {
            String provider_url;
            String description;
            String title;
            String url;
            float mean_alpha;
            int thumbnail_width;
            String thumbnail_url;
            String version;
            String provider_name;
            String type;
            int thumbnail_height;
        }
    }
    public static class ServerData {
        private static String TAG = "TEST/" + ServerData.class.getName() + " : ";
        private static LinkNetworkInterface.MainServerInterface mainServerInterface;
    }
}
