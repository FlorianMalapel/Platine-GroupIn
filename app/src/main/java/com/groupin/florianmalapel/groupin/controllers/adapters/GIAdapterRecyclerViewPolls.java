package com.groupin.florianmalapel.groupin.controllers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.groupin.florianmalapel.groupin.model.dbObjects.GIPoll;
import com.groupin.florianmalapel.groupin.views.GIPollResultView;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 28/01/2017.
 */

public class GIAdapterRecyclerViewPolls extends RecyclerView.Adapter<GIAdapterRecyclerViewPolls.PollsViewHolder> implements GIPollResultView.PollListener {

    private Context context = null;
    private ArrayList<GIPoll> pollsList = null;
    private GIVolleyRequest.RequestCallback callback = null;
    private GIVolleyHandler volleyHandler = null;

    public GIAdapterRecyclerViewPolls(Context context, ArrayList<GIPoll> pollsList, GIVolleyRequest.RequestCallback callback) {
        this.context = context;
        this.pollsList = pollsList;
        this.callback = callback;
        this.volleyHandler = new GIVolleyHandler();
    }

    public void refreshList(ArrayList<GIPoll> pollsList){
        this.pollsList = pollsList;
        this.notifyDataSetChanged();
    }

    @Override
    public PollsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.row_recycler_view_polls, parent, false);

        GIPollResultView resultView = new GIPollResultView(context);
        parent.addView(resultView);

        return new GIAdapterRecyclerViewPolls.PollsViewHolder(resultView);
    }

    @Override
    public void onBindViewHolder(PollsViewHolder holder, int position) {
        holder.pollResultView.setPollInView(pollsList.get(position), this);
    }

    @Override
    public int getItemCount() {
        if(pollsList == null)
            return 0;
        return pollsList.size();
    }

    @Override
    public void validateChoice(JSONObject objectPollAnswer) {
        volleyHandler.postPollAnswer(callback, objectPollAnswer);
    }

    public class PollsViewHolder extends RecyclerView.ViewHolder {

        public GIPollResultView pollResultView = null;

        public PollsViewHolder(View itemView) {
            super(itemView);
            pollResultView = (GIPollResultView) itemView;
        }
    }



}
