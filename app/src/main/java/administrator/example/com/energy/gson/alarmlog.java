package administrator.example.com.energy.gson;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class alarmlog {
    private int id;
    private String name;
    private String alarm_time;
    private String alarm_reason;
    public alarmlog(int id,String name,String alarm_time,String alarm_reason)
    {
        this.id=id;
        this.name=name;
        this.alarm_time=alarm_time;
        this.alarm_reason=alarm_reason;
    }

    public int getid()
    {
        return id;
    }
    public String getname()
    {
        return name;
    }
    public String getdate(){
        /*String dateStr=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);//将date类型转化为String类型
        return dateStr;*/
        return alarm_time;
    }
    public String getReason(){return alarm_reason;}
}
