package cn.edu.scut.phonebook;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private ViewPager viewPager;//滑动的Tab页
    private FragmentPagerAdapter pagerAdapter;//管理viewPager
    private List<Fragment> Pages = new ArrayList<>();//放fragment页
    //private FragmentPagerAdapter fragmentPagerAdapter;
    //private FragmentStatePagerAdapter fragmentStatePagerAdapter;

    private LinearLayout MyInfoLayout;
    private LinearLayout CallRecordLayout;
    private LinearLayout ContactsLayout;

    private Button MyInfo; //我的
    private Button CallRecord; //通话记录
    private Button Contacts; // 联系人
    private Button BitScan; //扫一扫

    private MyInfoFragment myInfoFragment;
    private CallRecordFragment callRecordFragment;
    private ContactsFragment contactsFragment;

    private int REQUEST_CODE_SCAN = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_main);

        //动态获取读取联系人权限
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_CONTACTS },1);
        }
        else{
            //Toast.makeText(this, "未获取读取联系人权限", Toast.LENGTH_SHORT).show();
        }

        // 动态获取拨号权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
        }else {
            //Toast.makeText(this, "未获取通话权限", Toast.LENGTH_SHORT).show();
        }
        // 动态获取读取通话记录权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CALL_LOG}, 1);
        }

        //初始化各组件
        initParts();
        initButtonsClickEvents();

        //隐藏状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


    }

    //初始化各组件
    private void initParts()
    {
        viewPager =(ViewPager)findViewById(R.id.ViewPager);

        MyInfoLayout = (LinearLayout)findViewById(R.id.MyInfoLayout);
        CallRecordLayout = (LinearLayout)findViewById(R.id.CallRecordLayout);
        ContactsLayout = (LinearLayout)findViewById(R.id.ContactsLayout);

        MyInfo = (Button)findViewById(R.id.MyInfo_Btn);
        CallRecord = (Button)findViewById(R.id.CallRecord_Btn);
        Contacts = (Button)findViewById(R.id.Contacts_Btn);

        LayoutInflater layoutInflater = LayoutInflater.from(this);


        /*
         * 使用View 加载
        View MyInfoView  = layoutInflater.inflate(R.layout.myinfolayout,null);
        View CallRecallView  =layoutInflater.inflate(R.layout.callrecordlayout,null);
        View ContactsView  = layoutInflater.inflate(R.layout.contactslayout,null);

        Pages.add(MyInfoView);
        Pages.add(CallRecallView);
        Pages.add(ContactsView);
        */

        myInfoFragment = new MyInfoFragment();
        callRecordFragment = new CallRecordFragment();
        contactsFragment = new ContactsFragment();


        Pages.add(callRecordFragment);
        Pages.add(contactsFragment);
        Pages.add(myInfoFragment);

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                //把对应的Fragment传递给FragmentPagerAdapter
                return Pages.get(position);
            }

            @Override
            public int getCount() {
                return Pages.size();
            }
        };

        viewPager.setAdapter(pagerAdapter);
    }
    //初始化按钮的点击事件
    private void initButtonsClickEvents()
    {
        MyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });
        CallRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void initFragment()
    {

    }
}
