package administrator.example.com.energy.gson;

import java.sql.Date;

public class history {
    private int id;
    private String name;
    private String day;
    private String hour;
    public history(int id,String name,String day,String hour)
    {
        this.id=id;
        this.name=name;
        this.day=day;
        this.hour=hour;
    }

    public int getid()
    {
        return id;
    }
    public String getname()
    {
        return name;
    }
    public String getday()
    {
        return day;
    }
    public String gethour()
    {
        return hour;
    }

}
