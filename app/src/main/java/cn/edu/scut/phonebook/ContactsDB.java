package cn.edu.scut.phonebook;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class ContactsDB extends DataSupport{
    private String id;
    private String name;
    private boolean importance;
    private Date birthday;
    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isImportance() {
        return importance;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImportance(boolean importance) {
        this.importance = importance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }
}
