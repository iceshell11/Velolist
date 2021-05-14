package com.dark1103.velolist.repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;
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

//        data2 = readData();

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

//    private String readData() {
//        String state = Environment.getExternalStorageState();
//        if (!Environment.MEDIA_MOUNTED.equals(state)) {
//            return null;
//        }
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "data.txt");
//
//        FileInputStream stream = null;
//        try {
//            stream = new FileInputStream(file);
//            return new BufferedReader(new InputStreamReader(stream)).readLine();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public void saveChanges(Context context) {
        String dataStr = this.dataSet.stream().filter(Park::isSelected).map(value -> value.getId().toString() + "##%" + (value.getName() == null ? "" : value.getName())).collect(Collectors.joining(";"));
        try {
            FileOutputStream stream = context.openFileOutput("data.cvx", Context.MODE_PRIVATE);
            stream.write(dataStr.getBytes());
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.data.setValue(this.dataSet);
    }


    public void update(final Context context) {
        setParks(context);
    }


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
                    Park park = new Park(obj.getInt(str), obj.getString("Address"), obj.getInt("FreePlaces"), obj.getInt("AvailableOrdinaryBikes"), obj.getBoolean("IsLocked"), selectedList.containsKey(obj.getInt(str)), selectedList.getOrDefault(obj.getInt(str), null));
                    ParksRepository.this.dataSet.add(park);
                }
                ParksRepository.this.data.setValue(ParksRepository.this.dataSet);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, error -> Toast.makeText(context, error.getMessage(), 1).show());
        queue.add(jsonRequest);
        queue.start();
    }

//    private void trustEveryone() {
//        try {
//            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }});
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, new X509TrustManager[]{new X509TrustManager(){
//                public void checkClientTrusted(X509Certificate[] chain,
//                                               String authType) throws CertificateException {}
//                public void checkServerTrusted(X509Certificate[] chain,
//                                               String authType) throws CertificateException {}
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }}}, new SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(
//                    context.getSocketFactory());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
