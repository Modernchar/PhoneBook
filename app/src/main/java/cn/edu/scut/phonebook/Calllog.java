package cn.edu.scut.phonebook;

/**
 * Created by Administrator on 2018/3/31 0031.
 */

public class Calllog {//一条通话记录的结构

    private int _id ;//记录的id

    private int _type;//类型

    private long _lDate;//通话的时间

    private long _duration;//通话时长

    private String name;//名字

    private String number;//电话号码

    private int type;

    private String lDate;

    private String duration;


    public Calllog(){}

    public Calllog(int _id, int _type, long _lDate, long _duration, String name, String number, int type, String lDate, String duration) {
        this._id=_id;
        this._type=_type;
        this._lDate=_lDate;
        this._duration=_duration;
        this.name = name;
        this.number = number;
        this.type = type;
        this.lDate = lDate;
        this.duration = duration;
    }

    public void SetCalllog(int _id, int _type, long _lDate, long _duration, String name, String number, int type, String lDate, String duration/*, */) {
        this._id=_id;
        this._type=_type;
        this._lDate=_lDate;
        this._duration=_duration;
        this.name = name;
        this.number = number;
        this.type = type;
        this.lDate = lDate;
        this.duration = duration;
    }

    public int get_id() { return _id; }

    public String getName() { return name; }

    public String getNumber() { return number;}

    public int getType() { return type; }

    public String getLDate() { return lDate; }

    public long get_LDate() { return _lDate; }

    public String getDuration() { return duration; }

    public long get_duration() { return _duration; }

}
