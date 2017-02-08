package com.groupin.florianmalapel.groupin.controllers.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityDisplayGroup;
import com.groupin.florianmalapel.groupin.helpers.GIJsonToObjectHelper;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIChatMessage;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.views.GIProgressIndicator;
import com.groupin.florianmalapel.groupin.volley.GIRequestData;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import jp.bassaer.chatmessageview.models.Message;
import jp.bassaer.chatmessageview.models.User;
import jp.bassaer.chatmessageview.utils.ITimeFormatter;
import jp.bassaer.chatmessageview.views.ChatView;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIFragmentGroupMenuChat extends Fragment implements GIVolleyRequest.RequestCallback, View.OnClickListener {

    private ChatView chatView = null;
    private GIProgressIndicator progressIndicator = null;
    private GIVolleyHandler volleyHandler = null;
    private ArrayList<GIChatMessage> chatMessagesList = null;
    private TextView textViewGroupChat = null;
    private String groupId = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_menu_chat, container, false);
        findViewById(view);
        initialize();
        requestGetMessages();
        initialize_chatView();
        return view;
    }

    private void findViewById(View view){
        chatView = (ChatView) view.findViewById(R.id.chatView);
        progressIndicator = (GIProgressIndicator) view.findViewById(R.id.progressIndicator);
        textViewGroupChat = (TextView) view.findViewById(R.id.textViewGroupPolls);
    }

    private void initialize(){
        groupId = getArguments().getString(GIActivityDisplayGroup.GROUP_ID);
        volleyHandler = new GIVolleyHandler();
        chatMessagesList = new ArrayList<>();
        textViewGroupChat.setTypeface(GIDesign.getBoldFont(getContext()));
    }

    private void requestGetMessages(){
        GIDesign.startRotatingProgressIndicator(progressIndicator);
        volleyHandler.getMessagesChatFromGroup(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid(), groupId);
    }
    
    private void initialize_chatView(){
        chatView.setOnClickSendButtonListener(this);
        chatView.setRightBubbleColor(ContextCompat.getColor(getContext(), R.color.GIYellow));
        chatView.setLeftBubbleColor(ContextCompat.getColor(getContext(), R.color.GIBlue));
        chatView.setBackgroundColor(Color.WHITE);
        chatView.setSendButtonColor(ContextCompat.getColor(getContext(), R.color.GIBlue));
        chatView.setSendIcon(R.drawable.ic_action_send);
        chatView.setRightMessageTextColor(ContextCompat.getColor(getContext(), R.color.GIBlue));
        chatView.setLeftMessageTextColor(ContextCompat.getColor(getContext(), R.color.GIYellow));
        chatView.setUsernameTextColor(Color.BLACK);
        chatView.setSendTimeTextColor(Color.WHITE);
        chatView.setDateSeparatorColor(Color.BLACK);
        chatView.setInputTextHint(getString(R.string.write));
        chatView.setMessageMarginTop(5);
        chatView.setMessageMarginBottom(5);
    }

    private void initMessages(){
        if(chatMessagesList == null || chatMessagesList.isEmpty()) {
            return;
        }

        for(final GIChatMessage message: chatMessagesList){
            GIUser author = GIApplicationDelegate.getInstance().getDataCache().allUsersList.get(message.authorUid);
            int idAuthor = GIApplicationDelegate.getInstance().getDataCache().userGroupsList.get(groupId).membersUids.indexOf(author.uid);

            Message msg = new Message.Builder()
                    .setUser(new User(idAuthor, (author.display_name == null) ? author.email : author.display_name, null))
                    .setMessageText(message.messageContent)
                    .setDateFormatter(new ITimeFormatter() {
                        @Override
                        public String getFormattedTimeText(Calendar createdAt) {
                            return DateUtils.getRelativeTimeSpanString(message.date).toString();
                        }
                    })
                    .build();

            if(author.uid.equals(GIApplicationDelegate.getInstance().getDataCache().getUserUid()))
                msg.setRightMessage(true);
            else msg.setRightMessage(false);


            chatView.send(msg);
        }
    }

    private void onRequestGetChatsComeBack(JSONObject object){
        GIDesign.stopRotatingProgressIndicator(progressIndicator);
        try {
            chatMessagesList = GIJsonToObjectHelper.getArrayMessagesFromJSON(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initMessages();
    }

    private void onClickOnButtonSend(){
        volleyHandler.postChatMessage(this, GIApplicationDelegate.getInstance().getDataCache().getUserUid(), groupId, chatView.getInputText());
        String name = GIApplicationDelegate.getInstance().getDataCache().user.display_name;
        if(name == null || name.isEmpty())
            name = GIApplicationDelegate.getInstance().getDataCache().user.email;
        Message message = new Message.Builder()
                .setUser(new User(1, name, null))
                .setRightMessage(true)
                .setMessageText(chatView.getInputText())
                .build();
        chatView.send(message);
        chatView.setInputText("");
    }

    @Override
    public void onClick(View view) {
        onClickOnButtonSend();
    }

    @Override
    public void onRequestStart() {

    }

    @Override
    public void onRequestFinishWithSuccess(int request_code, JSONObject object) {
        if(request_code == GIRequestData.GET_CHAT_GROUP_CODE)
            onRequestGetChatsComeBack(object);
        else if(request_code == GIRequestData.POST_CHAT_MESSAG_GROUP_CODE){

        }
    }

    @Override
    public void onRequestFinishWithFailure() {

    }
}
