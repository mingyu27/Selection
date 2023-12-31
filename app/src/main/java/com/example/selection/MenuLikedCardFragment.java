package com.example.selection;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MenuLikedCardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private LikecardAdapter adapter;
    private ArrayList<Item> items = new ArrayList<>();
    private List<Integer> likedShinhanCardIndexArrayList = new ArrayList<>();
    private List<Integer> likedKookminCardIndexArrayList = new ArrayList<>();
    private String mParam1;
    private String mParam2;
    private FunctionUser functionUser;
    private String TAG = "SMG";

    public MenuLikedCardFragment() {
        // Required empty public constructor
    }

    public static MenuLikedCardFragment newInstance(FunctionUser functionUser) {
        MenuLikedCardFragment fragment = new MenuLikedCardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("functionUser",functionUser);
        fragment.setArguments(bundle);
        return fragment;

    }

    private class LikecardViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public LikecardViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.like_textview);
            imageView = itemView.findViewById(R.id.like_imageview);
        }
    }

    private class LikecardAdapter extends RecyclerView.Adapter<LikecardViewHolder> {
        Context context;
        ArrayList<Item> items;

        public LikecardAdapter(Context context, ArrayList<Item> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public LikecardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View LikeitemView = LayoutInflater.from(context).inflate(R.layout.like_itemview, parent, false);
            return new LikecardViewHolder(LikeitemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LikecardViewHolder holder, int position) {
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

        public Item(String text, int img, int index) {
            this.img = img;
            this.index = index;
            this.text = text;
        }

        public String getText() {
            return text;
        }
        public int getImg() {
            return img;
        }
        public  int getCardIndex() {
            return index;
        }
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
                    likedKookminCardIndexArrayList = functionUser.getLikedKookminIndexList();
                    likedShinhanCardIndexArrayList = functionUser.getLikedShinhanIndexList();
                    Log.d(TAG, functionUser.getName() + "at MenuSavedCardFragment");}
                Log.d("YJH", "Saved Shinhan Card Index List:");
                for (Integer index : likedShinhanCardIndexArrayList) {
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
                Log.d("KBM", "Saved Kookmin Card Index List:");
                for (Integer index : likedKookminCardIndexArrayList) {
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
        Log.d("KBM", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_menu_liked_card, container, false);
        recyclerView = view.findViewById(R.id.like_recyclerview);
        adapter = new LikecardAdapter(requireContext(), items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }
}