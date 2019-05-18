package administrator.example.com.energy.gson;

import java.sql.Date;

public class history {
    private int id;
    private String name;
    private float[]day;
    private float[]hour;
    public history(int id,String name,float[]day,float[]hour)
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

}
