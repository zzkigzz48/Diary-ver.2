package com.example.Diary2.HistoryCustomAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Diary2.R;
import com.example.Diary2.model.HistoryItem;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<HistoryItem> itemList;
    private Activity activity;
    private Calendar currentCal = Calendar.getInstance();
    public final static String[] month = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

    public HistoryAdapter(Activity activity, List<HistoryItem> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.HistoryViewHolder holder, int position) {
        HistoryItem item = itemList.get(position);
        holder.mTvDateChanged.setText(getDateDifference(item.getDateChanged()));
        holder.mTvTimeChanged.setText(getTimeToShow(item.getDateChanged()));
        holder.mTvUser.setText(item.getUsername());
        holder.mTvContent.setText(item.getContentChanged());
    }

    private String getTimeToShow(Date date) {
        String time = "";
        Calendar itemCal = Calendar.getInstance();
        itemCal.setTime(date);
        time += itemCal.get(Calendar.HOUR) + ":" + itemCal.get(Calendar.MINUTE);
        if (itemCal.get(Calendar.AM_PM) == Calendar.AM) {
            time += "\nA.M";
        } else {
            time += "\nP.M";
        }
        return time;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private String getDateDifference(Date date) {
        String datediff = "";
        Calendar itemCal = Calendar.getInstance();
        itemCal.set(Calendar.HOUR_OF_DAY, 1);
        itemCal.set(Calendar.MINUTE, 1);
        itemCal.set(Calendar.SECOND, 1);
        Date currentDate = itemCal.getTime();
        itemCal.setTime(date);
        itemCal.set(Calendar.HOUR_OF_DAY, 1);
        itemCal.set(Calendar.MINUTE, 1);
        itemCal.set(Calendar.SECOND, 1);
        Date thatdate = itemCal.getTime();
        long timediff = currentDate.getTime() - thatdate.getTime();
        float daydiff = Math.round(((float) timediff / (1000 * 60 * 60 * 24)) * 10) / 10;
        if (daydiff == 0.0) {
            datediff += "Today";
        } else if (daydiff > 0 && daydiff < 7) {
            datediff += (int) Math.ceil(daydiff) + " days ago";
        } else {
            datediff += month[itemCal.get(Calendar.MONTH)] + " " + itemCal.get(Calendar.DATE);
            if (!(currentCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR))) {
                datediff += " " + itemCal.get(Calendar.YEAR);
            }
        }
        return datediff;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvDateChanged;
        private TextView mTvTimeChanged;
        private TextView mTvUser;
        private TextView mTvContent;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mTvDateChanged = itemView.findViewById(R.id.tv_itemHistoryDate);
            mTvTimeChanged = itemView.findViewById(R.id.tv_itemHistoryTime);
            mTvUser = itemView.findViewById(R.id.tv_itemChangedByUsername);
            mTvContent = itemView.findViewById(R.id.tv_itemHistoryContent);
        }
    }
}
