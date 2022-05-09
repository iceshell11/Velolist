package com.dark1103.velolist;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.dark1103.velolist.adapters.ListViewAdapter;
import com.dark1103.velolist.models.Park;
import com.dark1103.velolist.repositories.ParksRepository;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String cookies = CookieManager.getInstance().getCookie(url);
                if(cookies.contains("qrator_jsid")){
                    ParksRepository.getInstance().setCookies(cookies);

                    try{
                        loadList();
                    } catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    } finally {
                        ProgressBar progressBar = findViewById(R.id.progressBar_cyclic);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @SuppressWarnings("deprecation") @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;

            }

            @TargetApi(Build.VERSION_CODES.N) @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        webView.loadUrl("https://velobike.ru/");

//        if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)&& Build.VERSION.SDK_INT >= 23 ) {
//
//        }else{
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 142);
//        }
    }

    private void loadList() {
        listView = findViewById(R.id.listView);
        MutableLiveData<List<Park>> list = ParksRepository.getInstance().getParks(this);


        final ListViewAdapter adapter = new ListViewAdapter(this, R.layout.list_item);
        list.observe(this, parks -> {
            adapter.clear();
            List<Park> collect = parks.stream().filter(Park::isSelected).collect(Collectors.toList());
            adapter.addAll(collect);
        });
        this.listView.setAdapter(adapter);

        swipeLayout = findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(() -> {
            ParksRepository.getInstance().update(this);
            swipeLayout.setRefreshing(false);
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle = new Bundle();
            CharSequence description = view.getContentDescription();
            Integer id = Integer.valueOf(description.toString());
            bundle.putInt("Id", id);

            Intent intent = new Intent(this, ActivityParkSettings.class);
            intent.putExtras(bundle);
            startActivity(intent, bundle);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.editListMenuItem) {
            return super.onOptionsItemSelected(item);
        }
        startActivity(new Intent(this, ActivityEditList.class));
        return true;
    }

}
