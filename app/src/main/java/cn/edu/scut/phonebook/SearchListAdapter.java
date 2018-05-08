package cn.edu.scut.phonebook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SearchListAdapter extends ArrayAdapter<ContactsPerson> {

    private int resourceid;
    public SearchListAdapter(Context context, int resourceid, List<ContactsPerson> persons)
    {
        super(context,resourceid,persons);
        this.resourceid = resourceid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContactsPerson person = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceid,parent,false);
        TextView Name = (TextView)view.findViewById(R.id.Searchlist_item_Name_Text);
        TextView PhoneNum = (TextView)view.findViewById(R.id.Searchlist_item_PhoneNum_Text);
        Name.setText(person.getName());
        if(!person.isNumberSearchResultEmpty())
        {
            PhoneNum.setText(person.getSearchResult().get(0));
        }
        else{
            PhoneNum.setVisibility(View.GONE);
        }
        return view;
    }
}
