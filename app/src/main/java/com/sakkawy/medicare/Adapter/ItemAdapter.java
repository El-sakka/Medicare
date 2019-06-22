package com.sakkawy.medicare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakkawy.medicare.Model.ItemHome;
import com.sakkawy.medicare.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    List<ItemHome> list ;
    Context context;

    public ItemAdapter(List<ItemHome> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,viewGroup,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
            itemHolder.iconView.setImageResource(list.get(i).getIconId());
            itemHolder.textView.setText(list.get(i).getText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView iconView;
        TextView textView;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.icon_item_home);
            textView = itemView.findViewById(R.id.text_item_home);
        }
    }
}
