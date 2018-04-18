package cn.edu.scut.phonebook;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * 联系人类，用于存储单一联系人信息
 */
public class ContactsPerson implements Comparable,Serializable{
    private String PersonID;
    private String name;
    private String LastNameFirstLetter; //姓名首字母
    private ArrayList<String> PhoneNumbers;
    private ArrayList<String> NumberSearchResult;

    public ContactsPerson(String ID,String name)
    {
        this.PersonID = ID;
        this.name = name;
        this.LastNameFirstLetter = ContactsUtils.LastNameToPinyin(name);
    }

    public ContactsPerson(String ID,String name,ArrayList<String> Nums)
    {
        this.PersonID = ID;
        this.name = name;
        this.LastNameFirstLetter = ContactsUtils.LastNameToPinyin(name);
        this.PhoneNumbers = Nums;
    }

    public String getID() {
        return this.PersonID;
    }

    public ArrayList<String> getNumbers() {
        return this.PhoneNumbers;
    }

    public String getName()
    {
        return this.name;
    }

    public String getLastNameFirstLetter()
    {
        return this.LastNameFirstLetter;
    }

    public ArrayList<String> getPhoneNumbers(){
        return this.PhoneNumbers;
    }

    public void setName(String s)
    {
        this.name = s;
        //还需要加入修改联系人表的代码
    }

    @Override
    public int compareTo(@NonNull Object o) {
        ContactsPerson p = (ContactsPerson) o;
        if(this.LastNameFirstLetter.compareTo(p.LastNameFirstLetter)<0)
        {
            return -1;
        }
        else return 1;
    }

    public void addSearchResult(String foundNumber) {
        this.NumberSearchResult.add(foundNumber);
    }
}
