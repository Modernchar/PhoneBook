package cn.edu.scut.phonebook;

/*
 * 联系人列表的适配器
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsPersonListAdapter extends RecyclerView.Adapter<ContactsPersonListAdapter.ViewHolder> {

    private ArrayList<ContactsPerson> Persons;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView Tag; // 字母标签
        TextView Name;


        public ViewHolder(View view)
        {
            super(view);
            Name = view.findViewById(R.id.ContactsPersonListView_item_Name);
            Tag = view.findViewById(R.id.ContactsPersonListView_item_Tag);
        }
    }

    public ContactsPersonListAdapter(ArrayList<ContactsPerson> p)
    {
        this.Persons = p;
    }

    //获取一个视图
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactspersonlist_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
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
        }
        else
        {
            String LastPersonLastNameFirstLetter = Persons.get(position -1).getLastNameFirstLetter();
            if(Persons.get(position).getLastNameFirstLetter().equals(LastPersonLastNameFirstLetter))
            {
                holder.Tag.setVisibility(View.GONE);
            }
            else{
                holder.Tag.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return Persons.size();
    }


}
