package cn.edu.scut.phonebook;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

import a_vcard.android.provider.Contacts;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.ContactStruct;
import a_vcard.android.syncml.pim.vcard.VCardComposer;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;

/*
 * 联系人类，用于存储单一联系人信息
 */
public class ContactsPerson implements Comparable,Serializable{
    private String PersonID;
    private String name;
    private String LastNameFirstLetter; //姓名首字母
    private ArrayList<String> PhoneNumbers;
    private ArrayList<String> NumberSearchResult;
    public ContactsPerson(){

    }
    public ContactsPerson(String ID,String name)
    {
        this.PersonID = ID;
        this.name = name;
        this.LastNameFirstLetter = ContactsUtils.LastNameToPinyin(name);
        this.NumberSearchResult = new ArrayList<String>();
    }

    public ContactsPerson(String ID,String name,ArrayList<String> Nums)
    {
        this.PersonID = ID;
        this.name = name;
        this.LastNameFirstLetter = ContactsUtils.LastNameToPinyin(name);
        this.PhoneNumbers = Nums;
        this.NumberSearchResult = new ArrayList<String>();
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

    public static class PhoneData{
        public int type;
        public String data;
        public String label;
        public boolean isPrimary;

        public PhoneData() {
        }
    }
    private List<PhoneData> phoneList = new ArrayList<PhoneData>();

    public String getVcard(){
        VCardComposer composer = new VCardComposer();
        ContactStruct contact = new ContactStruct();
        String vcardString = null;
        contact.name = this.name;

        for (String phoneInfo : PhoneNumbers) {
            contact.addPhone(1 , phoneInfo,
                    null, true);
        }
        try {
            vcardString = composer.createVCard(contact, VCardComposer.VERSION_VCARD30_INT);
        } catch (VCardException e) {
            e.printStackTrace();
        }
        return vcardString;
    }
    /*int 1 成功*/
    public int setVcard(String vcardString) {
        if (vcardString == null) {
            return -1;//为空
        }
        VCardParser parse = new VCardParser();
        VDataBuilder builder = new VDataBuilder();
        boolean parsed = false;
        try {
            parsed = parse.parse(vcardString, "UTF-8", builder);
        } catch (VCardException e) {
            e.printStackTrace();
            return -2;//格式错误
        } catch (IOException e) {
            e.printStackTrace();
            return -2;//格式错误
        }
        if (parsed){
            return -1;
        }
        List<VNode> pimContacts = builder.vNodeList;
        ContactsPerson contactsPerson = null;
        for (VNode contact : pimContacts) {
            ContactStruct contactStruct = ContactStruct.constructContactFromVNode(contact, 1);
            List<ContactStruct.PhoneData> phoneDataList = contactStruct.phoneList;
            //List<String> phoneInfoList = new ArrayList<String>();
            for (ContactStruct.PhoneData phoneData : phoneDataList) {
                String phoneInfo = phoneData.data;
                PhoneNumbers.add(phoneInfo);
            }
            this.name = contactStruct.name;
        }
        return 1;
    }

    public ArrayList<String> getSearchResult()
    {
        return this.NumberSearchResult;
    }

    public Boolean isNumberSearchResultEmpty()
    {
        if(this.NumberSearchResult.size()>0)
            return false;
        else return true;
    }
}
