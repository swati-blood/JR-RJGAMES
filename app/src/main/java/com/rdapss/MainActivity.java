package com.rdapss;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.rdapss.Common.Common;
import com.rdapss.Config.BaseUrl;
import com.rdapss.Model.UsersObjects;
import com.rdapss.Prevalent.Prevalent;

import com.rdapss.utils.CustomJsonRequest;
import com.rdapss.utils.Module;
import com.rdapss.utils.SessionMangement;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    boolean doubleBackToExitPressedOnce=false;
  TextInputEditText etName,etPassword;
  TextInputLayout lay_u_name ,lay_pass;
   Common common;
   Module module ;
   SessionMangement sessionMangement;
    Button btn_login,btn_loginWithMPin;
    ProgressDialog progressDialog;
    private Dialog dialog;
    private EditText edtEmail,edtEmailId;
    public static String mainName="";

    private Button btnRegister,btnForgetPassword,btnForgetUserID;
    private Button btnForPassword,btnForUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionMangement=new SessionMangement(MainActivity.this);

        etName=findViewById(R.id.etUser);
        etPassword=findViewById(R.id.etPass);
        lay_pass=findViewById(R.id.lay_pass);
       lay_u_name=findViewById(R.id.lay_user_name);
        common=new Common(MainActivity.this);
        module = new Module();
        btnRegister=(Button)findViewById(R.id.login_register_btn);
        btnForPassword=(Button)findViewById(R.id.btnForgetPass);
        btnForUserID=(Button)findViewById(R.id.btnForgetUserId);

        boolean sdfff=isConnected(MainActivity.this);
        if(sdfff==true)
        {
           // Snackbar.make(findViewById(R.id.main_layout),"Network Status: ",Snackbar.LENGTH_INDEFINITE).show();
        }
        else
        {
            Snackbar.make(findViewById(R.id.main_layout),"No Internet Connection",Snackbar.LENGTH_INDEFINITE).show();
        }

       // final String[] net = new String[1];
        IntentFilter intentFilter=new IntentFilter(NetworkStateChangeReciever.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                boolean isNetworkAvailable=intent.getBooleanExtra(NetworkStateChangeReciever.IS_NETWORK_AVAILABLE,false);
                String netwotkStatus=isNetworkAvailable ? "connected" : "disconnected";

                if(netwotkStatus.equals("disconnected"))
                {
                    Snackbar.make(findViewById(R.id.main_layout),"No Internet Connection",Snackbar.LENGTH_INDEFINITE).show();
                }
                else
                {
                    Snackbar.make(findViewById(R.id.main_layout),"Connected",Snackbar.LENGTH_LONG).show();
                }
         //       net[0] =netwotkStatus;


              //  Toast.makeText(MainActivity.this,""+netwotkStatus,Toast.LENGTH_LONG).show();

            }
        },intentFilter);



       // Snackbar.make(findViewById(R.id.main_layout),"Network Status :"+sd,Snackbar.LENGTH_LONG).show();



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });


        progressDialog=new ProgressDialog(MainActivity.this);
//        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading...");
        btn_login=(Button) findViewById(R.id.login_login_btn);
        btn_loginWithMPin=(Button)findViewById(R.id.login_mpilogin_btn);

//        btn_loginWithMPin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//           Intent intent=new Intent(MainActivity.this,LoginWithMpinActivity.class);
//                startActivity(intent);
//
//
//            }
//        });

        btnForPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //                dialog=new Dialog(MainActivity.this);
        //                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //                dialog.setContentView(R.layout.dialog_forget_pass_layout);
        //                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //                btnForgetPassword=(Button)dialog.findViewById(R.id.forget_password);
        //                edtEmail=(EditText)dialog.findViewById(R.id.etForget_email);
        //
        //                dialog.setCanceledOnTouchOutside(false);
        //                dialog.show();
        //
        //
        //                btnForgetPassword.setOnClickListener(new View.OnClickListener() {
        //                    @Override
        //                    public void onClick(View v) {
        //
        //
        //                        if(TextUtils.isEmpty(edtEmail.getText().toString()))
        //                        {
        //                            edtEmail.setError("Enter registered Email Id");
        //                            edtEmail.requestFocus();
        //                            return;
        //                        }
        //                        else
        //                        {
        //                            String mail=edtEmail.getText().toString().trim();
        //                            getPassword(mail);
        //                        }
        //
        //                    }
        //                });
        //
                Intent intent = new Intent(MainActivity.this,VerificationActivity.class);
                intent.putExtra("type","f");
                startActivity(intent);
            }
        });


        btnForUserID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog=new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.setContentView(R.layout.dialog_forget_userid_layout);
                btnForgetPassword=(Button)dialog.findViewById(R.id.forget_userid);
                edtEmailId=(EditText)dialog.findViewById(R.id.etForget_email);

                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                btnForgetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(TextUtils.isEmpty(edtEmailId.getText().toString()))
                        {
                            edtEmailId.setError("Enter registered Email Id");
                            edtEmailId.requestFocus();
                            return;
                        }
                        else
                        {
//                            String mail=edtEmailId.getText().toString().trim();
//                            getUserId(mail);
                        }

                    }
                });


            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                   etName.setText("8081031624");
