package cn.edu.scut.phonebook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.icu.util.Measure;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ContactsFragment extends Fragment implements LetterListView.LetterListViewListener{

    public LetterListView letterListView;
    private Activity currentActivity;
    private TextView TextTip;
    private Handler handler;
    private ArrayList<ContactsPerson> Persons;

    private RecyclerView ContactsPersonListView;
    private LinearLayoutManager linearLayoutManager;
    private ContactsPersonListAdapter adapter;

    //有这样一种说法，Fragment的生命周期里，只有在onAttach()和onDetach()之间的时候getActivity()方法才不会返回null
    //emmmm但似乎依然会返回null 2018.4.8 1:05
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("onAttach","context");
        currentActivity = getActivity();
        if(currentActivity == null)
        {
            Log.i("unexpected", "In onAttach:currentActivity is null");
        }
        handler  = new Handler();
    }

    //这是个废弃的办法，只能用在Api 23以下的安卓版本中
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        currentActivity = activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //加载布局
        View view = inflater.inflate(R.layout.contactslayout, container, false);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i("onActivityCreated","onActivityCreated");
        letterListView = new LetterListView(currentActivity);

        if(currentActivity == null)
        {
            Log.i("unexpected", "currentActivity is null");
        }
        letterListView = (LetterListView) currentActivity.findViewById(R.id.LetterListView);
        letterListView.setLetterListViewListener(this);

        TextTip = currentActivity.findViewById(R.id.TextTipView);

        ContactsPersonListView = currentActivity.findViewById(R.id.ContactsPersonListView);
        linearLayoutManager = new LinearLayoutManager(currentActivity);
        ContactsPersonListView.setLayoutManager(linearLayoutManager);

        Persons  = ToolForContacts.getContactsPersonList(currentActivity);

        adapter = new ContactsPersonListAdapter(Persons);
        ContactsPersonListView.setAdapter(adapter);
    }

    @Override
    public void wordChange(String letter) {
        Log.i("wordChange","current letter:"+letter);
        updateTextTip(letter);
    }

    public void updateTextTip(String letter)
    {
        Log.i("updataTextTip","current letter:"+letter);
        TextTip.setText(letter);
        TextTip.setVisibility(View.VISIBLE);

        handler.removeCallbacksAndMessages(null);
        //500ms后让tv隐藏
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextTip.setVisibility(View.GONE);
            }
        }, 200);
    }

}
