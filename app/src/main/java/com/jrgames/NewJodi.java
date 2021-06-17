package com.jrgames;

import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jrgames.Adapter.NewPointsAdapter;
import com.jrgames.Common.Common;
import com.jrgames.Intefaces.VolleyCallBack;
import com.jrgames.Model.TableModel;
import com.jrgames.Prevalent.Prevalent;

import com.jrgames.utils.LoadingBar;
import com.jrgames.utils.SessionMangement;

import static com.jrgames.Adapter.PointsAdapter.is_error;
import static com.jrgames.Config.Constants.KEY_ID;
import static com.jrgames.Objects.sp_input_data.group_jodi_digits;

public class NewJodi extends AppCompatActivity implements View.OnClickListener {
    Common common;
    SessionMangement sessionMangement;
    List<TableModel> list;
List<String> digit_list ;
    private int val_p=0;
    public static Button btnSubmit,btnReset,btnGameType;
    TextView bt_back;
    TextView txtMatka ,txtboard;
    private EditText etDgt,etPnt;
    String matName="";
    private EditText etPoints;
    LoadingBar progressDialog;
    private TextView txtWallet_amount;
    private String game_id;
    private String m_id ,end_time,start_time ,bet_type;
    private Dialog dialog;
    int stat = 0 ;
    private TextView txtOpen,txtClose ,txt_timer,tv_timer,tv_star_time;
    RecyclerView rv_digits ;
    NewPointsAdapter pointsAdapter ;
   String dashName;

    public static ArrayList<TableModel> bet_list,tempList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_jodi);
        sessionMangement = new SessionMangement(NewJodi.this);
        common=new Common(NewJodi.this);
        dashName=getIntent().getStringExtra("matkaName");
        game_id=getIntent().getStringExtra("game_id");
        m_id=getIntent().getStringExtra("m_id");
        bet_type=getIntent().getStringExtra("m_type");
        end_time = getIntent().getStringExtra("end_time");
        start_time= getIntent().getStringExtra("start_time");
        list=new ArrayList<>();
       rv_digits = findViewById(R.id.jodi_points);
        etPoints=(EditText)findViewById(R.id.etPoints);
        txt_timer = findViewById(R.id.timer);
        tv_timer= findViewById(R.id.tv_timer);
        tv_star_time= findViewById(R.id.star_time);
        btnGameType=(Button)findViewById(R.id.btnBetStatus);
        txtMatka=(TextView)findViewById(R.id.matkaname);
        txtboard=(TextView)findViewById(R.id.board);
        txtMatka.setText(dashName);
        bet_list=new ArrayList<>();
        tempList=new ArrayList<>();
//        txtMatka.setText(dashName.toString()+"- Single Digit Board");
        txtboard.setText(dashName.toString()+"- Single Digit Board");
        progressDialog=new LoadingBar(NewJodi.this);
        txtWallet_amount=(TextView)findViewById(R.id.wallet_amount);
        bt_back=(TextView)findViewById(R.id.txtBack);
        btnSubmit = findViewById(R.id.btn_sbmit);
        btnReset = findViewById(R.id.btnreset);
        txtboard.setSelected(true);
