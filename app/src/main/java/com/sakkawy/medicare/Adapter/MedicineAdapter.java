package com.sakkawy.medicare.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sakkawy.medicare.Model.Medicine;
import com.sakkawy.medicare.R;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    Context context;
    List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine_layout,parent,false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.dos1.setText(medicine.getKidsDose()+" "+medicine.getKidsUnit());
        holder.dos2.setText(medicine.getTeenDose()+" "+medicine.getTeenUnit());
        holder.dos3.setText(medicine.getAdultDose()+" "+medicine.getAdultUnit());

        holder.port1.setText(medicine.getKidsPortion());
        holder.port2.setText(medicine.getTeenPortion());
        holder.port3.setText(medicine.getAdultPortion());

        holder.medName.setText(medicine.getName());
        holder.medDes.setText(medicine.getDescription());
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder{

        TextView medName , medDes, dos1 , dos2 , dos3 , port1 , port2 , port3 ;
        ImageView edit , delete;
        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);

            medName = itemView.findViewById(R.id.tv_medicine_name);
            medDes = itemView.findViewById(R.id.tv_medicine_des);

            dos1 = itemView.findViewById(R.id.dose1);
            dos2 = itemView.findViewById(R.id.dose2);
            dos3 = itemView.findViewById(R.id.dose3);

            port1 = itemView.findViewById(R.id.portion1);
            port2 = itemView.findViewById(R.id.portion2);
            port3 = itemView.findViewById(R.id.portion3);
        }
    }
}
