package com.example.codercoral.interview;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{
    private TextView textView;
    private Button button;
    public static final int UPDATA_TEXT = 1;
    public Button button_two;
    
    public ListView listView;
    private List<Fruit> fruitList = new ArrayList<>();
    private String[] fruits = { "Apple", "Banana", "Pie","Apple", "Banana", "Pie","Apple", "Banana", "Pie","Apple", "Banana", "Pie"} ;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATA_TEXT:
                    //UI操作
                    textView.setText(" baby");
                    break;
                    default:
                        break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        button = findViewById(R.id.button);
        button_two = findViewById(R.id.button_two);
        listView = findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fruit fruit = fruitList.get(position);
                Toast.makeText(MainActivity.this, fruit.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        FruitAdapter adapter = new FruitAdapter(MainActivity.this, R.layout.fruit_item, fruitList);
        listView.setAdapter(adapter);
        initFruit();
        button.setOnClickListener(this);
        button_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tag", "onClick execute");
            }
        });
        button_two.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(MainActivity.this, JsonActivity.class));
                Log.d("Tag", "onTouch execute, action"+event.getAction());
                return false;
            }
        });
        
    }

    public void initFruit(){
        Fruit apple = new Fruit ("Apple", R.mipmap.apple_pic);
        Fruit banana = new Fruit("banana", R.mipmap.banana_pic);
        for(int i = 0; i<100; i++){
            fruitList.add(apple);
            fruitList.add(banana);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATA_TEXT;
                        handler.sendMessage(message);
                    }
                }).start();
                break;
                default:
                    break;
                }
        }
    }

