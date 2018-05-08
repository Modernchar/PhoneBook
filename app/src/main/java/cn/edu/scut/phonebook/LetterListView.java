package cn.edu.scut.phonebook;
/*
 * 自定义的通讯录字母侧滑功能
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import cn.edu.scut.phonebook.R;

public class LetterListView extends View {

    private int currentLetter = -1;//当前位置
    public static String[] LetterList = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "#"};//所有字母
    private Paint paint = new Paint();//字画笔
    private Paint circlePaint = new Paint();//圆画笔
    private int currentChooseLetterIndex = -1;

    private LetterListViewListener letterListViewListener;

    private float SpaceWidth; //尾部留白距离
    private float singleHeight;

    private float ViewHeight;
    private float ViewWidth;
    private TextView TextTip;


    //新写的构造函数们
    public LetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public LetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public LetterListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        circlePaint.setColor(Color.parseColor("#DDDDDD"));

        if(TextTip!=null)
        {
            TextTip.setText("I'm Here!");
        }
        else{
            Log.i("unexpected","TextTip is null");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i("onDraw", "onDraw");

        ViewHeight = getHeight();// 获取组件高度
        ViewWidth = getWidth(); // 获取组件宽度

        SpaceWidth = ViewHeight / 8f ;
        singleHeight = (ViewHeight * 1f - SpaceWidth) / LetterList.length; //获取单字最大高度

        for (int i = 0; i < LetterList.length; i++) {

            //设置画笔属性
            paint.setColor(Color.parseColor("#3bcebf"));
            paint.setTypeface(Typeface.DEFAULT);//设置字体样式
            paint.setAntiAlias(true);//抗锯齿
            paint.setTextSize(30);

            float xPos = ViewWidth / 2 - paint.measureText(LetterList[i]) / 2;
            float yPos = singleHeight * (i+1);

            float xCircle = ViewWidth / 2;
            float yCircle = singleHeight * (i+1) - paint.measureText(LetterList[i])/2;
            // 选中的状态
            if (i == currentLetter) {
                //canvas.drawCircle(xCircle,yCircle,30,circlePaint);  //用来画当前字母的背景
                paint.setColor(Color.parseColor("#c60000")); //选中的字体为红色
                paint.setFakeBoldText(true); //字体加粗
            }
            // x坐标等于中间-字符串宽度的一半.
            // 坐标锚点为字母的左下角

            canvas.drawText(LetterList[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                Log.i("onTouchEvent","Action:"+event.getAction());
                float y = event.getY();//获取Y坐标
                Log.i("onTouchEvent","y:"+y);
                Log.i("onTouchEvent","ViewHeight:"+ViewHeight);
                final int chooseLetterIndex = (int) (y / (ViewHeight - SpaceWidth) * LetterList.length);
                Log.i("onTouchEvent","(AFTER caLCULATION)chooseLetterIndex:"+chooseLetterIndex);

                if (chooseLetterIndex != currentLetter) {
                    currentLetter = chooseLetterIndex;
                }

                if (letterListViewListener != null && 0 <= chooseLetterIndex && chooseLetterIndex < LetterList.length) {
                    Log.i("onTouchEvent","chooseLetterIndex:"+chooseLetterIndex);
                    letterListViewListener.wordChange(LetterList[chooseLetterIndex]);
                }
                invalidate();//重绘


                break;
            }

            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    public String getChooseLetter()
    {
        return LetterList[currentLetter];
    }

    //自定义的监听器
    public interface LetterListViewListener
    {
        public void wordChange(String letter);

    }

    //设置监听器
    public void setLetterListViewListener(LetterListViewListener letterListViewListener)
    {
        this.letterListViewListener = letterListViewListener;
    }

    public void setTouchIndex(String letter)
    {
        for(int i=0;i<LetterList.length;i++)
        {
            if(LetterList[i].equals(letter))
            {
                currentLetter = i;
                invalidate();
                return;
            }
        }
    }



}
