package com.cdeans.tweetposter.activities;

import android.util.Log;
import com.cdeans.tweetposter.OTweetApplication;
import com.cdeans.tweetposter.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import javax.security.auth.login.LoginException;

public class AuthorizationActivity extends Activity {

  private OTweetApplication app;
  private WebView webView;
  
  private WebViewClient webViewClient = new WebViewClient() {
    @Override
    public void onLoadResource(WebView view, String url) {
        Uri uri = Uri.parse(url);
        //Looking for the callback URL.
        if (uri.getHost().contains("google.com")) {
            String token = uri.getQueryParameter("oauth_token");
            if (null != token) {
                webView.setVisibility(View.INVISIBLE);
                app.authorized();
                Log.e("CAMSH", "Authorised");
                finish();
            } else {
            // tell user to try again
                Log.e("CAMSH", "FAIL");
            }
        } else {
        super.onLoadResource(view, url);
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    setContentView(R.layout.authorization_view);
    setUpViews();
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    String authURL = app.beginAuthorization();
    webView.loadUrl(authURL);
  }

  private void setUpViews() {
    webView = (WebView)findViewById(R.id.web_view);
    webView.setWebViewClient(webViewClient);
  }

}
