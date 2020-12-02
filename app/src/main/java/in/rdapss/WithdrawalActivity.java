 package in.rdapss;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.rdapss.Common.Common;
import in.rdapss.Model.TimeSlots;
import in.rdapss.rdapss.R;
import in.rdapss.utils.CustomJsonRequest;
import in.rdapss.utils.LoadingBar;
import in.rdapss.utils.SessionMangement;

import static in.rdapss.Config.Constants.KEY_ID;
import static in.rdapss.URLs.URL_TIME_SLOTS;
import static in.rdapss.splash_activity.withdrw_no;
import static in.rdapss.splash_activity.withdrw_text;

 public class WithdrawalActivity extends AppCompatActivity {
     Common common;
     SessionMangement sessionMangement;
     private TextView txtback,txtWalletAmount,txtMobile,txt_withdrw_instrctions,tv_number;
     private LoadingBar progressDialog;
     private EditText etPoint;
     private Button btnSave;
     ArrayList<TimeSlots> timeList;
     int amount_limt=0;
     int req_limit=1;
     String text="",no="";
     int wSaturday=0,wSunday=0;
     RelativeLayout rl_whts;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_withdrawal);
         sessionMangement = new SessionMangement(WithdrawalActivity.this);
         common=new Common(WithdrawalActivity.this);
         txtback=(TextView)findViewById(R.id.txtBack);
         tv_number=(TextView)findViewById(R.id.tv_number);
         txtWalletAmount=(TextView)findViewById(R.id.wallet_amount);
         etPoint=(EditText)findViewById(R.id.etRequstPoints);
         btnSave=(Button)findViewById(R.id.add_Request);
         txtMobile=(TextView)findViewById(R.id.textview5);
         txt_withdrw_instrctions = findViewById(R.id.withdrw_msg);
         rl_whts = findViewById(R.id.rl_whts);
         progressDialog=new LoadingBar(WithdrawalActivity.this);
         txt_withdrw_instrctions.setText(withdrw_text.toUpperCase());
         tv_number.setText(withdrw_no.toUpperCase());
         timeList=new ArrayList<>();
         getWithdrawDetails();
         txtback.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {  finish();  }
         });

         rl_whts.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 whatsapp(withdrw_no.toString(),"Hello! Admin ");

             }
         });
         btnSave.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String points=etPoint.getText().toString().trim();

                 if(TextUtils.isEmpty(points))
                 {
                     etPoint.setError("Enter Some points");
                     return;
                 }
                 else
                 {

//                     String user_id = Prevalent.currentOnlineuser.getId();
                     String user_id = sessionMangement.getUserDetails().get(KEY_ID);
                     String pnts = String.valueOf(points);
                     String st = "pending";
                     int w_amt = Integer.parseInt(txtWalletAmount.getText().toString().trim());
                     int t_amt = Integer.parseInt(pnts);
                     if (w_amt > 0) {

                         if(t_amt<amount_limt)
                         {
                             common.errorMessageDialog("Minimum Withdraw amount "+amount_limt+".");
                         }
                         else
                         {
                             if (t_amt > w_amt) {
                                 common.errorMessageDialog("Your requested amount exceeded");
                                 return;
                             } else {
                                 int flg=0;
                                 if(getCurrentDay().equalsIgnoreCase("Sunday"))
                                 {
                                     if(wSunday==1){
                                         flg=1;
                                     }
                                     else{
                                         flg=2;
                                     }
                                 }
                                 else if(getCurrentDay().equalsIgnoreCase("Saturday"))
                                 {
                                     if(wSaturday==1){
                                         flg=3;
                                     }
                                     else{
                                         flg=4;
                                     }
                                 }
                                 else
                                 {
                                     flg=5;
                                 }
                                 if(flg==1 || flg==3 || flg==5){
                                     if(getStartTimeOutStatus(timeList) || getEndTimOutStatus(timeList))
                                     {
                                         saveInfoWithDate(user_id,String.valueOf(t_amt),st);
                                     }
                                     else
                                     {
                                         common.showToast("Timeout");
                                     }

                                 }
                                 else if(flg==2 || flg==4)
                                 {
                                     common.showToast("Withdraw Request is not allowed on "+getCurrentDay());
                                 }
                                 // saveInfoIntoDatabase(user_id, String.valueOf(t_amt), st);

                             }
//
                         }

                     } else {
                         common.errorMessageDialog("You don't have enough points in wallet ");
                     }

                 }


