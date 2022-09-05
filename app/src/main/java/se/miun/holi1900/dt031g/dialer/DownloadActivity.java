package se.miun.holi1900.dt031g.dialer;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadActivity extends AppCompatActivity {

    private static final String URL_VOICES ="https://dt031g.programvaruteknik.nu/dialer/voices/";
    private WebView voicesWebView;
    private static final String DOWNLOADED_VOICES = "downloadedVoices.zip";
    private BroadcastReceiver receiver;
    private ConstraintLayout progressdialog;
    //private DownloadVoicesAsyncTask download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        progressdialog = findViewById(R.id.progress_constraint_layout);
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
        getVoicesFromUrl();
    }

    /**
     * Sets a download listener to the webView
     * Downloads file asynchronously, name file and save in download folder.
     * Registers a BroadCastReceiver  to send notification when download is completed.
     * Displays a progress dialog during download
     */
    private void getVoicesFromUrl(){
        //handle downloading
        voicesWebView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            ProgressDialog dialog =
                    ProgressDialog.show(DownloadActivity.this, "", "downloading voices...",true);

            new  Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    String state = Environment.getExternalStorageState();
                    boolean spaceFound =  Environment.MEDIA_MOUNTED.equals(state);
                    if(spaceFound){
                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(url));
                        request.setMimeType(getMimeType(url));
                        String cookies = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookies);
                        request.addRequestHeader("User-Agent", userAgent);
                        request.setDescription("Downloading File...");
                        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS, DOWNLOADED_VOICES);
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                    }
                    else{

                        Toast.makeText(getApplicationContext(), "There is not enough space available to download file.",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    //register receiver to notify when download is completed
                    receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String action = intent.getAction();
                            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                                dialog.dismiss();

                                Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                                File sourceFile = new File(
                                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                        DOWNLOADED_VOICES);
                                File destinationFile = new File(getApplicationContext().getFilesDir(), getText(R.string.voices_directory).toString());
                                String text;
                                if(!Util.unzip(sourceFile, destinationFile)){
                                    text = "Copying voices to internal storage was unsuccessful";
                                }else {
                                    text = "Download completed and voices stored.";
                                }
                                deleteFile();
                                dialog.dismiss();
                                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    registerReceiver(receiver,
                            new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                }
            });
        });
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * deletes files in download directory that starts with "downloadedVoices"
     */
    public void deleteFile() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString();
        File fileDir = new File(path);
        if (fileDir.isDirectory() && fileDir.listFiles() !=null) {
            File[] filesList = fileDir.listFiles();
            for (File file : filesList) {
                if (file.getName().startsWith(getText(R.string.downloaded_file_prefix).toString())) {
                    file.delete();
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        WebView wv = findViewById(R.id.voices_webview);
        if(wv.canGoBack()){
            wv.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * unregister receiver
     */
    private void destroyReceiver() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    /**
     * overrides onResume method and add code to unregister BroadCastReceiver
     */
    @Override
    protected void onResume() {
        super.onResume();
        destroyReceiver();
    }

    /**
     * overrides onRestart method and add code to unregister BroadCastReceiver
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        destroyReceiver();
    }

    /**
     * overrides onDestroy method and add code to unregister BroadCastReceiver
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }

    private void enableProgressBar() {
        progressdialog.setVisibility(View.VISIBLE);
    }

    private void disableProgressBar() {
        progressdialog.setVisibility(View.GONE);
    }



}