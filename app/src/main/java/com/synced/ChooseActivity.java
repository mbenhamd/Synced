/*

Created by Mohamed BEN HAMDOUNE, email contact : mbenhamd@gmail.com
Copyright 2015 Synced

 */

package com.synced;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.synced.ShareContactExitAccount.GenericMethods;


public class ChooseActivity extends Activity {

    private static final String TAG = "ChooseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#657ff5")));
        setContentView(R.layout.activity_choose);
        Log.d(TAG, "Choose Activity Initialized");
        CharSequence NameApp = getResources().getText(R.string.app_name);
        ImageButton Share = (ImageButton) findViewById(R.id.Share);
        ImageButton AddNewServer = (ImageButton) findViewById(R.id.AddButton);
        ImageButton Contact = (ImageButton) findViewById(R.id.Contact);
        ImageButton Exit = (ImageButton) findViewById(R.id.Exit);
        GenericMethods Same = new GenericMethods();

        Same.ShareOnClick(TAG, Share, NameApp);
        Same.Email(TAG, Contact);
        Same.ExitOnClick(Exit);

        AddNewConnectionServer(AddNewServer);

        Log.d(TAG, "Choose Activity Every Function is Fine");

    }

    public void AddNewConnectionServer(ImageButton AddNewServer) {
        AddNewServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(),NewStorageActivity.class);
                startActivity(intent);
            }
        });
    }

    public  void ShareOnClick(final String TAG,ImageButton Share, final CharSequence NameApp) {
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I invite you to Synced, a new app for simplify your life !");
                sendIntent.setType("text/plain");
                Intent A =Intent.createChooser(sendIntent, NameApp);
                startActivity(A);
                Log.d(TAG,"Share Button Worked");
            }
        });
    }

    public void Email(final String TAG,ImageButton Contact) {
        Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"synced.message@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Tell us what is it about ?");
                        i.putExtra(Intent.EXTRA_TEXT, "Thanks for helping us ! ");
                        startActivity(Intent.createChooser(i, "Contact us..."));
                        Log.d(TAG, "Email Button Worked");
                    }
        });
    }

    public void ExitOnClick(ImageButton Exit) {
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }


}

