package cn.edu.scut.phonebook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class CalllogSort extends AppCompatActivity {

    private List<Calllog> calllogList = new ArrayList<>();
    SortAdapter adapter ;
    RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calllog_sort);

        //隐藏状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


        Intent getIntent = getIntent();
        final String phone = getIntent.getStringExtra("key");

        final CallLogUtils callLogUtils = new CallLogUtils();
        calllogList = callLogUtils.RecordSort(this,phone);

        recyclerView = (RecyclerView) findViewById(R.id.sort_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Button delete_all = (Button) findViewById(R.id.delete_all);
        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CalllogSort.this);
                builder.setTitle("提示");
                builder.setMessage("确定要删除吗？");
                builder.setCancelable(true);
                builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //提示
                        Toast.makeText(CalllogSort.this, "已删除 ", Toast.LENGTH_SHORT).show();
                        //移除list对应的数据
                        CallLogUtils.DeleteRecord(CalllogSort.this,phone);
                        List<Calllog> temp = new ArrayList<>();
                        adapter = new SortAdapter(CalllogSort.this,temp);
                        recyclerView.setAdapter(adapter);
                    }
                });
                builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
        adapter = new SortAdapter(this,calllogList);
        recyclerView.setAdapter(adapter);

    }


}
