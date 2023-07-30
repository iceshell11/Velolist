package com.dark1103.velolist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.dark1103.velolist.models.Park;
import com.dark1103.velolist.repositories.ParksRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ActivityParkSettings extends AppCompatActivity {
    private Park park;

    private TextInputEditText inputbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_settings);

        ActionBar appBar = getSupportActionBar();
        appBar.setHomeButtonEnabled(true);
        appBar.setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getExtras().getString("Id");
        park = ParksRepository.getInstance().getParks(this).getValue().stream()
            .filter(x -> Objects.equals(x.getId(), id))
            .findAny()
            .orElseThrow(() -> new RuntimeException("Missing id " + id));

        inputbox = findViewById(R.id.inputbox);

        inputbox.setText(park.getName());

        TextView address = findViewById(R.id.address);
        address.setText(park.getAddress());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.park_settings_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        } else if (itemId == R.id.delete_menu_item) {
            park.setSelected(false);
            ParksRepository.getInstance().saveChanges(this);
            finish();
            return true;
        } else if (itemId == R.id.save_menu_item) {
            park.setName(inputbox.getText().toString());
            ParksRepository.getInstance().saveChanges(this);
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
