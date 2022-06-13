package se.miun.holi1900.dt031g.dialer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadActivity extends AppCompatActivity {

    private static final String URL_VOICES ="https://dt031g.programvaruteknik.nu/dialer/voices/";
    private WebView voicesWebView;
    //private static final String DOWNLOADED_VOICES = "downloadedVoices.zip";
    private BroadcastReceiver receiver;
    private ConstraintLayout progressdialog;
    private DownloadVoicesAsyncTask download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        progressdialog = findViewById(R.id.progress_constraint_layout);
        voicesWebView = findViewById(R.id.voices_webview);
        voicesWebView.loadUrl(URL_VOICES);

        //Runtime External storage permission for saving download files
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, 1);
        }

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
        voicesWebView.setDownloadListener(new DownloadListener()
        {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
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
                                    Environment.DIRECTORY_DOWNLOADS, getString(R.string.downloaded_voices_file));
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

                                    Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show();
                                    File sourceFile = new File(
                                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                            getText(R.string.downloaded_voices_file).toString());
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
            }
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

    /**
     * This class implements the methods to execute the async task to download weather information from a URL
     * The class gets the URL as input, displays a progress dialog on screen will the weather information
     * is downloaded in the background. When the download is done, the information if displayed on dialog Fragment
     */
    private class DownloadVoicesAsyncTask extends AsyncTask<URL, Integer, Void> {
        private static final String TAG = "DownloadZipFileAsyncTas";

        /**
         * display progress dialog
         */
        @Override
        protected void onPreExecute() {
            enableProgressBar();
        }

        /**
         * creates an HttpsURLConnection and read the data from the passed url and writes the data
         * as a file in download directory
         * @param urls url from which zip file will be read.
         */
        @Override
        protected Void doInBackground(URL... urls) {

            URL url = urls[urls.length - 1];
            HttpsURLConnection connection;
            try {
                // get a connection from url
                connection = (HttpsURLConnection) url.openConnection();
                int fileSize = connection.getContentLength();
                BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                File file = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        getText(R.string.downloaded_voices_file).toString());
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream out = new BufferedOutputStream(fos, 1024);
                byte[] buffer = new byte[1024];
                int read;
                double downloaded = 0.00;
                double percentDownloaded = 0.00;
                int bytesRead, totalBytesRead = 0;
                while(!isCancelled() && (read = in.read(buffer, 0, 1024)) >=0){
                    out.write(buffer, 0, read);
                    downloaded += read;
                    percentDownloaded = (downloaded*100)/fileSize;
                    publishProgress(totalBytesRead);
                }

                if(isCancelled()){
                    deleteFile();
                }
                out.close();
                in.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * unzip file and to the app specific storage
         *hide progress bar
         */
        @Override
        protected void onPostExecute(Void unused) {

            File sourceFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    getText(R.string.downloaded_voices_file).toString());
            File destinationFile = new File(getApplicationContext().getFilesDir(), getText(R.string.voices_directory).toString());
            String text;
            if(!Util.unzip(sourceFile, destinationFile)){
                text = "Copying voices to internal storage was unsuccessful";
            }else {
                text = "Download completed and voices stored.";
            }
            deleteFile();
            disableProgressBar();
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

}