package in.games.ChiragMatka;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.cardview.widget.CardView;

import android.text.Html;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import in.games.ChiragMatka.Common.Common;
import in.games.ChiragMatka.Config.BaseUrl;
import in.games.ChiragMatka.Model.MatkaObject;
import in.games.ChiragMatka.Prevalent.Prevalent;
import in.games.ChiragMatka.fragments.HomeFragment;
import in.games.ChiragMatka.utils.CustomJsonRequest;
import in.games.ChiragMatka.utils.CustomSlider;
import in.games.ChiragMatka.utils.CustomVolleyJsonArrayRequest;
import in.games.ChiragMatka.utils.LoadingBar;
import in.games.ChiragMatka.utils.Module;
import maes.tech.intentanim.CustomIntent;

import static in.games.ChiragMatka.Config.BaseUrl.IMG_STARLINE_URL;
import static in.games.ChiragMatka.splash_activity.home_text;
import static in.games.ChiragMatka.splash_activity.message;
import static in.games.ChiragMatka.splash_activity.withdrw_no;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    FrameLayout frame_home;
    private TextView txtWallet,txtNotification ,txt_tagline;
    TextView tv_vername,tv_name;
    NavigationView navigationView;
//    Module module;
    private Menu nav_menu;
    ArrayList<MatkaObject> list;
    LinearLayout lin_container;
    String chart_url="",no_chart_msg="No Chart Available";
    TextView user_profile_name,txt_admin,tv_admin,txt_coadmin,tv_coadmin,tv_number;
    private Dialog dialog;
    String whatsapp_no="";
    private Button btn_dialog_ok ,btn_add,btn_chart;
    private CardView pgCard,callCard,cardReload;
    private String name="";
    private TextView txtWallet_amount;
    private TextView txtUserName,txtNumber;
    LoadingBar progressDialog;
    Common common;
    RelativeLayout rl_whatsapp;
    public static String mainName="";
    int flag =0 ;
    SliderLayout home_slider;
    ImageView iv_starline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv_vername = findViewById(R.id.tv_version);
        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = getApplicationContext().getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String ver = pkgInfo.versionName;
        tv_vername.setText("Version " +ver);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        nav_menu = navigationView.getMenu();

        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        tv_name = (TextView) header.findViewById(R.id.profile_user_name);
//        module=new Module(HomeActivity.this);
        updatename();

        txtNotification=(TextView)findViewById(R.id.txtNotification);
        txtWallet=(TextView)findViewById(R.id.txtWallet);
        tv_coadmin = findViewById(R.id.tv_coadmin);
        tv_number = findViewById(R.id.tv_number);
        btn_chart = findViewById(R.id.btn_chart);
        txt_tagline = findViewById(R.id.tagline);
        btn_add = findViewById(R.id.add_points);
        lin_container = findViewById(R.id.lin_container);
        frame_home = findViewById(R.id.frame_home);
        txt_admin=findViewById(R.id.txt_admin);
        iv_starline=findViewById(R.id.iv_starline);
        tv_admin=findViewById(R.id.tv_admin);
        txt_coadmin=findViewById(R.id.txt_coadmin);
        rl_whatsapp=findViewById(R.id.rl_whatsapp);
        tv_coadmin=findViewById(R.id.tv_coadmin);
        home_slider=findViewById(R.id.home_slider);
       common=new Common(HomeActivity.this);
        txt_tagline.setText(Html.fromHtml(message.toString()).toString().toUpperCase());
