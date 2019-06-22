package com.sakkawy.medicare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;
import com.sakkawy.medicare.Model.Folder;
import com.sakkawy.medicare.Model.ItemHome;
import com.sakkawy.medicare.R;

import java.util.List;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<Folder> mList;
    private ItemClickListener mItemClickListener;

    public HomeRecycleViewAdapter(Context context, ItemClickListener mItemClickListener, List<Folder> mList){
        this.context = context;
        this.mItemClickListener = mItemClickListener;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.ivIcon.setImageResource(R.drawable.ic_person_24dp);
        viewHolder.tvTextMenu.setText(mList.get(i).getFolderName());
    }

    @Override
    public int getItemCount() {
        return mList.size()<1?0:mList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivIcon , ivFolderMenu;
        TextView tvTextMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.icon_item_home);
            ivFolderMenu = itemView.findViewById(R.id.icon_menu_home);
            tvTextMenu = itemView.findViewById(R.id.text_item_home);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Folder folder = mList.get(getAdapterPosition());
            mItemClickListener.onItemClickListener(folder);
        }
    }

    public interface ItemClickListener{
        void onItemClickListener(Folder folder);
    }
}
