/*

Created by Mohamed BEN HAMDOUNE, email contact : mbenhamd@gmail.com
Copyright 2015 Synced

 */

package com.synced;

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
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.synced.DirectoryPackage.CustomListViewAdapter;
import com.synced.DirectoryPackage.RowItem;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;




public class DirectoryActivity extends Activity {

    private static final String TAG = "DirectoryActivity";
    private TextView myPath;
    String data = "";
    Stack <String> Before = new Stack <String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#657ff5")));
        setContentView(R.layout.activity_directory);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        myPath = (TextView) findViewById(R.id.path);
        String user =getIntent().getExtras().getString("user");
        String password=getIntent().getExtras().getString("password");
        String host=getIntent().getExtras().getString("host");
        String port=getIntent().getExtras().getString("port");
        String PathLocal =getIntent().getExtras().getString("local_path");
        String NameOfFile=getIntent().getExtras().getString("name_file");


        Integer I_port=Integer.parseInt(port);
        Log.d(TAG,user+" "+password+" "+host+" "+port);
        FTPClient client = new FTPClient();
        try {
            client.setAutodetectUTF8(true);
            client.connect(host, I_port);
            client.login(user, password);
            client.setControlEncoding("UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, data);
        ListView List = (ListView) findViewById(R.id.list_files);
        getDir(client);
        ClickItem(List, client);
        ClickButtonUp(List, client);
        SetPath(List,user,port,host,password,PathLocal,NameOfFile);
    }


    public void SetPath(ListView List,final String user,final String port,final String host,final String password,final String PathLocal,final String NameOfFile ) {
        List.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long arg) {
                TextView v = (TextView) view.findViewById(R.id.directory_name);
                Toast.makeText(getApplicationContext(), "selected Item Name is " + v.getText(), Toast.LENGTH_LONG).show();
                String Send = v.getText().toString();
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
                intent.putExtra("server_path", data);
                intent.putExtra("name_file",NameOfFile);
                Log.d(TAG,"Name of File :"+NameOfFile);
                intent.putExtra("local_path", PathLocal);
                intent.putExtra("password", password);
                intent.putExtra("user", user);
                intent.putExtra("host", host);
                intent.putExtra("port", port);
                startActivity(intent);
                return true;
            }
        });
 }

    private void getDir(final FTPClient Client) {
        List<RowItem> rowItems;
        rowItems = new ArrayList<RowItem>();
        myPath.setText("Location: " + data);
        try {
            Client.changeWorkingDirectory(data);
            FTPFile[] ListFiles = Client.listFiles(data);
            String[] f = Client.listNames();
            Log.d(TAG, Arrays.toString(f));
            for (int i = 0; i < f.length; i++) {
                if(!f[i].equals("..")) {
                        if(ListFiles[i].getType()==ListFiles[i].DIRECTORY_TYPE) {
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

    public void ClickItem(final ListView List,final FTPClient Client) {
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                // TODO Auto-generated method stub
                TextView v = (TextView) view.findViewById(R.id.directory_name);
                Toast.makeText(getApplicationContext(), "selected Item Name is " + v.getText(), Toast.LENGTH_LONG).show();
                String Send = v.getText().toString();
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
                getDir(Client);
            }
        });
    }

    public void ClickButtonUp(final ListView List,final FTPClient Client) {
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
                    getDir(Client);
                }
            });
    }

}
