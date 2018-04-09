package cn.edu.scut.phonebook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


public class ContactsFragment extends Fragment {

    public LetterListView letterListView;
    private Activity currentActivity;

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
        if (letterListView != null) {
            letterListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Log.i("onTouch", "action:" + motionEvent.getAction());
                    return false;
                }
            });
        }
        else{
            Log.d("unexpected","letterListView is null");
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }
}
