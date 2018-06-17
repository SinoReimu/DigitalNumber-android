package cn.tecotaku.cn.digital_clock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Number {

    private int startX, startY;
    private int lineLength;
    private int color;
    private int margin = 2;
    private int offset = 10;

    public int currentStatus;
    public int nextStatus;
    public float process;
    public ValueAnimator animator;

    private Paint paint;
    int [][] lines = new int[7][];

    public Number(int x, int y, int length) {
        this(x, y, length, Color.parseColor("#66CCFF"), 14, 0);
    }

    public Number(int x, int y, int length, int initColor, int shadowRadius,int initStatus) {
        startX = x;
        startY = y;
        lineLength = length;
        color = initColor;
        nextStatus = currentStatus = initStatus;
        process = 0;

        initLines();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShadowLayer(shadowRadius, 0, 0, initColor);
        paint.setColor(initColor);
        paint.setStrokeWidth(lineLength/25);
    }

    private void initLines() {
        lines[0] = new int[]{startX, startY, 0};
        lines[1] = new int[]{startX, startY, 1};
        lines[2] = new int[]{startX+lineLength, startY, 1};
        lines[3] = new int[]{startX, startY+lineLength, 0};
        lines[4] = new int[]{startX, startY+lineLength, 1};
        lines[5] = new int[]{startX+lineLength, startY+lineLength, 1};
        lines[6] = new int[]{startX, startY+2*lineLength, 0};
    }

    public void onDraw(Canvas canvas) {
        for(int i=0; i<lines.length; i++) {
            int[] line = lines[i];
            if(line[2]==0) {
                setDrawPaint(getProcessIndex(i, 0));
                canvas.drawLine(line[0]+margin, line[1]+offset*(1-getProcessIndex(i, 0)),
                        line[0]+lineLength/2-margin, line[1]+offset*(1-getProcessIndex(i, 0)), paint);
                setDrawPaint(getProcessIndex(i, 1));
                canvas.drawLine(line[0]+lineLength/2+margin, line[1]+offset*(1-getProcessIndex(i, 1))/2,
                        line[0]+lineLength-margin, line[1]+offset*(1-getProcessIndex(i, 1))/2, paint);
            } else {
                setDrawPaint(getProcessIndex(i, 0));
                canvas.drawLine(line[0]+offset*(1-getProcessIndex(i, 0)), line[1]+margin,
                        line[0]+offset*(1-getProcessIndex(i, 0)), line[1]+lineLength/2-margin, paint);
                setDrawPaint(getProcessIndex(i, 1));
                canvas.drawLine(line[0]+offset*(1-getProcessIndex(i, 1))/2, line[1]+lineLength/2+margin,
                        line[0]+offset*(1-getProcessIndex(i, 1))/2, line[1]+lineLength-margin, paint);
            }
        }
    }

    void setDrawPaint(float process) {
        paint.setAlpha((int)(35+220*process));
        margin = 2+(int)((1-process)*10);
    }

    float getProcessIndex(int lineIndex, int subLineIndex) {
        float trueProcess;
        if(subLineIndex==1)
            trueProcess = process - 0.2f;
        else
            trueProcess = process;
        if(trueProcess > 1.0f) trueProcess = 1.0f;
        if(trueProcess < 0f) trueProcess = 0f;
        int from = Consts.numbers[currentStatus][lineIndex];
        int to = Consts.numbers[nextStatus][lineIndex];
        trueProcess = from + (to - from) * trueProcess;
        return trueProcess;
    }

    public void updateNumber(int newStatus) {
        newStatus%=10;
        nextStatus = newStatus;
        if(nextStatus != currentStatus) {

            if(animator != null&&animator.isRunning()) {
                animator.end();
                animator = ValueAnimator.ofFloat(1.2f-process, 1.2f).setDuration(800);
            } else
                animator = ValueAnimator.ofFloat(0, 1.2f).setDuration(800);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    currentStatus = nextStatus;
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    process = (float)valueAnimator.getAnimatedValue();
                }
            });
            animator.start();
        }
    }

    public Paint getPaint() {
        return paint;
    }

}
