package administrator.example.com.energy.gson;

public class equipment {
    private String no;
    private int data;
    private int value;
    private String state;
    public equipment(String no,int data,int value,String state)
    {
        this.no=no;
        this.data=data;
        this.value=value;
        this.state=state;
    }

    public String getno()
    {
        return no;
    }
    public int getdata()
    {
        return data;
    }
    public int getvalue()
    {
        return value;
    }
    public String getstate()
    {
        return state;
    }
}