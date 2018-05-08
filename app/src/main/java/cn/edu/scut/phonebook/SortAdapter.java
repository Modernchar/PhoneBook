package cn.edu.scut.phonebook;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by RangerJJB on 2018/5/8.
 */

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {

    private List<Calllog> mCalllogList;
    private Activity currentActivity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView type;
        TextView lDate;
        TextView duration;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.calllog_name);
            type = (ImageView) view.findViewById(R.id.calllog_type);
            lDate = (TextView) view.findViewById(R.id.calllog_lDate);
            duration = (TextView) view.findViewById(R.id.calllog_duration);
        }
    }

    public SortAdapter(List<Calllog> calllogList) {
        mCalllogList = calllogList;
    }

    public void AddCalllog(int position,Calllog calllog){
        mCalllogList.add(position,calllog);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_show, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i=holder.getAdapterPosition();
                final String phone;
                if(mCalllogList.get(i).getNumber()=="") {
                    phone = mCalllogList.get(i).getName();
                }
                else {
                    phone = mCalllogList.get(i).getNumber();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle("提示");
                builder.setMessage(phone);
                builder.setCancelable(true);
                builder.setPositiveButton("呼叫", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CallLogUtils.call(currentActivity, phone);
                        TelephonyManager tm = (TelephonyManager) parent.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                        while (true) {
                            try {
                                Thread.sleep(1000);
                                tm.getCallState();
                                if(tm.getCallState()==TelephonyManager.CALL_STATE_IDLE) {
                                    AddCalllog(0,CallLogUtils.getFirst(currentActivity));
                                    break;
                                }
                            }
                            catch (Exception e) {
                                Toast.makeText(parent.getContext(), "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.create().show();
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
        holder.type.setImageResource(calllog.getType());
        holder.lDate.setText(calllog.getLDate());
        holder.duration.setText(calllog.getDuration());
    }

    @Override
    public int getItemCount() {
        return mCalllogList.size();
    }

}
