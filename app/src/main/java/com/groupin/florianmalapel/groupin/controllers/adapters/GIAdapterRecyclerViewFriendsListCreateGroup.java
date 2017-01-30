package com.groupin.florianmalapel.groupin.controllers.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 13/01/2017.
 */

public class GIAdapterRecyclerViewFriendsListCreateGroup
        extends RecyclerView.Adapter<GIAdapterRecyclerViewFriendsListCreateGroup.FriendViewHolder> {

    private ArrayList<GIUser> listFriendsChosen = null;
    private ItemClickedCallback callback = null;

    public interface ItemClickedCallback {
        void itemDeletedAddPosition(int position);
    }

    public GIAdapterRecyclerViewFriendsListCreateGroup(ArrayList<GIUser> listFriendsChosen, ItemClickedCallback callback) {
        this.listFriendsChosen = listFriendsChosen;
        this.callback = callback;
    }

    public void refreshList(ArrayList<GIUser> listFriendsChosen){
        this.listFriendsChosen = listFriendsChosen;
        this.notifyDataSetChanged();
//        this.notifyItemRangeInserted(0, listFriendsChosen.size());
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler_view_friend_list_create_group, parent, false);

        return new GIAdapterRecyclerViewFriendsListCreateGroup.FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.textView_friendName.setText(listFriendsChosen.get(position).display_name);
    }

    @Override
    public int getItemCount() {
        if(listFriendsChosen == null)
            return 0;
        return listFriendsChosen.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView_friendName = null;
        public ImageView imageView_deleteFriend = null;

        public FriendViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView_friendName = (TextView) itemView.findViewById(R.id.textView_friendName);
            imageView_deleteFriend = (ImageView) itemView.findViewById(R.id.imageView_deleteFriend);
            imageView_deleteFriend.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        @Override
        public void onClick(View view) {
            callback.itemDeletedAddPosition(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyDataSetChanged();
        }
    }

}
