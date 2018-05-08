package cn.edu.scut.phonebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.encode.CodeCreator;

public class ContactsPersonCardActivity extends AppCompatActivity {

    ContactsPerson person;
    private TextView NameTextView;
    private Context context = this;
    private RecyclerView contactsPersonPhoneNumListView;
    private LinearLayoutManager linearLayoutManager;
    private ContactsPersonPhoneNumListAdapter adapter;

    private Button Bit_Card; //二维码名片
    private Button EditContactsButton;
    private Button CallLogBtn;

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
        initButton();
        initButtonsClickEvents();


    }

    private void initRecyclerViewParts()
    {
        contactsPersonPhoneNumListView = (RecyclerView)findViewById(R.id.ContactsPersonPhoneNumsListView);
        linearLayoutManager = new LinearLayoutManager(this);
        contactsPersonPhoneNumListView.setLayoutManager(linearLayoutManager);
        adapter = new ContactsPersonPhoneNumListAdapter(this,person.getPhoneNumbers());

        contactsPersonPhoneNumListView.setAdapter(adapter);
    }
    private void initButton(){
        Bit_Card = (Button)findViewById(R.id.QRCodeShare_Btn);
        EditContactsButton = (Button)findViewById(R.id.EditContacts_Btn);
        CallLogBtn = (Button)findViewById(R.id.ShowCallRecord_Btn);
    }


    private void initButtonsClickEvents(){

        //二维码按钮点击事件
        Bit_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = null;// 这里是获取图片Bitmap，也可以传入其他参数到Dialog中
                String contentEtString = person.getVcard();
                try {
                    Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    bitmap = CodeCreator.createQRCode(contentEtString, 500, 500, logo);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
                QRShowDialog.Builder dialogBuild = new QRShowDialog.Builder(ContactsPersonCardActivity.this);
                dialogBuild.setBitmap(bitmap);
                QRShowDialog dialog = dialogBuild.create();
                dialog.setCanceledOnTouchOutside(true);// 点击外部区域关闭
                dialog.show();
            }
        });

        EditContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsPersonCardActivity.this, EditContactsActivity.class);
                intent.putExtra("ID", person.getID());
                intent.putExtra("name", person.getName());
                intent.putExtra("phone", person.getPhoneNumbers().get(0));

                if(!person.isEmailEmpty())
                {
                    intent.putExtra("email", person.getEmails().get(0));
                }
                startActivity(intent);
            }
        });

        CallLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsPersonCardActivity.this,CalllogSort.class);
                intent.putExtra("key",person.getName());
                startActivity(intent);
            }
        });
    }
}
