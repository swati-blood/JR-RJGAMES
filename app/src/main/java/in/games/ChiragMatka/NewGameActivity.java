package in.games.ChiragMatka;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import in.games.ChiragMatka.Adapter.SelectGameAdapter;
import in.games.ChiragMatka.Common.Common;
import in.games.ChiragMatka.Model.GameModel;

public class NewGameActivity extends AppCompatActivity implements View.OnClickListener {
    TextView open_single_p , close_single_p , open_double,close_double ,open_triple , close_triple,open_cylce,close_cylce,halfsngm,fullsngm,jodi ,open_single,close_single;
    TextView bt_back ,txtMatkaName;
    private String dashName;
    RecyclerView rv_games;
    Common common;
    private Toolbar toolbar;
    SelectGameAdapter selectGameAdapter ;

    ArrayList<GameModel> game_list;
    private String m_id ,start_time ,end_time ,start_num , mid_num , end_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        m_id=getIntent().getStringExtra("m_id");
        dashName=getIntent().getStringExtra("matkaName");
        end_time = getIntent().getStringExtra("end_time");
        start_time= getIntent().getStringExtra("start_time");
        start_num= getIntent().getStringExtra("start_num");
        mid_num= getIntent().getStringExtra("num");
        end_num= getIntent().getStringExtra("end_num");
        common=new Common(NewGameActivity.this);
        game_list = new ArrayList<>();
        bt_back=(TextView)findViewById(R.id.txtBack);
        rv_games = findViewById(R.id.rv_games);
        open_cylce = findViewById(R.id.opencyclepatti);
        open_double = findViewById(R.id.opendoublepatti);
        open_single_p= findViewById(R.id.opensinglepatti);
        open_single= findViewById(R.id.opensingle);
        open_triple = findViewById(R.id.opentriplepatti);
        close_cylce = findViewById(R.id.closecyclepatti);
        close_double = findViewById(R.id.closedoublepatti);
        close_single_p = findViewById(R.id.closesinglepatti);
        close_single = findViewById(R.id.closesingle);
        close_triple = findViewById(R.id.closetriplepatti);
        halfsngm = findViewById(R.id.halfsangm);
        fullsngm= findViewById(R.id.fullsangm);
        jodi = findViewById(R.id.jodi);
        toolbar=findViewById(R.id.toolbar);
        txtMatkaName=findViewById(R.id.board);
        txtMatkaName.setText(dashName+ " DASHBOARD");
        txtMatkaName.setSelected(true);

       long t= common.getTimeDifference(start_time);




