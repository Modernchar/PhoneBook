package cn.edu.scut.phonebook;



import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/*
    * Persenter层，专注于处理与 联系人 界面有关的逻辑
 */
public class ContactsUtils {

    //将姓转成拼音,获取首字母
    public static String LastNameToPinyin(String name)
    {
        String LastNameLetter = new String();
        ArrayList<HanziToPinyin.Token> NamePinyin = HanziToPinyin.getInstance().get(name); //获取姓名的拼音

        // 获取姓的第一个拼音字母
        // NamePinyin.get(0).target 为获取姓的全部拼音
        if(NamePinyin.size()==0)
        {
            Log.d("NamePinyin","NamePinYin is empty");
            return "";

        }
        else if(NamePinyin.get(0)!=null)
        {
            LastNameLetter = NamePinyin.get(0).target.substring(0,1);
            return LastNameLetter;
        }
        // Log.i("ToPinyin","LastNameLetter:"+LastNameLetter);
        // Log.i("ToPinyin","NamePinyin.get(1):"+NamePinyin.get(1).target);
        return "";
    }


    //获取全部联系人
    public static ArrayList<ContactsPerson> getContactsPersonList(Activity currentActivity)
    {
        ArrayList<ContactsPerson> Persons = new ArrayList<>() ;//全部联系人

        //查找联系人数据
        Cursor cursor = currentActivity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        while(cursor.moveToNext()) {
            // 联系人ID
            String ID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            // 联系人姓名
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            // 联系人电话
            Cursor PhoneNumsCursor = currentActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ID, null, null);
            ArrayList<String> PhoneNums = new ArrayList<String>();

            while (PhoneNumsCursor.moveToNext()) {
                String num = PhoneNumsCursor.getString(PhoneNumsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                PhoneNums.add(num);// 添加电话号码
            }
            PhoneNumsCursor.close();

            ContactsPerson person = new ContactsPerson(ID,name,PhoneNums);
            Persons.add(person);


        }
        cursor.close();

        Collections.sort(Persons);
        return Persons;
    }


    //搜索符合相关信息的联系人
    //输入参数是字符串，可能是数字或汉字或字母
    //把符合要求的联系人信息储存到Persons中
    public static ArrayList<ContactsPerson> getContactsPersonForSearch(String input)
    {
        ArrayList<ContactsPerson> Persons = new ArrayList<>();
        return Persons;
    }



    public static void setContactsPersonName()
    {
        //玛德还没想好
    }

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static ArrayList<ContactsPerson> searchContacts(ArrayList<ContactsPerson> allContacts, String key) {
        ArrayList<ContactsPerson> resultList = new ArrayList<>();

        for(ContactsPerson person : allContacts) {
            int flag = 0;
            if(person.getName().contains(key)) {
                resultList.add(person);
                flag++;
            }
            if(isNumeric(key)) {
                for (String number : person.getNumbers()) {
                    if (number.contains(key)) {
                        person.addSearchResult(number);
                        flag++;
                    }
                }
            }
            if (flag > 0) {
                resultList.add(person);
            }
        }
        return resultList;
    }

}
