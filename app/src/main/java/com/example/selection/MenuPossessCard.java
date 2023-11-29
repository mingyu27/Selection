package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class MenuPossessCard extends AppCompatActivity {
    RecyclerView recyclerView;
    //MyAdapter adapter;
    ImageView button;
    ArrayList<FunctionItemCard> items = new ArrayList<>(); //카드 사진, 이름 저장할 ArrayList

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_possess_card);
        button = findViewById(R.id.possess_back_button);

        items.add(new FunctionItemCard("A", R.drawable.kookmin16));
        items.add(new FunctionItemCard("B", R.drawable.kookmin6));
        items.add(new FunctionItemCard("C", R.drawable.kookmin42));
        items.add(new FunctionItemCard("D", R.drawable.shinhancard5));
        items.add(new FunctionItemCard("E", R.drawable.shinhancard22));

        recyclerView = findViewById(R.id.recyclerview);
        //adapter = new MyAdapter(this,items);
        recyclerView.setAdapter(new MyAdapter(this, items));
        //recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPossessCard.this, MainActivity.class));
            }
        });
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemview) {

            super(itemview);
            textView = itemview.findViewById(R.id.textview);
            imageView = itemview.findViewById(R.id.imageview);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        Context context;
        ArrayList<FunctionItemCard> items;

        public MyAdapter(Context context, ArrayList<FunctionItemCard> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d("YJH", "onCreateViewHolder");
            View itemview = LayoutInflater.from(context).inflate(R.layout.itemview, parent, false);
            //itemview.xml은 RecyclerView에 반복해서 띄울 Layout
            MyViewHolder holder = new MyViewHolder(itemview);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            FunctionItemCard item = items.get(position);
            Log.d("YJH", "onBindViewHolder");
            holder.textView.setText(item.getText());
            holder.imageView.setImageResource(item.getImg());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}