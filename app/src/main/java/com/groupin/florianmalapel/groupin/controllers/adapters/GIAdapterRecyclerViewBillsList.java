package com.groupin.florianmalapel.groupin.controllers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIBill;
import com.groupin.florianmalapel.groupin.tools.GIDesign;
import com.groupin.florianmalapel.groupin.volley.GIVolleyHandler;
import com.groupin.florianmalapel.groupin.volley.GIVolleyRequest;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 31/01/2017.
 */

public class GIAdapterRecyclerViewBillsList extends RecyclerView.Adapter<GIAdapterRecyclerViewBillsList.BillViewHolder> {

    private ArrayList<GIBill> list_bills = null;
    private Context context = null;
    private GIVolleyHandler volleyHandler = null;
    private GIVolleyRequest.RequestCallback callback = null;
    private String groupId = null;


    public GIAdapterRecyclerViewBillsList(ArrayList<GIBill> list_bills, Context context, GIVolleyRequest.RequestCallback callback, String groupId) {
        this.list_bills = list_bills;
        this.context = context;
        this.callback = callback;
        volleyHandler = new GIVolleyHandler();
        this.groupId = groupId;
    }

    public void refreshList(ArrayList<GIBill> bills){
        this.list_bills = bills;
        this.notifyDataSetChanged();
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recycler_view_bill, parent, false);

        return new GIAdapterRecyclerViewBillsList.BillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        if(list_bills.get(position) == null)
            return;

        holder.textViewObjectBought.setText(list_bills.get(position).objectBought);
        holder.textViewAmount.setText(list_bills.get(position).price + "€");
        for(String uid : list_bills.get(position).paidForList){
            String name = GIApplicationDelegate.getInstance().getDataCache().allUsersList.get(uid).display_name;
            holder.textViewNameOwners.setText(name + "\n");
        }
        holder.textViewBoughtByUid.setText(GIApplicationDelegate.getInstance().getDataCache().allUsersList.get(list_bills.get(position).uidBuyer).display_name);
    }

    @Override
    public int getItemCount() {
        if(list_bills != null)
            return list_bills.size();
        return 0;
    }


    public class BillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewBought = null;
        public TextView textViewObjectBought = null;
        public TextView textViewAmount = null;
        public TextView textViewName = null;
        public TextView textViewNameOwners = null;
        public TextView textViewPrice = null;
        public TextView textViewBoughtBy = null;
        public TextView textViewBoughtByUid = null;
        public ImageButton imageButtonConfirm = null;

        public BillViewHolder(View itemView) {
            super(itemView);
            textViewBought = (TextView) itemView.findViewById(R.id.textViewBought);
            textViewBought.setTypeface(GIDesign.getBoldFont(context));
            textViewObjectBought = (TextView) itemView.findViewById(R.id.textViewObjectBought);
            textViewObjectBought.setTypeface(GIDesign.getRegularFont(context));
            textViewAmount = (TextView) itemView.findViewById(R.id.textViewAmount);
            textViewAmount.setTypeface(GIDesign.getBoldFont(context));
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewName.setTypeface(GIDesign.getRegularFont(context));
            textViewNameOwners = (TextView) itemView.findViewById(R.id.textViewNameOwners);
            textViewNameOwners.setTypeface(GIDesign.getLightFont(context));
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            textViewAmount.setTypeface(GIDesign.getBoldFont(context));
            textViewBoughtBy = (TextView) itemView.findViewById(R.id.textViewBoughtBy);
            textViewBoughtBy.setTypeface(GIDesign.getRegularFont(context));
            textViewBoughtByUid = (TextView) itemView.findViewById(R.id.textViewBoughtByUid);
            textViewBoughtByUid.setTypeface(GIDesign.getLightFont(context));
            imageButtonConfirm = (ImageButton) itemView.findViewById(R.id.imageButtonConfirm);
            imageButtonConfirm.getDrawable().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            imageButtonConfirm.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == imageButtonConfirm) {
                sendDeleteBill();
            }
        }

        private void sendDeleteBill(){
            volleyHandler.deleteBills(callback, groupId, list_bills.get(getAdapterPosition()).billId);
        }

    }
}
