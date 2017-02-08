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
import com.groupin.florianmalapel.groupin.model.dbObjects.GINotificationGroup;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 25/01/2017.
 */

public class GIAdapterRecyclerViewNotifsGroup  extends RecyclerView.Adapter<GIAdapterRecyclerViewNotifsGroup.NotifGroupViewHolder> {

    private Context context = null;
    private ArrayList<GINotificationGroup> groupNotifsList = null;
    private GIVolleyHandler volleyHandler = null;


    public GIAdapterRecyclerViewNotifsGroup(ArrayList<GINotificationGroup> groupNotifsList, Context context) {
        this.groupNotifsList = groupNotifsList;
        this.context = context;
        this.volleyHandler = new GIVolleyHandler();
    }

    @Override
    public NotifGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler_view_notifs_group, parent, false);


        return new GIAdapterRecyclerViewNotifsGroup.NotifGroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotifGroupViewHolder holder, int position) {
        holder.textViewDisplayNameGroup.setText(groupNotifsList.get(position).group.name);
        if(groupNotifsList.get(position).group.url_image != null && !groupNotifsList.get(position).group.url_image.isEmpty()) {
            Picasso.with(context)
                    .load(groupNotifsList.get(position).group.url_image)
                    .transform(new CircleTransform())
                    .into(holder.imageViewProfilPictureGroup);
        }
    }

    @Override
    public int getItemCount() {
        if(groupNotifsList == null)
            return 0;
        return groupNotifsList.size();
    }

    public class NotifGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewDisplayNameGroup = null;
        public ImageView imageViewProfilPictureGroup = null;
        public ImageButton imageButtonReject = null;
        public ImageButton imageButtonConfirm = null;

        public NotifGroupViewHolder(View itemView) {
            super(itemView);
            textViewDisplayNameGroup = (TextView) itemView.findViewById(R.id.textViewDisplayNameGroup);
            textViewDisplayNameGroup.setTypeface(GIDesign.getRegularFont(context));
            imageViewProfilPictureGroup = (ImageView) itemView.findViewById(R.id.imageViewProfilPictureGroup);
            imageButtonReject = (ImageButton) itemView.findViewById(R.id.imageButtonReject);
            imageButtonConfirm = (ImageButton) itemView.findViewById(R.id.imageButtonConfirm);
            imageButtonConfirm.setOnClickListener(this);
            imageButtonReject.setOnClickListener(this);
            imageButtonReject.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            imageButtonConfirm.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }



        @Override
        public void onClick(View view) {
            if(view == imageButtonConfirm){
                confirmJoinGroup();
            }

            else if(view == imageButtonReject){
                rejectJoinGroup();
            }
        }

        private void confirmJoinGroup(){
            volleyHandler.postUserJoinGroup(null,
                    GIApplicationDelegate.getInstance().getDataCache().user.uid,
                    groupNotifsList.get(getAdapterPosition()).group.id);
            hideNotification();
        }

        private void rejectJoinGroup(){
            volleyHandler.deleteNotifGroup(null,
                    GIApplicationDelegate.getInstance().getDataCache().user.uid,
                    groupNotifsList.get(getAdapterPosition()).group.id);
            hideNotification();
        }

        private void hideNotification(){
            GIApplicationDelegate.getInstance().getDataCache().notifsGroupList.remove(getAdapterPosition());
            notifyDataSetChanged();
        }
    }

}
