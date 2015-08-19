package org.sopt.linkbox.custom.network.main.url;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.url.TagListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;

import java.util.List;

import retrofit.Callback;

/**
 * Created by Junyoung on 2015-08-09.
 */
public class UrlListWrapper {
    private static UrlListInterface urlListInterface = null;

    private static void setUrlListInterface() {
        urlListInterface = LinkBoxController.getApplication().getUrlListInterface();
    }

    public UrlListWrapper() {
        setUrlListInterface();
    }

    public void allList(int startNum, int urlNum, Callback<MainServerData<List<UrlListData>>> callback) {
        urlListInterface.allList(LinkBoxController.usrListData.usrKey, startNum, urlNum, callback);
    }
    public void favoriteList(int startNum, int urlNum, Callback<MainServerData<List<UrlListData>>> callback) {
        urlListInterface.favoriteList(LinkBoxController.usrListData.usrKey, startNum, urlNum, callback);
    }
    public void hiddenList(int startNum, int urlNum, Callback<MainServerData<List<UrlListData>>> callback) {
        urlListInterface.hiddenList(LinkBoxController.usrListData.usrKey, startNum, urlNum, callback);
    }
    public void boxList(int startNum, int urlNum, Callback<MainServerData<List<UrlListData>>> callback) {
        urlListInterface.boxList(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, startNum, urlNum, callback);
    }
    public void add(UrlListData urlListData, BoxListData boxListData, Callback<MainServerData<UrlListData>> callback) {
        urlListInterface.add(LinkBoxController.usrListData.usrKey, boxListData.boxKey, urlListData, callback);
    }
    public void remove(UrlListData urlListData, Callback<MainServerData<Object>> callback) {
        urlListInterface.remove(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, urlListData, callback);
    }
    public void edit(UrlListData original, String newTitle, Callback<MainServerData<Object>> callback) {
        UrlListData urlListData = original.clone();
        urlListData.urlTitle = newTitle;
        urlListInterface.edit(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, urlListData, callback);
    }
    public void hidden(UrlListData origInal, int hidden, Callback<MainServerData<Object>> callback) {
        UrlListData urlListData = origInal.clone();
        urlListData.hidden = hidden;
        urlListInterface.hidden(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, urlListData, callback);
    }
    public void share(UrlListData urlListData, BoxListData newBox, Callback<MainServerData<UrlListData>> callback) {
        urlListInterface.share(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, newBox.boxKey, urlListData, callback);
    }
    public void like(UrlListData original, int good, Callback<MainServerData<Object>> callback) {
        UrlListData urlListData = original.clone();
        urlListData.good = good;
        urlListInterface.like(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, urlListData, callback);
    }
    public void tagList(UrlListData urlListData, Callback<MainServerData<List<TagListData>>> callback) {
        urlListInterface.tagList(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, urlListData.urlKey, callback);
    }
    public void tagAdd(UrlListData urlListData, String tag, Callback<MainServerData<TagListData>> callback) {
        TagListData tagListData = new TagListData();
        tagListData.tag = tag;
        urlListInterface.tagAdd(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, urlListData.urlKey, tagListData, callback);
    }
    public void tagRemove(UrlListData urlListData, TagListData tagListData, Callback<MainServerData<Object>> callback) {
        urlListInterface.tagRemove(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, urlListData.urlKey, tagListData, callback);
    }
}
