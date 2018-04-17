package cn.edu.scut.phonebook;

/*
 * 联系人列表的适配器
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactsPersonListAdapter extends RecyclerView.Adapter<ContactsPersonListAdapter.ViewHolder>{

    private ArrayList<ContactsPerson> Persons;
    private Activity currentActivity;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView Tag; // 字母标签
        TextView Name;
        View Line;
        View thisView; //保存子项最外围的布局实例


        public ViewHolder(View view)
        {
            super(view);
            Name = (TextView)view.findViewById(R.id.ContactsPersonListView_item_Name);
            Tag = (TextView)view.findViewById(R.id.ContactsPersonListView_item_Tag);
            Line = view.findViewById(R.id.ContactsPersonListView_item_TopLine);

            thisView = view;
        }
    }

    public ContactsPersonListAdapter(Activity activity, ArrayList<ContactsPerson> p)
    {
        this.Persons = p;
        currentActivity = activity;
    }

    //获取一个视图
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactspersonlist_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        // 点击整个联系人触发的事件
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ContactsPerson person  = Persons.get(position);

                // 打开一个新的活动
                Intent intent = new Intent(currentActivity,ContactsPersonCardActivity.class);
                intent.putExtra("ContactsPerson",person);
                currentActivity.startActivity(intent);
            }
        });
        return holder;
    }

    // 当子项被滚到屏幕内时执行
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactsPerson person  = Persons.get(position);
        holder.Name.setText(person.getName());
        holder.Tag.setText(person.getLastNameFirstLetter().trim().toUpperCase()); // 设置字母标签并且大写

        if(position == 0)
        {

            holder.Tag.setVisibility(View.VISIBLE);
            holder.Line.setVisibility(View.GONE);
        }
        else
        {
            String LastPersonLastNameFirstLetter = Persons.get(position -1).getLastNameFirstLetter();
            if(Persons.get(position).getLastNameFirstLetter().equals(LastPersonLastNameFirstLetter))
            {
                holder.Tag.setVisibility(View.GONE);
                holder.Line.setVisibility(View.VISIBLE);
            }
            else{
                holder.Tag.setVisibility(View.VISIBLE);
                holder.Line.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return Persons.size();
    }


}
