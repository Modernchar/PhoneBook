package cn.edu.scut.phonebook;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CallLogUtils {
    private static String DateExchange(long lDate){//把日期转化为String
        String sDate="";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date_today = sdf.format(date);
        Date callDate = new Date(lDate);
        String callDateStr = sdf.format(callDate);
        if (callDateStr.equals(date_today)) { //判断是否为今天
            sdf = new SimpleDateFormat("HH:mm");
            sDate = sdf.format(callDate);
        } else if (date_today.contains(callDateStr.substring(0, 7))) { //判断是否为当月
            sdf = new SimpleDateFormat("dd");
            int callDay = Integer.valueOf(sdf.format(callDate));

            int day = Integer.valueOf(sdf.format(date));
            if (day - callDay == 1) {
                sDate = "昨天";
            } else {
                sdf = new SimpleDateFormat("MM-dd");
                sDate = sdf.format(callDate);
            }
        } else if (date_today.contains(callDateStr.substring(0, 4))) { //判断是否为当年
            sdf = new SimpleDateFormat("MM-dd");
            sDate = sdf.format(callDate);
        }
        return sDate;
    }

    private static String DurationExchange(long duration){//把通话时长改成String
        String sduration="";
        int min = (int)duration / 60;
        int sec = (int)duration % 60;
        if (sec > 0) {
            if (min > 0) {
                sduration = min + "分" + sec + "秒";
            } else {
                sduration = sec + "秒";
            }
        }
        else{
            sduration="未接通";
        }
        return sduration;
    }

    public static void call(Activity activity, String phone) {
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},1);
        }else {
            Intent intent=new Intent(Intent.ACTION_CALL,CallLog.Calls.CONTENT_URI.parse("tel:"+phone));
            activity.startActivity(intent);
        }
    }

    public static void DeleteRecord(Activity activity,int id){
        ContentResolver contentResolver= activity.getContentResolver();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 1000);
        }
        contentResolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls._ID+"=?", new String[]{id+""});//根据ID删除数据

    }

    public static void DeleteRecord(Activity activity,String name){
        ContentResolver contentResolver= activity.getContentResolver();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 1000);
        }
        if(name!=null) {
            contentResolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.CACHED_NAME + "=?", new String[]{name});//根据名字删除该联系人全部数据
            contentResolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls.NUMBER+"=?", new String[]{name});
        }
    }

    public List<Calllog> RecordSort(Activity activity, String phone){//获取通话记录
        Cursor cursor = null;
        List<Calllog> calllogList = new ArrayList<>();
        ContentResolver contentResolver= activity.getContentResolver();
        //判断是否有权限
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 1000);
        }
        //系统方式获取通讯录存储地址，按日期倒序
        cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " desc");
        if (cursor == null)
            return null;

        while (cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));//ID
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));//姓名
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));//号码
            int _type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));//呼入1/呼出(2)/未接3
            long _lDate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));//拨打时间
            long _duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));//通话时长

            if(name==null) {
                name = number;
            }

            String lDate = DateExchange(_lDate);
            String duration = DurationExchange(_duration);

            int type;
            if(_type==1) {
                type=R.drawable.in;
            }
            else if(_type==2) {
                type=R.drawable.out;
            }
            else {
                type=R.drawable.down;
            }

            if(phone.equals(name)) {
                name = number;
                Calllog calllog = new Calllog(_id, _type, _lDate, _duration, name, number, type, lDate, duration);
                calllogList.add(calllog);
            }
        }
        cursor.close();
        return calllogList;
    }

    public static Calllog getFirst(Activity activity) {
        Cursor cursor = null;
        ContentResolver contentResolver= activity.getContentResolver();
        //判断是否有权限
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 1000);
        }
        //系统方式获取通讯录存储地址，按日期倒序
        cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " desc");
        if (cursor == null)
            return null;


        cursor.moveToFirst();
        int _id = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));//ID
        String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));//姓名
        String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));//号码
        int _type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));//呼入1/呼出(2)/未接3
        long _lDate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));//拨打时间
        long _duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));//通话时长

        if(name==null) {
            name = number;
            number = "";
        }

        String lDate = DateExchange(_lDate);
        String duration = DurationExchange(_duration);

        int type;
        if(_type==1) {
            type=R.drawable.in;
        }
        else if(_type==2) {
            type=R.drawable.out;
        }
        else {
            type=R.drawable.down;
        }
        Calllog calllog = new Calllog(_id, _type, _lDate, _duration, name, number, type, lDate, duration);


        cursor.close();
        return calllog;
    }

    public static List<Calllog> GetRecords(Activity activity){//获取通话记录
        Cursor cursor = null;
        List<Calllog> calllogList = new ArrayList<>();
        ContentResolver contentResolver= activity.getContentResolver();
        //判断是否有权限
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }
        //系统方式获取通讯录存储地址，按日期倒序
        cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " desc");
        if (cursor == null)
            return null;

        while (cursor.moveToNext()) {
            int _id = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));//ID
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));//姓名
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));//号码
            int _type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));//呼入1/呼出(2)/未接3
            long _lDate = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));//拨打时间
            long _duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION));//通话时长

            if(name==null) {
                name = number;
                number = "";
            }

            String lDate = DateExchange(_lDate);
            String duration = DurationExchange(_duration);

            int type;
            if(_type==1) {
                type=R.drawable.in;
            }
            else if(_type==2) {
                type=R.drawable.out;
            }
            else {
                type=R.drawable.down;
            }
            Calllog calllog = new Calllog(_id, _type, _lDate, _duration, name, number, type, lDate, duration);
            calllogList.add(calllog);
        }
        cursor.close();
        return calllogList;
    }

    public static String  GetTotalDuration(List<Calllog> calllogList){
        //按照严格标准相加
        /*......*/

        //按照收费标准，未满一分钟计费一分钟
        int sum=0;
        int n=calllogList.size();
        for(int i=0;i<n;i++){
            Calllog temp=calllogList.get(i);
            int min=(int)temp.get_duration()/60;
            int sec=(int)temp.get_duration()%60;
            if(sec>0)
                min++;
            sum+=min;
        }
        return ""+sum+"分钟";
    }

    public static String  GetTotalNum(List<Calllog> calllogList){
        //总联系人数量
        int sum=0;
        int n=calllogList.size();
        Map<String,Integer> map = new HashMap<String,Integer>();
        for(int i=0;i<n;i++){
            Calllog temp=calllogList.get(i);
            if(!map.containsKey(temp.getName()))
                map.put(temp.getName(),1);
        }
        sum=map.size();
        return ""+sum;
    }

    public static String  GetCurrentMonthDuration(List<Calllog> calllogList){
        //按照严格标准相加
        /*......*/

        //按照收费标准，未满一分钟计费一分钟
        int sum=0;
        int n=calllogList.size();
        for(int i=0;i<n;i++){
            Calllog temp=calllogList.get(i);
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date_today = sdf.format(date);
            Date callDate = new Date(temp.get_LDate());
            String callDateStr = sdf.format(callDate);
            if(date_today.contains(callDateStr.substring(0, 7))){
                int min=(int)temp.get_duration()/60;
                int sec=(int)temp.get_duration()%60;
                if(sec>0)
                    min++;
                sum+=min;
            }
        }
        return ""+sum+"分钟";
    }

    public static String  GetCurrentMonthNum(List<Calllog> calllogList){
        //当前月份人数
        int sum=0;
        int n=calllogList.size();
        Map<String,Integer> map = new HashMap<String,Integer>();
        for(int i=0;i<n;i++){
            Calllog temp=calllogList.get(i);
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date_today = sdf.format(date);
            Date callDate = new Date(temp.get_LDate());
            String callDateStr = sdf.format(callDate);
            if(date_today.contains(callDateStr.substring(0, 7))){
                if(!map.containsKey(temp.getName()))
                    map.put(temp.getName(),1);
            }
        }
        sum=map.size();
        return ""+sum;
    }

   static List<Calllog> FindRecords(List<Calllog> calllogs, Date Startdate, Date Enddate, String name){
       List<Calllog> result=new ArrayList<>();

       // 名字空
       if(name.equals("")|| name ==null){
           if(Startdate==null&&Enddate==null){
               return calllogs;
           }
           else {
               int len=calllogs.size();
               for(int i=0;i<len;i++){
                   Date callDate = new Date(calllogs.get(i).get_LDate());
                   if(callDate.compareTo(Startdate)>=0&&callDate.compareTo(Enddate)<=0)
                       result.add(calllogs.get(i));
               }
           }
       }
       // 名字不空
       else{
           if(Startdate==null && Enddate==null){ // 时间空

               int len=calllogs.size();
               Pattern MatchString = Pattern.compile(name);

               for(int i=0;i<len;i++){
                   String na=calllogs.get(i).getName();
                   String nu=calllogs.get(i).getNumber();

                   // 字符串匹配
                   Matcher NameMatch = MatchString.matcher(na);
                   Matcher NumMatch = MatchString.matcher(nu);

                   if(NameMatch.find()||(!nu.equals("") && NumMatch.find())) {

                       result.add(calllogs.get(i));
                   }
               }
               return result;
           }
           else {


               int len=calllogs.size();
               Pattern MatchString = Pattern.compile(name);
               for(int i=0;i<len;i++){
                   Date callDate = new Date(calllogs.get(i).get_LDate());
                   String na=calllogs.get(i).getName();
                   String nu=calllogs.get(i).getNumber();
                   // 字符串匹配
                   Matcher NameMatch = MatchString.matcher(na);
                   Matcher NumMatch = MatchString.matcher(nu);
                   if(callDate.compareTo(Startdate)>=0&&callDate.compareTo(Enddate)<=0&&(NameMatch.find()||(!nu.equals("") && NumMatch.find())))//修改3
                       result.add(calllogs.get(i));
               }
           }
       }
       return result;//修改2
   }

}
