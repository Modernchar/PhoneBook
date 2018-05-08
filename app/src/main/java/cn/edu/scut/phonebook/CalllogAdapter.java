package cn.edu.scut.phonebook;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
        ImageView more_inf;
        View delete;
        TextView Location;
        TextView Carrier;
        View Border; // 分割线

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.calllog_name);
            number = (TextView) view.findViewById(R.id.calllog_number);
            type = (ImageView) view.findViewById(R.id.calllog_type);
            lDate = (TextView) view.findViewById(R.id.calllog_lDate);
            duration = (TextView) view.findViewById(R.id.calllog_duration);
            more_inf = (ImageView) view.findViewById(R.id.more_inf);
            Location = (TextView) view.findViewById(R.id.calllog_location);
            Carrier = (TextView) view.findViewById(R.id.calllog_carri);
            Border = view.findViewById(R.id.calllog_border);
        }
    }

    public CalllogAdapter(Activity currentActivity,List<Calllog> calllogList) {
        this.currentActivity = currentActivity;
        mCalllogList = calllogList;
    }

    public void AddCalllog(int position,Calllog calllog){
        mCalllogList.add(position,calllog);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calllog_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.delete = view.findViewById(R.id.delete_button);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle("提示");
                builder.setMessage("确定要删除吗？");
                builder.setCancelable(true);
                builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int i = holder.getAdapterPosition();//获取index
                        //移除数据库数据(根据ID删除)
                        int id = mCalllogList.get(i).get_id();
                        if (id >= 0) CallLogUtils.DeleteRecord(currentActivity, id);
                        //提示
                        String s = "已删除 " + mCalllogList.get(i).getName();
                        Toast.makeText(parent.getContext(), s, Toast.LENGTH_SHORT).show();
                        //移除list对应的数据
                        mCalllogList.remove(i);
                        notifyDataSetChanged();
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
        holder.name.setOnClickListener(new View.OnClickListener() {
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
                        /*TelephonyManager tm = (TelephonyManager) parent.getContext().getSystemService(Context.TELEPHONY_SERVICE);
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
                        }*/
                    }
                });
                builder.create().show();
            }
        });
        holder.number.setOnClickListener(new View.OnClickListener() {
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
                        /*TelephonyManager tm = (TelephonyManager) parent.getContext().getSystemService(Context.TELEPHONY_SERVICE);
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
                        }*/
                    }
                });
                builder.create().show();
            }
        });
        holder.more_inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = holder.getAdapterPosition();
                String phone;
                if(mCalllogList.get(i).getName()=="") {
                    phone = mCalllogList.get(i).getNumber();
                }
                else {
                    phone = mCalllogList.get(i).getName();
                }
                Intent intent = new Intent(view.getContext(),CalllogSort.class);
                intent.putExtra("key",phone);
                view.getContext().startActivity(intent);
            };
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Calllog calllog = mCalllogList.get(position);
        String na=calllog.getName();
        if(na.length()>12){
            na=na.substring(0,11)+"...";
        }

        // 删除多余字符
        String num = calllog.getNumber().replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        String name = calllog.getName().replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");

        holder.name.setText(na);
        holder.number.setText(calllog.getNumber());
        holder.type.setImageResource(calllog.getType());
        holder.lDate.setText(calllog.getLDate());
        holder.duration.setText(calllog.getDuration());
        if(!num.equals("")) {
            holder.Location.setText(PhoneUtil.getGeo(num, 86));
            holder.Carrier.setText(PhoneUtil.getCarrier(num, 86));
            holder.Border.setVisibility(View.VISIBLE);

            holder.more_inf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ArrayList<ContactsPerson> persons = ContactsUtils.getContactsPersonList(currentActivity);

                    ContactsPerson person = ContactsUtils.findPersonByName(calllog.getName(),persons);

                    Intent intent = new Intent(view.getContext(),ContactsPersonCardActivity.class);
                    intent.putExtra("ContactsPerson",person);
                    view.getContext().startActivity(intent);
                };
            });
        }
        else
        {
            holder.Location.setText(PhoneUtil.getGeo(name, 86));
            holder.Carrier.setText(PhoneUtil.getCarrier(name, 86));
            holder.Border.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mCalllogList.size();
    }

}
