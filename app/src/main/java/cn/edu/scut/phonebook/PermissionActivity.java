package cn.edu.scut.phonebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KCrason on 2017/8/17.
 */

public class PermissionActivity extends AppCompatActivity {

    private final static int PERMISSION_REQUEST_CODE = 0x01;

    //权限请求成功
    public final static int CALL_BACK_RESULT_CODE_SUCCESS = 0x02;

    //权限请求失败
    public final static int CALL_BACK_RESULE_CODE_FAILURE = 0x03;

    //跳转至权限请求界面requestcode
    public final static int CALL_BACK_PERMISSION_REQUEST_CODE = 0x04;

    public final static String KEY_PERMISSIONS_ARRAY = "key_permission_array";

    public final static String KEY_FIRST_MESSAGE = "key_first_message";

    public final static String KEY_ALWAYS_MESSAGE = "key_always_message";

    private String[] mPermissions;

    private boolean isToSettingPermission;


    /**
     * 第一次拒绝该权限的提示信息。
     */
    private String mFirstRefuseMessage;

    /**
     * 永久拒绝权限提醒的提示信息
     */
    private String mAlwaysRefuseMessage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstRefuseMessage = getString(R.string.defaule_always_message);
        if (getIntent() != null) {
            mPermissions = getIntent().getStringArrayExtra(KEY_PERMISSIONS_ARRAY);
            mFirstRefuseMessage = getIntent().getStringExtra(KEY_FIRST_MESSAGE);
            mAlwaysRefuseMessage = getIntent().getStringExtra(KEY_ALWAYS_MESSAGE);
        }
        if (TextUtils.isEmpty(mFirstRefuseMessage)) {
            mFirstRefuseMessage = getString(R.string.defaule_first_message);
        }
        if (TextUtils.isEmpty(mAlwaysRefuseMessage)) {
            mAlwaysRefuseMessage = getString(R.string.defaule_always_message);
        }
        checkPermission(mPermissions);
    }


    /**
     * 请求多个权限
     */
    public void checkPermission(String... permissions) {
        if (permissions != null) {
            List<String> requestPermissionCount = getRequestPermissionList(permissions);
            if (requestPermissionCount != null && requestPermissionCount.size() > 0) {
                ActivityCompat.requestPermissions(this, requestPermissionCount.toArray(new String[0]), PERMISSION_REQUEST_CODE);
            } else {
                notifySuccess();
            }
        } else {
            notifySuccess();
        }
    }

    /**
     * 获取需要去申请权限的权限列表
     *
     * @param permissions
     * @return
     */
    public List<String> getRequestPermissionList(String... permissions) {
        List<String> reequestPermissionCount = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                reequestPermissionCount.add(permission);
            }
        }
        return reequestPermissionCount;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果是从权限设置界面过来，重新检查权限
        if (isToSettingPermission) {
            isToSettingPermission = false;
            checkPermission(mPermissions);
        }
    }

    public void onCancelPermission() {
        setResult(CALL_BACK_RESULE_CODE_FAILURE);
        finish();
    }

    public void onSurePermission(boolean isRefuse) {
        if (isRefuse) {
            isToSettingPermission = true;
            Intent localIntent = new Intent();
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
            startActivity(localIntent);
        } else {
            checkPermission(mPermissions);
        }
    }


    public List<String> getshouldRationaleList() {
        if (mPermissions != null) {
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < mPermissions.length; i++) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, mPermissions[i])) {
                    strings.add(mPermissions[i]);
                }
            }
            return strings;
        }
        return null;
    }


    public void notifySuccess() {
        setResult(CALL_BACK_RESULT_CODE_SUCCESS);
        finish();
    }

    public void showPermissionDialog(final boolean isAlwaysRefuse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(isAlwaysRefuse ? mAlwaysRefuseMessage : mFirstRefuseMessage).
                setTitle(getString(R.string.dialog_title)).
                setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onCancelPermission();
                    }
                })
                .setPositiveButton(isAlwaysRefuse ? getString(R.string.set) : getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onSurePermission(isAlwaysRefuse);
                    }
                })
                .show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults != null && grantResults.length > 0) {
            List<Integer> failurePermissionCount = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    failurePermissionCount.add(grantResults[i]);
                }
            }
            if (failurePermissionCount.size() == 0) {
                //全部成功
                notifySuccess();
            } else {
                //失败
                List<String> rationaleList = getshouldRationaleList();
                if (rationaleList != null && rationaleList.size() > 0) {
                    showPermissionDialog(false);
                } else {
                    showPermissionDialog(true);
                }
            }
        }
    }
}

