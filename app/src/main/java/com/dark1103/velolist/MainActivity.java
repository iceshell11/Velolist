package com.dark1103.velolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
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
