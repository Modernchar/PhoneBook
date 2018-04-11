package cn.edu.scut.phonebook;

import java.util.ArrayList;

/*
 * 联系人类，用于存储单一联系人信息
 */
public class ContactsPerson {
    private String PersonID;
    private String name;
    private String LastNameFirstLetter; //姓名首字母
    private ArrayList<String> PhoneNumbers;

    public ContactsPerson(String ID,String name)
    {
        this.PersonID = ID;
        this.name = name;
        this.LastNameFirstLetter = ToolForContacts.LastNameToPinyin(name);
    }

    public ContactsPerson(String ID,String name,ArrayList<String> Nums)
    {
        this.PersonID = ID;
        this.name = name;
        this.LastNameFirstLetter = ToolForContacts.LastNameToPinyin(name);
        this.PhoneNumbers = Nums;
    }

    public String getName()
    {
        return this.name;
    }

    public String getLastNameFirstLetter()
    {
        return this.LastNameFirstLetter;
    }

    /* 首字母应该不能认为变更才对
    public void setLastNameFirstLetter(String s)
    {
        this.LastNameFirstLetter = s;
    }
    */

    public void setName(String s)
    {
        this.name = s;
        //还需要加入修改联系人表的代码
    }


}