//        txt_game_name.setText(Html.fromHtml(home_text.toString()).toString().toUpperCase());
        tv_admin.setPaintFlags(tv_admin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_coadmin.setPaintFlags(tv_coadmin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        txt_admin.setText(common.getNumbers(home_text.toString())[0]);
//        tv_admin.setText(common.getNumbers(home_text.toString())[1]);
//        txt_coadmin.setText(common.getNumbers(home_text.toString())[2]);
//        tv_coadmin.setText(common.getNumbers(home_text.toString())[3]);
        tv_admin.setOnClickListener(this);
        tv_coadmin.setOnClickListener(this);
        btn_chart.setOnClickListener(this);



        toolbar.setTitleTextColor(getResources().getColor(R.color.txt_color));
       makeSliderRequest();
        boolean sdfff=common.isConnected();
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


            }
        },intentFilter);

        list=new ArrayList<>();

         pgCard=(CardView)findViewById(R.id.cardView3);
        callCard=(CardView)findViewById(R.id.cardView4);
        txtNumber=(TextView)findViewById(R.id.txtNumber);

        common.setMobileNumber(txtNumber);

        progressDialog=new LoadingBar(HomeActivity.this);


        txtNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(HomeActivity.this,NotificationActivity.class);
                startActivity(intent);

            }
        });



        //txtUserName.setText(name.toUpperCase());


            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this,RequestActivity.class);
                    startActivity(intent);

                }
            });

            rl_whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    whatsapp(whatsapp_no,"Hello! Admin ");
                }
            });


        callCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                String number=txtNumber.getText().toString().trim();
                intent.setData(Uri.parse("tel: "+number));
                startActivity(intent);
            }
        });
        pgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, PlayGameActivity.class);

                startActivity(intent);
                CustomIntent.customType(HomeActivity.this, "up-to-bottom");

             //matkaAdapter.notifyItemRemoved();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       navigationView.setItemIconTintList(HomeActivity.this.getResources().getColorStateList(R.color.txt_color));
       txtUserName=(TextView)navigationView.getHeaderView(0).findViewById(R.id.profile_user_name);
       if(Prevalent.currentOnlineuser.getName().isEmpty() || Prevalent.currentOnlineuser.getName().equals(""))
       {

       }
       else {
           txtUserName.setText(Prevalent.currentOnlineuser.getName());
       }
        HomeFragment fm=new HomeFragment();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_home, fm)
                .addToBackStack(null)
                .commit();

        getChartData();

    }

    private void updatename() {
//        tv_name.setText(session_management.getUserDetails().get(KEY_NAME));
        tv_name.setText("Isha");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);

            builder.setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            HomeActivity.super.onBackPressed();
                            finishAffinity();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            Intent intent=new Intent(HomeActivity.this,NotificationActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.action_wallet)
        {
             dialog=new Dialog(HomeActivity.this);
             dialog.setContentView(R.layout.dialog_wallet_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
             btn_dialog_ok=(Button)dialog.findViewById(R.id.wallet_ok);
             txtWallet_amount=(TextView)dialog.findViewById(R.id.wallet_amount);

             dialog.setTitle("Wallet Amount");
             dialog.setCanceledOnTouchOutside(false);
             dialog.show();

             common.setWallet_Amount(txtWallet_amount,progressDialog,Prevalent.currentOnlineuser.getId());

            btn_dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMobileData();
        common.setWallet_Amount(txtWallet,progressDialog,Prevalent.currentOnlineuser.getId());


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
//            Intent intent=new Intent(HomeActivity.this, DrawerProfileActivity.class);
            Intent intent=new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
      }
        else if (id == R.id.nav_home)
        {
            Intent intent=new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
        }
 else if (id == R.id.nav_mpin) {
//            Intent intent=new Intent(HomeActivity.this, DrawerGenMpinActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_how_toPlay) {

            Intent intent=new Intent(HomeActivity.this, DrawerHowToPlayActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_history) {

            Intent intent=new Intent(HomeActivity.this, BidActivity.class);
            intent.putExtra("type","game");
            startActivity(intent);

        }
        else if (id == R.id.nav_s_history) {

            Intent intent=new Intent(HomeActivity.this, Starline_Activity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_point_history) {

            Intent intent=new Intent(HomeActivity.this,Withdraw_history.class);
            intent.putExtra("type","points");
            startActivity(intent);
        }
        else if (id == R.id.nav_funds) {

            Intent intent=new Intent(HomeActivity.this, RequestActivity.class);
            intent.putExtra("type","add");
            startActivity(intent);
        }
        else if (id == R.id.nav_withdrw) {

            Intent intent=new Intent(HomeActivity.this, WithdrawalActivity.class);
            intent.putExtra("type","withdraw");
            startActivity(intent);
        }
        else if (id == R.id.nav_gameRates) {
            Intent intent=new Intent(HomeActivity.this, DrawerGameRates.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_noticeBoard) {
            Intent intent=new Intent(HomeActivity.this, DrawerNoticeBoardActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_logout) {

            AlertDialog.Builder builder=new AlertDialog.Builder(this);

            builder.setMessage("LOGOUT?")
                    .setCancelable(false)
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(HomeActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onClick(View v) {
   if(v.getId()==R.id.tv_admin)
   {
//       if(!(common.getNumbers(home_text.toString())[1].toString().isEmpty()))
//       {
//           whatsapp(common.getNumbers(home_text.toString())[1].toString(),"Hello, Admin!");
//       }

   }
   else if(v.getId()==R.id.tv_coadmin)
   {
//       if(!(common.getNumbers(home_text.toString())[3].toString().isEmpty()))
//       {
//           whatsapp(common.getNumbers(home_text.toString())[3].toString(),"Hello, Co-Admin!");
//       }
   }
   else if(v.getId() == R.id.btn_chart){
         if(chart_url==null || chart_url.isEmpty() || chart_url.equals("")){
             common.showToast(no_chart_msg);
         }else{
           common.clickUrl(chart_url);
         }
   }
    }

    private void makeSliderRequest() {
        HashMap<String,String> params = new HashMap<>();
        CustomJsonRequest req = new CustomJsonRequest(Request.Method.POST, BaseUrl.URL_SLIDERS,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("slider", response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equals("success"))
                            {
                                ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                               JSONObject dataObj=response.getJSONObject("data");
                                Picasso.with(HomeActivity.this).load(IMG_STARLINE_URL+dataObj.getString("starline_img").toString())
                                        .fit().into(iv_starline);
                                JSONArray jsonArray =dataObj.getJSONArray("sliders");
                                for(int i=0;i<jsonArray.length();i++){


                                JSONObject object = jsonArray.getJSONObject(i);

                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("id", object.getString("id"));
                                url_maps.put("title", object.getString("title"));
                                url_maps.put("image", BaseUrl.IMG_SLIDER_URL + object.getString("image"));
                                url_maps.put("description",object.getString("description"));
                                //   Toast.makeText(context,""+modelList.get(position).getProduct_image(),Toast.LENGTH_LONG).show();

                                listarray.add(url_maps);
                                }

                                for (final HashMap<String, String> name : listarray) {
                                    CustomSlider textSliderView = new CustomSlider(HomeActivity.this);
                                    textSliderView.description(name.get("")).image(name.get("image")).setScaleType( BaseSliderView.ScaleType.Fit);
                                    textSliderView.bundle(new Bundle());
                                    textSliderView.getBundle().putString("extra", name.get("title"));
                                    textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                    home_slider.addSlider(textSliderView);



                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HomeActivity.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
                Toast.makeText(HomeActivity.this,""+error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    public void getChartData() {

        String json_tag="json_splash_request";
        HashMap<String,String> params=new HashMap<String, String>();
        CustomVolleyJsonArrayRequest customJsonRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET, BaseUrl.URL_INDEX, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                Log.e("asdasd",""+response.toString());
                try
                {
                    JSONObject dataObj=response.getJSONObject(0);

                    chart_url = dataObj.getString("chart_url");
                    String msg = dataObj.getString("no_chart_msg");
                    if(msg!=null || (!msg.isEmpty()) ||(!msg.equalsIgnoreCase("null"))){
                        no_chart_msg=msg;
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
//                    Toast.makeText(splash_activity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(""+msg);
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customJsonRequest,json_tag);


    }
    public void getMobileData() {

        String json_tag="json_splash_request";
        HashMap<String,String> params=new HashMap<String, String>();
        CustomVolleyJsonArrayRequest customJsonRequest=new CustomVolleyJsonArrayRequest(Request.Method.GET, BaseUrl.URL_MOBILEDATA, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                Log.e("asdasd",""+response.toString());
                try
                {
                    JSONObject dataObj=response.getJSONObject(0);

                    whatsapp_no = dataObj.getString("mobile");
                    tv_number.setText(whatsapp_no.toString());

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
//                    Toast.makeText(splash_activity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                String msg=common.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    common.showToast(""+msg);
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customJsonRequest,json_tag);


    }
}


