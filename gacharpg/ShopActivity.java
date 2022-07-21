package com.zybooks.gacharpg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class ShopActivity extends AppCompatActivity {
    private GachaDB db;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        db = GachaDB.getInstance(this);
        random = new Random();
    }

    public void onClick(View view) {
        String[] names = db.getAll();
        int bound = names.length;

        db.unlockCharacter(names[random.nextInt(bound)]);
    }
}
