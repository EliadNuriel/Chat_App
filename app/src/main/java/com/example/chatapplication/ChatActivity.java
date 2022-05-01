package com.example.chatapplication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    private FirebaseAnalytics mFirebaseAnalytics;

    private EditText messageEt;
    private ImageButton sendBtn;
    private RecyclerView rvChat;
    private MessagesAdapter rvAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageEt = findViewById(R.id.message_et);
        sendBtn = findViewById(R.id.send_btn);
        rvChat = findViewById(R.id.rvChat);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        rvChat.setLayoutManager(new LinearLayoutManager(this));
        FirebaseFirestore.getInstance()
                .collection("chat")
                .orderBy("dateMillis")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ChatMessage> cmList = new ArrayList<>();
                    for(DocumentSnapshot messageSnap : queryDocumentSnapshots.getDocuments()) {
                        try {
                            cmList.add(messageSnap.toObject(ChatMessage.class));
                        }catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    System.out.println(cmList.size());
                    rvAdapter = new MessagesAdapter(cmList);
                    rvAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                        @Override
                        public void onChanged() {
                            rvChat.smoothScrollToPosition(rvAdapter.getItemCount() > 0 ? rvAdapter.getItemCount()-1 : 0);
                            super.onChanged();
                        }
                    });
                        rvChat.setAdapter(rvAdapter);
                    rvChat.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
                        if (rvAdapter.getItemCount()>0) {
                            rvChat.smoothScrollToPosition(rvAdapter.getItemCount()-1);
                        }
                    });

                    FirebaseFirestore.getInstance()
                            .collection("chat")
                            .orderBy("dateMillis")
                            .addSnapshotListener((value, error) -> {
                                List<ChatMessage> messages = new ArrayList<>();
                                if(value ==null)return;
                                for (DocumentSnapshot document : value.getDocuments())
                                {
                                    try {
                                        messages.add(document.toObject(ChatMessage.class));
                                    } catch (Exception e) {
                                    }
                                }
                                rvAdapter.setMessages(messages);
                            });
                });

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            TextView tv = findViewById(R.id.user_name);
            tv.setText("Hello, " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        sendBtn.setOnClickListener(view -> {

            String uid = FirebaseAuth.getInstance().getUid();
            String senderName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null)
                senderName = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
            String messageContent = messageEt.getText().toString();
            if(messageContent.isEmpty()) {
                return;
            }
            String userPhoto = "undefined";
            if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null) {
                userPhoto = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
            }
            long time = System.currentTimeMillis();
            ChatMessage cm = new ChatMessage(senderName,uid,userPhoto,messageContent,time);
            FirebaseFirestore.getInstance()
                    .collection("chat")
                    .add(cm);
            messageEt.getText().clear();
           FcmNotificationsSender sender = new FcmNotificationsSender("all","Chat App",cm.getMessageContent(),this,this);
           sender.sendNotification();
        });

    }


}


