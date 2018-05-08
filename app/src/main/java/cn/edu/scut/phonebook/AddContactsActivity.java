package cn.edu.scut.phonebook;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddContactsActivity extends AppCompatActivity {

	EditText editText_contactsName;
	EditText editText_phoneNumber;
	EditText editText_email;
	Button button_add_contacts;
	Button button_cancel_add_contacts;
	Button button_open_qr_scan;

	Toast toast = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//getSupportActionBar().hide();//隐藏标题栏
		setContentView(R.layout.activity_add_contacts);

		//隐藏状态栏
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		final Context thisContext = this;

		//动态获取写入联系人权限
		if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
			{
			ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.WRITE_CONTACTS },1);
		}
		else{
			//Toast.makeText(this, "未获取读取联系人权限", Toast.LENGTH_SHORT).show();
		}

		//initialize parts
		editText_contactsName = (EditText)findViewById(R.id.editText_addContactsName);
		editText_phoneNumber = (EditText)findViewById(R.id.editText_phoneNumber);
		editText_email = (EditText)findViewById(R.id.editText_email);
		button_add_contacts = (Button)findViewById(R.id.button_add_contacts);
		button_cancel_add_contacts = (Button)findViewById(R.id.button_cancel_add_contacts);
		button_open_qr_scan = (Button)findViewById(R.id.button_open_qr_scan);

		button_add_contacts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = editText_contactsName.getText().toString();
				String phone = editText_phoneNumber.getText().toString();
				String email = editText_email.getText().toString();

				if(name.isEmpty()) {
					if (toast != null) {
						toast.setText("姓名不能为空");
						toast.setDuration(Toast.LENGTH_SHORT);
						toast.show();
					} else {
						toast = Toast.makeText(thisContext, "姓名不能为空", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				else {

				/*String id = java.util.UUID.randomUUID().toString();
				ArrayList<String> NumList = new ArrayList<>();
				NumList.add(phone1);
				NumList.add(phone2);
				ContactsPerson person = new ContactsPerson(id, name, NumList);*/

					ContentValues values = new ContentValues();
					Uri rawContactUri = thisContext.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
					long rawContactId = ContentUris.parseId(rawContactUri);

					values.clear();
					values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
					values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
					values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
					thisContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

					values.clear();
					values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
					values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
					values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
					values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
					thisContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

					values.clear();
					values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
					values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
					values.put(ContactsContract.CommonDataKinds.Email.DATA, email);
					values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
					thisContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

					Toast.makeText(thisContext, "添加成功", Toast.LENGTH_SHORT).show();

					finish();
				}
			}
		});

		button_cancel_add_contacts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		button_open_qr_scan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(thisContext, "扫码", Toast.LENGTH_SHORT).show();

				//TODO
				//open the QR Scan activity
			}
		});
	}
}
