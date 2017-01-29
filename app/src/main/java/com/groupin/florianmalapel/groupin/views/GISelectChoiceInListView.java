package com.groupin.florianmalapel.groupin.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIChoice;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 29/01/2017.
 */

public class GISelectChoiceInListView extends ScrollView implements View.OnClickListener {

    private Context context = null;
    private LinearLayout linearLayoutContainer = null;
    private ArrayList<GIChoice> choicesArray = null;
    private ArrayList<ItemList> itemListArray = null;
    private ArrayList<String> arrayChoicesChosen = null;
    private boolean isQCM = false;

    public GISelectChoiceInListView(Context context) {
        super(context);
        initialize(context);
    }

    public GISelectChoiceInListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public GISelectChoiceInListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context){
        this.context = context;
        initializeLayoutContainer();
    }

    private void initializeLayoutContainer(){
        linearLayoutContainer = new LinearLayout(context);
        linearLayoutContainer.setOrientation(LinearLayout.VERTICAL);
        this.addView(linearLayoutContainer);
        this.setVerticalScrollBarEnabled(true);
    }

    public void initializeWithChoices(ArrayList<GIChoice> choicesArray, boolean isQCM){
        this.choicesArray = choicesArray;
        this.isQCM = isQCM;
        itemListArray = new ArrayList<>();
        arrayChoicesChosen = new ArrayList<>();
        for(GIChoice choice : choicesArray){
            linearLayoutContainer.addView(getLinearLayoutRowChoice(choice));
        }

    }

    private View getLinearLayoutRowChoice(GIChoice choice){
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View viewChoice = inflater.inflate(R.layout.row_choice_of_poll_for_scrollview, null);
        TextView textViewChoice = (TextView) viewChoice.findViewById(R.id.textViewChoice);
        CheckBox checkBoxChoice = (CheckBox) viewChoice.findViewById(R.id.checkboxChoice);
        viewChoice.setOnClickListener(this);
        textViewChoice.setText(choice.choice);
        itemListArray.add(new ItemList(checkBoxChoice, textViewChoice, viewChoice));
        return viewChoice;
    }

    private void refreshOnCheck(View view){

        if(!isQCM){
            for(int i=0; i<itemListArray.size(); i++){
                if(view != itemListArray.get(i).viewChoice
                        && itemListArray.get(i).checkBox.isChecked())
                    itemListArray.get(i).checkBox.setChecked(false);

                int position = getPositionOfChoice(arrayChoicesChosen, choicesArray.get(i).choice);
                if(position != -1)
                    arrayChoicesChosen.remove(position);
            }
        }

        for(int i=0; i<itemListArray.size(); i++){
            if(view == itemListArray.get(i).viewChoice
                    && itemListArray.get(i).checkBox.isChecked()){
                itemListArray.get(i).checkBox.setChecked(false);
                int position = getPositionOfChoice(arrayChoicesChosen, choicesArray.get(i).choice);
                if(position != -1)
                    arrayChoicesChosen.remove(position);
                arrayChoicesChosen.remove(position);
            }

            else if(view == itemListArray.get(i).viewChoice
                    && !itemListArray.get(i).checkBox.isChecked()) {
                itemListArray.get(i).checkBox.setChecked(true);
                arrayChoicesChosen.add(choicesArray.get(i).choice);
            }
        }
    }

    private void refreshOnCheck(CheckBox checkBox, boolean isChecked){
        if(isQCM)
            return;
        for(int i=0; i<itemListArray.size(); i++){
            if(checkBox == itemListArray.get(i).checkBox && isChecked){
                arrayChoicesChosen.add(choicesArray.get(i).choice);
            }
            else if(checkBox == itemListArray.get(i).checkBox && !isChecked) {
                arrayChoicesChosen.remove(getPositionOfChoice(arrayChoicesChosen, choicesArray.get(i).choice));
            }
        }
    }

    private int getPositionOfChoice(ArrayList<String> choices, String choice){
        for(int i=0; i<choices.size(); i++){
            if(choices.get(i).equals(choice)){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<String> getChoicesChosen(){
        return arrayChoicesChosen;
    }

    @Override
    public void onClick(View view) {
        Log.v("∆∆DD∆∆††††††", "CLICK CLICK CLICK CLICK");
        refreshOnCheck(view);
    }

    public class ItemList {
        public View viewChoice = null;
        public TextView textView = null;
        public CheckBox checkBox = null;

        public ItemList(CheckBox checkBox, TextView textView, View viewChoice) {
            this.viewChoice = viewChoice;
            this.checkBox = checkBox;
            this.textView = textView;
        }
    }
}
