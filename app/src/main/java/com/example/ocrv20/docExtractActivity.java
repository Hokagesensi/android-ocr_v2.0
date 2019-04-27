package com.example.ocrv20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class docExtractActivity extends AppCompatActivity {


    private TextView text_extract;
    private Button btn_extract_cancel;
    private Button btn_extract_save;
    private String data;
    private String tag="appTest";
    private StringBuffer sb=new StringBuffer();
    private String text = new String();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_extract);

        text_extract = findViewById(R.id.text_extract);
        btn_extract_cancel = findViewById(R.id.btn_extract_cancel);
        btn_extract_save = findViewById(R.id.btn_extract_save);

        Intent intent = getIntent();
        data = intent.getStringExtra("text");

        String[] clauses = StrSplit.splitText(data);
        String[] keyWords = StrSplit.getKeyWords();

        for(int i=0;i<clauses.length;i++) {
            if(clauses[i]!=null) {
                Log.i(tag, keyWords[i] + ": " + clauses[i]);
                sb.append(keyWords[i]);
                sb.append(":");
                sb.append(clauses[i]);
                sb.append("\n\n");
            }
            else {
                Log.i(tag, keyWords[i]);
                sb.append(keyWords[i]);
                sb.append("\n");
            }
        }

        text = sb.toString();
        text_extract.setText(text);
        text_extract.setMovementMethod(ScrollingMovementMethod.getInstance());

        btn_extract_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docExtractActivity.this.finish();
            }
        });
        //保存数据
        btn_extract_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filepath = getExternalFilesDir("txt").toString()+"/";

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                //获取当前时间
                Date date = new Date(System.currentTimeMillis());
                String time = simpleDateFormat.format(date);
                String filename = time+".txt";
                Log.i(tag,"text文本保存路径："+filepath+filename);
                FileUtils.writeTxtToFile(text,filepath,filename);
            }
        });

    }
}
