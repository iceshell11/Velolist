package com.dark1103.velolist.repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dark1103.velolist.models.Park;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ParksRepository {
    private static ParksRepository instance;
    public MutableLiveData<List<Park>> data = new MutableLiveData<>();
    public ArrayList<Park> dataSet = null;

    public static ParksRepository getInstance() {
        if (instance == null) {
            instance = new ParksRepository();
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public MutableLiveData<List<Park>> getParks(Context context) {
        if (this.dataSet == null) {
            this.dataSet = new ArrayList<>();
            setParks(context);
        }
        this.data.setValue(this.dataSet);
        return this.data;
    }

    @SuppressLint({"WrongConstant", "ShowToast"})
    public HashMap<Integer, String> loadSelectedParks(Context context) {
        HashMap<Integer, String> map = new HashMap<>();
        String data2 = null;
        try {
            FileInputStream stream = context.openFileInput("data.cvx");
            data2 = new BufferedReader(new InputStreamReader(stream)).readLine();
            Toast.makeText(context, data2, 0);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data2 != null) {
            for (String item : data2.split(";")) {
                String[] split = item.split("##%");
                String name = null;
                if (split.length > 1 && split[1].length() > 0) {
                    name = split[1];
                }
                map.put(Integer.parseInt(split[0]), name);
            }
        }
        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveChanges(Context context) {
        String dataStr = this.dataSet.stream().filter(Park::isSelected).map(value -> value.getId().toString() + "##%" + (value.getName() == null ? "" : value.getName())).collect(Collectors.joining(";"));
        try {
            FileOutputStream stream = context.openFileOutput("data.cvx", 0);
            stream.write(dataStr.getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.data.setValue(this.dataSet);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(final Context context) {
        setParks(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("WrongConstant")
    private void setParks(final Context context) {
        final HashMap<Integer, String> selectedList = loadSelectedParks(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(0, "https://velobike.ru/ajax/parkings/", null, response -> {
            String str = "Id";
            try {
                ParksRepository.this.dataSet.clear();
                JSONArray array = response.getJSONArray("Items");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Park park = new Park(obj.getInt(str), obj.getString("Address"), obj.getInt("FreePlaces"),  obj.getInt("AvailableOrdinaryBikes"), selectedList.containsKey(obj.getInt(str)), selectedList.getOrDefault(obj.getInt(str), null));
                    ParksRepository.this.dataSet.add(park);
                }
                ParksRepository.this.data.setValue(ParksRepository.this.dataSet);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), 1).show();
            }
        }, error -> Toast.makeText(context, "Loading fail", 1).show());
        queue.add(jsonRequest);
        queue.start();
    }
}