//                   etPassword.setText("111111");
                    String mName=etName.getText().toString().trim();
                    String mPass=etPassword.getText().toString().trim();
                if(!module.validateEditText(etName,lay_u_name))
                    return;
                if(!module.validateEditText(etPassword,lay_pass))
                    return;
                else{
                        mainName=mName;
                        getUserLoginRequest(mName,mPass);
                    }
                }



//                Intent intent=new Intent(MainActivity.this,GameActivity.class);
//                startActivity(intent);

        });
    }

    private void getUserLoginRequest(final String mName, final String mPass) {


        progressDialog.show();
        final String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("mobileno",mName);
        params.put("password",mPass);

//        final CustomJsonRequest loginRequest=new CustomJsonRequest(Request.Method.POST, URLs.URL_USER_LOGIN, params, new Response.Listener<JSONObject>() {
        final CustomJsonRequest loginRequest=new CustomJsonRequest(Request.Method.POST, BaseUrl.URL_LOGIN, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.e("login",response.toString());

                try {
                    JSONObject object = response;
                    boolean status = object.getBoolean("responce");
                    if (status) {
                        JSONObject jsonObject = object.getJSONObject("data");

                        sessionMangement.createLoginSession(jsonObject.getString("id"),jsonObject.getString("name"),jsonObject.getString("username"),
                                jsonObject.getString("mobileno"),jsonObject.getString("email"),jsonObject.getString("dob"),jsonObject.getString("address"),jsonObject.getString("city"),
                                jsonObject.getString("pincode"),jsonObject.getString("accountno"),jsonObject.getString("bank_name"),jsonObject.getString("ifsc_code"),jsonObject.getString("account_holder_name"),
                                jsonObject.getString("paytm_no"),jsonObject.getString("tez_no"),jsonObject.getString("phonepay_no"),jsonObject.getString("wallet"));


                        UsersObjects users = new UsersObjects();
                        users.setId(common.chechNull(jsonObject.getString("id")));
                        users.setName(common.chechNull(jsonObject.getString("name")));
                        users.setUsername(common.chechNull(jsonObject.getString("username")));
                        users.setMobileno(common.chechNull(jsonObject.getString("mobileno")));
                        users.setAddress(common.chechNull(jsonObject.getString("address")));
                        users.setCity(common.chechNull(jsonObject.getString("city")));
                        users.setPincode(common.chechNull(jsonObject.getString("pincode")));
                        users.setPassword(common.chechNull(jsonObject.getString("password")));
                        users.setAccountno(common.chechNull(jsonObject.getString("accountno")));
                        users.setBank_name(common.chechNull(jsonObject.getString("bank_name")));
                        users.setIfsc_code(common.chechNull(jsonObject.getString("ifsc_code")));
                        users.setAccount_holder_name(common.chechNull(jsonObject.getString("account_holder_name")));
                        users.setPaytm_no(common.chechNull(jsonObject.getString("paytm_no")));
                        users.setTez_no(common.chechNull(jsonObject.getString("tez_no")));
                        users.setPhonepay_no(common.chechNull(jsonObject.getString("phonepay_no")));

                        Prevalent.currentOnlineuser = users;
                        Log.e(TAG, "onResponse: "+ Prevalent.currentOnlineuser.getId());
                        String p = jsonObject.getString("password");
                        if (mPass.equals(p)) {
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("username", jsonObject.getString("username").toString());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            CustomIntent.customType(MainActivity.this, "up-to-bottom");
                            progressDialog.dismiss();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Password is not correct ", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, ""+response.getString("error"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(""+msg);
                    Log.e("error",error.getMessage());
                }

            }
        });
        AppController.getInstance().addToRequestQueue(loginRequest,tag_json_obj);


    }


    
    public boolean isConnected(Context context)
    {
        ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();

        if(netInfo != null && netInfo.isConnectedOrConnecting())
        {
            android.net.NetworkInfo wifi=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobileNet=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobileNet != null && mobileNet.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
            {
                return true;
            }
            else {
                return false;
            }

        }
        else
        {
            return false;
        }
    }

    public AlertDialog.Builder builDialog(Context c)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press OK to exit.. ");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        return builder;
    }


    @Override
    public void onBackPressed() {

        if(doubleBackToExitPressedOnce)
        {
            this.finishAffinity();
            super.onBackPressed();

            return;
        }
        this.doubleBackToExitPressedOnce=true;
        Toast.makeText(MainActivity.this,"Press again for exit",Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        },2000);


    }


    public void getMatkaData()
    {


        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URLs.URL_Matka, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   matkaAdapter.notifyDataSetChanged();


                    Toast.makeText(MainActivity.this,""+response,Toast.LENGTH_LONG).show();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,""+error.getMessage(),Toast.LENGTH_LONG).show();
                           }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);



    }


    public void getStarlineData()
    {


        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URLs.URL_StarLine, new
                Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //   matkaAdapter.notifyDataSetChanged();


                        Toast.makeText(MainActivity.this,""+response,Toast.LENGTH_LONG).show();
                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,""+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);



    }

}