//                        saveInfoIntoDatabase(user_id,pnts,st);
             }




         });

     }

     @Override
     protected void onStart() {
         super.onStart();
         //setSessionTimeOut(WithdrawalActivity.this);
//        details.setWallet_Amount(txtWalletAmount,progressDialog,Prevalent.currentOnlineuser.getId(),WithdrawalActivity.this);
//         common.setWallet_Amount(txtWalletAmount,progressDialog, Prevalent.currentOnlineuser.getId());
         common.setWallet_Amount(txtWalletAmount,progressDialog, sessionMangement.getUserDetails().get(KEY_ID));

     }



     private void saveInfoWithDate(final String user_id, final String points, final String st)
     {
         progressDialog.show();
         Date date=new Date();
         SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
         String dt=dateFormat.format(date);

         String json_request_tag="json_withdraw_request";
         HashMap<String,String> params=new HashMap<String, String>();
         params.put("user_id",user_id);
         params.put("points",points);
         params.put("request_status",st);
         params.put("date",dt);
         params.put("req_limit",String.valueOf(req_limit));
         Log.e("asdasd",""+params.toString());

         CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URLs.URL_WITHDRAW_REQUEST, params, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {

                 Log.d("withdrw_req",response.toString());
                 try
                 {
                     JSONObject jsonObject=response;
                     String status=jsonObject.getString("status");
                     if(status.equals("success"))
                     {

                         String msg=jsonObject.getString("message");
                         progressDialog.dismiss();
                         Intent intent=new Intent(WithdrawalActivity.this,HomeActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         startActivity(intent);
                         finish();
                         common.errorMessageDialog(msg);
                         //Toast.makeText(WithdrawalActivity.this,msg,Toast.LENGTH_LONG).show();

                     }
                     else
                     {
                         progressDialog.dismiss();
                         common.errorMessageDialog(""+jsonObject.getString("message").toString());
                         //Toast.makeText(WithdrawalActivity.this,""+,Toast.LENGTH_LONG).show();
                     }
                 }
                 catch(Exception ex)
                 {
                     progressDialog.dismiss();
                     Toast.makeText(WithdrawalActivity.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 progressDialog.dismiss();

                 common.errorMessageDialog(""+error.getMessage());

             }
         });
         AppController.getInstance().addToRequestQueue(customJsonRequest,json_request_tag);
     }





     private void saveInfoIntoDatabase(final String user_id, final String points, final String st) {

         progressDialog.show();

         StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.Url_data_insert_req, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {

                     JSONObject jsonObject=new JSONObject(response);
                     String status=jsonObject.getString("status");
                     if(status.equals("success"))
                     {
                         progressDialog.dismiss();
                         Toast.makeText(WithdrawalActivity.this,"successfull",Toast.LENGTH_LONG).show();
                         Intent intent=new Intent(WithdrawalActivity.this,HomeActivity.class);
                         startActivity(intent);
                         finish();

                         return;
                     }
                     else
                     {
                         progressDialog.dismiss();

                         Toast.makeText(WithdrawalActivity.this,"Something Wrong",Toast.LENGTH_LONG).show();
                         return;
                     }


                 }
                 catch (Exception ex)
                 {
                     progressDialog.dismiss();

                     Toast.makeText(WithdrawalActivity.this,"Error :"+ex.getMessage(),Toast.LENGTH_LONG).show();
                     return;
                 }

             }
         },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {

                         progressDialog.dismiss();

                         Toast.makeText(WithdrawalActivity.this,"Error :"+error.getMessage(),Toast.LENGTH_LONG).show();
                         return;

                     }
                 }
         )
         {

             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String,String> params=new HashMap<>();

                 params.put("user_id",user_id);
                 params.put("points",points);
                 params.put("request_status",st);
                 params.put("type","withdraw");

                 // params.put("phonepay",phonepaynumber);


                 return params;
             }

         };

         RequestQueue requestQueue= Volley.newRequestQueue(WithdrawalActivity.this);
         requestQueue.add(stringRequest);
     }

     public void  getWithdrawDetails(){
         progressDialog.show();

         HashMap<String,String> params=new HashMap<>();
         timeList.clear();
         CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_TIME_SLOTS, params, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {
                 progressDialog.dismiss();
                 try
                 {
                     Log.e("timerere",""+response.toString());
                     timeList.clear();
                     boolean resp=response.getBoolean("responce");
                     if(resp){
                         Gson gson=new Gson();
                         Type listType=new TypeToken<List<TimeSlots>>(){}.getType();
                         timeList=gson.fromJson(response.getString("data").toString(),listType);
                         amount_limt=Integer.parseInt(response.getString("min_amount"));
                         req_limit=Integer.parseInt(response.getString("withdraw_limit"));
                         wSaturday=Integer.parseInt(response.getString("w_saturday"));
                         wSunday=Integer.parseInt(response.getString("w_sunday"));


                     }
                     else{
                         common.showToast(""+response.getString("message"));
                     }
                 }
                 catch (Exception ex)
                 {
                     ex.printStackTrace();
                 }

             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 progressDialog.dismiss();
                 common.showVolleyError(error);
             }
         });
         AppController.getInstance().addToRequestQueue(customJsonRequest);
     }
     public boolean getStartTimeOutStatus(ArrayList<TimeSlots> list){
         int j=0;
         boolean flag=false;
         SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
         SimpleDateFormat spdfMin=new SimpleDateFormat("mm");
         Date c_date=new Date();
         int chours=Integer.parseInt(simpleDateFormat.format(c_date));
         int cMins=Integer.parseInt(spdfMin.format(c_date));
         for(int i=0; i<list.size();i++){
             int shours=Integer.parseInt(list.get(i).getStart_time().split(":")[0].toString());
             int sMins=Integer.parseInt(list.get(i).getStart_time().split(":")[1].toString());
             if(chours>shours)
             {j=1;
                 flag=true;
                 break;
             }
             else if(chours == shours)
             {
                 if(cMins<=sMins)
                 {
                     j=2;
                     flag=true;
                     break;
                 }
                 else{
                     j=3;
                     flag=false;
                     break;
                 }
             }
         }
//         common.showToast("start_timeout-  "+j);

         return flag;

     }
     public boolean getEndTimOutStatus(ArrayList<TimeSlots> list){
         int j=0;
         boolean flag=false;
         SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
         SimpleDateFormat spdfMin=new SimpleDateFormat("mm");
         Date c_date=new Date();
         int chours=Integer.parseInt(simpleDateFormat.format(c_date));
         int cMins=Integer.parseInt(spdfMin.format(c_date));
         for(int i=0; i<list.size();i++){
             int ehours=Integer.parseInt(list.get(i).getEnd_time().split(":")[0].toString());
             int eMins=Integer.parseInt(list.get(i).getEnd_time().split(":")[1].toString());
             if(chours<ehours)
             {j=1;
                 flag=true;
                 break;
             }
             else if(chours == ehours)
             {
                 if(cMins<=eMins)
                 {
                     j=4;
                     flag=true;
                     break;
                 }
                 else{
                     j=5;
                     flag=false;
                     break;
                 }
             }
         }
//         common.showToast("end_timeout-  "+j);

         return flag;

     }
     public boolean getTimeOutStatus(ArrayList<TimeSlots> list)
     {
         int n=0;
         boolean flag=false;
         SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH");
         SimpleDateFormat spdfMin=new SimpleDateFormat("mm");
         Date c_date=new Date();
         int chours=Integer.parseInt(simpleDateFormat.format(c_date));
         int cMins=Integer.parseInt(spdfMin.format(c_date));
         for(int i=0; i<list.size();i++) {
             int shours = Integer.parseInt(list.get(i).getStart_time().split(":")[0].toString());
             int ehours = Integer.parseInt(list.get(i).getEnd_time().split(":")[0].toString());
             int sMins = Integer.parseInt(list.get(i).getStart_time().split(":")[1].toString());
             int eMins = Integer.parseInt(list.get(i).getEnd_time().split(":")[1].toString());
             if (chours > shours && chours < ehours) {
                 flag = true;
                 n = 1;
                 break;
             } else {
                 if (chours == shours) {
                     if (cMins <= sMins) {
                         flag = true;
                         n = 2;
                         break;
                     } else {
                         flag = false;
                         n = 3;
                         break;
                     }
                 } else if (chours == ehours) {
                     if (cMins <= eMins) {
                         n = 4;
                         flag = true;
                         break;
                     } else {
                         n = 5;
                         flag = false;
                         break;
                     }
                 } else {
                     n = 6;
                 }
                 Log.e("hours", "" + chours + " - " + shours + " - " + ehours);
                 Log.e("minutes", "" + cMins + " - " + sMins + " - " + eMins);
             }
         }

         Log.e("sdas"," - "+n);
         return flag;
     }

     public String getCurrentDay()
     {
         Date date=new Date();
         SimpleDateFormat smdf=new SimpleDateFormat("EEEE");
         String day=smdf.format(date);
         return day;
     }
     @SuppressLint("NewApi")
     public void whatsapp( String phone,String message) {


         PackageManager packageManager = getPackageManager();
         Intent i = new Intent(Intent.ACTION_VIEW);

         try {
             String url = "https://api.whatsapp.com/send?phone=+91"+ phone +"&text=" + URLEncoder.encode(message, "UTF-8");
             i.setPackage("com.whatsapp");
             i.setData(Uri.parse(url));
             if (i.resolveActivity(packageManager) != null) {
                 startActivity(i);
             }
         } catch (Exception e){
             e.printStackTrace();
         }
     }
 }
