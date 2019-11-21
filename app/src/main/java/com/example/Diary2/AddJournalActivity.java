package com.example.Diary2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Diary2.HistoryCustomAdapter.HistoryAdapter;
import com.example.Diary2.model.HistoryItem;
import com.example.Diary2.model.JournalItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

public class AddJournalActivity extends AppCompatActivity {
    private EditText mEdtTitle, mEdtContent;
    private ImageButton mBtnDelete;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private JournalItem editItem;
    private RecyclerView mRvHistory;
    private List<HistoryItem> historyList;
    private HistoryAdapter historyAdapter;
    public ArrayList<String> colorList = new ArrayList(Arrays.asList(new String[]{"#d9d9d9", "#ffcdd2", "#f8bbd0",
            "#e1bee7", "#bbdefb", "#d7ccc8", "#ffe0b2", "#fff9c4", "#c8e6c9", "#b2dfdb"}));
    private int state, basecolor;
    private String username;
    private boolean isEdited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_journal_activity);
        addDefaultValues();
        addComponents();
    }

    private void addDefaultValues() {
        state = MainActivity.REQUEST_ADD_JOURNAL;
        editItem = new JournalItem();
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        ((TextView) findViewById(R.id.edt_toolbar_edit_title)).setText("Welcome " + username);
        int requestCode = intent.getExtras().getInt("request");
        if (requestCode == MainActivity.REQUEST_EDIT_JOURNAL) {
            state = MainActivity.REQUEST_EDIT_JOURNAL;
            Bundle bundle = intent.getBundleExtra("package");
            JournalItem item = (JournalItem) bundle.getSerializable("journalItem");
            editItem.setId(item.getId());
            editItem.setTitle(item.getTitle());
            editItem.setContent(item.getContent());
            editItem.setDate(item.getDate());
            editItem.setColor(item.getColor());
            editItem.setHistoryList(item.getHistoryList());
            basecolor = editItem.getColor();
            mRvHistory = findViewById(R.id.rv_journal_history);
            historyList = new ArrayList<>();
            historyList.addAll(editItem.getHistoryList());
            historyAdapter = new HistoryAdapter(this, historyList);
            mRvHistory.setLayoutManager(new LinearLayoutManager(this));
            mRvHistory.setAdapter(historyAdapter);
        } else {
            editItem.setDate(Calendar.getInstance().getTime());
            editItem.setColor(Color.parseColor("#bbdefb"));
        }
    }

    private void addComponents() {
        Toolbar mCustomToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mCustomToolbar);
        ImageButton mBtnBack = findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        mEdtTitle = findViewById(R.id.edt_title);
        mEdtContent = findViewById(R.id.edt_content);
        if (state == MainActivity.REQUEST_EDIT_JOURNAL) {
            mEdtTitle.setText(editItem.getTitle());
            mEdtContent.setText(editItem.getContent());
        }
        ImageButton mBtnOk = findViewById(R.id.btn_done);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEdtTitle.getText().toString().equals("") || mEdtContent.getText().toString().equals("")) {
                    Toast.makeText(AddJournalActivity.this, "Insert information", Toast.LENGTH_SHORT).show();
                } else {
                    String newTitle = mEdtTitle.getText().toString();
                    String newContent = mEdtContent.getText().toString();
                    HistoryItem historyItem;
                    if (state == MainActivity.REQUEST_ADD_JOURNAL) {
                        historyItem = new HistoryItem("0", true, username,
                                username + " created this Journal", new Date());
                    } else {
                        String oldTitle = editItem.getTitle();
                        String oldContent = editItem.getContent();
                        String historyContent = "";
                        if (!newTitle.equals(oldTitle)) {
                            isEdited = true;
                            historyContent += "Title changed: " + oldTitle + " to " + newTitle + "\n";
                        }
                        if (!newContent.equals(oldContent)) {
                            isEdited = true;
                            historyContent += "Content changed: " + oldContent + " to " + newContent;
                        }
                        if (basecolor != editItem.getColor()) {
                            isEdited = true;
                            historyContent += "Color changed: " + basecolor + " to " + editItem.getColor();
                        }
                        int nextId = editItem.getHistoryList().size();
                        historyItem = new HistoryItem(nextId + "",
                                false, username, historyContent, new Date());
                    }
                    if (isEdited) {
                        editItem.getHistoryList().add(0, historyItem);
                        editItem.setTitle(newTitle);
                        editItem.setContent(newContent);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("journalItem", editItem);
                        Intent intent = new Intent();
                        intent.putExtra("requestConfirm", MainActivity.REQUEST_EDIT_JOURNAL);
                        intent.putExtra("bundle", bundle);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    }
                }
            }
        });
        mBtnDelete = findViewById(R.id.btn_delete_diary);
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteJournalConfirm();
            }
        });
    }

    private void deleteJournalConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Journal Delete Confirm");
        builder.setMessage("Confirm to delete this journal?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (state == MainActivity.REQUEST_ADD_JOURNAL) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("requestConfirm", MainActivity.REQUEST_DELETE_JOURNAL);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    public void onClickDatePickerButton(View v) {
        if (state == MainActivity.REQUEST_ADD_JOURNAL) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            Calendar itemCal = Calendar.getInstance();
                            itemCal.setTime(editItem.getDate());
                            itemCal.set(year, monthOfYear, dayOfMonth);
                            editItem.setDate(itemCal.getTime());
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else {
            Toast.makeText(this, "You cannot change the date", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickTimePickerButton(View v) {
        if (state == MainActivity.REQUEST_ADD_JOURNAL) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            Calendar itemCal = Calendar.getInstance();
                            itemCal.setTime(editItem.getDate());
                            itemCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            itemCal.set(Calendar.MINUTE, minute);
                            editItem.setDate(itemCal.getTime());
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        } else {
            Toast.makeText(this, "You cannot change the time", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickColorPickerButton(View v) {
        final ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setColors(colorList);
        colorPicker.setDefaultColorButton(R.color.colorPrimary);
        colorPicker.setRoundColorButton(true)
                .setColumns(5)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        editItem.setColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                })
                .show();
    }
}
