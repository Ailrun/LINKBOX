package org.sopt.linkbox.custom.network.main.search;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.data.tempData.SearchBody;

import java.util.List;

import retrofit.Callback;

/**
 * Created by sy on 2015-09-19.
 */
public class SearchWrapper {
    private static SearchInterface searchInterface = null;

   private static void setSearchInterface() {
       if(searchInterface == null){
           searchInterface = LinkBoxController.getApplication().getSearchInterface();
       }
   }

    public SearchWrapper(){
        setSearchInterface();
    }

    public void allSearch(int startNum, int urlNum, int searchType, SearchBody searchbody, Callback<MainServerData<List<UrlListData>>> callback){
        searchInterface.allSearch(LinkBoxController.usrListData.usrKey, startNum, urlNum, searchType, searchbody, callback);
    }

    public void boxSearch(int boxKey, int startNum, int urlNum, int searchType, SearchBody searchbody, Callback<MainServerData<List<UrlListData>>> callback) {
        searchInterface.boxSearch(LinkBoxController.usrListData.usrKey, boxKey, startNum, urlNum, searchType, searchbody, callback);
    }
}
