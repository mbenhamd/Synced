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
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.io.FileInputStream;
import java.io.InputStream;



public class SetupServerActivity extends Activity {

    private static final String TAG = "SetupServerActivity";
    ImageButton SearchLocal;
    ImageButton SendFile;
    FTPClient client = new FTPClient();
    InputStream Fichier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "SetupServerActivity");
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#657ff5")));
        setContentView(R.layout.activity_setup_server);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        String user =getIntent().getExtras().getString("user");
        String host=getIntent().getExtras().getString("host");
        String port=getIntent().getExtras().getString("port");
        String password=getIntent().getExtras().getString("password");
        String PathLocal=getIntent().getExtras().getString("local_path");
        String PathServer=getIntent().getExtras().getString("server_path");
        String NameOfFile=getIntent().getExtras().getString("name_file");

        TextView ViewUser = (TextView) findViewById(R.id.Fill_User);
        TextView ViewHost= (TextView) findViewById(R.id.Fill_Host);
        TextView ViewPort= (TextView) findViewById(R.id.Fill_Port);
        TextView ViewInformations = (TextView) findViewById(R.id.Fill_Informations);
        TextView ViewPath = (TextView) findViewById(R.id.Fill_Local_Path);
        TextView ViewServer = (TextView) findViewById(R.id.Fill_Server_Path);
        TextView ViewNameFile = (TextView) findViewById(R.id.Fill_name_file_local);

        ConnectFTP(host, port, user, password);

        SetTextInformations(ViewUser, ViewHost, ViewPort, ViewInformations, host, user, port);
        PrintPath(PathLocal, PathServer, ViewPath, ViewServer, ViewNameFile, NameOfFile);
        GetFileOnServer(host, port, user, password, PathLocal, PathServer, NameOfFile);
        GetFileOnLocal(host, port, user, password, PathLocal, PathServer, NameOfFile);

        SendThisFile(PathServer, PathLocal, NameOfFile);
        CharSequence NameApp = getResources().getText(R.string.app_name);
        ImageButton Share = (ImageButton) getWindow().findViewById(R.id.Share);
        ImageButton Contact = (ImageButton) getWindow().findViewById(R.id.Contact);
        ImageButton Exit = (ImageButton) getWindow().findViewById(R.id.Exit);

        ShareOnClick(TAG, Share,NameApp);
        Email(TAG, Contact);
        ExitOnClick(Exit);
    }

    public void PrintPath(String PathLocal,String PathServer,TextView ViewPath,TextView ViewServer,TextView ViewNameFile,String NameOfFile){
        if (PathLocal==null)
        {
            ViewPath.setText("Local path empty");

        }
        else{
            ViewPath.setText(PathLocal);
        }
        if (PathServer==null){
            ViewServer.setText("Server path empty");
            ViewNameFile.setText("Empty");
        }
        else{
            ViewServer.setText(PathServer);

        }
        if (NameOfFile==null){
            ViewNameFile.setText("Empty");

        }
        else{
            ViewNameFile.setText(NameOfFile);
        }
    }

    public void SendThisFile(final String PathServer,final String PathLocal,final  String NameOfFile) {
        SendFile = (ImageButton) findViewById(R.id.Send_file);
        SendFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                Log.d(TAG,NameOfFile);
                try {
                    Fichier = new FileInputStream(PathLocal);
                    client.setFileType(FTP.BINARY_FILE_TYPE);
                    client.changeWorkingDirectory(PathServer);
                    if(client.storeFile(NameOfFile,Fichier)){
                        Log.d(TAG,"Envoi success");
                        Log.d(TAG,"Local: "+PathLocal+" Server: "+PathServer+"et le nom du fichier : ");
                    }
                    else{
                        Log.d(TAG,"Envoi error "+client.getReplyCode());
                        Log.d(TAG,"Local: "+PathLocal+" Server: "+PathServer+"et le nom du fichier : ");
                    }
                } catch (Exception a) {
                    a.printStackTrace();

                }
            }
        });
    }


    public void GetFileOnLocal(final String host, final String port, final String user, final String password,final String PathLocal,final String PathServer,final String NameOfFile){
        SearchLocal = (ImageButton) findViewById(R.id.Search_local);
        SearchLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), LocalDirectoryActivity.class);
                intent.putExtra("password", password);
                intent.putExtra("user", user);
                intent.putExtra("host", host);
                intent.putExtra("port", port);
                intent.putExtra("local_path", PathLocal);
                intent.putExtra("server_path", PathServer);
                intent.putExtra("name_file", NameOfFile);
                startActivity(intent);
            }
        });
    }

    public void GetFileOnServer(final String host, final String port, final String user, final String password,final  String PathLocal,final  String PathServer,final String NameOfFile){
        SearchLocal = (ImageButton) findViewById(R.id.Search_server);
        SearchLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(),DirectoryActivity.class);
                intent.putExtra("password",password);
                intent.putExtra("user",user);
                intent.putExtra("host",host);
                intent.putExtra("port", port);
                intent.putExtra("local_path",PathLocal);
                intent.putExtra("server_path", PathServer);
                intent.putExtra("name_file", NameOfFile);
                startActivity(intent);
            }
        });
    }

    public void ConnectFTP(String host,String port,String user,String password){
        try {
            client.setAutodetectUTF8(true);
            client.connect(host, Integer.parseInt(port));
            client.login(user, password);
            client.setControlEncoding("UTF-8");
            client.enterLocalPassiveMode();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SetTextInformations(TextView ViewUser,TextView ViewHost,TextView ViewPort,TextView ViewInformations,String host,String user,String port){
        ViewHost.setText(host);
        ViewUser.setText(user);
        ViewPort.setText(port);
        try{
            ViewInformations.setText(client.getSystemType());
        }
        catch (Exception e){
            ViewInformations.setText("No informations avalaible");
            e.printStackTrace();
        }
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

