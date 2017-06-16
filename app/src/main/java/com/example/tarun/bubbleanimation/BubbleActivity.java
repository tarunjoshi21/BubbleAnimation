package com.example.tarun.bubbleanimation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tarun.bubbleanimation.model.BubbleModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import rb.popview.PopField;

public class BubbleActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout parentContainer;
    //private ArrayList<BubbleModel> mBubbleModelList, mainList;
    private ArrayList<BubbleModel>  mainList;
    //  private ExplosionField explosionField;
    private PopField popField;
    private Timer mTimer;
    private int projectedIndex = 0;
    private boolean isLeft;
    private ListIterator<BubbleModel> bubbleModelListIterator;
    private CopyOnWriteArrayList<BubbleModel> mBubbleModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);
        getSupportActionBar().hide();
       // mBubbleModelList = new ArrayList<>();
        mBubbleModelList = new CopyOnWriteArrayList();
        mainList = new ArrayList<>();
        popField = PopField.attach2Window(this);

        // fill list
        for (int i = 0; i < 20; i++) {
            BubbleModel bubbleModel = new BubbleModel();
            bubbleModel.setId(i);
            bubbleModel.setTitle("Bubble " + i);
            bubbleModel.setBurst(false);
            mBubbleModelList.add(bubbleModel);
            mainList.add(bubbleModel);
        }
        //mainList = (ArrayList<BubbleModel>) mBubbleModelList.clone();
        bubbleModelListIterator = mBubbleModelList.listIterator();
        parentContainer = (RelativeLayout) findViewById(R.id.parent_container);
        // And From your main() method or any other method
     /*   mTimer = new Timer();
        mTimer.schedule(new BubbleTimer(), 0, 3000);*/

       // calling first time
        addBubble(bubbleModelListIterator.next());
    }

    @Override
    public void onClick(View v) {
        Log.i("RemovingIndex", v.getId() + "");

        for (int i = 0; i < mBubbleModelList.size(); i++) {
            Log.i("CheckBubble", "ID" + mBubbleModelList.get(i).getId());
            if (v.getId() == mBubbleModelList.get(i).getId()) {
                Log.i("RemovingIndex", "confirm");
                mainList.get(v.getId()).setBurst(true);
                mBubbleModelList.remove(i);


                break;
            }

        }
        Log.e("RemainingBubble", mBubbleModelList.size() + "");
        popField.popView(v);
    }

    /**
     * Class that execute in each specified seconds and add bubble on every execution
     */
    private class BubbleTimer extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mBubbleModelList.size() > 0) {
                        // check if projected index size is equal to list size, then in this case, initialize
                        // projectedIndex to zero
                        if (projectedIndex >= mBubbleModelList.size()) {
                            projectedIndex = mBubbleModelList.get(0).getId();
                        }
                        // check if same bubble is already added on the parent view or not
                        View view = parentContainer.findViewById(projectedIndex);
                        if (view == null) {
                            // add bubble
                            try {
                                Log.e("Bubble", "Adding Bubble " + mBubbleModelList.get(projectedIndex).getId());
                                addBubble(mBubbleModelList.get(projectedIndex));
                            } catch (IndexOutOfBoundsException ex) {
                                projectedIndex = 0;
                                addBubble(mBubbleModelList.get(projectedIndex));
                            }
                        }
                        // add bubble
                        // increment projectedIndex
                        projectedIndex++;
                        Log.e("Bubble", "Index incremented "+projectedIndex);
                    } else {
                        // stops timer, you have destroy all of your stresses
                        cancel();
                    }
                }
            });


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void addBubble(final BubbleModel bubbleModel) {
        final TextView tv1 = new TextView(this);
        RelativeLayout.LayoutParams lprams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lprams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        if (isLeft) {
            lprams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lprams.rightMargin = 100;
            isLeft = false;
        } else {
            lprams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lprams.leftMargin = 100;
            isLeft = true;
        }
        tv1.setText(bubbleModel.getTitle());
        tv1.setBackground(ContextCompat.getDrawable(this, R.drawable.circle));
        tv1.setPadding(30, 10, 30, 10);
        tv1.setGravity(Gravity.CENTER);
        tv1.setLayoutParams(lprams);
        tv1.setId(bubbleModel.getId());
        parentContainer.addView(tv1);

        tv1.setOnClickListener(this);

        ViewTreeObserver vto = parentContainer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = parentContainer.getMeasuredHeight();
                final ObjectAnimator anim;
                anim = ObjectAnimator.ofFloat(tv1, "translationY", 200, -(height));
                anim.setDuration(10000);
                anim.setInterpolator(new LinearInterpolator());
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (bubbleModelListIterator.hasNext()) {
                                    try {
                                        addBubble(bubbleModelListIterator.next());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }
                        }, 3000);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        parentContainer.removeView(tv1);
                        if (parentContainer.getChildCount() == 0) {

                            if (mBubbleModelList.size() >0) {
                                bubbleModelListIterator = mBubbleModelList.listIterator();
                                addBubble(bubbleModelListIterator.next());
                            }
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                anim.start();
            }
        });
    }
}
