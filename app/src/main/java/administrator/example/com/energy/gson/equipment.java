package administrator.example.com.energy.gson;

import java.sql.Date;

public class equipment {
    private int id;
    private String name;
    private Date date;
    private String current;
    private String voltage;
    private String power;
    private String energy;
    public equipment(int id,String name,Date date,String current,String voltage,String power,String energy)
    {
        this.id=id;
        this.name=name;
        this.date=date;
        this.current=current;
        this.voltage=voltage;
        this.power=power;
        this.energy=energy;
    }

    public int getid()
    {
        return id;
    }
    public String getname()
    {
        return name;
    }
    public Date getdate(){return date;}
    public String getcurrent(){return current;}
    public String getvoltage(){return voltage;}
    public String getpower()
    {
        return power;
    }
    public String getenergy()
    {
        return energy;
    }
}