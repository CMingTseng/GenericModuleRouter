package com.spinytech.webdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.spinytech.macore.RouterCallback;
import com.spinytech.macore.RouterManager;
import com.spinytech.macore.RouterRequest;

import static com.spinytech.webdemo.R.id.web;

public class WebActivity extends AppCompatActivity {

    public static final String YOUR_PROTOCOL = "yourprotocol://";
    private WebView mContentWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mContentWv = (WebView) findViewById(web);
        mContentWv.setWebViewClient(new MyWebViewClient());
        mContentWv.getSettings().setBuiltInZoomControls(true);
        mContentWv.getSettings().setJavaScriptEnabled(true);
        mContentWv.getSettings().setSupportZoom(true);
        mContentWv.getSettings().setUseWideViewPort(true);
        mContentWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mContentWv.getSettings().setLoadWithOverviewMode(true);
        mContentWv.loadUrl("file:///android_asset/page.html");
    }

    public void dispatchAction(String url) {
        if (url.indexOf(YOUR_PROTOCOL) >= 0) {
            String command = url.substring(YOUR_PROTOCOL.length());
            try {
                Bundle result = RouterManager.getInstance().route(this, RouterRequest.obtain(this)
                        .url(command), new RouterCallback() {
                    @Override
                    public void onResult(int resultCode, Bundle resultData) {
                        // callback to mContentWv.loadUrl();
                        String result = "";
                        if(resultCode == RouterCallback.CODE_SUCCESS && resultData!=null){
                            result =  resultData.getString(RouterCallback.KEY_VALUE);
                        } else {
                            result = resultData.getString(RouterCallback.KEY_ERROR_MSG);
                        }
                        toastOnUIThread(resultCode + "\t" + result);
                    }
                });
                if (result != null) {
                    String resultString = result.getString(RouterCallback.KEY_VALUE);
                    toastOnUIThread("sync result:" + resultString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void toastOnUIThread(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WebActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url) && url.startsWith(YOUR_PROTOCOL)) {
                dispatchAction(url);
            } else {
                mContentWv.loadUrl(url);
            }
            return true;
        }
    }
}
