package cn.edu.scut.phonebook;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

public class ContactsDBHandle {
    ContactsDBHandle(){

    }
    public void Create(Activity currentActivity){
        List<ContactsDB> contactsDBS = DataSupport.findAll(ContactsDB.class);
        if(contactsDBS.isEmpty()){
            Cursor cursor = currentActivity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
            while(cursor.moveToNext()) {
                String ID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                addContant(name,false,null);
            }
            cursor.close();
        }
    }
    public void addContant( String name, boolean importance, Date brith){
        ContactsDB contactsDB = new ContactsDB();
        contactsDB.setName(name);
        contactsDB.setImportance(importance);
        contactsDB.setBirthday(brith);
        contactsDB.save();
    }
    public void switchimportance(String name){
        ContactsDB contactsDB = new ContactsDB();
        if(getimportance(name)){
            contactsDB.setToDefault("importance");
        }else{
            contactsDB.setImportance(true);
        }
        contactsDB.updateAll("name = ?",name);
    }
    public boolean getimportance(String name){
        boolean isimportance = false;
        List<ContactsDB> contactsDBS = DataSupport.where("name = ?",name).find(ContactsDB.class);
        for(ContactsDB contactsDB:contactsDBS) {
            isimportance = contactsDB.isImportance();
        }
        return isimportance;
    }
    public Date getDate(String name){
        Date date = null;
        List<ContactsDB>  contactsDBS = DataSupport.where("name = ?",name).find(ContactsDB.class);
        for(ContactsDB contactsDB:contactsDBS){
            date = contactsDB.getBirthday();
        }
        return date;
    }
    public void changename(String oldname, String newname){
        ContactsDB contactsDB = new ContactsDB();
        contactsDB.setName(newname);
        contactsDB.updateAll("name = ?",oldname);
    }
    public void setDate(String name, Date date){
        ContactsDB contactsDB = new ContactsDB();
        contactsDB.setBirthday(date);
        contactsDB.updateAll("name = ?",name);
    }
    public void delete(String name){
        DataSupport.deleteAll(ContactsDB.class,"name = ?",name);
    }

}
