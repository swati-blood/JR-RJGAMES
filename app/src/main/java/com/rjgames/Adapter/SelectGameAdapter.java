package com.rjgames.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.rjgames.CyclePana;
import com.rjgames.DoublePanaActivity;
import com.rjgames.FullSangamActivity;
import com.rjgames.HalfSangamActivity;
import com.rjgames.Model.GameModel;
import com.rjgames.NewJodi;
import com.rjgames.NewSingleDigit;

import com.rjgames.R;
import com.rjgames.SinglePannaActivity;
import com.rjgames.TriplePanaActivity;


public class SelectGameAdapter extends RecyclerView.Adapter<SelectGameAdapter.ViewHolder> {
    Activity activity;
    ArrayList<GameModel> game_list;
    String matka_id,matka_name,start_time,end_time, s_num,num,e_num;
    Animation animation ;
    LayoutAnimationController controller;
    public SelectGameAdapter(Activity activity, ArrayList<GameModel> game_list, String matka_id, String matka_name, String start_time, String end_time, String s_num, String num, String e_num) {
        this.activity = activity;
        this.game_list = game_list;
        this.matka_id = matka_id;
        this.matka_name = matka_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.s_num = s_num;
        this.num = num;
        this.e_num = e_num;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(activity).inflate(R.layout.row_games,null);
     ViewHolder holder = new ViewHolder(view);
     return holder;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final GameModel model = game_list.get(position);
        if(model.getId().equals("0")){
            holder.itemView.setVisibility(View.INVISIBLE);
        }
       holder.game_name.setText(model.getName());
       /*switch (position%5)
       {
           case 0 :
               holder.game_name.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.play1)));
               break;
           case 1 :
               holder.game_name.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.play4)));
               break;
           case 2 :
               holder.game_name.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.play2)));
               break;
           case 3 :
               holder.game_name.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.rates_color)));
               break;
           case 4 :
               holder.game_name.setBackgroundTintList(ColorStateList.valueOf(activity.getResources().getColor(R.color.play3)));
               break;
       }*/

holder.game_name.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (model.getId().equals("0")) {

        } else {
            Intent i = null;
            switch (model.getName().trim()) {
                case "Half Sangam":
                    i = new Intent(activity, HalfSangamActivity.class);
                    break;
                case "Open Single":
                case "Close Single":
                    i = new Intent(activity, NewSingleDigit.class);
                    break;
                case "Jodi":
                    i = new Intent(activity, NewJodi.class);
                    break;
                case "Open Cycle \n Patti":
                case "Close Cycle \n Patti":
                    i = new Intent(activity, CyclePana.class);
                    break;
                case "Open Single Patti":
                case "Close Single Patti":
                    i = new Intent(activity, SinglePannaActivity.class);
                    break;
                case "Open Double Patti":
                case "Close Double Patti":
                    i = new Intent(activity, DoublePanaActivity.class);
                    break;
                case "Open Triple Patti":
                case "Close Triple Patti":
                    i = new Intent(activity, TriplePanaActivity.class);
                    break;

                case "Full Sangam":
                    i = new Intent(activity, FullSangamActivity.class);
                    break;

//
            }
            if (i != null) {
                i.putExtra("game_name", model.getName());
                i.putExtra("game_id", model.getId());
                i.putExtra("matkaName", matka_name);
                i.putExtra("m_id", matka_id);
                i.putExtra("end_time", end_time);
                i.putExtra("start_time", start_time);
                i.putExtra("m_type", model.getType());
                activity.startActivity(i);
            }
        }
    }

});
    }

    @Override
    public int getItemCount() {
        return game_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView game_name ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            game_name = itemView.findViewById(R.id.game_name);
//


        }
    }
}