       if(t<=0)
       {game_list.clear();
         common.setBackTint(open_single);
         common.setBackTint(jodi);
         common.setBackTint(open_single_p);
         common.setBackTint(open_double);
         common.setBackTint(open_triple);
         common.setBackTint(open_cylce);
         common.setBackTint(open_single);
         common.setBackTint(halfsngm);
         common.setBackTint(fullsngm);
         common.setNormalTint(close_single_p);
         common.setNormalTint(close_single);
         common.setNormalTint(close_double);
         common.setNormalTint(close_triple);
         common.setNormalTint(close_cylce);
           game_list.add(new GameModel("2", "Close Single ", R.drawable.logo, "Close"));
//           game_list.add(new GameModel("5", "Close Group Patti", R.drawable.logo, "Close"));
           game_list.add(new GameModel("7", "Close Single Patti", R.drawable.logo, "Close"));
           game_list.add(new GameModel("8", "Close Double Patti", R.drawable.logo, "Close"));
           game_list.add(new GameModel("9", "Close Triple Patti ", R.drawable.logo, "Close"));
           game_list.add(new GameModel("14", "Close Cycle \n Patti ", R.drawable.logo, "Close"));



       }
       else
       {game_list.clear();
           game_list.add(new GameModel("2", "Open Single ", R.drawable.logo, "Open"));
           game_list.add(new GameModel("2", "Close Single ", R.drawable.logo, "Close"));
           game_list.add(new GameModel("3", "Jodi", R.drawable.logo, "Close"));
//           game_list.add(new GameModel("5", "Open Group Patti", R.drawable.logo, "Open"));
//           game_list.add(new GameModel("5", "Close Group Patti", R.drawable.logo, "Close"));
//           game_list.add(new GameModel("6", "Group Jodi", R.drawable.logo, "Close"));
           game_list.add(new GameModel("7", "Open Single Patti", R.drawable.logo, "Open"));
           game_list.add(new GameModel("7", "Close Single Patti", R.drawable.logo, "Close"));
           game_list.add(new GameModel("12", "Half Sangam", R.drawable.logo, "Close"));
           game_list.add(new GameModel("8", "Open Double Patti", R.drawable.logo, "Open"));
           game_list.add(new GameModel("8", "Close Double Patti", R.drawable.logo, "Close"));
           game_list.add(new GameModel("13", "Full Sangam", R.drawable.logo, "Open"));

           game_list.add(new GameModel("9", "Open Triple Patti", R.drawable.logo, "Open"));
           game_list.add(new GameModel("9", "Close Triple Patti", R.drawable.logo, "Close"));
//           game_list.add(new GameModel("0", "Close Triple Patti", R.drawable.logo, "Close"));

           game_list.add(new GameModel("14", "Open Cycle \n Patti ", R.drawable.logo, "Open"));
           game_list.add(new GameModel("14", "Close Cycle \n Patti ", R.drawable.logo, "Close"));
//           game_list.add(new GameModel("0", "Close Cycle \n Patti ", R.drawable.logo, "Close"));
       }
        if (Integer.parseInt(m_id)>15)
        {game_list.clear();
//           Toast.makeText(this,"starlne",Toast.LENGTH_LONG).show();
            common.setBackTint(open_single);
            common.setBackTint(jodi);
            common.setBackTint(fullsngm);
            common.setBackTint(close_single_p);
            common.setBackTint(close_single);
            common.setBackTint(close_double);
            common.setBackTint(close_triple);
            common.setBackTint(close_cylce);
            common.setNormalTint(open_single);
            common.setNormalTint(open_single_p);
            common.setNormalTint(open_double);
            common.setNormalTint(open_triple);
            common.setNormalTint(open_cylce);
            common.setNormalTint(halfsngm);
            game_list.add(new GameModel("2", "Open Single ", R.drawable.logo, "Open"));
            game_list.add(new GameModel("7", "Open Single Patti", R.drawable.logo, "Open"));
            game_list.add(new GameModel("8", "Open Double Patti", R.drawable.logo, "Open"));
            game_list.add(new GameModel("9", "Open Triple Patti", R.drawable.logo, "Open"));
//            game_list.add(new GameModel("12", "Half Sangam", R.drawable.logo, "Close"));
            game_list.add(new GameModel("14", "Open Cycle \n Patti  ", R.drawable.logo, "Open"));


//
        }
        bt_back.setOnClickListener(this);
        open_double.setOnClickListener(this);
        open_cylce.setOnClickListener(this);
        open_single.setOnClickListener(this);
        open_single_p.setOnClickListener(this);
        open_triple.setOnClickListener(this);
        close_triple.setOnClickListener(this);
        close_single.setOnClickListener(this);
        close_single_p.setOnClickListener(this);
        close_double.setOnClickListener(this);
        close_cylce.setOnClickListener(this);
        halfsngm.setOnClickListener(this);
        fullsngm.setOnClickListener(this);
        jodi.setOnClickListener(this);
        rv_games.setLayoutManager(new GridLayoutManager(NewGameActivity.this,3));
        selectGameAdapter = new SelectGameAdapter(NewGameActivity.this,game_list,
                m_id,
              dashName,
                start_time,
              end_time,
                start_num,mid_num,end_num);
        rv_games.setAdapter(selectGameAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id ==R.id.opensinglepatti)
        {
            Intent intent=new Intent(NewGameActivity.this,SinglePannaActivity.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","7");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
          intent.putExtra("m_type","open");
            startActivity(intent);
        }
        else if (id==R.id.opensingle)
        {
            Intent intent=new Intent(NewGameActivity.this,NewSingleDigit.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","2");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
             intent.putExtra("m_type","open");
             startActivity(intent);
        }
        else if (id==R.id.closesingle)
        {
            Intent intent=new Intent(NewGameActivity.this,NewSingleDigit.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","2");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
               intent.putExtra("m_type","close");
            startActivity(intent);
        }
        else if (id==R.id.jodi)
        {
            Intent intent=new Intent(NewGameActivity.this,NewJodi.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","3");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
           intent.putExtra("m_type","close");
            startActivity(intent);
        }
        else if (id ==R.id.opendoublepatti)
        {
            Intent intent=new Intent(NewGameActivity.this,DoublePanaActivity.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","8");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
           intent.putExtra("m_type","open");
            startActivity(intent);
        }
        else if(id ==R.id.opentriplepatti)
        {
            Intent intent=new Intent(NewGameActivity.this,TriplePanaActivity.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","9");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
              intent.putExtra("m_type","open");
            startActivity(intent);
        }
        else if (id ==R.id.opencyclepatti)
        {
            Intent intent=new Intent(NewGameActivity.this,CyclePana.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","14");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
            intent.putExtra("m_type","open");
            startActivity(intent);}
        else if(id ==R.id.halfsangm)
        {
            Intent intent=new Intent(NewGameActivity.this,HalfSangamActivity.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","12");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
            //intent.putExtra("m_type",bet_type);
            startActivity(intent);
        }
        else if (id ==R.id.closedoublepatti)
        {
            Intent intent=new Intent(NewGameActivity.this,DoublePanaActivity.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","8");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
            intent.putExtra("m_type","close");
            startActivity(intent);
        }
        else if (id ==R.id.closesinglepatti)
        {
            Intent intent=new Intent(NewGameActivity.this,SinglePannaActivity.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","7");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
            intent.putExtra("m_type","close");
            startActivity(intent);
        }

        else if (id ==R.id.closetriplepatti)
        {
            Intent intent=new Intent(NewGameActivity.this,TriplePanaActivity.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","9");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
           intent.putExtra("m_type","close");
            startActivity(intent);
        }
        else if (id ==R.id.closecyclepatti)
        {

            Intent intent=new Intent(NewGameActivity.this,CyclePana.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","14");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
           intent.putExtra("m_type","close");
            startActivity(intent);
        }
        else if (id==R.id.fullsangm)
        {
            Intent intent=new Intent(NewGameActivity.this,FullSangamActivity.class);
            intent.putExtra("matkaName",dashName.toString());
            intent.putExtra("game_id","13");
            intent.putExtra("m_id",m_id.toString());
            intent.putExtra("end_time",end_time);
            intent.putExtra("start_time",start_time);
            //intent.putExtra("m_type",bet_type);
            startActivity(intent);
        }
        else if (id == R.id.txtBack)
        {
            finish();
        }

    }
}
