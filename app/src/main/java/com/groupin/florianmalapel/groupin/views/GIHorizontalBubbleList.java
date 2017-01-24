package com.groupin.florianmalapel.groupin.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.groupin.florianmalapel.groupin.R;
import com.groupin.florianmalapel.groupin.model.dbObjects.GIUser;
import com.groupin.florianmalapel.groupin.transformations.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by florianmalapel on 09/01/2017.
 */

public class GIHorizontalBubbleList extends HorizontalScrollView implements View.OnClickListener {

    private static final int MARGIN_END_BUBBLE = 30;

    private Context context = null;
    private LinearLayout layout_container = null;
    private int screen_width = 0;

    private ArrayList<BubbleItem> array_bubbleItems = null;

    private OnClick listener = null;

    public interface OnClick {
        void onClickBubble(int position);
        void onClickAdd();
    }

    public GIHorizontalBubbleList(Context context) {
        super(context);
    }

    public GIHorizontalBubbleList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GIHorizontalBubbleList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void createContainerScrollView(OnClick listener){
        array_bubbleItems = new ArrayList<>();
        this.listener = listener;
        if(context == null)
            context = getContext();

        layout_container = new LinearLayout((context == null) ? getContext() : context);
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_container.setOrientation(LinearLayout.HORIZONTAL);
        layout_container.setLayoutParams(params);
        layout_container.setPadding((int)context.getResources().getDimension(R.dimen.size10dp),
                (int)context.getResources().getDimension(R.dimen.size10dp),
                (int)context.getResources().getDimension(R.dimen.size10dp),
                (int)context.getResources().getDimension(R.dimen.size10dp));
        screen_width = getResources().getDisplayMetrics().widthPixels - (2*((int)context.getResources().getDimension(R.dimen.size10dp)));
        layout_container.requestLayout();
        this.addView(layout_container);
    }

    public void addImageViewAddMoreIcon(int color){
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_add_black_24dp));
        imageView.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        imageView.setOnClickListener(this);
        LinearLayout.LayoutParams imageView_linearParams = new LinearLayout.LayoutParams((int)context.getResources().getDimension(R.dimen.size50dp), (int)context.getResources().getDimension(R.dimen.size50dp));
        imageView_linearParams.setMargins(0, 0, (int)context.getResources().getDimension(R.dimen.size10dp), 0);
        imageView.setLayoutParams(imageView_linearParams);

        layout_container.addView(imageView);
        array_bubbleItems.add(new BubbleItem(imageView, null));
    }

    public void addBubbleInViewWithPicasso(GIUser user){
        ImageView imageView = new ImageView(context);
        Picasso.with(context).load(user.photoURL).transform(new CircleTransform()).into(imageView);
        imageView.setOnClickListener(this);
        /*if(imageView.getDrawable() != null) {
            CircleTransform circleTransform = new CircleTransform();
            Bitmap normalImage = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            Bitmap circleImage = circleTransform.transform(normalImage);
            imageView.setImageBitmap(circleImage);
        }*/

        LinearLayout.LayoutParams imageView_linearParams = new LinearLayout.LayoutParams((int)context.getResources().getDimension(R.dimen.size50dp), (int)context.getResources().getDimension(R.dimen.size50dp));
        imageView_linearParams.setMargins(0, 0, (int)context.getResources().getDimension(R.dimen.size10dp), 0);
        imageView.setLayoutParams(imageView_linearParams);

        layout_container.addView(imageView);
        array_bubbleItems.add(new BubbleItem(imageView, null));
    }

    @Override
    public void onClick(View view) {

        for(int position = 0; position<array_bubbleItems.size(); position++){
            if(view == array_bubbleItems.get(position).imageViewBubble){
                if(position == 0)
                    listener.onClickAdd();
                else listener.onClickBubble(position);
            }
        }
    }

    public class BubbleItem {
        public ImageView imageViewBubble = null;
        public GIUser user = null;

        public BubbleItem(ImageView imageViewBubble, GIUser user) {
            this.imageViewBubble = imageViewBubble;
            this.user = user;
        }
    }
}
