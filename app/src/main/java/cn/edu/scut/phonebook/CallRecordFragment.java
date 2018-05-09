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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CallRecordFragment extends Fragment {

    private Activity currentActivity;
    private List<Calllog> calllogList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;


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
    @Override
    public void onResume() {
        super.onResume();
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
}
