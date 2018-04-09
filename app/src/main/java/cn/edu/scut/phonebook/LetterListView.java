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
    private Paint paint = new Paint();//字画笔
    private Paint circlePaint = new Paint();//圆画笔
    private int currentChooseLetterIndex = -1;

    private OnTouchListener onTouchListener;
    private LetterListViewListener letterListViewListener;

    private float SpaceWidth; //尾部留白距离
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
        circlePaint.setColor(Color.parseColor("#DDDDDD"));

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


        int height = getHeight();// 获取组件高度
        int width = getWidth(); // 获取组件宽度

        SpaceWidth = height / 8f ;
        singleHeight = (height * 1f - SpaceWidth) / LetterList.length; //获取单字最大高度

        for (int i = 0; i < LetterList.length; i++) {
            paint.setColor(Color.rgb(23, 122, 0));
            paint.setTypeface(Typeface.DEFAULT);//设置字体样式
            paint.setAntiAlias(true);//抗锯齿
            paint.setTextSize(30);

            float xPos = width / 2 - paint.measureText(LetterList[i]) / 2;
            float yPos = singleHeight * (i+1);

            float xCircle = width/2;
            float yCircle = singleHeight * (i+1) - paint.measureText(LetterList[i])/2;
            // 选中的状态
            if (i == currentLetter) {
                canvas.drawCircle(xCircle,yCircle,30,circlePaint);
                paint.setColor(Color.parseColor("#c60000")); //选中的字体为红色
                paint.setFakeBoldText(true); //字体加粗
            }
            // x坐标等于中间-字符串宽度的一半.
            // 坐标锚点为字母的左下角

            canvas.drawText(LetterList[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    //   @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        final int action = event.getAction();
//        final float y = event.getY();// 点击y坐标
//        final int oldChoose = currentLetter;
//        //final OnTouchListener listener;
//
//        final int chooseLetterIndex = (int) (y / getHeight() * LetterList.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
//
//        switch (action) {
//            case MotionEvent.ACTION_UP:
//                //setBackgroundDrawable(new ColorDrawable(0x00000000));
//                currentLetter = -1;//
//                invalidate();//重绘
//                if (TextTip != null) {
//                    TextTip.setVisibility(View.INVISIBLE);
//                }
//                break;
//            // 除开松开事件的任何触摸事件
//            default:
//                //setBackgroundResource(R.drawable.sidebar_background);
//
//                //Log.i("TextTip","otherMotionEvent");
//                if (oldChoose != chooseLetterIndex) {
//                    if (chooseLetterIndex >= 0 && chooseLetterIndex < LetterList.length) {
//
//                        Log.i("MotionEvent","now:"+LetterList[chooseLetterIndex]);
//                        if (TextTip != null) {
//                            TextTip.setText(LetterList[chooseLetterIndex]);
//                            TextTip.setVisibility(View.VISIBLE);
//                            // 动态改变文字dialog的位置
//                            //int right = TextTip.getLeft();
//                            //TextTip.setX(right / 2 * 3);
//                            /*
//                            if (chooseLetterIndex > 24) {
//                                TextTip.setY(singleHeight * 24);
//                            } else {
//                                TextTip.setY(singleHeight * chooseLetterIndex);
//                            }
//                            */
//                            //变色
//                            //TextTip.setBackground(getContext().getResources().getDrawable(dialogColor[c / 6]));
//                        }
//
//                        currentLetter = chooseLetterIndex;
//                        invalidate();//重绘
//                    }
//                }
//
//                break;
//        }
//        return true;
//    }


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
                Log.i("onTouchEvent","Action:"+event.getAction());
                float y = getY();//获取Y坐标

                final int chooseLetterIndex = (int) (y / (getHeight() - SpaceWidth) * LetterList.length);

                if (chooseLetterIndex != currentLetter) {
                    currentLetter = chooseLetterIndex;
                }

                if (letterListViewListener != null && 0 <= currentLetter && currentLetter < LetterList.length) {
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



}
