package com.groupin.florianmalapel.groupin.model.dbObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by florianmalapel on 30/01/2017.
 */

public class GIBill implements Serializable {

    public String billId = null;
    public String groupId = null;
    public ArrayList<String> paidForList = new ArrayList<>();
    public double price = 0;
    public String objectBought = null;
    public String uidBuyer = null;

    public GIBill() {
    }

    public GIBill(String groupId, ArrayList<String> paidForList, double price, String objectBought, String uidBuyer) {
        this.groupId = groupId;
        this.paidForList = paidForList;
        this.price = price;
        this.objectBought = objectBought;
        this.uidBuyer = uidBuyer;
    }

    public JSONObject getCreateBillJSON() throws JSONException {
        JSONObject billJSON = new JSONObject();
        billJSON.put("id", this.groupId);

        JSONArray payerTabJSON = new JSONArray();
        for(String string : paidForList){
            payerTabJSON.put(string);
        }
        billJSON.put("payerTab", payerTabJSON);
        billJSON.put("prix", this.price);
        billJSON.put("uid", this.uidBuyer);
        billJSON.put("what", this.objectBought);
        return billJSON;
    }
}
