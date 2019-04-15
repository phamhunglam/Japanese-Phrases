package com.burningteam.phamhunglamsp.japanesephrases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    ContentAdapter adapter;

    ArrayList<Content> contentArrayList = new ArrayList<>();

    ArrayList<String> target1 = new ArrayList<>();
    ArrayList<String> target2 = new ArrayList<>();
    ArrayList<String> translation = new ArrayList<>();
    ArrayList<String> mp3 = new ArrayList<>();
    SQLiteDatabase myDatabase;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.reset_menu_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.reload:
                myDatabase = this.openOrCreateDatabase("Phrases",MODE_PRIVATE,null);
                myDatabase.execSQL("DROP TABLE IF EXISTS phrasesJapanses ");
                contentArrayList.clear();

                adapter.notifyDataSetChanged();
                listView.invalidateViews();
                listView.refreshDrawableState();
//                Animation an = new RotateAnimation(0.0f, 360.0f, pivotX, pivotY);
//
//                // Set the animation's parameters
//                an.setDuration(10000);               // duration in ms
//                an.setRepeatCount(0);                // -1 = infinite repeated
//                an.setRepeatMode(Animation.REVERSE); // reverses each repeat
//                an.setFillAfter(true);               // keep rotation after animation
//
//                // Aply animation to image view
//                diskView.setAnimation(an);
                setup();
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public class DownloadTask extends AsyncTask<String, Void , String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1){

                    char current = (char) data;

                    result += current;

                    data = inputStreamReader.read();

                }

                return result;

            }
            catch(Exception e){

                e.printStackTrace();

                return "Failed";

            }
        }
    }

    public void Gettarget1 (String result){

        Pattern p = Pattern.compile("\"target1\"><strong>(.*?)</strong>");

        Matcher m = p.matcher(result);

        while (m.find()){

            target1.add(m.group(1));

        }

    }

    public void Gettarget2 (String result){

        Pattern p = Pattern.compile("\"target2\"><strong>(.*?)</strong>");

        Matcher m = p.matcher(result);

        while (m.find()){

            target2.add(m.group(1));

        }

    }

    public void Gettranslation (String result){

        Pattern p = Pattern.compile("\"translation\">(.*?)</div>");

        Matcher m = p.matcher(result);

        while (m.find()){

            translation.add(m.group(1));

        }

    }

    public void GetAudio (String result){

        Pattern p = Pattern.compile("JAPFND1_(.*?).mp3");

        Matcher m = p.matcher(result);

        while (m.find()){

            mp3.add(m.group(1));

        }

    }

    public void setup(){

        DownloadTask task = new DownloadTask();
        myDatabase = this.openOrCreateDatabase("Phrases",MODE_PRIVATE,null);
        String result = null;

        try {
            result = task.execute("http://www.nemoapps.com/phrasebooks/japanese").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Gettarget1(result);
        Gettarget2(result);
        Gettranslation(result);
        GetAudio(result);
        try{

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS phrasesJapanses(target1 VARCHAR, target2 VARCHAR,translation VARCHAR," +
                    "audio VARCHAR ,id INTEGER PRIMARY KEY )");

            for (int i =0; i<target1.size(); i++){
                ContentValues myContentValues = new ContentValues();
                myContentValues.put("target1",target1.get(i));
                myContentValues.put("target2",target2.get(i));
                myContentValues.put("translation",translation.get(i));
                myContentValues.put("audio",mp3.get(i));
                myDatabase.insert("phrasesJapanses",null,myContentValues);
            }
            Database();
            adapter.notifyDataSetChanged();
            listView.invalidateViews();
            listView.refreshDrawableState();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void Database(){
        try{
            myDatabase = this.openOrCreateDatabase("Phrases",MODE_PRIVATE,null);

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS phrasesJapanses(target1 VARCHAR, target2 VARCHAR,translation VARCHAR," +
                    "audio VARCHAR ,id INTEGER PRIMARY KEY )");

            Cursor c =myDatabase.rawQuery("SELECT * FROM phrasesJapanses", null);


            c.moveToFirst();
            while (c!=null){
                int id = getResources().getIdentifier("_"+c.getString(3), "raw", getPackageName());
                Uri mUri = Uri.parse("android.resource://" + getPackageName() + "/" + id);
                contentArrayList.add(new Content(c.getString(0),c.getString(1),c.getString(2),mUri));
                c.moveToNext();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database();

        listView = (ListView ) findViewById(R.id.listview);

        adapter = new ContentAdapter(contentArrayList,getApplicationContext());
        listView.setAdapter(adapter);

    }
}
