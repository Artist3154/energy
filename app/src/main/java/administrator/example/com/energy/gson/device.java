package administrator.example.com.energy.gson;

public class device {
    public int id;
    public String method;
    public Device_Info device_info;
    public class Device_Info{
        public String current;
        public String energy;
    }
}
