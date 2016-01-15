/*

Created by Mohamed BEN HAMDOUNE, email contact : mbenhamd@gmail.com
Copyright 2015 Synced

 */

package com.synced.ShareContactExitAccount;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class GenericMethods extends Activity {

    public  void ShareOnClick(final String TAG,ImageButton Share, final CharSequence NameApp){
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I invite you to Synced, a new app to simplify your life !");
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
                i.putExtra(Intent.EXTRA_SUBJECT,"Tell us what is it about ?");
                i.putExtra(Intent.EXTRA_TEXT,"Thanks for helping us ! ");
                Intent A =Intent.createChooser(i, "Contact us...");
                startActivity(A);
                Log.d(TAG,"Email Button Worked");
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
