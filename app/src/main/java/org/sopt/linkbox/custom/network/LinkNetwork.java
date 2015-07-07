package org.sopt.linkbox.custom.network;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.LinkBoxUrlListData;
import org.sopt.linkbox.custom.data.LoginData;
import org.sopt.linkbox.custom.data.SignupData;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-07-07.
 *
 */
public class LinkNetwork {
    public static class Embedly {
        private static String TAG = "TEST/" + Embedly.class.getName() + " : ";
        private static LinkNetworkInterface.EmbedlyInterface embedlyInterface;

        public static void getThumbUrlFromEmbedlyAsync(final LinkBoxUrlListData data) {
            embedlyInterface = LinkBoxController.getApplication().getLinkNetworkEmbedlyInterface();
            if (TextUtils.isEmpty(data.url)) {
                Log.d(TAG, "data is empty.");
                return;
            }

            HashMap<String, String> parameter = new HashMap<>();

            String url = makeEncodedUrl(data.url);
            if (url == null) {
                Log.e(TAG, data.url + " can't be parsed.");
                return;
            }

            parameter.put("url", url);
            parameter.put("format", "json");
            Log.d(TAG, url + " is parsed data of " + data.url);
            embedlyInterface.getDataAsync(parameter, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(o);
                    JSONObject json = null;
                    try {
                        json = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ResultEmbedly resultEmbedly = gson.fromJson(json.toString(), ResultEmbedly.class);
                    data.urlThumb = resultEmbedly.thumbnail_url;
                    data.width = resultEmbedly.thumbnail_width;
                    data.height = resultEmbedly.thumbnail_height;
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("ERROR", "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
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
    public static class Server {
        private static String TAG = "TEST/" + Server.class.getName() + " : ";
        private static LinkNetworkInterface.MainServerInterface mainServerInterface;
        private static boolean nowUsing;

        public static boolean postLoginToServerSync(LoginData data) {
            fillInterface();

            if (TextUtils.isEmpty(data.usremail) || TextUtils.isEmpty(data.pass)) {
                return false;
            }

            HashMap<String, String> parameter = new HashMap<>();

            parameter.put("usremail", data.usremail);
            parameter.put("pass", data.pass);
            Log.d(TAG, "Login with usremail : " + data.usremail + " / pass : " + data.pass);
            Gson gson = new Gson();
            String jsonString = gson.toJson(mainServerInterface.postLoginSync(parameter));
            JSONObject json = null;
            try {
                json = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gson.fromJson(json.toString(), boolean.class);
        }
        public static boolean postSignupToServerSync(SignupData data) {
            fillInterface();

            if (TextUtils.isEmpty(data.usremail)
                    || TextUtils.isEmpty(data.usrname)
                    || TextUtils.isEmpty(data.pass)) {
                return false;
            }

            HashMap<String, String> parameter = new HashMap<>();

            parameter.put("usremail", data.usremail);
            parameter.put("usrname", data.usrname);
            parameter.put("pass", data.pass);
            Log.d(TAG, "Signup with usremail : " + data.usremail +
                    " / usrname : " + data.usrname +
                    " / pass : " + data.pass);
            Gson gson = new Gson();
            String jsonString = gson.toJson(mainServerInterface.postSignUpSync(parameter));
            JSONObject json = null;
            try {
                json = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return gson.fromJson(json.toString(), boolean.class);
        }
        public static void getBoxListFromServerAsync() {
            fillInterface();


        }

        private static void fillInterface() {
            if (mainServerInterface == null) {
                mainServerInterface = LinkBoxController.getApplication().getLinkNetworkMainServerInterface();
            }
        }
    }
}
