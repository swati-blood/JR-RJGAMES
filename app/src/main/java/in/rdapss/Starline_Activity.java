package in.rdapss;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.rdapss.Adapter.AllHistoryAdapter;

import in.rdapss.Adapter.StarlinehistoryAdapter;
import in.rdapss.Common.Common;

import in.rdapss.Model.HistryModel;
import in.rdapss.rdapss.R;
import in.rdapss.utils.CustomJsonRequest;
import in.rdapss.utils.LoadingBar;
import in.rdapss.utils.RecyclerTouchListener;
import in.rdapss.utils.SessionMangement;

import static in.rdapss.Config.BaseUrl.GET_HISTORY_URL;
import static in.rdapss.Config.BaseUrl.Url_bid_history;
import static in.rdapss.Config.Constants.KEY_ID;


public class Starline_Activity extends AppCompatActivity {

    Common common;
    ArrayList<HistryModel> list;
    ArrayList<String> pageList;
    LoadingBar progressDialog;
    RecyclerView rv_history,rv_pages;
    private TextView bt_back;
    private String user_id;
    private String matka_id;
    int page=1;
    SessionMangement session_management;
    AllHistoryAdapter historyAdapter;
    private StarlinehistoryAdapter bidHistoryAdapter;

//
   PageAdapter pageAdapter;
//    BidActivity.PageAdapter pageAdapter;
    String type="",url="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_starline_);
        setContentView(R.layout.activity_bid);
//       getActionBar().setDisplayHomeAsUpEnabled(true);
        common=new Common(Starline_Activity.this);
//        matka_id="19";
        matka_id=getIntent().getStringExtra("matka_id");
//        browser = findViewById(R.id.bid_histry_webview);
        progressDialog=new LoadingBar(Starline_Activity.this);

//        recyclerView=(ListView) findViewById(R.id.recyclerView);
        rv_pages=findViewById(R.id.rv_pages);
        rv_history=findViewById(R.id.rv_history);
        bt_back=(TextView)findViewById(R.id.txtBack);
//        bt_back = (TextView) findViewById(R.id.txt_back);
        user_id= new SessionMangement(this).getUserDetails().get(KEY_ID);
        list=new ArrayList();
        type=getIntent().getStringExtra("type");
        pageList=new ArrayList();
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }

        });

//        //   user_id= "3";
//        user_id= Prevalent.currentOnlineuser.getId().toString().trim();
//        //recyclerView.setHasFixedSize(false);
//        //layoutManager= new LinearLayoutManager(this);
//        //recyclerView.setLayoutManager(layoutManager);
//        list=new ArrayList();
//        getStarlineBidData(user_id,"20");
//        //user_id= Prevalent.currentOnlineuser.getId().toString().trim();
//
//        bidHistoryAdapter=new StarlinehistoryAdapter(this,list);
//        recyclerView.setAdapter(bidHistoryAdapter);

        rv_history.setLayoutManager(new LinearLayoutManager(this));
        rv_history.setNestedScrollingEnabled(false);
        rv_pages.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        rv_pages.setNestedScrollingEnabled(false);

//        url=URL_BID_HISTORY;
        url=GET_HISTORY_URL ;
        getStarlineBidData(user_id,"20",page);

        rv_pages.addOnItemTouchListener(new RecyclerTouchListener(Starline_Activity.this, rv_pages, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getStarlineBidData(user_id,matka_id,(position+1));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        //setSessionTimeOut(Starline_Activity.this);
//    }
    private void getStarlineBidData(final String user_id, final String matka_id,final int page) {

        final String json_request_tag="json_starline_history_tag";
       progressDialog.show();
        list.clear();
        pageList.clear();
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("page",String.valueOf(page));
        Log.e("parameters",""+params);


        CustomJsonRequest request=new CustomJsonRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("respddee_history",""+response.toString());
                progressDialog.dismiss();
                try{
                    if(response.getBoolean("responce")){
                        Gson gson=new Gson();
                        Type listType=new TypeToken<List<HistryModel>>(){}.getType();
                        list=gson.fromJson(response.getJSONArray("data").toString(),listType);
                        historyAdapter =new AllHistoryAdapter(Starline_Activity.this,list);
                        if(list.size()<=0){
//                            new ToastMsg(BidActivity.this).toastIconError("No History Available");
                            common.showToast("No History Available");
                        }else {
                            int totsize = response.getInt("total_data");
//                     int pageListsize=common.getPageCount(String.valueOf((float)totsize/(float) 10));
                            int pageListsize = getPageCount((float) totsize / (float) 10);
                            for (int j = 1; j <= pageListsize; j++) {
                                pageList.add(String.valueOf(j));
                            }

                            pageAdapter = new Starline_Activity.PageAdapter(pageList, Starline_Activity.this, page);
                            rv_history.setAdapter(historyAdapter);
                            rv_pages.setAdapter(pageAdapter);
                            historyAdapter.notifyDataSetChanged();
                            pageAdapter.notifyDataSetChanged();
                        }

                    }else{
                        common.showToast(response.getString("message"));
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
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

        AppController.getInstance().addToRequestQueue(request);

    }

    private int getPageCount(float v) {
        //3.6
        int cnt=0;
        float ss= (float)(int)v;

        if(ss==v){
            cnt=(int)ss;
        }else{
            cnt=(int)(ss+1);
        }
        return cnt;
    }

//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//        }
//        return true;
//    }
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

                            HistryModel matkasObjects = new HistryModel();
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
//                            matkasObjects.setS_game_time(jsonObject.getString("s_game_time"));
//                            matkasObjects.setPlay_for(jsonObject.getString("play_for"));
//                            matkasObjects.setPlay_on(jsonObject.getString("play_on"));
//                            matkasObjects.setDay(jsonObject.getString("day"));

                            list.add(matkasObjects);


                        }
//                        bidHistoryAdapter = new BidHistoryAdapter(pageList, Starline_Activity.this);
                        rv_history.setAdapter(historyAdapter);
                        rv_pages.setAdapter(pageAdapter);
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

    public class PageAdapter extends RecyclerView.Adapter<Starline_Activity.PageAdapter.ViewHolder>{
        ArrayList<String> list;
        Context context;
        int pg=0;

        public PageAdapter(ArrayList<String> list, Context context,int pg) {
            this.list = list;
            this.context = context;
            this.pg=pg;
        }

        @NonNull
        @Override public Starline_Activity.PageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.row_pages,null);
            return new Starline_Activity.PageAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Starline_Activity.PageAdapter.ViewHolder holder, int position) {
            holder.tv_pages.setText(list.get(position));
            if(position == (pg-1)){
                common.setRelativeTint(holder.rl_main,getResources().getColor(R.color.colorPrimary));
                holder.tv_pages.setTextColor(getResources().getColor(R.color.white));
            }else{
                common.setRelativeTint(holder.rl_main,getResources().getColor(R.color.white));
                holder.tv_pages.setTextColor(getResources().getColor(R.color.black));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout rl_main;
            TextView tv_pages;
            public ViewHolder(@NonNull View v) {
                super(v);
                rl_main=v.findViewById(R.id.rl_main);
                tv_pages=v.findViewById(R.id.tv_pages);

            }
        }
    }

//    private class myWebViewClient extends WebViewClient {
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
//            view.loadUrl(Url);
////            txt.setText(Url);
//            return true;
//        }

//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
////            progressDialog.show();
//        }

//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
////            progressDialog.dismiss();
//        }
    }

//}







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
