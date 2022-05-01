package com.example.chatapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {

    private List<ChatMessage> messageList;
    public MessagesAdapter(List<ChatMessage> messageList) {
       this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessagesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        ChatMessage cm = messageList.get(position);
        holder.bind(cm);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setMessages(List<ChatMessage> chatMessages) {
        this.messageList = chatMessages;
        notifyDataSetChanged();
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder {


        ImageView senderImage;
        TextView messageSenderNameTv,messageTv,messageTimeTv;
        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            senderImage = itemView.findViewById(R.id.user_chat_image);
            messageTimeTv = itemView.findViewById(R.id.user_chat_message_time);
            messageSenderNameTv = itemView.findViewById(R.id.chat_message_sender_name);
            messageTv = itemView.findViewById(R.id.user_chat_message);
        }

        public void bind(ChatMessage cm) {
            if(cm.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
                itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }else {itemView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);}

            messageSenderNameTv.setText(cm.getSenderName());
            messageTv.setText(cm.getMessageContent());
            messageTimeTv.setText(DateHelper.getTimeFromMillis(cm.getDateMillis()));
            if(!cm.getSenderPhoto().equals("undefined"))
                Picasso.get().load(cm.getSenderPhoto()).into(senderImage);
        }
    }

}
