package com.jarvis.assistant;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE}, 1);
        Button btn = findViewById(R.id.btnStart);
        TextView log = findViewById(R.id.txtLog);
        btn.setOnClickListener(v -> {
            Intent i = new Intent(this, JarvisService.class);
            startService(i);
            log.setText("JARVIS ON!\nBolo: Facebook Kholo\nCall Rafi\nMessage de");
        });
    }
}
