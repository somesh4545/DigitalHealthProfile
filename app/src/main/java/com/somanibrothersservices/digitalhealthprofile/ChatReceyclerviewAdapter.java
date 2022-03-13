package com.somanibrothersservices.digitalhealthprofile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatReceyclerviewAdapter extends RecyclerView.Adapter<ChatReceyclerviewAdapter.ViewHolder> {

    public List<PatientModel> patientModelList;

    public ChatReceyclerviewAdapter(List<PatientModel> patientModelList) {
        this.patientModelList = patientModelList;
    }

    @NonNull
    @Override
    public ChatReceyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatReceyclerviewAdapter.ViewHolder holder, int position) {
        holder.setData(patientModelList.get(position).name , " " , patientModelList.get(position).age);
    }

    @Override
    public int getItemCount() {
        return patientModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView image;
        private TextView name , age ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chat_name_txt);
            age = itemView.findViewById(R.id.chat_notifs_txt);
            image = itemView.findViewById(R.id.icon);
        }

        private void setData(String pname , String pimage , String page) {
            name.setText(pname);
            age.setText(page);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatDetailIntent = new Intent(itemView.getContext() , PatientDetailsActivity.class);
                    chatDetailIntent.putExtra("position" , getAdapterPosition());
                    itemView.getContext().startActivity(chatDetailIntent);
                }
            });
        }
    }
}
