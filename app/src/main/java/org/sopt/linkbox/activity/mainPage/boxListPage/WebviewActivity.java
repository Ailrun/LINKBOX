package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.adapters.listViewAdapter.WebviewCommentListAdapter;
import org.sopt.linkbox.custom.data.mainData.url.CommentListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WebviewActivity extends AppCompatActivity {

    private Toolbar tToolbar = null;

    private WebView webView;
    private WebSettings webSettings;

    private ListView lvComment;
    private EditText etComment;
    private TextView tvNumOfComment;
    private final int maxHeight = 20;
    private ImageButton ibSendButton;
    private int position;

    private InputMethodManager mInputMethodManager;

    private Boolean isAnimationRunning = false;
    private Boolean isOpened = false;
    private FrameLayout flContentLayout;
    private FrameLayout flHeaderLayout;
    private Animation animation;

    private UrlListData urlListData = null;
    private UrlListWrapper urlListWrapper = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        urlListWrapper = new UrlListWrapper();

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);

        urlListData = LinkBoxController.urlListSource.get(position);
        
        initview();
        initToolbarView();
        initControl();
        
        webSettings = webView.getSettings();
        webSettings.setSaveFormData(false);//Form 데이터 저장 여부
        webSettings.setJavaScriptEnabled(true);//javaScript 사용 여부
        webSettings.setAppCacheMaxSize(1024*1024*1);//캐시 최대크기. 나중에는 자동이 되므로 삭제될것
        webSettings.setAppCacheEnabled(true);//캐시사용여부
        webSettings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());//캐시 경로
        webSettings.setSupportZoom(true);//줌 지원 여부
        webSettings.setBuiltInZoomControls(true); // 멀티터치 줌 지원
        webSettings.setDisplayZoomControls(false);//줌 컨트롤러 표시 여부
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//캐시 사용 모드

        webView.setWebViewClient(new WebViewClientClass());

        webView.loadUrl(urlListData.url);
    }
    @Override
    public void onBackPressed() {
        if (flContentLayout.getVisibility() == View.VISIBLE) {
            collapse(flContentLayout);
        }
        else {
            if (webView.canGoBack()) {
                webView.goBack();
            }
            else {
                webView.clearCache(false);
                finish();
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
                break;
            case R.id.action_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.putExtra(Intent.EXTRA_SUBJECT, urlListData.urlTitle);
                intent.putExtra(Intent.EXTRA_TEXT, urlListData.url);
                intent.putExtra(Intent.EXTRA_TITLE, urlListData.urlTitle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.action_like:
                urlListWrapper.like(urlListData, 1-urlListData.liked, new UrlLikeCallback(urlListData, item));
                item.setEnabled(false);
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem liked = menu.findItem(R.id.action_like);
        if(urlListData.liked == 0)
        {
            liked.setIcon(R.drawable.mainpage_bookmark_unchecked);
        }
        else
        {
            liked.setIcon(R.drawable.mainpage_bookmark_checked);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }


    private void initControl() {
        LinkBoxController.webviewCommentListAdapter = new WebviewCommentListAdapter(getApplicationContext(), LinkBoxController.commentListSource, urlListData);
        lvComment.setAdapter(LinkBoxController.webviewCommentListAdapter);
    }
    private void initview() {

        webView = (WebView) findViewById(R.id.WV_webview);
        lvComment = (ListView) findViewById(R.id.LV_container_expandable_view_content);
        etComment = (EditText) findViewById(R.id.ET_comment_expandable_view_content);
        tvNumOfComment = (TextView) findViewById(R.id.TV_number_of_reply_webview);//TODO 이거 숫자 어디서 어떻게 받아오지
        ibSendButton = (ImageButton) findViewById(R.id.IB_send_button_expandable_view_content);

        flHeaderLayout = (FrameLayout) findViewById(R.id.FL_expandable_headerlayout_webview);
        flContentLayout = (FrameLayout) findViewById(R.id.FL_expandable_contentLayout_webview);

        flContentLayout.setVisibility(View.GONE);

        flHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimationRunning) {
                    if (flContentLayout.getVisibility() == View.VISIBLE)
                        collapse(flContentLayout);
                    else
                        expand(flContentLayout);

                    isAnimationRunning = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isAnimationRunning = false;
                        }
                    }, 200);
                }
            }
        });

        ibSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMethodManager.hideSoftInputFromWindow(etComment.getWindowToken(), 0);//댓글 쓰기 버튼 누를때 키보드 숨기기
                //버튼 눌렀을떄 댓글 서버에 전송
                if (etComment != null) {
                    //boxListWrapper.invite(twoString, new BoxInviteCallback()); 같은 콜백 부르기
                    urlListWrapper.commentAdd(urlListData, etComment.getText().toString(), new CommentAddCallback());
                }
                else {
                    Toast.makeText(WebviewActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initToolbarView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        tToolbar.setTitleTextColor(getResources().getColor(R.color.real_white));
        tToolbar.setTitle(urlListData.urlTitle);
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(urlListData.urlTitle);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void expand(final View v) {
        urlListWrapper.commentList(urlListData, new CommentListCallback());
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);

        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1)
                    isOpened = true;
                v.getLayoutParams().height = (interpolatedTime == 1) ? RelativeLayout.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(200);
        v.startAnimation(animation);
    }
    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    isOpened = false;
                }
                else {
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(200);
        v.startAnimation(animation);
    }

    public Boolean isOpened() {
        return isOpened;
    }

    public void show() {
        if (!isAnimationRunning) {
            expand(flContentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAnimationRunning = false;
                }
            }, 200);
        }
    }
    public void hide() {
        if (!isAnimationRunning) {
            collapse(flContentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAnimationRunning = false;
                }
            }, 200);
        }
    }

    private class UrlLikeCallback implements Callback<MainServerData<Object>> {
        UrlListData urlListData = null;
        MenuItem item = null;

        public UrlLikeCallback(UrlListData urlListData, MenuItem item) {
            this.urlListData = urlListData;
            this.item = item;
        }
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            /*
            urlListData.liked = (1-urlListData.liked);
            urlListData.likedNum += 2*urlListData.liked - 1;
            */
            if(urlListData.liked == 0) {
                urlListData.liked = 1;
                urlListData.likedNum += 1;
            }
            else {
                urlListData.liked = 0;
                urlListData.likedNum -= 1;
            }
            item.setIcon(urlListData.liked == 0 ? R.drawable.mainpage_bookmark_unchecked : R.drawable.mainpage_bookmark_checked);
            item.setEnabled(true);
            LinkBoxController.notifyUrlDataSetChanged();
        }
        @Override
        public void failure(RetrofitError error) {
            item.setEnabled(true);
        }
    }

    private class CommentListCallback implements Callback<MainServerData<List<CommentListData>>> {
        @Override
        public void success(MainServerData<List<CommentListData>> wrappedCommentListDatas, Response response) {
            if (wrappedCommentListDatas.result) {
                LinkBoxController.commentListSource.clear();
                LinkBoxController.commentListSource.addAll(wrappedCommentListDatas.object);
                LinkBoxController.notifyCommentDataSetChanged();
            }
            else {
                Toast.makeText(WebviewActivity.this, "댓글 불러오기 실패.", Toast.LENGTH_SHORT).show();
                hide();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(WebviewActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            RetrofitDebug.debug(error);
        }
    }

    private class CommentAddCallback implements Callback<MainServerData<CommentListData>> {
        @Override
        public void success(MainServerData<CommentListData> wrappedCommentListData, Response response) {
            if (wrappedCommentListData.result) {
                Toast.makeText(WebviewActivity.this, "댓글작성!", Toast.LENGTH_SHORT).show();
                LinkBoxController.commentListSource.add(wrappedCommentListData.object);
                LinkBoxController.notifyUrlDataSetChanged();
                etComment.setText("");
            }
            else {
                Toast.makeText(WebviewActivity.this, "댓글작성 실패.", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(WebviewActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            RetrofitDebug.debug(error);
        }
    }
    //웹뷰 클라이언트
    private class WebViewClientClass extends WebViewClient {
        //페이지 로딩이 끝났을때 호출
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
        /**
        * 웹뷰 내 링크 터치 시 새로운 창이 뜨지 않고
	    * 해당 웹뷰 안에서 새로운 페이지가 로딩되도록 함
	    */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
