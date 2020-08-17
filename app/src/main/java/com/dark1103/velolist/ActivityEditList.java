package com.dark1103.velolist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import com.dark1103.velolist.adapters.ListViewAdapter;
import com.dark1103.velolist.models.Park;
import com.dark1103.velolist.repositories.ParksRepository;

import java.util.List;

public class ActivityEditList extends AppCompatActivity {
    private ListView listView;
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        listView = findViewById(R.id.editList);
        searchBar = findViewById(R.id.searchBar);

        MutableLiveData<List<Park>> list = ParksRepository.getInstance().getParks(this);
        final ListViewAdapter adapter = new ListViewAdapter(this, R.layout.edit_list_item);
        list.observe(this, parks -> {
            adapter.clear();
            adapter.notifyDataSetChanged();
            adapter.addAll(parks);
        });

        listView.setAdapter(adapter);


        ActionBar appBar = getSupportActionBar();
        appBar.setHomeButtonEnabled(true);
        appBar.setDisplayHomeAsUpEnabled(true);


        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        } else if (itemId != R.id.save_menu_item) {
            return super.onOptionsItemSelected(item);
        } else {
            ParksRepository.getInstance().saveChanges(this);
            finish();
            return true;
        }
    }
}
