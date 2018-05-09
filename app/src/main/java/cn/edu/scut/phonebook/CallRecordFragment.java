package cn.edu.scut.phonebook;

/*
 * 通话记录页
 * 写在onCreate方法中的内容可以写到onActivityCreated里
 * 使用currentActivity成员变量获取当前活动
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CallRecordFragment extends Fragment {

    private Activity currentActivity;
    private List<Calllog> calllogList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;

    private LinearLayout T9btn0;
    private LinearLayout T9btn1;
    private LinearLayout T9btn2;
    private LinearLayout T9btn3;
    private LinearLayout T9btn4;
    private LinearLayout T9btn5;
    private LinearLayout T9btn6;
    private LinearLayout T9btn7;
    private LinearLayout T9btn8;
    private LinearLayout T9btn9;
    private LinearLayout T9btn10;
    private LinearLayout T9btn11;

    private Button T9_CallBtn;
    private Button T9_HideBtn;
    private TextView T9_ResultView;
    private Button T9_ShowBtn;

    private Button T9_DeleteBtn;
    private LinearLayout T9_LinearLayout;
    private RelativeLayout T9_ResultLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //加载布局
        View view = inflater.inflate(R.layout.callrecordlayout,container,false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("onAttach","context");

        //获取当前Activity
        currentActivity = getActivity();
        if(currentActivity == null)
        {
            Log.i("unexpected", "In onAttach:currentActivity is null");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //新建工具类
        Log.i("OnActivityCreated","getin");
        //获取通话记录信息
        calllogList=CallLogUtils.GetRecords(currentActivity);

        recyclerView = (RecyclerView) currentActivity.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(currentActivity);
        recyclerView.setLayoutManager(layoutManager);
        final FloatingActionButton uptt = (FloatingActionButton) currentActivity.findViewById(R.id.uptt);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean loading = true;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (loading) {
                    if (layoutManager.findFirstVisibleItemPosition() >= 9) {
                        uptt.setVisibility(View.VISIBLE);
                    } else {
                        uptt.setVisibility(View.GONE);
                    }
                }
            }
        });

        uptt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        CalllogAdapter adapter = new CalllogAdapter(currentActivity,calllogList);
        recyclerView.setAdapter(adapter);

        initT9KeyBoard();
        setT9KeyListener();
    }
    @Override
    public void onResume() {
        super.onResume();

        Log.i("OnActivityCreated","Resume");
        //获取通话记录信息
        calllogList=CallLogUtils.GetRecords(currentActivity);

        recyclerView = (RecyclerView) currentActivity.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(currentActivity);
        recyclerView.setLayoutManager(layoutManager);
        final FloatingActionButton uptt = (FloatingActionButton) currentActivity.findViewById(R.id.uptt);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean loading = true;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (loading) {
                    if (layoutManager.findFirstVisibleItemPosition() >= 9) {
                        uptt.setVisibility(View.VISIBLE);
                    } else {
                        uptt.setVisibility(View.GONE);
                    }
                }
            }
        });

        uptt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        CalllogAdapter adapter = new CalllogAdapter(currentActivity,calllogList);
        recyclerView.setAdapter(adapter);
    }

    public void initT9KeyBoard()
    {
        T9btn0 = currentActivity.findViewById(R.id.T9_btn0);
        T9btn1 = currentActivity.findViewById(R.id.T9_btn1);
        T9btn2 = currentActivity.findViewById(R.id.T9_btn2);
        T9btn3 = currentActivity.findViewById(R.id.T9_btn3);
        T9btn4 = currentActivity.findViewById(R.id.T9_btn4);
        T9btn5 = currentActivity.findViewById(R.id.T9_btn5);
        T9btn6 = currentActivity.findViewById(R.id.T9_btn6);
        T9btn7 = currentActivity.findViewById(R.id.T9_btn7);
        T9btn8 = currentActivity.findViewById(R.id.T9_btn8);
        T9btn9 = currentActivity.findViewById(R.id.T9_btn9);
        T9btn10 = currentActivity.findViewById(R.id.T9_btn10);
        T9btn11 = currentActivity.findViewById(R.id.T9_btn11);

        T9_CallBtn = (Button)currentActivity.findViewById(R.id.T9_btn_call);
        T9_HideBtn = (Button)currentActivity.findViewById(R.id.T9_btn_hide);

        T9_ResultView = (TextView)currentActivity.findViewById(R.id.T9_ResultView);

        T9_DeleteBtn = (Button)currentActivity.findViewById(R.id.T9_btn_delete);
        T9_ShowBtn = (Button)currentActivity.findViewById(R.id.T9_btn_show);

        T9_LinearLayout = currentActivity.findViewById(R.id.T9_LinearLayout);
        T9_ResultLayout = currentActivity.findViewById(R.id.T9_ResultLayout);

        T9_ResultLayout.setVisibility(View.GONE);
        T9_ShowBtn.setVisibility(View.GONE);
    }

    public void setT9KeyListener()
    {
        T9btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"0");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"1");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"2");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"3");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"4");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"5");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"6");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"7");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"8");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"9");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"*");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });
        T9btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_ResultView.setText(T9_ResultView.getText().toString()+"#");
                T9_ResultLayout.setVisibility(View.VISIBLE);
            }
        });

        T9_DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = T9_ResultView.getText().toString();
                if(s.length()>0){
                    s = s.substring(0,s.length()-1);
                    T9_ResultView.setText(s);
                }
                if(s.length()==0){
                    T9_ResultLayout.setVisibility(View.GONE);
                }
            }
        });

        T9_CallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = T9_ResultView.getText().toString();
                CallLogUtils.call(currentActivity, s);
            }
        });

        T9_HideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_LinearLayout.setVisibility(View.GONE);
                T9_ShowBtn.setVisibility(View.VISIBLE);
            }
        });
        T9_ShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T9_LinearLayout.setVisibility(View.VISIBLE);
                T9_ShowBtn.setVisibility(View.GONE);
            }
        });
    }
}
