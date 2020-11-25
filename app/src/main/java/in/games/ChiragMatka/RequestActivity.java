package in.games.ChiragMatka;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import in.games.ChiragMatka.Common.Common;
import in.games.ChiragMatka.Config.BaseUrl;
import in.games.ChiragMatka.Prevalent.Prevalent;
import in.games.ChiragMatka.utils.CustomJsonRequest;
import in.games.ChiragMatka.utils.CustomVolleyJsonArrayRequest;
import in.games.ChiragMatka.utils.LoadingBar;
import in.games.ChiragMatka.utils.SessionMangement;

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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static in.games.ChiragMatka.Config.BaseUrl.URL_INDEX;
import static in.games.ChiragMatka.Config.BaseUrl.URL_INSERT_REQUEST;
import static in.games.ChiragMatka.Config.Constants.KEY_ID;
import static in.games.ChiragMatka.splash_activity.min_add_amount;

public class RequestActivity extends AppCompatActivity implements PaymentStatusListener {
    Common common;
    SessionMangement sessionMangement;
    EditText etPoints;
    LoadingBar progressDialog;
    private TextView bt_back,txtMatka;
    Button btnRequest;
    private TextView txtWallet_amount;
    String upi_status="";
    private EasyUpiPayment mEasyUpiPayment;
    int min_amount,amt_limit=0 ;
    boolean upi_flag=false;
    String upi="",upi_name="",upi_desc="",upi_type="",transactionId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        sessionMangement = new SessionMangement(RequestActivity.this);
        txtMatka=(TextView)findViewById(R.id.board);
        etPoints=(EditText)findViewById(R.id.etRequstPoints);
        btnRequest=(Button)findViewById(R.id.add_Request);
        progressDialog=new LoadingBar(RequestActivity.this);

        bt_back=(TextView)findViewById(R.id.txtBack);
        txtWallet_amount=(TextView)findViewById(R.id.wallet_amount);
        common=new Common(RequestActivity.this);

        txtMatka.setText("FUNDS");
        getDetails();
        min_amount = amt_limit;

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int points=Integer.parseInt(etPoints.getText().toString().trim());

                if(TextUtils.isEmpty(etPoints.getText().toString()))
                {
                    etPoints.setError("Enter Some Points");
                    return;
                }
                else
                {
                    if(points<amt_limit)
                    {
                        common.errorMessageDialog("Minimum Range for points is "+ amt_limit);

                    }
                    else
                    {

//                        String user_id= Prevalent.currentOnlineuser.getId();
                        String user_id= sessionMangement.getUserDetails().get(KEY_ID);

                        String p=String.valueOf(points);
                        String st="pending";
//                       saveInfoIntoDatabase(user_id,p,st);
                        transactionId = "TXN" + System.currentTimeMillis();
                        String payeeVpa = upi;
                        String payeeName = upi_name;
                        String transactionRefId = transactionId;
                        String description = upi_desc;
                        String amount = p.toString()+".00";
//                        String user_id= common.getUserID();
                        if(upi_status.equals("0")){
                            addRequest(user_id,p,"pending","");

                        }else{
                            payViaUpi(transactionId,payeeVpa,payeeName,transactionRefId,description,amount);

                        }

                    }
                }






            }
        });

