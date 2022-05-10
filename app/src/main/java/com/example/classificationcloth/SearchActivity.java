package com.example.classificationcloth;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.badge.BadgeUtils;

import java.io.IOException;
import java.util.Timer;

public class SearchActivity extends AppCompatActivity {
    private EditText EditText;
    private Button btnSearch;
    private Switch switchBrowser;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText = (EditText) findViewById(R.id.EditText);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        switchBrowser = (Switch) findViewById(R.id.switchBrowser);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        Bundle bundle=getIntent().getExtras();

        EditText.setText(bundle.getString("label"));


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(v.VISIBLE);

                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        //Do Something
                    }
                    public void onFinish() {
                        spinner.setVisibility(v.INVISIBLE);
                        String words = EditText.getText().toString();

                        if(FieldNotBlank()) {
                            if(switchBrowser.isChecked()) //Browser si apre nell'app
                                SearchOnInternet(words);
                            else //Browser si apre fuori l'app
                                SearchOnInternetCompat(words);
                        }
                    }
                }.start();

            }
        });
    }

    //Browser si apre nell'app
    public void SearchOnInternet(String words){
        try{
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, words);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            e.printStackTrace();
            SearchOnInternetCompat(words);
        }
    }

    //Browser si apre fuori l'app
    public void SearchOnInternetCompat(String words){
        try{
            Uri uri = Uri.parse("https://www.google.com/search?q=" + words);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean FieldNotBlank()
    {
        boolean res = false;
        if( EditText.getText().toString().trim().equals(""))
            EditText.setError( "Is required!" );
        else
            res = true;
        return res;
    }

}