//        txtMatka.setText(dashName.toString()+"- Jodi Board");
        bt_back.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
       digit_list =  Arrays.asList(group_jodi_digits);
        pointsAdapter = new NewPointsAdapter(digit_list,NewJodi.this);
        rv_digits.setNestedScrollingEnabled(false);
        rv_digits.setLayoutManager(new GridLayoutManager(this,2));
        rv_digits.setAdapter(pointsAdapter);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        String cur_time = format.format(date);

        try {
            Date s_date = format.parse(start_time);
            Date e_date = format.parse(end_time);
            Date c_date = format.parse(cur_time);
            common.setCounterTimer( common.getTimeDifference(start_time),txt_timer);
            common.setEndCounterTimer( common.getTimeDifference(end_time),tv_timer);

            if (c_date.before(s_date))
            {
                if(tv_timer.getVisibility()==View.VISIBLE)
                {
                    tv_timer.setVisibility(View.GONE);
                }
                txt_timer.setVisibility(View.VISIBLE);
//                txt_timer.setVisibility(View.VISIBLE);

            }
            else if (c_date.before(e_date) && c_date.after(s_date))
            {
                if(tv_timer.getVisibility()==View.VISIBLE)
                {
                    tv_timer.setVisibility(View.GONE);

                }

                txt_timer.setVisibility(View.VISIBLE);
                txt_timer.setText("Bid closed");

//                txt_timer.setVisibility(View.GONE);

            }
            else if (c_date.after(e_date))
            {
                if(tv_timer.getVisibility()==View.VISIBLE)
                {
                    tv_timer.setVisibility(View.GONE);
                }
                txt_timer.setText("Bid Closed");
            }
            Log.e("date",s_date +"\n"+e_date +"\n"+c_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }





    }

    @Override
    protected void onStart() {
        super.onStart();
//        common.setWallet_Amount(txtWallet_amount,progressDialog, Prevalent.currentOnlineuser.getId());
        common.setWallet_Amount(txtWallet_amount,progressDialog, sessionMangement.getUserDetails().get(KEY_ID));
//        details.setBetDateDay(JodiDigitActivity.this,m_id,btnGameType,progressDialog);
//        getBetDateDay(m_id);


        int m = Integer.parseInt(m_id.toString());
        if (m > Prevalent.Matka_count) {
            Date date = new Date();
             SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy EEEE");
            String ctt=dateFormat.format(date);
                if (common.getTimeDifference(start_time)>0)
            {
                btnGameType.setText(ctt+" "+"Bet Open");
            }
            else
            {
                btnGameType.setText(ctt+" "+"Bet Close");
            }
            txt_timer.setVisibility(View.GONE);
            tv_timer.setVisibility(View.GONE);
            tv_star_time.setVisibility(View.VISIBLE);
            tv_star_time.setText(common.changeTimeFormat(start_time));
//            common.getStarlineGameData(String.valueOf(m), btnGameType, progressDialog);
            // btnType.setText("5:00");
            btnGameType.setClickable(false);
            stat = 1;
//            btnType.setClickable(false);
//            common.setWallet_Amount(txtWallet_amount, progressDialog, Prevalent.currentOnlineuser.getId());
            common.setWallet_Amount(txtWallet_amount, progressDialog, sessionMangement.getUserDetails().get(KEY_ID));
        } else {
            stat = 2;
//            common.setWallet_Amount(txtWallet_amount, progressDialog, Prevalent.currentOnlineuser.getId());
            common.setWallet_Amount(txtWallet_amount, progressDialog, sessionMangement.getUserDetails().get(KEY_ID));
            common.getBetSession(m_id, progressDialog, new VolleyCallBack() {
                @Override
                public void getTimeDiffrence(HashMap<String, String> map) {

                    long s_diff=Long.parseLong(map.get("s_diff").toString());
                    long e_diff=Long.parseLong(map.get("e_diff").toString());
                    Date c_dat=new Date();
                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy EEEE");
                    String s_dt=dateFormat.format(c_dat);
                    btnGameType.setText(s_dt+" Bet " +bet_type.toUpperCase());
//                if(e_diff>0)
//                {
//
//                    btnGameType.setText(s_dt+" Bet Open");
//                }
//                else
//                {
//                    btnGameType.setText(s_dt+" Bet Close");
//                }


                    progressDialog.dismiss();
                }
            });




        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtBack)
        {
            finish();
        }
        else if (id == R.id.btn_sbmit) {
            tempList.clear();
            for(int k=0; k<bet_list.size();k++)
            {
                if(bet_list.get(k).getPoints().toString().equals("0") || bet_list.get(k).getPoints().toString().equals(""))
                { }
                else
                {

                    tempList.add(bet_list.get(k));

                }
            }
            for(TableModel model:tempList){
                Log.e("temp_data",""+model.getDigits()+" - "+model.getPoints());
            }
            if (tempList.size()<=0) {
                Toast.makeText(NewJodi.this, "Please enter some points", Toast.LENGTH_LONG).show();
            } else {
                if (is_error) {
                    Toast.makeText(NewJodi.this, "Minimum bid amount is 10", Toast.LENGTH_LONG).show();
                } else {
                    String dt = btnGameType.getText().toString().trim();
                    String d[] = dt.split(" ");

                    String c = d[0].toString();
                    String w = txtWallet_amount.getText().toString().trim();
                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                    String cur_time = format.format(date);

                    try {
                        Date s_date = format.parse(start_time);
                        Date e_date = format.parse(end_time);
                        Date c_date = format.parse(cur_time);
                        long difference = c_date.getTime() - s_date.getTime();
                        long as = (difference / 1000) / 60;

                        long diff_close = c_date.getTime() - e_date.getTime();
                        long curr = (diff_close / 1000) / 60;
                        long current_time = c_date.getTime();
//                       Log.e("timmmmmmm",""+common.getTimeDifference(start_time)) ;

//                            if (as < 0) {
//
//                                common.setBidsDialog(Integer.parseInt(w), list, m_id, c, game_id, w, dashName, progressDialog, btnSubmit, start_time, end_time);
////                                clearCntrls();
//                            }
//
//                          else  if (curr < 0) {
//                                common.setBidsDialog(Integer.parseInt(w), list, m_id, c, game_id, w, dashName, progressDialog, btnSubmit, start_time, end_time);
////                                clearCntrls();
//                            } else {
//                                common.errorMessageDialog("Betting is Closed Now");
//                            }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long bidTime=common.getTimeDifference(start_time);
if(bidTime>0)
{
    common.setBidsDialog(Integer.parseInt(w), tempList, m_id, c, game_id, w, dashName, progressDialog, btnSubmit, start_time, end_time);

}
else
{
  common.errorMessageDialog("BID IS CLOSED");
}

//                    list.clear();
                }
            }

        }
        else if (id == R.id.btnreset)
        {

        }
    }
}
