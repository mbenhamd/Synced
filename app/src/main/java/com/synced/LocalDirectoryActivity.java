/*

Created by Mohamed BEN HAMDOUNE, email contact : mbenhamd@gmail.com
Copyright 2015 Synced

 */

package com.synced;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.synced.DirectoryPackage.CustomListViewAdapter;
import com.synced.DirectoryPackage.RowItem;


public class LocalDirectoryActivity extends Activity {

    private static final String TAG = "LocalDirectoryActivity";
    private TextView myPath;
    String data = ".";
    String Send=null;
    Stack <String> Before = new Stack <String>();
    File[] FileArray;
    File OneFile= new File(data);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#657ff5")));
        setContentView(R.layout.activity_local_directory);
        myPath = (TextView) findViewById(R.id.path);

        String user =getIntent().getExtras().getString("user");
        String password=getIntent().getExtras().getString("password");
        String host=getIntent().getExtras().getString("host");
        String port=getIntent().getExtras().getString("port");
        String PathServer=getIntent().getExtras().getString("server_path");

        ListView List = (ListView) findViewById(R.id.list_files);
        getDir();
        ClickItem(List);
        ClickButtonUp();
        SetPath(List, user, port, host, password,PathServer);
    }

    public void SetPath(ListView List,final String user,final String port,final String host,final String password,final String PathServer) {
        List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
                TextView v = (TextView) view.findViewById(R.id.directory_name);
                Toast.makeText(getApplicationContext(), "selected Item Name is " + v.getText(), Toast.LENGTH_LONG).show();
                Send = v.getText().toString();
                Log.d(TAG,"Selection : "+Send);
                if (Send.equals("..")) {
                    if (!Before.empty()) {
                        String RemovePath = Before.pop();
                        data = data.substring(0, data.length() - RemovePath.length() - 1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Can't Up Directory, you're at the top of the World" + v.getText(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Before.push(Send);
                    data = data + "/" + Send;
                }
                if (Before.empty()) {
                    data = "/";
                }
                Log.d(TAG, "Le path est : " + data);
                Intent intent = new Intent(getApplicationContext(), SetupServerActivity.class);
                intent.putExtra("local_path", data);
                intent.putExtra("server_path",PathServer);
                intent.putExtra("password", password);
                intent.putExtra("user", user);
                intent.putExtra("host", host);
                intent.putExtra("port", port);
                intent.putExtra("name_file",Send);
                Log.d(TAG,"AAAAAAAAAAAAAAAAAAAAAAAA "+Send);
                startActivity(intent);
                return true;
            }
        });
    }



    private void getDir() {
        List<RowItem> rowItems;
        rowItems = new ArrayList<RowItem>();
        myPath.setText("Location: " + data);
        try {
            OneFile = new File(data+"/");
            Log.d(TAG, "Dans getDir : "+data);
            FileArray = OneFile.listFiles();
            String[] f = OneFile.list();
            Log.d(TAG, Arrays.toString(f));
            for (int i = 0; i < f.length; i++) {
                if(!f[i].equals("..")) {
                    if(FileArray[i].isDirectory()) {
                        RowItem item = new RowItem(R.drawable.ic_folder_black_24dp, f[i]);
                        rowItems.add(item);
                    }
                    else
                    {
                        RowItem item = new RowItem(R.drawable.ic_add_box_black_24dp, f[i]);
                        rowItems.add(item);
                    }
                }
            }
            ListView listView;
            listView = (ListView) findViewById(R.id.list_files);
            CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                    R.layout.template_directory, rowItems);
            listView.setAdapter(adapter);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ClickItem(final ListView List) {
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                // TODO Auto-generated method stub
                TextView v = (TextView) view.findViewById(R.id.directory_name);
                Toast.makeText(getApplicationContext(), "selected Item Name is " + v.getText(), Toast.LENGTH_LONG).show();
                Send = v.getText().toString();
                if (Send.equals("..")) {
                    if (!Before.empty()) {
                        String RemovePath = Before.pop();
                        data = data.substring(0, data.length() - RemovePath.length() - 1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Can't Up Directory, you're at the top of the World" + v.getText(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Before.push(Send);
                    data = data + "/" + Send;
                }
                if (Before.empty()) {
                    data = ".";
                }
                Log.d(TAG, "Le path est : " + data);
                getDir();
            }
        });
    }

    public void ClickButtonUp() {
        ImageButton ButtonUp = (ImageButton) findViewById(R.id.Up);
        ButtonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (!Before.empty()) {
                    String RemovePath = Before.pop();
                    data = data.substring(0, data.length() - RemovePath.length() - 1);
                } else {
                    data="/";
                    Toast.makeText(getApplicationContext(), "Can't Up Directory, you're at the top of the World", Toast.LENGTH_LONG).show();
                }
                getDir();
            }
        });
    }

}
