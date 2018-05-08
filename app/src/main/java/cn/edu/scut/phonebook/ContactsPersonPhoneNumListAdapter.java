package cn.edu.scut.phonebook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactsPersonPhoneNumListAdapter extends RecyclerView.Adapter<ContactsPersonPhoneNumListAdapter.ViewHolder> {

    private ArrayList<String> PhoneNums;
    private Activity currentActivity;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView TeleComTag; // 运营商标签
        TextView PhoneNum;// 手机号码
        TextView LocationTag; // 归属地标签
        View Line; //顶线
        View thisView; //保存子项最外围的布局实例

        Button CallBtn; // 快速通话按钮
        Button MsgBtn; // 快速发送短信按钮


        public ViewHolder(View view)
        {
            super(view);
            PhoneNum = (TextView)view.findViewById(R.id.ContactsPersonPhoneNumsListView_item_PhoneNum);
            TeleComTag =(TextView) view.findViewById(R.id.ContactsPersonPhoneNumsListView_item_Tele);
            LocationTag =(TextView) view.findViewById(R.id.ContactsPersonPhoneNumsListView_item_Location);

            Line = view.findViewById(R.id.ContactsPersonPhoneNumsListView_item_TopLine);

            CallBtn = (Button)view.findViewById(R.id.ContactsPersonPhoneNumsListView_item_CallBtn);
            MsgBtn  =(Button)view.findViewById(R.id.ContactsPersonPhoneNumsListView_item_MsgBtn);

            thisView = view;
        }
    }

    public ContactsPersonPhoneNumListAdapter(Activity activity,ArrayList<String> Nums)
    {
        currentActivity = activity;
        PhoneNums = Nums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactspersonphonenumlist_item,parent,false);
        final ContactsPersonPhoneNumListAdapter.ViewHolder holder = new ContactsPersonPhoneNumListAdapter.ViewHolder(view);

        // 点击整个联系人触发的事件
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String num  = PhoneNums.get(position);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);// 设置活动类型
                intent.setData(Uri.parse("tel:"+num));// 设置数据
                currentActivity.startActivity(intent);// 开启意图


            }
        });
        // 点击拨号按钮触发的事件
        holder.CallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String num  = PhoneNums.get(position);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);// 设置活动类型
                intent.setData(Uri.parse("tel:"+num));// 设置数据
                currentActivity.startActivity(intent);// 开启意图
            }
        });
        // 点击发送短信按钮触发的事件
        holder.MsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String num  = PhoneNums.get(position);

                Uri uri2 = Uri.parse("smsto:"+num);
                Intent intentMessage = new Intent(Intent.ACTION_VIEW,uri2); // 创建意图
                currentActivity.startActivity(intentMessage);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String num  = PhoneNums.get(position);
        holder.PhoneNum.setText(num);

        // 删除多余字符
        String num_after = num.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");

        holder.TeleComTag.setText(PhoneUtil.getCarrier(num_after,86));
        holder.LocationTag.setText(PhoneUtil.getGeo(num_after,86)); // 号码所在地
        if(position == 0)
        {
            holder.Line.setVisibility(View.GONE);
        }
        else
        {
            holder.Line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return PhoneNums.size();
    }
}
