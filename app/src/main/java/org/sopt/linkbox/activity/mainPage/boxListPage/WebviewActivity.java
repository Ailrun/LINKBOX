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
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import org.sopt.linkbox.R;

public class WebviewActivity extends AppCompatActivity {

    private Toolbar tToolbar = null;

    private String sAddress;
    private String sTitle;

    private WebView webView;
    private WebSettings webSettings;

    private EditText etReply;
    private ImageButton ibSendButton;

    private InputMethodManager mInputMethodManager;

    private Boolean isAnimationRunning = false;
    private Boolean isOpened = false;
    private FrameLayout eContentLayout;
    private FrameLayout eHeaderLayout;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Intent intent = getIntent();
        sAddress = intent.getStringExtra("url");
        sTitle = intent.getStringExtra("title");

        initview();
        initToolbarView();



        webSettings = webView.getSettings();
        webSettings.setSaveFormData(false);//Form 데이터 저장 여부
        webSettings.setJavaScriptEnabled(true);//javaScript 사용 여부
        webSettings.setSupportZoom(true);//줌 지원 여부
        webSettings.setBuiltInZoomControls(true); // 멀티터치 줌 지원
        webSettings.setDisplayZoomControls(false);//줌 컨트롤러 표시 여부
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//캐시 사용 모드

        webView.setWebViewClient(new WebViewClientClass());


        webView.loadUrl(sAddress);
    }

    @Override
    public void onBackPressed() {
            if (eContentLayout.getVisibility() == View.VISIBLE)
            {
                collapse(eContentLayout);
                return;
        } else {
            if (webView.canGoBack()) {

                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    webView.clearCache(false);
                    finish();
                }
                return;
            }

        }
        super.onBackPressed();
    }

    //웹뷰 클라이언트
    private class WebViewClientClass extends WebViewClient {

        //페이지 로딩이 끝났을때 호출
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        /*
     * 웹뷰 내 링크 터치 시 새로운 창이 뜨지 않고
	 * 해당 웹뷰 안에서 새로운 페이지가 로딩되도록 함
	 */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return super.shouldOverrideUrlLoading(view, url);
        }


    }


    private void initview() {

        webView = (WebView)findViewById(R.id.WV_webview);
        etReply = (EditText) findViewById(R.id.ET_reply_expandable_view_content);
        ibSendButton = (ImageButton) findViewById(R.id.IB_send_button_expandable_view_content);


        eHeaderLayout = (FrameLayout)findViewById(R.id.FL_expandable_headerlayout_webview);
        eContentLayout = (FrameLayout)findViewById(R.id.FL_expandable_contentLayout_webview);

        eContentLayout.setVisibility(View.GONE);

        eHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimationRunning) {
                    if (eContentLayout.getVisibility() == View.VISIBLE)
                        collapse(eContentLayout);
                    else
                        expand(eContentLayout);

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
                mInputMethodManager.hideSoftInputFromWindow(etReply.getWindowToken(), 0);//보내기 버튼 누를때 키보드 숨기기

            }
        });
    }
    private void expand(final View v)
    {
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);

        animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
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

    private void collapse(final View v)
    {
        final int initialHeight = v.getMeasuredHeight();
        animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1)
                {
                    v.setVisibility(View.GONE);
                    isOpened = false;
                }
                else{
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

    public Boolean isOpened()
    {
        return isOpened;
    }

    public void show()
    {
        if (!isAnimationRunning)
        {
            expand(eContentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    isAnimationRunning = false;
                }
            }, 200);
        }
    }
    public void hide()
    {
        if (!isAnimationRunning)
        {
            collapse(eContentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    isAnimationRunning = false;
                }
            }, 200);
        }
    }

    private void initToolbarView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        tToolbar.setTitleTextColor(getResources().getColor(R.color.real_white));
        tToolbar.setTitle(sTitle);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setTitle(sTitle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etReply.setHint("title = " + sTitle);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
