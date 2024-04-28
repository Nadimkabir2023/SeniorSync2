package com.example.seniorsync2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Custom adapter class for RecyclerView to display health data
public class hCustomAdapter extends RecyclerView.Adapter<hCustomAdapter.MyViewHolder> {
    // Variables to store the activity context and health data lists
    private Context context;
    Activity activity;
    private ArrayList current_time, low_pressure, high_pressure, heartbeat, before_eat, after_eat;
    // Constructor to initialize context, activity, and data lists
    hCustomAdapter(Activity activity, Context context, ArrayList current_time, ArrayList low_pressure, ArrayList high_pressure, ArrayList heartbeat, ArrayList before_eat, ArrayList after_eat) {
        this.activity =activity;
        this.context = context;
        this.current_time = current_time;
        this.low_pressure = low_pressure;
        this.high_pressure = high_pressure;
        this.heartbeat = heartbeat;
        this.before_eat = before_eat;
        this.after_eat = after_eat;
    }

    // Inflates the row layout from XML when needed
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.h_my_row, parent, false);
        return new MyViewHolder(view);
    }
    // Binds the data to the TextViews in each row
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.current_time_txt.setText(String.valueOf(current_time.get(position)));
        holder.low_pressure_txt.setText(String.valueOf(low_pressure.get(position)));
        holder.high_pressure_txt.setText(String.valueOf(high_pressure.get(position)));
        holder.heartbeat_txt.setText(String.valueOf(heartbeat.get(position)));
        holder.before_eat_txt.setText(String.valueOf(before_eat.get(position)));
        holder.after_eat_txt.setText(String.valueOf(after_eat.get(position)));
        // Set a click listener to open the update activity when a row is clicked
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, hUpdate_health_data.class);
                intent.putExtra("time", String.valueOf(current_time.get(position)));
                intent.putExtra("low", String.valueOf(low_pressure.get(position)));
                intent.putExtra("high", String.valueOf(high_pressure.get(position)));
                intent.putExtra("beat", String.valueOf(heartbeat.get(position)));
                intent.putExtra("before", String.valueOf(before_eat.get(position)));
                intent.putExtra("after", String.valueOf(after_eat.get(position)));
                activity.startActivityForResult(intent,1);
            }
        });

    }
    // Total number of rows
    @Override
    public int getItemCount() {
        return current_time.size();
    }
    // Stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView current_time_txt, low_pressure_txt, high_pressure_txt, heartbeat_txt, before_eat_txt, after_eat_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the instance variables for the TextViews
            current_time_txt = itemView.findViewById(R.id.current_time_txt);
            low_pressure_txt = itemView.findViewById(R.id.low_pressure_txt);
            high_pressure_txt = itemView.findViewById(R.id.high_pressure_txt);
            heartbeat_txt = itemView.findViewById(R.id.heartbeat_txt);
            before_eat_txt = itemView.findViewById(R.id.before_eat_txt);
            after_eat_txt= itemView.findViewById(R.id.after_eat_txt);

            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
