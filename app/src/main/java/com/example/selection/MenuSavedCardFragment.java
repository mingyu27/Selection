
package com.example.selection;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MenuSavedCardFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<Item> items = new ArrayList<>();
    private List<Integer> savedShinhanCardIndexArrayList = new ArrayList<>();
    private List<Integer> savedKookminCardIndexArrayList = new ArrayList<>();
    private String TAG = "SMG";
    private FunctionUser functionUser;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static MenuSavedCardFragment newInstance(FunctionUser functionUser) {
        MenuSavedCardFragment fragment = new MenuSavedCardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("functionUser",functionUser);
        fragment.setArguments(bundle);
        return fragment;
    }
    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview); // itemview.xml에 정의된 ID를 사용합니다.
            imageView = itemView.findViewById(R.id.imageview); // itemview.xml에 정의된 ID를 사용합니다.
        }
    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        Context context;
        ArrayList<Item> items;

        public MyAdapter(Context context, ArrayList<Item> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.itemview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Item item = items.get(position);
            holder.textView.setText(item.getText());
            holder.imageView.setImageResource(item.getImg());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
    class Item {
        private String text;
        private int img;
        private int index;

        public Item(String text, int img,int index) {
            this.text = text;
            this.img = img;
            this.index=index;
        }

        public String getText() {
            return text;
        }

        public int getImg() {
            return img;
        }
        public int getCardIndex() { return index; }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //매장이름 넣기
            //FunctionUser넣기
            try{
                FunctionUser tempUser;
                tempUser = (FunctionUser) getArguments().getSerializable("functionUser");
                if(tempUser != null){
                    functionUser = tempUser;
                    //functionUser을 통해 list값 불러오기
                    savedKookminCardIndexArrayList = functionUser.getSavedKookmin();
                    savedShinhanCardIndexArrayList = functionUser.getSavedShinhan();
                    Log.d(TAG, functionUser.getName() + "at MenuSavedCardFragment");}
                    Log.d("YJH", "Saved Shinhan Card Index List:");
                for (Integer index : savedShinhanCardIndexArrayList) {
                    // Shinhan Card의 이름 리스트
                    String[] shinHanCardNameList = getResources().getStringArray(R.array.shinHanCardNameList);
                    // Shinhan Card의 이미지 리소스 ID 리스트
                    TypedArray shinHanCardImageList = getResources().obtainTypedArray(R.array.shinHanCardImageList);

                    // index에 해당하는 이름과 이미지의 리소스 ID 가져오기
                    String cardName = shinHanCardNameList[index];
                    int cardImageResourceId = shinHanCardImageList.getResourceId(index, -1);

                    // 리소스 사용이 끝났으면 TypedArray 해제
                    shinHanCardImageList.recycle();

                    // Item 객체 생성 및 리스트에 추가
                    items.add(new Item(cardName, cardImageResourceId, index));
                    // Log.d("YJH", String.valueOf(index));
                }
                Log.d("YJH", "Saved Kookmin Card Index List:");
                for (Integer index : savedKookminCardIndexArrayList) {
                    // Shinhan Card의 이름 리스트
                    String[] kookMinCardNameList = getResources().getStringArray(R.array.kookMinCardNameList);
                    // Shinhan Card의 이미지 리소스 ID 리스트
                    TypedArray kookMinCardImageList = getResources().obtainTypedArray(R.array.kookMinCardImageList);

                    // index에 해당하는 이름과 이미지의 리소스 ID 가져오기
                    String cardName = kookMinCardNameList[index];
                    int cardImageResourceId = kookMinCardImageList.getResourceId(index, -1);

                    // 리소스 사용이 끝났으면 TypedArray 해제
                    kookMinCardImageList.recycle();

                    // Item 객체 생성 및 리스트에 추가
                    items.add(new Item(cardName, cardImageResourceId, index));
                    //  Log.d("YJH", String.valueOf(index));
                }
            }catch (NullPointerException e){}

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("YJH", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_menu_saved_card, container, false);
        recyclerView = view.findViewById(R.id.recyclerview); // fragment_menu_possess_card.xml에 정의된 RecyclerView ID를 사용합니다.
        adapter = new MyAdapter(requireContext(), items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
//        super.onViewCreated(view, savedInstanceState);
//        Intent intent = new Intent(getActivity(),MenuPossessCard.class);
//        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(intent);
//    }
}