package cn.edu.scut.phonebook;

import java.io.Serializable;
import java.util.Date;

public class SearchCondition implements Serializable{
    private Date StartTime;
    private Date EndTime;
    private String name;

    public SearchCondition(Date StartTime, Date EndTime, String name){
        this.EndTime = EndTime;
        this.name = name;
        this.StartTime = StartTime;
    }

    public Date GetStartTime(){return StartTime;}
    public Date GetEndTime(){return EndTime;}
    public String GetName(){return name;}
}
