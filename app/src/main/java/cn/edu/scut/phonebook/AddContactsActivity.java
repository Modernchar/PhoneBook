package cn.edu.scut.phonebook;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;


public class AddContactsActivity extends AppCompatActivity {

	EditText editText_contactsName;
	EditText editText_phoneNumber;
	EditText editText_email;
	Button button_add_contacts;
	Button button_cancel_add_contacts;
	Button button_open_qr_scan;

	private int REQUEST_CODE_SCAN = 111;
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

					ContactsUtils.insertContacts(thisContext, name, phone, email);
					Intent intent = new Intent();
					intent.putExtra("Newly_Save_ContactsPerson",new ContactsPerson(name,phone,email));
					setResult(RESULT_OK,intent);

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

				AndPermission.with(AddContactsActivity.this)
						.permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
						.onGranted(new Action() {
							@Override
							public void onAction(List<String> permissions) {
								Intent intent = new Intent(AddContactsActivity.this, CaptureActivity.class);

								/*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
								 * 也可以不传这个参数
								 * 不传的话  默认都为默认不震动  其他都为true
								 * */

								ZxingConfig config = new ZxingConfig();
								config.setPlayBeep(true);
								config.setShake(true);
								intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);

								startActivityForResult(intent, REQUEST_CODE_SCAN);
							}
						})
						.onDenied(new Action() {
							@Override
							public void onAction(List<String> permissions) {
								Uri packageURI = Uri.parse("package:" + getPackageName());
								Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

								startActivity(intent);

								Toast.makeText(AddContactsActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
							}
						}).start();
			}
		});


	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// 扫描二维码/条码回传
		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
			if (data != null) {

				String content = data.getStringExtra(Constant.CODED_CONTENT);
				Log.i("vcardcontent", content);
				//Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
				ContactsPerson contactsPerson = new ContactsPerson();
				Log.i("vcardcontent", content);
				if(contactsPerson.setVcard(content)==1){
					if(contactsPerson.getName()!= null){
						Log.i("Person", contactsPerson.getName());
						editText_contactsName.setText(contactsPerson.getName());
						for (String phoneinf : contactsPerson.getNumbers()){
							editText_phoneNumber.setText(phoneinf);
						}

					}

				}

			}
		}
	}
}
