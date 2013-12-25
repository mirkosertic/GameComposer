package de.mirkosertic.gameengine.android;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import de.mirkosertic.gameengine.android.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameEngineActivity extends Activity {

    /**
     * Called when the activity is first created.
     *
     * @param aSavedState If the activity is being re-initialized after
     *                    previously being shut down then this Bundle contains the data it most
     *                    recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle aSavedState) {
        super.onCreate(aSavedState);
        setContentView(R.layout.activity_main);

        WebView theWebView = (WebView) findViewById(R.id.webview);
        WebSettings theWebSettings = theWebView.getSettings();
        theWebSettings.setJavaScriptEnabled(true);

        // We load the HTML app stored in the asset folder and we are done here...
        AssetManager theAssetManager = getAssets();

        StringBuilder theHTML = new StringBuilder();
        try (BufferedReader theReader = new BufferedReader(new InputStreamReader(theAssetManager.open("index.html")))) {
            while(theReader.ready()) {
                String theLine = theReader.readLine();
                if (theLine != null) {
                    theHTML.append(theLine);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        theWebView.loadDataWithBaseURL("file:///android_asset/", theHTML.toString(),
  //              "text/html", "UTF-8", null);
        theWebView.loadUrl("http://www.mirkosertic.de/games/dukeplatform/index.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu aMenu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, aMenu);
        return true;
    }
}
