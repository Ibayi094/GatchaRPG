package com.zybooks.gacharpg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private GachaDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = GachaDB.getInstance(this);
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void OnClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.buttonShop:
                intent = new Intent(this, ShopActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonBattle:
                intent = new Intent(this, BattleActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonRoster:
                intent = new Intent(this, RosterActivity.class);
                startActivity(intent);
                break;
        }
    }

}
