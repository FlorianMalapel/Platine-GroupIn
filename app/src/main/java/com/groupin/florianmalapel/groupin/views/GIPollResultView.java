package com.groupin.florianmalapel.groupin.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eralp.circleprogressview.CircleProgressView;
import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.model.GIApplicationDelegate;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIChoice;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIPoll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by florianmalapel on 28/01/2017.
 */

public class GIPollResultView extends LinearLayout implements View.OnClickListener {

    private Context context = null;
    private STATE_VIEW viewState = STATE_VIEW.SMALL;
    private STATE_RESPOND respondState = STATE_RESPOND.HAS_NOT_RESPOND;
    private PollListener listener = null;
    private GIPoll poll = null;
    private View pollView = null;
    private LinearLayout linearLayoutChoicesStats = null;
    private TextView textViewPollQuestion = null;
    private ImageButton imageButtonSeeMore = null;
    private GISelectChoiceInListView choiceList = null;
    private Button buttonAnswer = null;

    private enum STATE_VIEW {
        SMALL, LARGE
    }

    private enum STATE_RESPOND {
        HAS_NOT_RESPOND,RESPONDED
    }

    public interface PollListener {
        void validateChoice(JSONObject objectPollAnswer);
    }


    public GIPollResultView(Context context) {
        super(context);
        initialize(context);
    }

    public GIPollResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public GIPollResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context){
        this.context = context;
        this.setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        pollView = inflater.inflate(R.layout.row_recycler_view_polls, this);
        linearLayoutChoicesStats = (LinearLayout) pollView.findViewById(R.id.linearLayoutChoices);
        textViewPollQuestion = (TextView) pollView.findViewById(R.id.textViewQuestion);
        imageButtonSeeMore = (ImageButton) pollView.findViewById(R.id.imageButtonSeeMore);
        buttonAnswer = (Button) pollView.findViewById(R.id.buttonAnswer);
        buttonAnswer.setOnClickListener(this);
        imageButtonSeeMore.setOnClickListener(this);
        choiceList = (GISelectChoiceInListView) pollView.findViewById(R.id.choiceList);
    }

    public void setPollInView(GIPoll poll, PollListener listener){
        this.poll = poll;
        this.listener = listener;
        if(!poll.hasAlreadyVote)
            respondState = STATE_RESPOND.HAS_NOT_RESPOND;
        else respondState = STATE_RESPOND.RESPONDED;
        initViewWithPoll();
    }

    private void initViewWithPoll(){
        textViewPollQuestion.setText(poll.question);
        if(respondState == STATE_RESPOND.RESPONDED) {
            if (viewState == STATE_VIEW.SMALL) {
                linearLayoutChoicesStats.removeAllViews();
                linearLayoutChoicesStats.addView(getLinearLayoutViewChoice(poll.getCurrentTopChoice()));
                imageButtonSeeMore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_up));
            } else if (viewState == STATE_VIEW.LARGE) {
                linearLayoutChoicesStats.removeAllViews();
                imageButtonSeeMore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down));
                for (GIChoice choice : poll.listChoice) {
                    linearLayoutChoicesStats.addView(getLinearLayoutViewChoice(choice));
                }
            }
        }
        else if(respondState == STATE_RESPOND.HAS_NOT_RESPOND){
            imageButtonSeeMore.setVisibility(GONE);
            linearLayoutChoicesStats.setVisibility(GONE);
            if(viewState == STATE_VIEW.SMALL) {
                choiceList.setVisibility(GONE);
                buttonAnswer.setText(context.getString(R.string.answer));
            }
            else {
                choiceList.setVisibility(VISIBLE);
                choiceList.initializeWithChoices(poll.listChoice, poll.isQcm);
                buttonAnswer.setText(context.getString(R.string.valid));
            }
            buttonAnswer.setVisibility(VISIBLE);
        }
    }

    private View getLinearLayoutViewChoice(GIChoice choice){
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View viewChoice = inflater.inflate(R.layout.row_poll_choice_result, null);
        TextView textViewChoice = (TextView) viewChoice.findViewById(R.id.textViewChoice);
        CircleProgressView circleProgressViewChoice = (CircleProgressView) viewChoice.findViewById(R.id.circle_progress_view);
        textViewChoice.setText(choice.choice);
        circleProgressViewChoice.setProgress(choice.percentage);
        return viewChoice;
    }

    public void changeView(){
        if(respondState == STATE_RESPOND.RESPONDED) {
            if (viewState == STATE_VIEW.SMALL) {
                viewState = STATE_VIEW.LARGE;
            } else if (viewState == STATE_VIEW.LARGE) {
                viewState = STATE_VIEW.SMALL;
            }
        }
        else if(respondState == STATE_RESPOND.HAS_NOT_RESPOND){
            if (viewState == STATE_VIEW.SMALL) {
                viewState = STATE_VIEW.LARGE;
            } else if (viewState == STATE_VIEW.LARGE) {
                listener.validateChoice(getPollAnswerJSONObject());
                return;
            }
        }
        initViewWithPoll();
    }

    private JSONObject getPollAnswerJSONObject(){
        JSONObject pollAnswer = new JSONObject();
        try {
            pollAnswer.put("uid", GIApplicationDelegate.getInstance().getDataCache().getUserUid());
            pollAnswer.put("group", poll.groupId);
            pollAnswer.put("idVote", poll.pollId);
            JSONArray arrayChoicesJSON = new JSONArray();
            for(GIChoice choice : poll.listChoice){
                JSONObject choiceJSON = new JSONObject();
                choiceJSON.put("choix", choice.choice);
                if(choiceList.getChoicesChosen().contains(choice.choice))
                    choiceJSON.put("reponse", true);
                else choiceJSON.put("reponse", false);
                arrayChoicesJSON.put(choiceJSON);
            }
            pollAnswer.put("choix", arrayChoicesJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pollAnswer;
    }

    @Override
    public void onClick(View view) {
        changeView();
    }
}
