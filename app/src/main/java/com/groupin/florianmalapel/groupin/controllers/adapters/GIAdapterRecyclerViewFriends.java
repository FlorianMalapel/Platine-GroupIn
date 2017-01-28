package com.groupin.florianmalapel.groupin.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 07/12/2016.
 */

public class GIAdapterRecyclerViewFriends extends RecyclerView.Adapter<GIAdapterRecyclerViewFriends.UserViewHolder> {

    private ArrayList<GIUser> friendsList = null;
    private static int numberOfFriends = 0;
    private ArrayList<Boolean> friendsSelected = null;
    private boolean isCheckBoxVisible = false;
    private Context context = null;

    public GIAdapterRecyclerViewFriends(ArrayList<GIUser> friendsList, boolean withCheckBoxVisible, Context context) {
        this.friendsList = friendsList;
        this.numberOfFriends = (friendsList == null) ? 0 : friendsList.size();
        initFriendsSelectedArrayToFalse();
        this.isCheckBoxVisible = withCheckBoxVisible;
        this.context = context;
    }

    private void initFriendsSelectedArrayToFalse(){
        friendsSelected = new ArrayList<>();

        if(friendsList == null)
            return;

        for(GIUser user : friendsList){
            friendsSelected.add(false);
        }
    }

    public ArrayList<Boolean> getFriendsSelected() {
        return friendsSelected;
    }

    public GIUser getGIUserAtPosition(int position){
        return friendsList.get(position);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

        public TextView textViewFirstLetter = null;
        public ImageView cirleImageViewProfilPic = null;
        public TextView textViewLastName = null;
        public TextView textViewFirstName = null;
        public CheckBox checkBox = null;

        public UserViewHolder(View itemView) {
            super(itemView);

            textViewFirstLetter = (TextView) itemView.findViewById(R.id.textViewFirstLetter);
            textViewLastName = (TextView) itemView.findViewById(R.id.textViewLastName);
            textViewFirstName = (TextView) itemView.findViewById(R.id.textViewFirstName);
            cirleImageViewProfilPic = (ImageView) itemView.findViewById(R.id.imageViewProfilPicture);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkboxFriend);
            if(isCheckBoxVisible)
                checkBox.setVisibility(View.VISIBLE);
//            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            if(checkBox.isChecked()) {
                checkBox.setChecked(false);
                friendsSelected.set(getAdapterPosition(), false);
            }
            else {
                checkBox.setChecked(true);
                friendsSelected.set(getAdapterPosition(), true);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if(compoundButton == checkBox){
                if(isChecked){
                    friendsSelected.set(getAdapterPosition(), true);
                }
                else {
                    friendsSelected.set(getAdapterPosition(), false);
                }
            }
        }

        public ArrayList<Boolean> getFriendsSelected(){
            return friendsSelected;
        }
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler_view_friends, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if(friendsList.isEmpty() || friendsList.get(position) == null) return;

        holder.textViewLastName.setText(friendsList.get(position).display_name);
        holder.textViewFirstName.setVisibility(View.GONE);
        if(position > 0 && friendsList.get(position - 1) != null
                && friendsList.get(position - 1).display_name != null && !friendsList.get(position - 1).display_name.isEmpty()
                && String.valueOf(friendsList.get(position - 1).display_name.charAt(0)).equals(String.valueOf(friendsList.get(position).display_name.charAt(0)))) {
            holder.textViewFirstLetter.setVisibility(View.INVISIBLE);
        }
        else holder.textViewFirstLetter.setText(String.valueOf(friendsList.get(position).display_name.charAt(0)));

//        else {
//            if(friendsList.get(position).lastName != null) {
//
//                if(position > 0 && String.valueOf(friendsList.get(position - 1).lastName.charAt(0)).equals(String.valueOf(friendsList.get(position).lastName.charAt(0))))
//                    holder.textViewFirstLetter.setVisibility(View.INVISIBLE);
//                else holder.textViewFirstLetter.setText(String.valueOf(friendsList.get(position).lastName.charAt(0)));
//
//                holder.textViewLastName.setText(friendsList.get(position).lastName);
//            }
//
//            if(friendsList.get(position).firstName != null)
//                holder.textViewFirstName.setText(friendsList.get(position).firstName);
//        }

        if(friendsList.get(position).photoURL != null) { // TODO Need to be changed
            Picasso.with(context).load(friendsList.get(position).photoURL).transform(new CircleTransform()).into(holder.cirleImageViewProfilPic);
        }
    }

    @Override
    public int getItemCount() {
        return numberOfFriends;
    }

}
