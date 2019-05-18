package administrator.example.com.energy.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import administrator.example.com.energy.gson.equipment;


public class GsonUtil {
    //private List<equipment> equipmentList = new ArrayList<equipment>();
    public static equipment handleequ(String jsonData) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonData, new TypeToken<List<equipment>>() {}.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
