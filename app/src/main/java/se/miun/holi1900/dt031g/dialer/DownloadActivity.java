package se.miun.holi1900.dt031g.dialer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DownloadActivity extends AppCompatActivity {

    private static final String URL_VOICES ="https://dt031g.programvaruteknik.nu/dialer/voices/";
    private WebView voicesWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        voicesWebView = findViewById(R.id.voices_webview);
        voicesWebView.loadUrl(URL_VOICES);

        ProgressDialog progressDialog =
                ProgressDialog.show(this, "", "Loading...",true);

        // Our WebView should handle URL loading instead of the default handler of URL's
        voicesWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
            }
        });
    }
}