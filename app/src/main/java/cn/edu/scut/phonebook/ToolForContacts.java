package cn.edu.scut.phonebook;



import java.util.ArrayList;

/*
    * Persenter层，专注于处理与 联系人 界面有关的逻辑
 */
public class ToolForContacts {

    //将姓名转成拼音
    public static String ToPinyin(String name)
    {
        String LastNameLetter = new String();
        ArrayList<HanziToPinyin.Token> NamePinyin = HanziToPinyin.getInstance().get(name); //获取姓名的拼音
        LastNameLetter = NamePinyin.get(0).target; //获取姓名拼音的第一位
        return LastNameLetter;
    }

    //获取全部联系人
    public static ArrayList<ContactsPerson> getContactsPersonList()
    {
        ArrayList<ContactsPerson> Persons = new ArrayList<>() ;
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

}
