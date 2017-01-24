package com.groupin.florianmalapel.groupin.controllers.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.controllers.activities.GIActivityDisplayEvent;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIEvent;
import com.groupin.florianmalapel.groupin.tools.GIColors;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by florianmalapel on 07/01/2017.
 */

public class GIAdapterRecyclerViewEventsList extends RecyclerView.Adapter<GIAdapterRecyclerViewEventsList.EventViewHolder> {

    private ArrayList<GIEvent> list_events = null;
    private Context context = null;

    public GIAdapterRecyclerViewEventsList(ArrayList<GIEvent> list_events, Context context) {
        this.list_events = list_events;
        this.context = context;
    }

//    public GIAdapterRecyclerViewEventsList(HashMap<String, GIEvent> list_events, Context context) {
//        this.list_events = getList_events(list_events);
//        this.context = context;
//    }

    public ArrayList<GIEvent> getList_events(HashMap<String,GIEvent> list){
        if(list == null)
            return new ArrayList<>();

        ArrayList<GIEvent> events = new ArrayList<>();
        for(String key: list.keySet()){
            events.add(list.get(key));
        }
        return events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_view_event, parent, false);

        return new GIAdapterRecyclerViewEventsList.EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.textView_nameEvent.setText(list_events.get(position).name);
        holder.textView_dateEvent.setText(DateUtils.getRelativeTimeSpanString(Long.valueOf(list_events.get(position).date_start)));
        if(list_events.get(position).participantsUids != null) {
            int nbMembers = list_events.get(position).participantsUids.size();
            holder.textView_nbParticipantEvent.setText(nbMembers + " " + context.getString(R.string.participants));
        }

        Picasso.with(context).load(list_events.get(position).url_image).transform(new CircleTransform()).into(holder.imageView_eventPicture);

//        CircleTransform circleTransform = new CircleTransform();
//        Bitmap normalImage = ((BitmapDrawable)holder.imageView_eventPicture.getDrawable()).getBitmap();
//        Bitmap circleImage = circleTransform.transform(normalImage);
//        holder.imageView_eventPicture.setImageBitmap(circleImage);
        holder.imageView_personLogo.getDrawable().mutate().setColorFilter(Color.parseColor(GIColors.GREEN), PorterDuff.Mode.SRC_ATOP);
        holder.imageView_personLogo.getBackground().mutate().setColorFilter(Color.parseColor(GIColors.GREEN), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        return list_events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView_nameEvent = null;
        public TextView textView_dateEvent = null;
        public TextView textView_nbParticipantEvent = null;
        public ImageView imageView_eventPicture = null;
        public ImageView imageView_personLogo = null;

        public EventViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView_nameEvent = (TextView) itemView.findViewById(R.id.textView_eventName);
            textView_dateEvent = (TextView) itemView.findViewById(R.id.textView_eventDate);
            textView_nbParticipantEvent = (TextView) itemView.findViewById(R.id.textView_groupNbParticipant);
            imageView_eventPicture = (ImageView) itemView.findViewById(R.id.imageView_event);
            imageView_personLogo = (ImageView) itemView.findViewById(R.id.imageView_personLogo);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, GIActivityDisplayEvent.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(GIActivityDisplayEvent.EVENT_ID, list_events.get(getAdapterPosition()));
            intent.putExtra(GIActivityDisplayEvent.BUNDLE_ID, bundle);
            context.startActivity(intent);
        }
    }
}
