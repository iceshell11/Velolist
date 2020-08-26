package com.dark1103.velolist.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.dark1103.velolist.R;
import com.dark1103.velolist.models.Park;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ListViewAdapter extends ArrayAdapter<Park> {
    private Context mContext;
    private int mResource;
    private CustomFilter filter = new CustomFilter();

    private List<Park> fullList;

    public ListViewAdapter(Context context, int resource) {
        this(context, resource, new ArrayList<>());
    }

    public ListViewAdapter(Context context, int resource, List<Park> list) {
        super(context, resource, new ArrayList<>());
        this.mContext = context;
        this.mResource = resource;
        fullList = list;
    }

    @Override
    public void add(@Nullable Park object) {
        fullList.add(object);
        filter.applyFilter();
    }

    @Override
    public void addAll(@NonNull Collection<? extends Park> collection) {
        fullList.addAll(collection);
        filter.applyFilter();
    }

    @Override
    public void clear() {
        fullList.clear();
        filter.applyFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Park item = (Park) getItem(position);
        View convertView2 = LayoutInflater.from(this.mContext).inflate(this.mResource, parent, false);
        ProgressBar progressBar = (ProgressBar) convertView2.findViewById(R.id.availableBikeBar);
        TextView availableBikes = (TextView) convertView2.findViewById(R.id.availableBikeText);

        if (item.getName() != null) {
            ((TextView) convertView2.findViewById(R.id.address)).setText(item.getName());
        } else {
            ((TextView) convertView2.findViewById(R.id.address)).setText(item.getAddress());
        }

        StringBuilder sb = new StringBuilder();
        sb.append(item.getAvailableBikes());
        sb.append("/");
        sb.append(item.getFreePlaces() + item.getAvailableBikes());
        availableBikes.setText(sb.toString());

        if(item.isLocked()) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            progressBar.setProgress(100);
        }else{

            progressBar.setProgress((item.getAvailableBikes() * 100) / (item.getFreePlaces() + item.getAvailableBikes()));
        }


        Switch sw = (Switch) convertView2.findViewById(R.id.park_switch_button);
        if (sw != null) {
            sw.setChecked(item.isSelected());
            sw.setOnCheckedChangeListener((buttonView, isChecked) -> item.setSelected(isChecked));
        }

        convertView2.setContentDescription(item.getId().toString());
        return convertView2;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    public class CustomFilter extends Filter {
        private CharSequence charSequence = "";

        public void applyFilter() {
            filter(charSequence);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            this.charSequence = charSequence;
            FilterResults results = new FilterResults();

            List<Park> list;

            if (charSequence.equals("+")) {
                list = fullList.stream().filter(Park::isSelected).collect(Collectors.toList());
            } else {
                list = fullList.stream().filter(x -> x.getAddress().contains(charSequence)).collect(Collectors.toList());
            }

            results.values = list;
            results.count = list.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ListViewAdapter.super.clear();
            ListViewAdapter.super.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    }
}
