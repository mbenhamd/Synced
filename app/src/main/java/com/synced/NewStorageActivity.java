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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import org.apache.commons.net.ftp.*;




public class NewStorageActivity extends Activity {

    private static final String TAG = "NewStorageActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "New Storage Lunched");
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#657ff5")));
        setContentView(R.layout.activity_new_storage);
        CharSequence NameApp = getResources().getText(R.string.app_name);

        ImageButton Share = (ImageButton) findViewById(R.id.Share);
        ImageButton Exit = (ImageButton) findViewById(R.id.Exit);
        ImageButton GoButton = (ImageButton) findViewById(R.id.Go);
        ImageButton Contact = (ImageButton) findViewById(R.id.Contact);

        ShareOnClick(TAG, Share, NameApp);
        Email(TAG, Contact);
        ExitOnClick(Exit);
        Go(GoButton);


    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if(msg.arg1==1)
            {
                Toast.makeText(getApplicationContext(),"Your User Name or Password is incorrect",Toast.LENGTH_LONG).show();
            }
            if(msg.arg2==2)
            {
                Toast.makeText(getApplicationContext(),"Hostname or Port is incorrect", Toast.LENGTH_LONG).show();

            }
            return false;
        }
    });

    public void Go(ImageButton GoButton) {
        GoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean ExecThread=true;
                EditText User = (EditText) findViewById(R.id.User);
                EditText PassWord = (EditText) findViewById(R.id.Password);
                EditText Port = (EditText) findViewById(R.id.Port);
                EditText Host = (EditText) findViewById(R.id.IPAddress);
                final String SUser = User.getText().toString();
                final String SPassWord = PassWord.getText().toString();
                final String SPort = Port.getText().toString();
                final String SHost = Host.getText().toString();
                final Integer IPort = Integer.parseInt(SPort);
                if (SUser.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"User is empty",Toast.LENGTH_LONG).show();
                    ExecThread=false;
                }
                if(SHost.isEmpty()){
                    Toast.makeText(getApplicationContext(),"IP Address is empty",Toast.LENGTH_LONG).show();
                    ExecThread=false;
                }
                if (SPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Port is empty",Toast.LENGTH_LONG).show();
                    ExecThread=false;
                }
                if(IPort<0 || IPort>65535){
                    Toast.makeText(getApplicationContext(),"Port range 0-65535",Toast.LENGTH_LONG).show();
                    ExecThread=false;
                }

                if (ExecThread) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FTPClient test = new FTPClient();
                            try {
                                test.connect(SHost, IPort);
                                test.login(SUser, SPassWord);
                                Log.d(TAG, "OK");
                                Intent intent = new Intent(getApplicationContext(), SetupServerActivity.class);
                                intent.putExtra("password",SPassWord);
                                intent.putExtra("user",SUser);
                                intent.putExtra("host",SHost);
                                intent.putExtra("port",SPort);
                                startActivity(intent);
                            } catch (Exception e) {
                                Message msg = new Message();
                                if (test.isConnected()) {
                                    msg.arg1 = 1;
                                    handler.sendMessage(msg);
                                } else {
                                    msg.arg2 = 2;
                                    handler.sendMessage(msg);
                                }
                            }
                        }
                    });
                    thread.start();
                }
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
                Log.d(TAG, "Share Button Worked");
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

