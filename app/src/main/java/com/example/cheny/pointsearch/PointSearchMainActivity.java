package com.example.cheny.pointsearch;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class PointSearchMainActivity extends AppCompatActivity {
    WebView mWebview;
    ImageView imgView;
    WebSettings mWebSettings;
    boolean firstLaunch=true;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_search_main);
        mWebview = (WebView) findViewById(R.id.webView);
        imgView = (ImageView) findViewById(R.id.imgView);
        mWebSettings = mWebview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);//设置支持javascript
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //自适应屏幕
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setAllowFileAccess(true);  //设置可以访问文件
        mWebSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        mWebview.loadUrl("http://www.cityfoodmap.com:8081");
        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebview.setWebViewClient(new WebViewClient() {
            
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //如果网络错误提示
                if(!isOnline()){
                    Toast.makeText(PointSearchMainActivity.this,"网络链接不可用，请检查网络后重试",Toast.LENGTH_SHORT).show();
                    return false;
                }
                view.loadUrl(url);
                return true;
            }
    
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(firstLaunch){
                    Animation fadeIn=AnimationUtils.loadAnimation(PointSearchMainActivity.this,R.anim.abc_fade_in);
                    mWebview.setAnimation(fadeIn);
                    mWebview.setVisibility(View.VISIBLE);
                }
            }
    
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(firstLaunch){
                    Animation fadeOut=AnimationUtils.loadAnimation(PointSearchMainActivity.this,R.anim.abc_fade_out);
                    imgView.setAnimation(fadeOut);
                    imgView.setVisibility(View.GONE);
                    firstLaunch=false;
                    //如果网络错误提示
                    if(!isOnline()){
                        Toast.makeText(PointSearchMainActivity.this,"网络链接不可用，请检查网络后重试",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    //判断网络是否正确
    public boolean isOnline() {
        ConnectivityManager cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        return !((networkInfo==null) || (!networkInfo.isAvailable()) || (!networkInfo.isConnected()));
    }
    
    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            
            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }
}
