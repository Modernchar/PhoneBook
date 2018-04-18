package cn.edu.scut.phonebook;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class CalllogAdapter extends RecyclerView.Adapter<CalllogAdapter.ViewHolder> {

    private List<Calllog> mCalllogList;
    private Activity currentActivity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        ImageView type;
        TextView lDate;
        TextView duration;
        View delete;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.calllog_name);
            number = (TextView) view.findViewById(R.id.calllog_number);
            type = (ImageView) view.findViewById(R.id.calllog_type);
            lDate = (TextView) view.findViewById(R.id.calllog_lDate);
            duration = (TextView) view.findViewById(R.id.calllog_duration);
        }
    }

    public CalllogAdapter(Activity currentActivity,List<Calllog> calllogList) {
        this.currentActivity = currentActivity;
        mCalllogList = calllogList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calllog_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.delete = view.findViewById(R.id.delete_button);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i=holder.getAdapterPosition();//获取index

                //移除数据库数据(根据ID删除)
                int id=mCalllogList.get(i).get_id();
                if(id>=0)
                    CallLogUtils.DeleteRecord(currentActivity,id);
                //移除list对应的数据
                mCalllogList.remove(i);
                notifyDataSetChanged();
                //提示
                String s = "已删除 "+mCalllogList.get(i).getName();
                Toast.makeText(parent.getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Calllog calllog = mCalllogList.get(position);
        String na=calllog.getName();
        if(na.length()>12){
            na=na.substring(0,11)+"...";
        }
        holder.name.setText(na);
        holder.number.setText(calllog.getNumber());
        holder.type.setImageResource(calllog.getType());
        holder.lDate.setText(calllog.getLDate());
        holder.duration.setText(calllog.getDuration());
    }

    @Override
    public int getItemCount() {
        return mCalllogList.size();
    }

}
