package com.example.calendartracker.ui.record;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calendartracker.R;
import com.example.calendartracker.model.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> implements Filterable {

    private static final String TAG = "RVADAPTER";

    private List<Record> recordList = new ArrayList<>();
    private List<Record> recordListFiltered = new ArrayList<>();
    private final OnItemClickListener listener;
    private Filter recordFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Record> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(recordListFiltered);
            }
            else {
                Log.d(TAG, "performFiltering: ");
                String stringFilter = charSequence.toString().toLowerCase().trim();
                for (Record record:recordListFiltered) {
                    if (record.getName().toLowerCase().trim().contains(stringFilter)) {
                        filteredList.add(record);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            recordList.clear();
            recordList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return recordFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(Record record);
    }

    public RecordAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordAdapter.RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.bind(recordList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }


    public void setRecordList(List<Record> records) {
        this.recordList = records;
        recordListFiltered = new ArrayList<>(records);
        notifyDataSetChanged();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView dDayTextView;
        private TextView gregorianTextView;
        private TextView easternLunarTextView;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.record_name);
            dDayTextView = itemView.findViewById(R.id.record_d_day);
            gregorianTextView = itemView.findViewById(R.id.record_gr_date);
            easternLunarTextView = itemView.findViewById(R.id.record_el_date);
        }

        public void bind(final Record record, final OnItemClickListener listener) {
            nameTextView.setText(record.getName());
            dDayTextView.setText(record.getName());
            gregorianTextView.setText(record.getName());
            easternLunarTextView.setText(record.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(record);
                }
            });
        }
    }
}

