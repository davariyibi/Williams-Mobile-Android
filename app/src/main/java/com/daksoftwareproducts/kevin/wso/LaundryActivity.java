package com.daksoftwareproducts.kevin.wso;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class LaundryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry);
        WebView browser = (WebView) findViewById(R.id.laundry);
        WebSettings webSettings = browser.getSettings();
        browser.setInitialScale(1);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString("Android");
        browser.loadUrl("m.laundryview.com/lvs.php");
    }
}
