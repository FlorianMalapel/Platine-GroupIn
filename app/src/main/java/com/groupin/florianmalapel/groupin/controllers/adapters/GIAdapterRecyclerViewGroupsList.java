package com.groupin.florianmalapel.groupin.controllers.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityDisplayGroup;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIGroup;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by florianmalapel on 06/01/2017.
 */

public class GIAdapterRecyclerViewGroupsList extends RecyclerView.Adapter<GIAdapterRecyclerViewGroupsList.GroupViewHolder> {

    private ArrayList<GIGroup> list_groups = null;
    private Context context = null;

    public GIAdapterRecyclerViewGroupsList(HashMap<String,GIGroup> list_groups, Context context) {
        this.list_groups = getList_groups(list_groups);
        this.context = context;
    }


    public ArrayList<GIGroup> getList_groups(HashMap<String,GIGroup> list){
        if(list == null)
            return new ArrayList<>();

        ArrayList<GIGroup> groups = new ArrayList<>();
        for(String key: list.keySet()){
            groups.add(list.get(key));
        }
        return groups;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_view_group, parent, false);

        return new GIAdapterRecyclerViewGroupsList.GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        holder.textView_nameGroup.setText(list_groups.get(position).name);
        if(list_groups.get(position).membersUids != null) {
            int nbMembers = list_groups.get(position).membersUids.size();
            holder.textView_nbParticipant.setText(nbMembers + "");
        }

        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, r.getDisplayMetrics());
        Picasso.with(context).load(list_groups.get(position).url_image).transform(new CircleTransform()).into(holder.imageView_groupPicture);
//        CircleTransform circleTransform = new CircleTransform();
//        Bitmap normalImage = ((BitmapDrawable)holder.imageView_groupPicture.getDrawable()).getBitmap();
//        Bitmap circleImage = circleTransform.transform(normalImage);
//        holder.imageView_groupPicture.setImageBitmap(circleImage);
    }

    @Override
    public int getItemCount() {
        return list_groups.size();
    }


    public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView_nameGroup = null;
        public TextView textView_nbParticipant = null;
        public ImageView imageView_groupPicture = null;

        public GroupViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView_nameGroup = (TextView) itemView.findViewById(R.id.textView_groupName);
            textView_nbParticipant = (TextView) itemView.findViewById(R.id.textView_groupNbParticipant);
            imageView_groupPicture = (ImageView) itemView.findViewById(R.id.imageView_group);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, GIActivityDisplayGroup.class);
            context.startActivity(intent);
        }
    }

}
