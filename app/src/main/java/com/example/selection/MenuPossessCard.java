package com.example.selection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class MenuPossessCard extends AppCompatActivity {
    RecyclerView recyclerView;
    MyAdapter adapter;
    ArrayList<Item> items = new ArrayList<>(); // 카드사진, 이름 저장할 ArrayList
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_possess_card);

        items.add(new Item("A", R.drawable.kookmin16));
        items.add(new Item("B",R.drawable.kookmin6));
        items.add(new Item("C",R.drawable.kookmin42));
        items.add(new Item("D",R.drawable.shinhancard5));
        items.add(new Item("E",R.drawable.shinhancard22));

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new MyAdapter(this,items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemview){

            super(itemview);
            textView = itemview.findViewById(R.id.textview);
            imageView = itemview.findViewById(R.id.imageview);
        }
    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        Context context;
        ArrayList<Item> items;
        public MyAdapter(Context context, ArrayList<Item> items){
            this.context = context;
            this.items = items;
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.d("YJH", "onCreateViewHolder");
            View itemview = LayoutInflater.from(context).inflate(R.layout.itemview,parent,false);
            //itemview.xml은 RecyclerView에 반복해서 띄울 Layout
            MyViewHolder holder = new MyViewHolder(itemview);
            return holder;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
            Item item = items.get(position);
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

class Item{
    private String text;
    private int img;
    public Item(String text, int img){
        this.text = text;
        this.img = img;
    }
    public String getText(){
        return text;
    }
    public int getImg(){
        return img;
    }
}