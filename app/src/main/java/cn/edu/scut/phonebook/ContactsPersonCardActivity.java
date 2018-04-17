package cn.edu.scut.phonebook;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.TextView;

public class ContactsPersonCardActivity extends AppCompatActivity {

    ContactsPerson person;
    private TextView NameTextView;

    private RecyclerView contactsPersonPhoneNumListView;
    private LinearLayoutManager linearLayoutManager;
    private ContactsPersonPhoneNumListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contacts_person_card);

        Intent intent = getIntent();
        person = (ContactsPerson)intent.getSerializableExtra("ContactsPerson");

        NameTextView = (TextView)findViewById(R.id.ContactsPersonNameTextView);


        NameTextView.setText(person.getName());

        //隐藏状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        initRecyclerViewParts();



    }

    private void initRecyclerViewParts()
    {
        contactsPersonPhoneNumListView = (RecyclerView)findViewById(R.id.ContactsPersonPhoneNumsListView);
        linearLayoutManager = new LinearLayoutManager(this);
        contactsPersonPhoneNumListView.setLayoutManager(linearLayoutManager);
        adapter = new ContactsPersonPhoneNumListAdapter(this,person.getPhoneNumbers());

        contactsPersonPhoneNumListView.setAdapter(adapter);
    }
}
