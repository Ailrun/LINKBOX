package org.sopt.linkbox.custom.network;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.sopt.linkbox.LinkBoxActivity;
import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.MainActivity;
import org.sopt.linkbox.custom.data.LinkUrlListData;
import org.sopt.linkbox.custom.data.LinkBoxListData;
import org.sopt.linkbox.custom.data.LinkUserData;
import org.sopt.linkbox.custom.data.TwoString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        public static void getThumbUrlFromEmbedlyAsync(final LinkUrlListData data, final ImageView iv) {
            embedlyInterface = LinkBoxController.getApplication().getLinkNetworkEmbedlyInterface();
            if (TextUtils.isEmpty(data.address)) {
                Log.d(TAG, "data is empty.");
                return;
            }

            HashMap<String, String> parameter = new HashMap<>();

            parameter.put("url", data.address);
            parameter.put("format", "json");
            embedlyInterface.getDataAsync(parameter, new Callback<ResultEmbedly>() {
                @Override
                public void success(ResultEmbedly res, Response response) {
                    data.urlthumb = res.thumbnail_url;
                    LinkBoxController.notifyUrlDataSetChanged();
                    if (iv != null) {
                        Glide.with(LinkBoxController.getApplication().getApplicationContext()).load(data.urlthumb).into(iv);
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    Log.e("ERROR", "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }

        public class ResultEmbedly {
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
        private static boolean isFirst;

        public static void postLoginToServerAsync() {
            fillInterface();
            if (TextUtils.isEmpty(LinkBoxController.linkUserData.usremail) || TextUtils.isEmpty(LinkBoxController.linkUserData.pass)) {
                return;
            }
            Log.d(TAG, "Login with usremail : " + LinkBoxController.linkUserData.usremail +
                    " / pass : " + LinkBoxController.linkUserData.pass);
            mainServerInterface.postLoginAsync(LinkBoxController.linkUserData, new Callback<LinkUserData>() {
                @Override
                public void success(LinkUserData linkUserData, Response response) {
                    isFirst = true;
                    LinkBoxController.linkUserData.usrid = linkUserData.usrid;
                    LinkBoxController.linkUserData.usrname = linkUserData.usrname;
                    Log.d(TAG, linkUserData.usrid + " : " + linkUserData.usremail + ", " + linkUserData.pass + ", " + linkUserData.usrname);
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Hello, " + linkUserData.usrname, Toast.LENGTH_LONG).show();
                    getBoxListFromServerAsync();
                }
                @Override
                public void failure(RetrofitError error) {
                    SharedPreferences sharedPreferences = LinkBoxController.getApplication().getSharedPreferences("user", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(LinkBoxController.getApplication().getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Can't Login - Check your Id and Password", Toast.LENGTH_LONG).show();
                    LinkBoxController.getApplication().startActivity(intent);
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postSignupToServerSync() {
            fillInterface();
            if (TextUtils.isEmpty(LinkBoxController.linkUserData.usremail)
                    || TextUtils.isEmpty(LinkBoxController.linkUserData.usrname)
                    || TextUtils.isEmpty(LinkBoxController.linkUserData.pass)) {
                return;
            }
            Log.d(TAG, "Signup with usremail : " + LinkBoxController.linkUserData.usremail +
                    " / usrname : " + LinkBoxController.linkUserData.usrname +
                    " / pass : " + LinkBoxController.linkUserData.pass);
            mainServerInterface.postSignUpAsync(LinkBoxController.linkUserData, new Callback<LinkUserData>() {
                @Override
                public void success(LinkUserData linkUserData, Response response) {
                    LinkBoxController.linkUserData.usrid = linkUserData.usrid;
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Congraturation! SignUp Success!", Toast.LENGTH_LONG).show();
                    getBoxListFromServerAsync();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void getBoxListFromServerAsync() {
            fillInterface();
            Log.d(TAG, "GetBox by usrid : " + LinkBoxController.linkUserData.usrid);
            mainServerInterface.getBoxListAsync(LinkBoxController.linkUserData.usrid, new Callback<List<LinkBoxListData>>() {
                @Override
                public void success(List<LinkBoxListData> linkBoxListDatas, Response response) {
                    if (linkBoxListDatas != null) {
                        LinkBoxController.boxListSource = (ArrayList<LinkBoxListData>) linkBoxListDatas;
                    }
                    LinkBoxController.notifyBoxDataSetChanged();
                    LinkBoxController.currentBox = 0;
                    postUrlListFromServerAsync();
                }

                @Override
                public void failure(RetrofitError error) {
                    if (isFirst) {
                        isFirst = false;
                        Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Fail to Connect. Please retry connection.", Toast.LENGTH_LONG).show();
                        LinkBoxController.getApplication().startActivity(new Intent(LinkBoxController.getApplication().getApplicationContext(), LinkBoxActivity.class));
                    } else {
                        Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    }
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postAddBoxToServerAsync(String cbname) {
            fillInterface();
            LinkBoxListData linkBoxListData = new LinkBoxListData();
            linkBoxListData.cbname = cbname;
            Log.d(TAG, "AddBox by usrid : " + LinkBoxController.linkUserData.usrid
                    + " as cbname : " + cbname);
            mainServerInterface.postAddBoxAsync(LinkBoxController.linkUserData.usrid, linkBoxListData, new Callback<LinkBoxListData>() {
                @Override
                public void success(LinkBoxListData linkBoxListData, Response response) {
                    LinkBoxController.boxListSource.add(linkBoxListData);
                    LinkBoxController.notifyBoxDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postRemoveBoxFromServerAsync(final int ind) {
            fillInterface();
            final LinkBoxListData linkBoxListData = LinkBoxController.boxListSource.get(ind);
            Log.d(TAG, "RemoveBox by usrid : " + LinkBoxController.linkUserData.usrid
                    + " as cbid : " + linkBoxListData.cbid);
            mainServerInterface.postRemoveBoxAsync(LinkBoxController.linkUserData.usrid, linkBoxListData, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    LinkBoxController.boxListSource.remove(ind);
                    LinkBoxController.notifyBoxDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postEditBoxFromServerAsync(final int ind, final String newname) {
            fillInterface();
            final LinkBoxListData linkBoxListDataOrig = LinkBoxController.boxListSource.get(ind);
            linkBoxListDataOrig.cbname = newname;
            Log.d(TAG, "EditBox by usrid : " + LinkBoxController.linkUserData.usrid +
                    " as cbid : " + linkBoxListDataOrig.cbid +
                    " to new name : " + newname);
            mainServerInterface.postEditBoxAsync(LinkBoxController.linkUserData.usrid, linkBoxListDataOrig, new Callback<LinkBoxListData>() {
                @Override
                public void success(LinkBoxListData linkBoxListData, Response response) {
                    //LinkBoxController.boxListSource.set(ind, linkBoxListData);
                    LinkBoxController.notifyBoxDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postUrlListFromServerAsync() {
            fillInterface();
            final LinkBoxListData linkBoxListData = LinkBoxController.boxListSource.get(LinkBoxController.currentBox);
            mainServerInterface.postUrlListAsync(linkBoxListData.cbid, LinkBoxController.linkUserData, new Callback<List<LinkUrlListData>>() {
                @Override
                public void success(List<LinkUrlListData> linkUrlListDatas, Response response) {
                    Log.d(TAG, "num=" + linkUrlListDatas.size() + ", boxid=" + linkBoxListData.cbid);
                    LinkBoxController.urlListSource = (ArrayList<LinkUrlListData>) linkUrlListDatas;
                    LinkBoxController.notifyUrlDataSetChanged();
                    if (isFirst) {
                        isFirst = false;
                        Intent intent = new Intent(LinkBoxController.getApplication().getApplicationContext(), LinkBoxActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        LinkBoxController.getApplication().startActivity(intent);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (isFirst) {
                        isFirst = false;
                        Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Fail to Connect. Please retry connection.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LinkBoxController.getApplication().getApplicationContext(), LinkBoxActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        LinkBoxController.getApplication().startActivity(intent);
                    } else {
                        Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    }
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postAddUrlToServerAsync(LinkUrlListData linkUrlListData) {
            fillInterface();
            final LinkBoxListData linkBoxListData = LinkBoxController.boxListSource.get(LinkBoxController.currentBox);
            mainServerInterface.postAddUrlAsync(LinkBoxController.linkUserData.usrid, linkBoxListData.cbid, linkUrlListData, new Callback<LinkUrlListData>() {
                @Override
                public void success(LinkUrlListData linkUrlListData, Response response) {
                    LinkBoxController.urlListSource.add(linkUrlListData);
                    LinkBoxController.notifyUrlDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postRemoveUrlFromServerAsync(final int ind) {
            fillInterface();
            final LinkBoxListData linkBoxListData = LinkBoxController.boxListSource.get(LinkBoxController.currentBox);
            LinkUrlListData linkUrlListData = LinkBoxController.urlListSource.get(ind);
            mainServerInterface.postRemoveUrlAsync(linkBoxListData.cbid, linkUrlListData, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    LinkBoxController.urlListSource.remove(ind);
                    LinkBoxController.notifyUrlDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postEditUrlFromServerAsync(final int ind, final String newname) {
            fillInterface();
            final LinkBoxListData linkBoxListData = LinkBoxController.boxListSource.get(LinkBoxController.currentBox);
            LinkUrlListData linkUrlListDataOrig = LinkBoxController.urlListSource.get(ind);
            linkUrlListDataOrig.urlname = newname;
            mainServerInterface.postEditUrlAsync(linkBoxListData.cbid, linkUrlListDataOrig, new Callback<LinkUrlListData>() {
                @Override
                public void success(LinkUrlListData linkUrlListData, Response response) {
                    //LinkBoxController.urlListSource.set(ind, linkUrlListData);
                    LinkBoxController.notifyUrlDataSetChanged();
                }
                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void getUsrListFromServerAsync() {
            fillInterface();
            int cbid = LinkBoxController.boxListSource.get(LinkBoxController.currentBox).cbid;
            Log.d(TAG, "GetUsr by cbid : " + cbid);
            mainServerInterface.getUsrListAsync(cbid, new Callback<List<LinkUserData>>() {
                @Override
                public void success(List<LinkUserData> linkUserDatas, Response response) {
                    LinkBoxController.editorListSource = (ArrayList<LinkUserData>) linkUserDatas;
                    LinkBoxController.notifyEditorDataSetChanged();
                }
                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postAddUsrToServerAsync(String email, String message) {
            fillInterface();
            int cbid = LinkBoxController.boxListSource.get(LinkBoxController.currentBox).cbid;
            Log.d(TAG, "AddUsr by cbid : " + cbid);
            TwoString twoStr = new TwoString();
            twoStr.usremail = email;
            twoStr.message = message;
            mainServerInterface.postAddUsrAsync(cbid, twoStr, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Send request!", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postEditGoodFromServerAsync(int ind, boolean isgood) {
            fillInterface();
            final LinkBoxListData linkBoxListData = LinkBoxController.boxListSource.get(LinkBoxController.currentBox);
            LinkUrlListData linkUrlListData = LinkBoxController.urlListSource.get(ind);
            if (linkUrlListData.isgood != isgood) {
                linkUrlListData.isgood = isgood;
                mainServerInterface.postEditGoodAsync(linkBoxListData.cbid, LinkBoxController.linkUserData.usrid, linkUrlListData, new Callback<LinkUrlListData>() {
                    @Override
                    public void success(LinkUrlListData linkUrlListData, Response response) {
                        //LinkBoxController.urlListSource.set(ind, linkUrlListData);
                        LinkBoxController.notifyUrlDataSetChanged();
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                    }
                });
            }
        }
        public static void postSigndownFromServerAsync() {
            fillInterface();
            mainServerInterface.postSignDownAsync(LinkBoxController.linkUserData, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    LinkBoxController.linkUserData = null;
                }
                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(LinkBoxController.getApplication().getApplicationContext(), "Server Connection Error", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Error : " + error.getUrl() + ">>>>" + error.getMessage());
                }
            });
        }
        public static void postPushTokenToServerAsync(final String token) {
            fillInterface();
            mainServerInterface.postPushTokenAsync(LinkBoxController.linkUserData.usrid, token, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                }

                @Override
                public void failure(RetrofitError error) {
                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mainServerInterface.postPushTokenAsync(LinkBoxController.linkUserData.usrid, token, this);
                }
            });
        }
        public static void postPremiumToServerAsync() {
            fillInterface();
            if (!LinkBoxController.linkUserData.premium) {
                mainServerInterface.postPremium(LinkBoxController.linkUserData, new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {

                    }
                    @Override
                    public void failure(RetrofitError error) {
                    }
                });
            }
        }

        private static void fillInterface() {
            if (mainServerInterface == null) {
                mainServerInterface = LinkBoxController.getApplication().getLinkNetworkMainServerInterface();
            }
        }
    }
}
