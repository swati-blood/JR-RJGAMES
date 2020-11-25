package in.games.ChiragMatka;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.games.ChiragMatka.Adapter.StarlinehistoryAdapter;
import in.games.ChiragMatka.Common.Common;
import in.games.ChiragMatka.Config.BaseUrl;
import in.games.ChiragMatka.Model.Starline_History_Objects;
import in.games.ChiragMatka.Prevalent.Prevalent;
import in.games.ChiragMatka.utils.CustomVolleyJsonArrayRequest;
import in.games.ChiragMatka.utils.LoadingBar;
import in.games.ChiragMatka.utils.SessionMangement;

import static in.games.ChiragMatka.Config.Constants.KEY_ID;
import static in.games.ChiragMatka.URLs.Starline_Histry_Url;


public class Starline_Activity extends AppCompatActivity {
  Common common;
  SessionMangement sessionMangement;
    private ListView recyclerView;
    ArrayList<Starline_History_Objects> list;
   LoadingBar progressDialog;
    private StarlinehistoryAdapter bidHistoryAdapter;
    private TextView bt_back;
    private String user_id;
    private String matka_id;
    WebView browser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starline_);
        sessionMangement = new SessionMangement(Starline_Activity.this);
        common=new Common(Starline_Activity.this);
        matka_id="19";
        browser = findViewById(R.id.bid_histry_webview);
        progressDialog=new LoadingBar(Starline_Activity.this);

        recyclerView=(ListView) findViewById(R.id.recyclerView);
        bt_back = (TextView) findViewById(R.id.txt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //   user_id= "3";
//        user_id= Prevalent.currentOnlineuser.getId().toString().trim();
        user_id= sessionMangement.getUserDetails().get(KEY_ID).toString().trim();
        //recyclerView.setHasFixedSize(false);
        //layoutManager= new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        list=new ArrayList();
//        getStarlineBidData(user_id,"20");
        //user_id= Prevalent.currentOnlineuser.getId().toString().trim();

        bidHistoryAdapter=new StarlinehistoryAdapter(this,list);
        recyclerView.setAdapter(bidHistoryAdapter);

        // getMatkaData();

        String url=BaseUrl.Starline_Histry_Url+user_id;
        Log.e("asdasd",""+url);
        browser.loadUrl(url);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        browser.setWebViewClient(new myWebViewClient());

    }

    @Override
    protected void onStart() {
        super.onStart();
        //setSessionTimeOut(Starline_Activity.this);
    }




    private class myWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
            view.loadUrl(Url);
//            txt.setText(Url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            progressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            progressDialog.dismiss();
        }
    }

}
