package cn.edu.scut.phonebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.codbking.widget.OnSureLisener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FindRecordsActivity extends AppCompatActivity {
    private List<Calllog> calllogList = new ArrayList<>();
    RecyclerView recyclerView;

    Date Startdate;
    Date Enddate;
    String name;//按姓名查找

    Button button_s;
    Button button_e;
    EditText editText_name;
    Button button_find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_records);

       /* if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/

        Intent intent=getIntent();
        SearchCondition searchCondition=(SearchCondition) intent.getSerializableExtra("searchcondition");

        //获取通话记录信息
        calllogList=CallLogUtils.GetRecords(this);
        //查找
        List<Calllog> tempcall = new ArrayList<>();
        tempcall=CallLogUtils.FindRecords(calllogList,searchCondition.GetStartTime(),searchCondition.GetEndTime(),searchCondition.GetName());

        //显示列表
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CalllogAdapter adapter = new CalllogAdapter(this,tempcall);
        recyclerView.setAdapter(adapter);


        //显示初始化按钮
        button_s = (Button)findViewById(R.id.button_s);
        button_e = (Button)findViewById(R.id.button_e);
        editText_name=(EditText)findViewById(R.id.editText_findName);
        button_find = (Button)findViewById(R.id.button_find2);


        if(searchCondition.GetEndTime()!=null){
            Enddate = searchCondition.GetEndTime();
            button_e.setText(new SimpleDateFormat("yyyy-MM-dd").format(searchCondition.GetEndTime()));
        }
        if(searchCondition.GetStartTime()!=null){
            Startdate = searchCondition.GetStartTime();
            button_s.setText(new SimpleDateFormat("yyyy-MM-dd").format(searchCondition.GetStartTime()));
        }

        button_s.setOnClickListener(new View.OnClickListener(){
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
                            button_e.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                        }
                        button_s.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
                    }
                };
                showDatePickDialog(onSureLisener);
            }
        });
        button_e.setOnClickListener(new View.OnClickListener(){
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
                            button_s.setText(new SimpleDateFormat("yyyy-MM-dd").format(Startdate));
                        }
                        else if(Startdate.compareTo(Enddate)>0){//开始时间大于结束时间，则调整结束时间
                            Date temp=Startdate;
                            Startdate=Enddate;
                            Enddate=temp;
                            button_s.setText(new SimpleDateFormat("yyyy-MM-dd").format(Startdate));
                        }
                        button_e.setText(new SimpleDateFormat("yyyy-MM-dd").format(Enddate));
                    }
                };
                showDatePickDialog(onSureLisener);
            }
        });
        button_find.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){//查找联系通话记录
                //显示
                name=editText_name.getText().toString();
                List<Calllog> tempcall = new ArrayList<>();
                tempcall =CallLogUtils.FindRecords(calllogList,Startdate,Enddate,name);
                CalllogAdapter adapter = new CalllogAdapter(FindRecordsActivity.this,tempcall);
                recyclerView.setAdapter(adapter);
            }
        });

        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ResarchString = editText_name.getText().toString();
                List<Calllog> Result =CallLogUtils.FindRecords(calllogList,Startdate,Enddate,ResarchString);
                CalllogAdapter adapter = new CalllogAdapter(FindRecordsActivity.this,Result);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void showDatePickDialog(OnSureLisener onSureLisener) {
        DatePickDialog dialog = new DatePickDialog(this);
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
