package cn.edu.scut.phonebook;

/*
 * 我的信息页
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.codbking.widget.OnSureLisener;

public class MyInfoFragment extends Fragment {

    Date Startdate;
    Date Enddate;
    Button button_startDate;
    Button button_endDate;
    Button button_find;
    EditText editline;
    String name;//按姓名查找
    private List<Calllog> calllogList = new ArrayList<>();

    private Activity currentActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //加载布局
        View view = inflater.inflate(R.layout.myinfolayout,container,false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("onAttach","context");

        //获取当前的Activity
        currentActivity = getActivity();
        if(currentActivity == null)
        {
            Log.i("unexpected", "In onAttach:currentActivity is null");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        button_startDate = (Button) currentActivity.findViewById(R.id.button_startDate);
        button_endDate = (Button) currentActivity.findViewById(R.id.button_endDate);
        button_find = (Button) currentActivity.findViewById(R.id.button_find);
        editline = (EditText) currentActivity.findViewById(R.id.editText);

        button_startDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){//设置开始时间
                OnSureLisener onSureLisener = new OnSureLisener(){
                    @Override
                    public void onSure(Date date){
                        Date nowDate = new Date();
                        if(nowDate.compareTo(date)<0)
                            date=nowDate;
                        Startdate=date;
                        if(Enddate==null||Startdate.compareTo(Enddate)>0){//开始时间大于结束时间，则调整结束时间
                            Enddate=Startdate;
                            button_endDate.setText("结束时间 "+new SimpleDateFormat("yyyy-MM-dd").format(date));
                        }
                        button_startDate.setText("开始时间 "+new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                };
                showDatePickDialog(onSureLisener);
            }
        });
        button_endDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){//设置结束时间
                OnSureLisener onSureLisener = new OnSureLisener(){
                    @Override
                    public void onSure(Date date){
                        Date nowDate = new Date();
                        if(nowDate.compareTo(date)<0)
                            date=nowDate;
                        Enddate=date;
                        if(Startdate==null){
                            Startdate=Enddate;
                            button_startDate.setText("开始时间 "+new SimpleDateFormat("yyyy-MM-dd").format(Startdate));
                        }
                        else if(Startdate.compareTo(Enddate)>0){//开始时间大于结束时间，则调整结束时间
                            Date temp=Startdate;
                            Startdate=Enddate;
                            Enddate=temp;
                            button_startDate.setText("开始时间 "+new SimpleDateFormat("yyyy-MM-dd").format(Startdate));
                        }
                        button_endDate.setText("结束时间 "+new SimpleDateFormat("yyyy-MM-dd").format(Enddate));

                    }
                };
                showDatePickDialog(onSureLisener);
            }
        });
        button_find.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){//查找联系通话记录
                //显示
                //跳转页面
                name=editline.getText().toString();
                SearchCondition searchCondition=new SearchCondition(Startdate,Enddate,name);

                Intent intent = new Intent(currentActivity,FindRecordsActivity.class);
                intent.putExtra("searchcondition",searchCondition);
                startActivity(intent);
            }
        });
    }

    private void showDatePickDialog(OnSureLisener onSureLisener) {
        DatePickDialog dialog = new DatePickDialog(currentActivity);
        //设置上5年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(onSureLisener);
        dialog.show();
    }
}
