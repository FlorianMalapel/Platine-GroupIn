package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groupin.florianmalapel.groupin.R;

import java.util.Random;

import jp.bassaer.chatmessageview.models.Message;
import jp.bassaer.chatmessageview.utils.ChatBot;
import jp.bassaer.chatmessageview.views.ChatView;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIFragmentGroupMenuChat extends Fragment {

    private ChatView chatView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_menu_chat, container, false);
        findViewById(view);
        initialize_chatView();
        setListenerChatView();
        return view;
    }

    private void findViewById(View view){
        chatView = (ChatView) view.findViewById(R.id.chatView);
    }

    private void initialize_chatView(){
        chatView.setRightBubbleColor(ContextCompat.getColor(getContext(), R.color.holoBlueDark));
        chatView.setLeftBubbleColor(ContextCompat.getColor(getContext(), R.color.lightGray));
        chatView.setBackgroundColor(Color.WHITE);
        chatView.setSendButtonColor(ContextCompat.getColor(getContext(), R.color.holoGreen));
        chatView.setSendIcon(R.drawable.ic_action_send);
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.BLACK);
        chatView.setUsernameTextColor(Color.BLACK);
        chatView.setSendTimeTextColor(Color.BLACK);
        chatView.setDateSeparatorColor(Color.BLACK);
        chatView.setInputTextHint("Ecrire...");
    }

    private void setListenerChatView(){
        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new message
                Message message = new Message.Builder()
//                        .setUserIcon(myIcon)
                        .setUserName("Charlie")
                        .setRightMessage(true)
                        .setMessageText(chatView.getInputText())
                        .build();
                //Set to chat view
                chatView.send(message);
                //Reset edit text
                chatView.setInputText("");

                //Receive message
                final Message receivedMessage = new Message.Builder()
                        .setUserName("Flo")
                        .setRightMessage(false)
                        .setMessageText(ChatBot.talk(message.getUserName(), message.getMessageText()))
                        .build();

                // This is a demo bot
                // Return within 3 seconds
                int sendDelay  = (new Random().nextInt(4) +1) * 1000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chatView.receive(receivedMessage);
                    }
                }, sendDelay);
            }
        });
    }
}