//        AddPointsFragment searchFragment=new AddPointsFragment();
//        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.flMain,searchFragment);
//        //  fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();


    }





    @Override
    protected void onStart() {
        super.onStart();
        // setSessionTimeOut(RequestActivity.this);
//        common.setWallet_Amount(txtWallet_amount,progressDialog, Prevalent.currentOnlineuser.getId());
        common.setWallet_Amount(txtWallet_amount,progressDialog, sessionMangement.getUserDetails().get(KEY_ID));
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
                        Toast.makeText(RequestActivity.this,"successfull",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(RequestActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();

                        return;
                    }
                    else
                    {
                        progressDialog.dismiss();

                        Toast.makeText(RequestActivity.this,"Something Wrong",Toast.LENGTH_LONG).show();
                        return;
                    }


                }
                catch (Exception ex)
                {
                    progressDialog.dismiss();

                    Toast.makeText(RequestActivity.this,"Error :"+ex.getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();

                        Toast.makeText(RequestActivity.this,"Error :"+error.getMessage(),Toast.LENGTH_LONG).show();
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
                params.put("type","Add");

                // params.put("phonepay",phonepaynumber);


                return params;
            }

        };

        RequestQueue requestQueue= Volley.newRequestQueue(RequestActivity.this);
        requestQueue.add(stringRequest);
    }

    public void getDetails(){
        progressDialog.show();
        HashMap<String,String> params=new HashMap<>();
        CustomVolleyJsonArrayRequest customVolleyJsonArrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.POST, URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();
                Log.e("respne",""+response);
                try{
//                    if(response.getBoolean("responce")){
                        JSONObject dataObj=response.getJSONObject(0);
                        upi=dataObj.getString("upi");
                        upi_name=dataObj.getString("upi_name");
                        upi_desc=dataObj.getString("upi_desc");
                        upi_type=dataObj.getString("upi_type");
                        upi_status=dataObj.getString("upi_status");
                        amt_limit=Integer.parseInt(dataObj.getString("min_amount").toString());

//                    }else{
//                        common.showToast(response.getString("message"));
//                    }
                }catch (Exception ex){
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
        AppController.getInstance().addToRequestQueue(customVolleyJsonArrayRequest);

    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Log.e("transactionDetails",""+transactionDetails);
        if(transactionDetails.getStatus().equalsIgnoreCase("success"))
        {
//            String user_id= Prevalent.currentOnlineuser.getId();
            String user_id= sessionMangement.getUserDetails().get(KEY_ID);
            addRequest(user_id,transactionDetails.getAmount().toString(),"approved",transactionDetails.getTransactionId().toString());

        }
        else
        {
            common.showToast("Payment Failed.");
        }

    }

    @Override
    public void onTransactionSuccess() {

    }

    @Override
    public void onTransactionSubmitted() {

    }

    @Override
    public void onTransactionFailed() {
        common.showToast("Failed");
    }

    @Override
    public void onTransactionCancelled() {
        common.showToast("Cancelled");
    }

    @Override
    public void onAppNotFound() {
        common.showToast("App Not Found");
    }

    private void payViaUpi(String transactionId, String payeeVpa, String payeeName, String transactionRefId, String description, String amount) {
        // START PAYMENT INITIALIZATION
        upi_flag=true;
        mEasyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                .setPayeeVpa(payeeVpa)
                .setPayeeName(payeeName)
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionRefId)
                .setDescription(description)
                .setAmount(amount)
                .build();

        // Register Listener for Events
        mEasyUpiPayment.setPaymentStatusListener(this);


        switch (upi_type) {
            case "None":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);
                break;
            case "AMAZON_PAY":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.AMAZON_PAY);
                break;
            case "BHIM_UPI":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.BHIM_UPI);
                break;
            case "GOOGLE_PAY":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.GOOGLE_PAY);
                break;
            case "PHONE_PE":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PHONE_PE);
                break;
            case "PAYTM":
                mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PAYTM);
                break;
        }

//        mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);

        // Check if app exists or not
        if (mEasyUpiPayment.isDefaultAppExist()) {
            onAppNotFound();
            return;
        }
        // END INITIALIZATION

        // START PAYMENT
        mEasyUpiPayment.startPayment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(upi_flag){
            mEasyUpiPayment.detachListener();
        }
    }

    public void addRequest(String user_id,String points,String status,String txn_id)
    {
        progressDialog.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("points",points);
        params.put("request_status",status);
        params.put("type","Add");
        params.put("txn_id",txn_id);
        params.put("wallet",txtWallet_amount.getText().toString().trim());
        CustomJsonRequest customJsonRequest=new CustomJsonRequest(Request.Method.POST, URL_INSERT_REQUEST, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String status=response.getString("status");
                    if(status.equals("success"))
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RequestActivity.this,"successfull",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(RequestActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();

                        return;
                    }
                    else
                    {
                        progressDialog.dismiss();

                        Toast.makeText(RequestActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        return;
                    }


                }
                catch (Exception ex)
                {
                    progressDialog.dismiss();

                    Toast.makeText(RequestActivity.this,"Error :"+ex.getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customJsonRequest);
    }


}
