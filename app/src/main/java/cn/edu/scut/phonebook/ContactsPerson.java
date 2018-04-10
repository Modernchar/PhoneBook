package cn.edu.scut.phonebook;

/*
 * 联系人类，用于存储单一联系人信息
 */
public class ContactsPerson {
    private String name;
    private String LastNameLetter; //姓名首字母
    private String PhoneNumber;

    public ContactsPerson(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getLastNameLetter()
    {
        return this.getLastNameLetter();
    }

}
