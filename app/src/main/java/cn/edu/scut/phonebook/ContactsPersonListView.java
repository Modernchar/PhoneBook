package cn.edu.scut.phonebook;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


public class ContactsPersonListView extends RecyclerView {

    // 讲道理，这个逐层的构造函数封装我是服气的
    public ContactsPersonListView(Context context)
    {
        this(context,null);
    }
    public ContactsPersonListView(Context context, @Nullable AttributeSet attr)
    {
        this(context,attr,0);
    }
    public ContactsPersonListView(Context context, @Nullable AttributeSet attr, int def)
    {
        super(context,attr,def);
    }


}
