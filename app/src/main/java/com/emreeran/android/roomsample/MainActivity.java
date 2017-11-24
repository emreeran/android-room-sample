package com.emreeran.android.roomsample;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.emreeran.android.roomsample.db.SampleDb;

public class MainActivity extends AppCompatActivity {

    private SampleDb mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDb = Room.databaseBuilder(this, SampleDb.class, "sample.db").build();
    }
}
