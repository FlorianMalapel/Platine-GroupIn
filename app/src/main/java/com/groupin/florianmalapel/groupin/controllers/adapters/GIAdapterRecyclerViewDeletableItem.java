package com.groupin.florianmalapel.groupin.controllers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.tools.GIDesign;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 29/01/2017.
 */

public class GIAdapterRecyclerViewDeletableItem extends RecyclerView.Adapter<GIAdapterRecyclerViewDeletableItem.ItemViewHolder> {

    private ArrayList<String> stringListChosen = null;
    private ItemClickedCallback callback = null;
    private Context context = null;

    public interface ItemClickedCallback {
        void itemDeletedAddPosition(int position);
    }

    public GIAdapterRecyclerViewDeletableItem(ArrayList<String> stringListChosen, ItemClickedCallback callback, Context context) {
        this.stringListChosen = stringListChosen;
        this.callback = callback;
        this.context = context;
    }

    public void refreshList(ArrayList<String> stringListChosen){
        this.stringListChosen = stringListChosen;
        this.notifyDataSetChanged();
    }

    @Override
    public GIAdapterRecyclerViewDeletableItem.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler_view_friend_list_create_group, parent, false);

        return new GIAdapterRecyclerViewDeletableItem.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GIAdapterRecyclerViewDeletableItem.ItemViewHolder holder, int position) {
        holder.textViewStringItem.setText(stringListChosen.get(position));
    }

    @Override
    public int getItemCount() {
        if(stringListChosen == null)
            return 0;
        return stringListChosen.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewStringItem = null;
        public ImageView imageViewStringDelete = null;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewStringItem = (TextView) itemView.findViewById(R.id.textView_friendName);
            imageViewStringDelete = (ImageView) itemView.findViewById(R.id.imageView_deleteFriend);
            imageViewStringDelete.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            textViewStringItem.setTypeface(GIDesign.getRegularFont(context));
        }

        @Override
        public void onClick(View view) {
            callback.itemDeletedAddPosition(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyDataSetChanged();
        }
    }

}
