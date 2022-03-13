package com.somanibrothersservices.digitalhealthprofile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.somanibrothersservices.digitalhealthprofile.LoginActivity.ACCESS;

public class ChatDetailsRecyclerviewAdapter extends RecyclerView.Adapter<ChatDetailsRecyclerviewAdapter.ViewHolder> {

    public List<Block> chain;

    public ChatDetailsRecyclerviewAdapter(List<Block> chain) {
        for(int i = 0;i < chain.size(); i++)
        {
            Block c = chain.get(i);
            if (!c.access.contains(ACCESS)) {
                chain.remove(i--);
            }
        }
        this.chain = chain;
    }

    @NonNull
    @Override
    public ChatDetailsRecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_detail_item, parent, false);
        return new ChatDetailsRecyclerviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDetailsRecyclerviewAdapter.ViewHolder holder, int position) {
        holder.setData(chain.get(position).data , chain.get(position).timestamp);
    }

    @Override
    public int getItemCount() {
        return chain.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView msg , msgTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.text_msg_chat_detail_item);
            msgTime = itemView.findViewById(R.id.msg_time_chat_detail_item);
        }

        private void setData(String msgText , String msgTimeText){
            msg.setText(msgText);
            msgTime.setText(msgTimeText);
        }
    }
}
