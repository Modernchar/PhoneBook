package cn.edu.scut.phonebook;

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
    boolean showBackground = false;
    private Paint paint = new Paint();//画笔
    private Paint rectPaint = new Paint();
    private int currentChooseLetterIndex = -1;

    private OnTouchListener onTouchListener;

    private float rectWidth;
    private float singleHeight;
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
        rectPaint.setColor(Color.parseColor("#CCCCCC"));
        rectWidth = paint.measureText("#");
        //TextTip = findViewById(R.id.TextTipView);
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

        if (showBackground) {
            canvas.drawColor(Color.parseColor("#CC0000"));
        }
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度

        Log.i("height", "height:" + height);
        Log.i("width", "width:" + width);

        // 获取每一个字母的高度
        singleHeight = (height * 1f) / LetterList.length; //获取单字最大高度
        //singleHeight = (height * 1f - singleHeight / 3) / LetterList.length; //设置字间总间距为
        for (int i = 0; i < LetterList.length; i++) {
            paint.setColor(Color.rgb(23, 122, 0));
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT);//设置字体样式
            paint.setAntiAlias(true);//抗锯齿
            paint.setTextSize(30);
            // 选中的状态
            if (i == currentLetter) {
                paint.setColor(Color.parseColor("#c60000")); //选中的字体为红色
                paint.setFakeBoldText(true); //字体加粗
            }
            // x坐标等于中间-字符串宽度的一半.
            // 坐标锚点为字母的左下角
            float xPos = width / 2 - paint.measureText(LetterList[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(LetterList[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = currentLetter;
        //final OnTouchListener listener;

        final int chooseLetterIndex = (int) (y / getHeight() * LetterList.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                //setBackgroundDrawable(new ColorDrawable(0x00000000));
                currentLetter = -1;//
                invalidate();//重绘
                if (TextTip != null) {
                    TextTip.setVisibility(View.INVISIBLE);
                }
                break;
            // 除开松开事件的任何触摸事件
            default:
                //setBackgroundResource(R.drawable.sidebar_background);

                //Log.i("TextTip","otherMotionEvent");
                if (oldChoose != chooseLetterIndex) {
                    if (chooseLetterIndex >= 0 && chooseLetterIndex < LetterList.length) {

                        Log.i("MotionEvent","now:"+LetterList[chooseLetterIndex]);
                        if (TextTip != null) {
                            TextTip.setText(LetterList[chooseLetterIndex]);
                            TextTip.setVisibility(View.VISIBLE);
                            // 动态改变文字dialog的位置
                            //int right = TextTip.getLeft();
                            //TextTip.setX(right / 2 * 3);
                            /*
                            if (chooseLetterIndex > 24) {
                                TextTip.setY(singleHeight * 24);
                            } else {
                                TextTip.setY(singleHeight * chooseLetterIndex);
                            }
                            */
                            //变色
                            //TextTip.setBackground(getContext().getResources().getDrawable(dialogColor[c / 6]));
                        }

                        currentLetter = chooseLetterIndex;
                        invalidate();//重绘
                    }
                }

                break;
        }
        return true;
    }


    /**
     * 向外公开的方法
     *
     * @param //onTouchingLetterChangedListener public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
     *                                          this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
     *                                          }
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                float y = getY();//获取Y坐标
            }
        }

        return true;
    }

    public String getChooseLetter()
    {
        return LetterList[currentLetter];
    }



}
