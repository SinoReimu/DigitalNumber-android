package cn.tecotaku.cn.digital_clock.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import cn.tecotaku.cn.digital_clock.Number;
import cn.tecotaku.cn.digital_clock.R;

public class ClockView extends View {

    int width = -1;
    int height = -1;
    int s = 0;

    int lineColor;
    int shadowRadius;

    StaticHandler myHandler;
    ArrayList<Number> nums = new ArrayList();

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        myHandler = new StaticHandler((Activity)getContext());

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyDigitalClock);

        lineColor = typedArray
                .getColor(R.styleable.MyDigitalClock_lineColor, Color.parseColor("#66ccff"));
        shadowRadius = typedArray.getInteger(R.styleable.MyDigitalClock_shadowRadius,
                14);
        typedArray.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(width==-1||height==-1) {
            width = canvas.getWidth();
            height = canvas.getHeight();

            initNumber();
            startClock();

        } else {
            for(int i=0; i<nums.size(); i++)
                nums.get(i).onDraw(canvas);
        }
    }

    private void updateNumber(int numIndex, int newNumber) {
        Message msg = new Message();
        msg.what = 0x001;
        msg.obj = nums.get(numIndex);
        msg.arg1 = newNumber;
        myHandler.sendMessage(msg);
    }

    private void startClock() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        s+=1;
                        if(s==40) {
                            s=0;
                            Time t=new Time();
                            t.setToNow();
                            updateNumber(0, t.hour/10);
                            updateNumber(1, t.hour%10);

                            updateNumber(2, t.minute/10);
                            updateNumber(3, t.minute%10);

                            updateNumber(4, t.second/10);
                            updateNumber(5, t.second%10);
                        }
                        postInvalidate();
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initNumber() {
        int length = width/10;  //0.6 width
        int textMargin = width/35; //total 7 margin 0.2 width
        int marginLeft = width/10;
        int marginTop = height/2 - length;

        int x = marginLeft;
        nums.add(new Number(x, marginTop, length, lineColor, shadowRadius, 0));
        x += (length+textMargin);
        nums.add(new Number(x, marginTop, length, lineColor, shadowRadius, 0));
        x += (length+2*textMargin);
        nums.add(new Number(x, marginTop, length, lineColor, shadowRadius, 0));
        x += (length+textMargin);
        nums.add(new Number(x, marginTop, length, lineColor, shadowRadius, 0));
        x += (length+2*textMargin);
        nums.add(new Number(x, marginTop, length, lineColor, shadowRadius, 0));
        x += (length+textMargin);
        nums.add(new Number(x, marginTop, length, lineColor, shadowRadius, 0));
    }

    static class StaticHandler extends Handler {

        WeakReference<Activity> mActivity;

        public StaticHandler(Activity activity) {
            //构造创建弱引用
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            //通过弱引用获取外部类.
            Activity activity = mActivity.get();
            //进行非空再操作
            if (activity != null) {
                if(msg.what==0x001) {
                    final Number num = (Number) msg.obj;
                    final int nextStatus = msg.arg1;
                    num.updateNumber(nextStatus);
                }
            }
        }
    }

}
