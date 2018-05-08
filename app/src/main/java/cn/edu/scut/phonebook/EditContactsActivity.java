package cn.edu.scut.phonebook;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditContactsActivity extends AppCompatActivity {


	EditText editText_contactsName;
	EditText editText_phoneNumber;
	EditText editText_email;
	Button button_add_contacts;
	Button button_cancel_add_contacts;
	Button button_open_qr_scan;

	String id;
	String name;
	String phone;
	String email;

	Toast toast = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_contacts);

		//隐藏状态栏
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		//initialize parts
		editText_contactsName = (EditText)findViewById(R.id.editText_addContactsName);
		editText_phoneNumber = (EditText)findViewById(R.id.editText_phoneNumber);
		editText_email = (EditText)findViewById(R.id.editText_email);
		button_add_contacts = (Button)findViewById(R.id.button_add_contacts);
		button_cancel_add_contacts = (Button)findViewById(R.id.button_cancel_add_contacts);
		button_open_qr_scan = (Button)findViewById(R.id.button_open_qr_scan);

		Intent intent = getIntent();
		id = intent.getStringExtra("ID");
		name = intent.getStringExtra("name");
		phone = intent.getStringExtra("phone");
		email = intent.getStringExtra("email");

		editText_contactsName.setText(name);
		editText_phoneNumber.setText(phone);
		editText_email.setText(email);

		button_add_contacts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = editText_contactsName.getText().toString();
				String phone = editText_phoneNumber.getText().toString();
				String email = editText_email.getText().toString();

				if (name.isEmpty()) {
					if (toast != null) {
						toast.setText("姓名不能为空");
						toast.setDuration(Toast.LENGTH_SHORT);
						toast.show();
					} else {
						toast = Toast.makeText(EditContactsActivity.this, "姓名不能为空", Toast.LENGTH_SHORT);
						toast.show();
					}
				} else {
					ContactsUtils.updateContact(EditContactsActivity.this, id, name, phone, email);

					Toast.makeText(EditContactsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

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
	}
}
