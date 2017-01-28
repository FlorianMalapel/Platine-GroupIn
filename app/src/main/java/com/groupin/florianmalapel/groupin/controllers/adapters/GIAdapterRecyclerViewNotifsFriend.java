package com.groupin.florianmalapel.groupin.controllers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationFriend;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 25/01/2017.
 */

public class GIAdapterRecyclerViewNotifsFriend extends RecyclerView.Adapter<GIAdapterRecyclerViewNotifsFriend.NotifUserViewHolder> {

    private Context context = null;
    private ArrayList<GINotificationFriend> friendNotifsList = null;
    private GIVolleyHandler volleyHandler = null;

    public GIAdapterRecyclerViewNotifsFriend(ArrayList<GINotificationFriend> friendNotifsList, Context context) {
        this.friendNotifsList = friendNotifsList;
        this.context = context;
        this.volleyHandler = new GIVolleyHandler();
    }

    @Override
    public NotifUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler_view_notifs_friend, parent, false);

        return new GIAdapterRecyclerViewNotifsFriend.NotifUserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotifUserViewHolder holder, int position) {
        holder.textViewDisplayNameFriend.setText(friendNotifsList.get(position).user.display_name);
        if(friendNotifsList.get(position).user.photoURL != null && !friendNotifsList.get(position).user.photoURL.isEmpty()) {
            Picasso.with(context)
                    .load(friendNotifsList.get(position).user.photoURL)
                    .transform(new CircleTransform())
                    .into(holder.imageViewProfilPictureFriend);
        }
        holder.imageButtonConfirm.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        holder.imageButtonReject.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        if(friendNotifsList == null)
            return 0;
        return friendNotifsList.size();
    }

    public class NotifUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewDisplayNameFriend = null;
        public ImageView imageViewProfilPictureFriend = null;
        public ImageButton imageButtonReject = null;
        public ImageButton imageButtonConfirm = null;

        public NotifUserViewHolder(View itemView) {
            super(itemView);
            textViewDisplayNameFriend = (TextView) itemView.findViewById(R.id.textViewDisplayNameFriend);
            imageViewProfilPictureFriend = (ImageView) itemView.findViewById(R.id.imageViewProfilPictureFriend);
            imageButtonReject = (ImageButton) itemView.findViewById(R.id.imageButtonReject);
            imageButtonConfirm = (ImageButton) itemView.findViewById(R.id.imageButtonConfirm);
            imageButtonConfirm.setOnClickListener(this);
            imageButtonReject.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == imageButtonConfirm){
                confirmFriendship();
            }

            else if(view == imageButtonReject){
                rejectFriendship();
            }
        }

        private void confirmFriendship(){
            volleyHandler.postFriendShip(null,
                    friendNotifsList.get(getAdapterPosition()).user.uid,
                    GIApplicationDelegate.getInstance().getDataCache().user.uid);
            hideNotification();
        }

        private void rejectFriendship(){
            volleyHandler.deleteNotifFriend(null,
                    friendNotifsList.get(getAdapterPosition()).user.uid,
                    GIApplicationDelegate.getInstance().getDataCache().user.uid);
            hideNotification();
        }

        private void hideNotification(){
            GIApplicationDelegate.getInstance().getDataCache().notifsFriendList.remove(getAdapterPosition());
            notifyDataSetChanged();
        }
    }

}
