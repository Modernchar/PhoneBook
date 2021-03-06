package cn.edu.scut.phonebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class ContactsFragment extends Fragment implements LetterListView.LetterListViewListener,Runnable{

    public LetterListView letterListView;
    private Activity currentActivity;
    private TextView TextTip;
    private Handler handler;
    private ArrayList<ContactsPerson> Persons;

    private RecyclerView ContactsPersonListView;
    private LinearLayoutManager linearLayoutManager;
    private ContactsPersonListAdapter adapter;

    private SearchView searchView;
    private ListView searchListView;
    private LinearLayout searchInfoLinearLayout;
    private TextView searchInfoTextView;


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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("initPart","contactFragmentbegin");

        Log.i("onActivityCreated","onActivityCreated");
        letterListView = new LetterListView(currentActivity);

        if(currentActivity == null)
        {
            Log.i("unexpected", "currentActivity is null");
        }
        letterListView =  (LetterListView) currentActivity.findViewById(R.id.LetterListView);
        letterListView.setLetterListViewListener(this);

        TextTip = (TextView)currentActivity.findViewById(R.id.TextTipView);

        // 搜索部分初始化
        searchView = (SearchView)currentActivity.findViewById(R.id.SearchView);
        searchListView = (ListView)currentActivity.findViewById(R.id.SearchListView);
        searchInfoLinearLayout = (LinearLayout)currentActivity.findViewById(R.id.SearchInfo_LinearLayout);
        searchInfoTextView = (TextView)currentActivity.findViewById(R.id.SearchInfo_Text);


        searchView.setIconifiedByDefault(false); // 禁止收缩成图标
        searchView.clearFocus(); // 清除焦点
        setSearchViewHintTextSize();
        searchInfoLinearLayout.setVisibility(View.GONE);


        // 搜索框搜索联系人
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    searchInfoLinearLayout.setVisibility(View.GONE);
                }
                else {
                    searchInfoLinearLayout.setVisibility(View.VISIBLE);
                    // 获取搜索结果
                    final ArrayList<ContactsPerson> SearchPerson = ContactsUtils.searchContacts(Persons, newText);

                    searchInfoTextView.setText("找到"+ SearchPerson.size()+"位联系人");
                    SearchListAdapter SearchAdapter = new SearchListAdapter(currentActivity,R.layout.searchlist_item,SearchPerson);
                    searchListView.setAdapter(SearchAdapter);

                    searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ContactsPerson p =SearchPerson.get(position);

                            Intent intent = new Intent(currentActivity,ContactsPersonCardActivity.class);
                            intent.putExtra("ContactsPerson",p);
                            currentActivity.startActivity(intent);
                        }
                    });
                    // Toast.makeText(currentActivity, "字符改变", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });



        FloatingActionButton addContactActivityButton = (FloatingActionButton) currentActivity.findViewById(R.id.add_contact_activity_button);
        addContactActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity, AddContactsActivity.class);
                startActivityForResult(intent,100); // 100 请求保存结果
            }
        });

        ContactsPersonListView = (RecyclerView)currentActivity.findViewById(R.id.ContactsPersonListView);
        ContactsPersonListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //letterListView.setTouchIndex();
                Log.i("onScrolled","dy:"+dy);
            }
        });


        linearLayoutManager = new LinearLayoutManager(currentActivity);
        ContactsPersonListView.setLayoutManager(linearLayoutManager);


        // 这里查询非常慢，需要新建一个线程优化一下
        //  new Thread(this).run();

        // TAG 1

        // 下面这两句原来是放在 onActivityCreated 最后的 TAG 1的地方
        // ——————TAG 1 START ——————
        Log.i("initPart","contactFragmentend1");
        Persons  = ContactsUtils.getContactsPersonList(currentActivity);
        Log.i("initPart","contactFragmentend2");
        // 提前加载会不会快一点？
        // 并不会
        adapter = new ContactsPersonListAdapter(currentActivity,Persons);
        // ——————TAG 1 END ——————

        ContactsPersonListView.setAdapter(adapter);
        Log.i("initPart","contactFragmentend");

    }

    @Override
    public void wordChange(String letter) {
        Log.i("wordChange","current letter:"+letter);
        updateTextTip(letter);
        updateContactsPersonListView(letter);
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

    // 联系人列表滑动至指定位置
    private void updateContactsPersonListView(String letter)
    {
        for(int i=0;i<Persons.size();i++)
        {
            String PersonNameLetter = Persons.get(i).getLastNameFirstLetter() ;

            // 如果指定的字母与联系人数组中的姓名首字母匹配，列表滑动到当前位置
            if(letter.equals(PersonNameLetter))
            {

                // 滑动到开头，舍弃偏移值
                linearLayoutManager.scrollToPositionWithOffset(i,0);
                linearLayoutManager.setStackFromEnd(true);

                return;
            }
        }
    }



    @Override
    public void run() {

        adapter = new ContactsPersonListAdapter(currentActivity,Persons);
        ContactsPersonListView.setAdapter(adapter);
    }


    public void setSearchViewHintTextSize()
    {
        EditText textView = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        textView.setHintTextColor(ContextCompat.getColor(currentActivity, R.color.UnselectedGray));
        textView.setTextSize(16);
        searchView.setQueryHint(currentActivity.getString(R.string.searchContactPerson));
    }

    @Override
    public void onResume() {
        super.onResume();

        // Persons  = ContactsUtils.getContactsPersonList(currentActivity);
        adapter = new ContactsPersonListAdapter(currentActivity,Persons);
        ContactsPersonListView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case 100:{ // 获得联系人保存之后传回来的数据
                if(resultCode == RESULT_OK)
                {
                    ContactsPerson NewlyPerson = (ContactsPerson)data.getSerializableExtra("Newly_Save_ContactsPerson");
                    adapter.addContactsPersonToListView(ContactsUtils.getContactsPersonPositionInList(NewlyPerson,Persons),NewlyPerson);
                    break;
                }
            }
        }
    }
}
