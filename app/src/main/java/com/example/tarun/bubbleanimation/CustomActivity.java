package com.example.tarun.bubbleanimation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tarun.bubbleanimation.model.BubbleModel;

import java.util.ArrayList;
import java.util.List;

import rb.popview.PopField;

public class CustomActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout parentContainer;
    private ArrayList<BubbleModel> mBubbleModelList, mainList;
    //  private ExplosionField explosionField;
    private PopField popField;
    private boolean mRemovedAllStress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        getSupportActionBar().hide();
        mBubbleModelList = new ArrayList<>();
        mainList = new ArrayList<>();
        //explosionField = ExplosionField.attach2Window(this);
        popField = PopField.attach2Window(this);

        mRemovedAllStress = false;
        // fill list
        for (int i = 0; i < 3; i++) {
            BubbleModel bubbleModel = new BubbleModel();
            bubbleModel.setId(i);
            bubbleModel.setTitle("Bubble " + i);
            bubbleModel.setBurst(false);
            mBubbleModelList.add(bubbleModel);
        }
        mainList = (ArrayList<BubbleModel>) mBubbleModelList.clone();
        parentContainer = (RelativeLayout) findViewById(R.id.parent_container);
        textViewArray = new TextView[mBubbleModelList.size()];

        for (int i = 0; i < mBubbleModelList.size(); i++) {
            textViewArray[i] = new TextView(this);
        }

        addInitialBubble();
           /* try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/


    }

    private void addInitialBubble() {
        if (mBubbleModelList.size() > 0)
            addView(mBubbleModelList.get(currentIndex));
    }

    private int currentIndex = 0;

    private TextView[] textViewArray;


    private void addView(BubbleModel bubbleModel) throws IllegalStateException {
        final TextView tv1 = textViewArray[currentIndex];

        RelativeLayout.LayoutParams lprams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        lprams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        if (bubbleModel.getId() % 2 == 0) {
            lprams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lprams.rightMargin = 100;
        } else {
            lprams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lprams.leftMargin = 100;
        }
       /* if (currentIndex > 0 && currentIndex < 5) {
            Log.e("PreviousId", textViewArray[currentIndex - 1].getId() + "");
            lprams.addRule(RelativeLayout.ABOVE, textViewArray[currentIndex - 1].getId());
        }*/
        // lprams.bottomMargin = 100;

        //lprams.topMargin = 100;

        //    final TextView tv1 = new TextView(this);

        tv1.setText(bubbleModel.getTitle());
        //tv1.setBackgroundColor(Color.RED);
        tv1.setBackground(ContextCompat.getDrawable(this, R.drawable.circle));
        tv1.setPadding(30, 10, 30, 10);
        tv1.setGravity(Gravity.CENTER);
        tv1.setLayoutParams(lprams);
        tv1.setId(bubbleModel.getId());
        parentContainer.addView(tv1);


        ViewTreeObserver vto = parentContainer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parentContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = parentContainer.getMeasuredWidth();
                int height = parentContainer.getMeasuredHeight();

                Log.e("Height", height + "");
               /* TranslateAnimation tAnimation = new TranslateAnimation(0, 0, 0, -height);
                tAnimation.setDuration(15000);
                tAnimation.setRepeatCount(android.view.animation.Animation.INFINITE);
                tAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                tAnimation.setFillAfter(true);
                tAnimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub

                    }
                });
                tv1.startAnimation(tAnimation);*/

                // Move the object at the X position 500
                ObjectAnimator anim;
                //anim = ObjectAnimator.ofFloat(tv1, "y", 15f);
                anim = ObjectAnimator.ofFloat(tv1, "translationY", 200, -(height));
               /* if (currentIndex == 0)
                anim = ObjectAnimator.ofFloat(tv1, "y", 10f);
                else if (currentIndex == 1)
                    anim = ObjectAnimator.ofFloat(tv1, "y", 15f);
                else if (currentIndex == 2)
                    anim = ObjectAnimator.ofFloat(tv1, "y", 20f);
                else if (currentIndex == 3)
                    anim = ObjectAnimator.ofFloat(tv1, "y", 25f);
                else
                    anim = ObjectAnimator.ofFloat(tv1, "y", 30f);*/
                anim.setDuration(10000);
               /* if (currentIndex == 0)
                anim.setDuration(10000); // duration 40 seconds
                else if (currentIndex == 1)
                    anim.setDuration(20000);
                    else if (currentIndex == 2)
                        anim.setDuration(30000);
                else if (currentIndex == 3)
                    anim.setDuration(40000);
                else if (currentIndex == 4)
                    anim.setDuration(50000);*/
                // anim.setRepeatCount(1);
                anim.setInterpolator(null);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.e("calling", "onAnimationStart " + tv1.getId());

                        addBubble();

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.e("calling", "onAnimationEnd " + tv1.getId());
                        // remove view
                        parentContainer.removeView(tv1);
                        if (parentContainer.getChildCount() == 0) {
                            currentIndex = 0;
                            addInitialBubble();
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Log.e("calling", "onAnimationCancel");
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        Log.e("calling", "onAnimationRepeat" + tv1.getId());
                        if (tv1.getVisibility() == View.INVISIBLE) {
                            tv1.setVisibility(View.VISIBLE);
                            parentContainer.removeView(tv1);
                        }

                        if (parentContainer.getChildCount() == 0) {
                            //Toast.makeText(CustomActivity.this, "You have removed your all stress", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                anim.start();

            }
        });


        tv1.setOnClickListener(this);

    }

    private void addBubble() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentIndex++;
                if (currentIndex == mBubbleModelList.size()) {
                    currentIndex = 0;
                }
                if (currentIndex < mBubbleModelList.size()) {
                    try {
                        // check view already added or not

                        addView(mBubbleModelList.get(currentIndex));
                    } catch (IllegalStateException ex) {
                        // view already added
                        ex.printStackTrace();
                    }

                }
            }
        }, 3000);
    }


    @Override
    public void onClick(View v) {
      /*  if (v.getId() == textViewArray[0].getId()) {
            Toast.makeText(CustomActivity.this, textViewArray[0].getText(), Toast.LENGTH_SHORT).show();
            v.setVisibility(View.INVISIBLE);
        }
        else if (v.getId() == mBubbleModelList.get(1).getId()) {
            Toast.makeText(CustomActivity.this, "Clicked2", Toast.LENGTH_SHORT).show();
            v.setVisibility(View.INVISIBLE);
        }
        else if (v.getId() == mBubbleModelList.get(2).getId()) {
            Toast.makeText(CustomActivity.this, "Clicked3", Toast.LENGTH_SHORT).show();
            v.setVisibility(View.INVISIBLE);
        }
        else if (v.getId() == mBubbleModelList.get(3).getId()) {
            Toast.makeText(CustomActivity.this, "Clicked4", Toast.LENGTH_SHORT).show();
            v.setVisibility(View.INVISIBLE);
        }
        else if (v.getId() == mBubbleModelList.get(4).getId()) {
            Toast.makeText(CustomActivity.this, "Clicked5", Toast.LENGTH_SHORT).show();
            v.setVisibility(View.INVISIBLE);
        }
        */
        for (int i = 0; i < textViewArray.length; i++) {
            if (v.getId() == textViewArray[i].getId()) {
                Toast.makeText(CustomActivity.this, textViewArray[i].getText(), Toast.LENGTH_SHORT).show();
                //v.setVisibility(View.INVISIBLE);
                // explosionField.explode(textViewArray[i]);
                mainList.get(i).setBurst(true);
                mBubbleModelList.remove(i);
                Log.e("RemainingBubble", mBubbleModelList.size() + "");
                popField.popView(textViewArray[i]);

                break;
            }
        }

        // in background
       /* new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        new Thread() {
            public void run() {
                mRemovedAllStress = true;
                for (BubbleModel bubbleModel : mainList) {
                    if (!bubbleModel.isBurst()) {
                        mRemovedAllStress = false;
                        break;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mRemovedAllStress) {
                            Toast.makeText(CustomActivity.this, "Well done! You have destroyed all of your stress!!!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }.start();*/
    }
}
