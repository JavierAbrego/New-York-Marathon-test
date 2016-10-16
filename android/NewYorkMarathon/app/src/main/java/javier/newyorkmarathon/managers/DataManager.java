package javier.newyorkmarathon.managers;

import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javier.newyorkmarathon.domain.UserField;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by javier on 16/10/16.
 */
public class DataManager {

    static final String kWebserviceBase = "http://192.168.1.107:8080/adidas_dev_test";
    static final String kWebserviceUserConfig = "%s/user/fields";
    static final String kWebserviceUserSave = "%s/user/save?%s";

    private static DataManager _dataManager;

    public static DataManager getInstance(){
        if(_dataManager==null){
            _dataManager =  new DataManager();
        }
        return  _dataManager;
    }

    public List<UserField> fetchFieds() throws IOException {
        String url = String.format(kWebserviceUserConfig, kWebserviceBase);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        Type listType = new TypeToken<ArrayList<UserField>>(){}.getType();
        List<UserField> fields = new Gson().fromJson(response.body().string(), listType);
        return fields;
    }

    public Response saveUser(String params) throws IOException {
        String url = String.format(kWebserviceUserSave, kWebserviceBase, params);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }
}
