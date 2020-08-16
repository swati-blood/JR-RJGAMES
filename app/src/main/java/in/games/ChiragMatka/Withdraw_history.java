package in.games.ChiragMatka;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import in.games.ChiragMatka.Adapter.Withdraw_request_Adapter;
import in.games.ChiragMatka.Config.BaseUrl;
import in.games.ChiragMatka.Model.Withdraw_requwset_obect;
import in.games.ChiragMatka.Prevalent.Prevalent;
import in.games.ChiragMatka.utils.LoadingBar;

public class Withdraw_history extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<Withdraw_requwset_obect> list;
    LoadingBar progressDialog;
    RecyclerView.LayoutManager layoutManager;
    TextView txtBack;
    Withdraw_request_Adapter request_historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_history);
        progressDialog=new LoadingBar(Withdraw_history.this);

        list=new ArrayList();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtBack=(TextView)findViewById(R.id.txtBack);

        String User_id= Prevalent.currentOnlineuser.getId();
        getRequestData(User_id);


        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //setSessionTimeOut(Withdraw_history.this);
    }

    private void getRequestData(final String user_id) {

        progressDialog.show();

        list.clear();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, BaseUrl.Url_req_history, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("points_histry",response.toString());

                if(response.equals("empty"))
                {
                    progressDialog.dismiss();

                    Toast.makeText(Withdraw_history.this,"empty",Toast.LENGTH_LONG).show();
//                        Log.e("Volley",error.toString());

                }
                else
                {
                    try
                    {
                        JSONObject obj=new JSONObject(response);
                        JSONArray jsonArray=obj.getJSONArray("data");
                        //progressDialog.dismiss();
                        for(int i=0; i<jsonArray.length();i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Withdraw_requwset_obect matkasObjects = new Withdraw_requwset_obect();
                            matkasObjects.setId(jsonObject.getString("request_id"));
                            matkasObjects.setWithdraw_points(jsonObject.getString("request_points"));
                            matkasObjects.setTime(jsonObject.getString("time"));
                            matkasObjects.setWithdraw_status(jsonObject.getString("request_status"));
                            matkasObjects.setUser_id(jsonObject.getString("user_id"));
                            matkasObjects.setType(jsonObject.getString("type"));
                            list.add(matkasObjects);
                        }
                        request_historyAdapter=new Withdraw_request_Adapter(Withdraw_history.this,list);
                        //matakListViewAdapter=new MatakListViewAdapter(this,matkaList);
                        recyclerView.setAdapter(request_historyAdapter);

                        request_historyAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        Toast.makeText(Withdraw_history.this,"There is no history",Toast.LENGTH_LONG).show();
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

                        Toast.makeText(Withdraw_history.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();
//                        Log.e("Volley",error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();

                params.put("user_id",user_id);



                // params.put("phonepay",phonepaynumber);


                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
