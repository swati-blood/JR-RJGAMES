package in.rdapss;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.rdapss.Adapter.NotificationAdapter;
import in.rdapss.Common.Common;
import in.rdapss.Model.NotificationObjects;
import in.rdapss.rdgames.R;
import in.rdapss.utils.CustomJsonRequest;
import in.rdapss.utils.LoadingBar;

import static in.rdapss.Config.BaseUrl.URL_NOTIFICAITONS;

public class NotificationActivity extends AppCompatActivity {

    Common common;
    Switch aSwitch;
   LoadingBar progressDialog;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<NotificationObjects> list;
    TextView btn_back;
    TextView txtNot,txtSwitch;
    NotificationAdapter notificationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        txtNot = (TextView) findViewById(R.id.m_noti);
        txtSwitch = (TextView) findViewById(R.id.text_notification);
        common = new Common(NotificationActivity.this);
        btn_back = (TextView) findViewById(R.id.txt_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        aSwitch = findViewById(R.id.notification_switch);
        progressDialog = new LoadingBar(NotificationActivity.this);

        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getNotifications();


    }

  private void getNotifications(){
        list.clear();
        progressDialog.show();
        HashMap<String,String> params=new HashMap<>();
      CustomJsonRequest request=new CustomJsonRequest(Request.Method.POST, URL_NOTIFICAITONS, params, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
              progressDialog.dismiss();
              try{
                  if(response.getBoolean("responce")){
                      Gson gson=new Gson();
                      Type type=new TypeToken<List<NotificationObjects>>(){}.getType();
                      list=gson.fromJson(response.getJSONArray("data").toString(),type);
                      notificationAdapter=new NotificationAdapter(NotificationActivity.this,list);
                      recyclerView.setAdapter(notificationAdapter);
                      notificationAdapter.notifyDataSetChanged();

                    }else{
                      common.showToast(""+response.getString("error"));
                  }

              }catch (Exception ex){

              }

          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              progressDialog.dismiss();
              common.showVolleyError(error);
          }
      });
      AppController.getInstance().addToRequestQueue(request);
  }

}
