package in.games.rdgames;
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

import in.games.rdgames.Adapter.StarlinehistoryAdapter;
import in.games.rdgames.Common.Common;
import in.games.rdgames.Model.Starline_History_Objects;
import in.games.rdgames.Prevalent.Prevalent;
import in.games.rdgames.utils.CustomVolleyJsonArrayRequest;
import in.games.rdgames.utils.LoadingBar;

import static in.games.rdgames.Config.BaseUrl.Url_bid_history;
import static in.games.rdgames.URLs.Starline_Histry_Url;


public class Starline_Activity extends AppCompatActivity {
    Common common;
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
        user_id= Prevalent.currentOnlineuser.getId().toString().trim();
        //recyclerView.setHasFixedSize(false);
        //layoutManager= new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        list=new ArrayList();
//        getStarlineBidData(user_id,"20");
        //user_id= Prevalent.currentOnlineuser.getId().toString().trim();

        bidHistoryAdapter=new StarlinehistoryAdapter(this,list);
        recyclerView.setAdapter(bidHistoryAdapter);

        // getMatkaData();

        String url=Starline_Histry_Url+user_id;
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
    private void getStarlineBidData(final String user_id, final String matka_id) {

        final String json_request_tag="json_starline_history_tag";
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("us_id",user_id);
        params.put("matka_id",matka_id);

        progressDialog.show();

        list.clear();

        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.POST, URLs.URL_BID_HISTORY, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try
                {
                    JSONArray jsonArray=response;
                    // Log.d("star_his",response.toString());
                    //  Toast.makeText(Starline_Activity.this,""+response,Toast.LENGTH_LONG).show();
                    String h=jsonArray.getString(0);
                    if(h.equals("null") || h.equals(null))
                    {
                        progressDialog.dismiss();
                        common.errorMessageDialog("No history");
                    }
                    else
                    {

                        for(int i=0; i<=jsonArray.length()-1;i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Starline_History_Objects matkasObjects = new Starline_History_Objects();
                            matkasObjects.setId(jsonObject.getString("id"));
                            matkasObjects.setUser_id(jsonObject.getString("user_id"));
                            matkasObjects.setMatka_id(jsonObject.getString("matka_id"));
                            matkasObjects.setBet_type(jsonObject.getString("bet_type"));
                            matkasObjects.setPoints(jsonObject.getString("points"));
                            matkasObjects.setDigits(jsonObject.getString("digits"));
                            matkasObjects.setDate(jsonObject.getString("date"));
                            matkasObjects.setTime(jsonObject.getString("time"));
                            // matkasObjects.setName(jsonObject.getString("name"));
                            matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
                            matkasObjects.setGame_id(jsonObject.getString("game_id"));
                            matkasObjects.setStatus(jsonObject.getString("status"));
                            matkasObjects.setPlay_for(jsonObject.getString("play_for"));
                            matkasObjects.setPlay_on(jsonObject.getString("play_on"));
                            matkasObjects.setDay(jsonObject.getString("day"));
                            list.add(matkasObjects);


                        }
                        bidHistoryAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();


                    }
                    //progressDialog.dismiss();

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(Starline_Activity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                common.errorMessageDialog("Something Went Wrong");

                //  Toast.makeText(BidActivity.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();
                Log.e("Volley",error.toString());
                progressDialog.dismiss();
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest,json_request_tag);




    }

    private void getBidData(final String user_id, final String matka_id) {

        progressDialog.show();

        list.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Url_bid_history, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("empty"))
                {
                    progressDialog.dismiss();
                    Toast.makeText(Starline_Activity.this,"empty",Toast.LENGTH_LONG).show();
//                        Log.e("Volley",error.toString());

                }
                else
                {
                    try
                    {
                        Log.d("Starline History: ",response.toString());
                        JSONArray jsonArray=new JSONArray(response);
                        //progressDialog.dismiss();
                        for(int i=0; i<=jsonArray.length()-1;i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Starline_History_Objects matkasObjects = new Starline_History_Objects();
                            matkasObjects.setId(jsonObject.getString("id"));
                            matkasObjects.setUser_id(jsonObject.getString("user_id"));
                            matkasObjects.setMatka_id(jsonObject.getString("matka_id"));
                            matkasObjects.setBet_type(jsonObject.getString("bet_type"));
                            matkasObjects.setPoints(jsonObject.getString("points"));
                            matkasObjects.setDigits(jsonObject.getString("digits"));
                            matkasObjects.setDate(jsonObject.getString("date"));
                            matkasObjects.setTime(jsonObject.getString("time"));
                            //matkasObjects.setName(jsonObject.getString("name"));
                            matkasObjects.setGame_id(jsonObject.getString("game_id"));
                            matkasObjects.setStatus(jsonObject.getString("status"));
                            matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
                            matkasObjects.setPlay_for(jsonObject.getString("play_for"));
                            matkasObjects.setPlay_on(jsonObject.getString("play_on"));
                            matkasObjects.setDay(jsonObject.getString("day"));

                            list.add(matkasObjects);


                        }
                        bidHistoryAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(Starline_Activity.this,"There is no history for this game",Toast.LENGTH_LONG).show();
//                        Log.e("Volley",error.toString());
                        progressDialog.dismiss();
                    }
                }


                //  Toast.makeText(BidActivity.this,response,Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Starline_Activity.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();
//                        Log.e("Volley",error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();

                params.put("us_id",user_id);
                params.put("matka_id",matka_id);


                // params.put("phonepay",phonepaynumber);


                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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







//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import in.games.ChiragMatka.Adapter.StarlinehistoryAdapter;
//import in.games.ChiragMatka.Common.Common;
//import in.games.ChiragMatka.Config.BaseUrl;
//import in.games.ChiragMatka.Model.Starline_History_Objects;
//import in.games.ChiragMatka.Prevalent.Prevalent;
//import in.games.ChiragMatka.utils.CustomVolleyJsonArrayRequest;
//import in.games.ChiragMatka.utils.LoadingBar;
//import in.games.ChiragMatka.utils.SessionMangement;
//
//import static in.games.ChiragMatka.Config.Constants.KEY_ID;
//import static in.games.ChiragMatka.URLs.Starline_Histry_Url;
//
//
//public class Starline_Activity extends AppCompatActivity {
//  Common common;
//  SessionMangement sessionMangement;
//    private ListView recyclerView;
//    ArrayList<Starline_History_Objects> list;
//   LoadingBar progressDialog;
//    private StarlinehistoryAdapter bidHistoryAdapter;
//    private TextView bt_back;
//    private String user_id;
//    private String matka_id;
//    WebView browser ;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_starline_);
//        sessionMangement = new SessionMangement(Starline_Activity.this);
//        common=new Common(Starline_Activity.this);
//        matka_id="19";
//        browser = findViewById(R.id.bid_histry_webview);
//        progressDialog=new LoadingBar(Starline_Activity.this);
//
//        recyclerView=(ListView) findViewById(R.id.recyclerView);
//        bt_back = (TextView) findViewById(R.id.txt_back);
//        bt_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                finish();
//            }
//        });
//
//        //   user_id= "3";
////        user_id= Prevalent.currentOnlineuser.getId().toString().trim();
//        user_id= sessionMangement.getUserDetails().get(KEY_ID).toString().trim();
//        //recyclerView.setHasFixedSize(false);
//        //layoutManager= new LinearLayoutManager(this);
//        //recyclerView.setLayoutManager(layoutManager);
//        list=new ArrayList();
////        getStarlineBidData(user_id,"20");
//        //user_id= Prevalent.currentOnlineuser.getId().toString().trim();
//
//        bidHistoryAdapter=new StarlinehistoryAdapter(this,list);
//        recyclerView.setAdapter(bidHistoryAdapter);
//
//        // getMatkaData();
//
//        String url=BaseUrl.Starline_Histry_Url+user_id;
//        Log.e("asdasd",""+url);
//        browser.loadUrl(url);
//        browser.getSettings().setLoadsImagesAutomatically(true);
//        browser.getSettings().setJavaScriptEnabled(true);
//        browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        browser.setWebViewClient(new myWebViewClient());
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        //setSessionTimeOut(Starline_Activity.this);
//    }
//
//
//
//
//    private class myWebViewClient extends WebViewClient {
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
//            view.loadUrl(Url);
////            txt.setText(Url);
//            return true;
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
////            progressDialog.show();
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
////            progressDialog.dismiss();
//        }
//    }
//
//}
