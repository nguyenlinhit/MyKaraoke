package ar6t.nguyenlinh.android.com.karaoke.app;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ar6t.nguyenlinh.android.com.karaoke.adapter.MusicAdapter;
import ar6t.nguyenlinh.android.com.karaoke.model.Music;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView lvBaiHatGoc;
    ArrayList<Music> dsBHGoc;
    MusicAdapter adapterBHGoc;

    ListView lvBaiHatYeuThich;
    ArrayList<Music> dsBaiHatYeuThich;
    MusicAdapter adapterBaiHatYeuThich;

    TabHost tabHost;

    String chuoitimkiem = "";

    public static String DATABASE_NAME = "Arirang.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xyLySaoChepCSDL();

        addControls();
        addEvents();

        xyLyHienThiBaiHatGoc();
    }

    private void xyLySaoChepCSDL() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()){
            try{
                CoppyDataBaseAsset();
                Toast.makeText(this,"Coppying sucess from" +
                        "Assets folder", Toast.LENGTH_LONG).show();
            } catch (Exception ex){
                Toast.makeText(this, ex.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void CoppyDataBaseAsset() {
        try{
            InputStream myInput;
            //Lấy đường dẫn SQLite
            myInput = getAssets().open(DATABASE_NAME);
            //Lấy đường dẫn thư mục gốc
            String outFileName= LayDuongDanLuuTru();

            File f = new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            //Kiểm tra tồn tại
            if(!f.exists()){
                f.mkdir();
            }
            //Mở db rỗng từ outputStream
            OutputStream myOutput = new FileOutputStream(outFileName);
            //Lấy dữ liệu từ inputFile lên outputFile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0){
                //ghi file
                myOutput.write(buffer,0,length);
            }
            //Đóng file
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception ex){
            Log.e("Loi_SaoChep",ex.toString());
        }
    }

    private String LayDuongDanLuuTru() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+
                DATABASE_NAME;
    }

    private void addEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if (s.equalsIgnoreCase("t1")){
                    xyLyHienThiBaiHatGoc();
                } else if (s.equalsIgnoreCase("t2")){
                    xuLyBaiHatYeuThich();
                }
            }
        });
    }

    private void xuLyBaiHatYeuThich() {
        //Buocs 1: Mở CSDL
        database = openOrCreateDatabase(DATABASE_NAME,
                MODE_PRIVATE,null);
        //Cursor cursor = database.query("MyContact",null,null,
        //      null,null,null,null);
        Cursor cursor1 = database.rawQuery("select * from ArirangSongList where YEUTHICH = 1 ",null);

        dsBaiHatYeuThich.clear();
        while (cursor1.moveToNext()){
            String maBH = cursor1.getString(0);
            String tenBh = cursor1.getString(1);
            String casi = cursor1.getString(3);
            int yeuthich = cursor1.getInt(5);

            Music music = new Music();
            music.setMaBaiHat(maBH);
            music.setTenBaiHat(tenBh);
            music.setCaSi(casi);
            music.setThich(yeuthich==1);
            dsBaiHatYeuThich.add(music);
        }
        cursor1.close();
        adapterBaiHatYeuThich.notifyDataSetChanged();
    }


    private void xyLyHienThiBaiHatGoc() {
        //Buocs 1: Mở CSDL
        database = openOrCreateDatabase(DATABASE_NAME,
                MODE_PRIVATE,null);
        //Cursor cursor = database.query("MyContact",null,null,
        //      null,null,null,null);
        Cursor cursor1 = database.rawQuery("select * from ArirangSongList",null);

        dsBHGoc.clear();
        while (cursor1.moveToNext()){
            String maBH = cursor1.getString(0);
            String tenBh = cursor1.getString(1);
            String casi = cursor1.getString(3);
            int yeuthich = cursor1.getInt(5);

            Music music = new Music();
            music.setMaBaiHat(maBH);
            music.setTenBaiHat(tenBh);
            music.setCaSi(casi);
            music.setThich(yeuthich==1);
            dsBHGoc.add(music);
        }
        cursor1.close();
        adapterBHGoc.notifyDataSetChanged();
    }

    public ArrayList<Music> layDanhSachBaiHatTheoMa(String tenbaihat){
        database = openOrCreateDatabase(DATABASE_NAME,
                MODE_PRIVATE,null);

        ArrayList<Music> list = new ArrayList<Music>();
        Cursor cursor = database.rawQuery("select * from ArirangSongList where TENBH LIKE '%" + tenbaihat.toLowerCase() +"%'",null);

        while (cursor.moveToNext()){
            String maBH = cursor.getString(0);
            String tenBh = cursor.getString(1);
            String casi = cursor.getString(3);
            int yeuthich = cursor.getInt(5);

            Music music = new Music();
            music.setMaBaiHat(maBH);
            music.setTenBaiHat(tenBh);
            music.setCaSi(casi);
            music.setThich(yeuthich==1);
            list.add(music);
        }

        cursor.close();
        return list;
    }

    private void addControls() {

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("",getResources().getDrawable(R.drawable.music));
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("",getResources().getDrawable(R.drawable.favorite));
        tabHost.addTab(tab2);


        lvBaiHatGoc = (ListView) findViewById(R.id.lvBaiHatGoc);
        dsBHGoc = new ArrayList<>();
        adapterBHGoc = new MusicAdapter(MainActivity.this,
                R.layout.item,
                dsBHGoc);
        lvBaiHatGoc.setAdapter(adapterBHGoc);

        lvBaiHatYeuThich = (ListView) findViewById(R.id.lvBaiHatYeuThich);
        dsBaiHatYeuThich = new ArrayList<>();
        adapterBaiHatYeuThich = new MusicAdapter(MainActivity.this,
                R.layout.item,
                dsBaiHatYeuThich);
        lvBaiHatYeuThich.setAdapter(adapterBaiHatYeuThich);

        //giaLapBaiHat();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        if(searchView != null){
            searchView.setOnQueryTextListener(this);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        dsBHGoc = layDanhSachBaiHatTheoMa(newText);
        adapterBHGoc = new MusicAdapter(MainActivity.this,
                R.layout.item,
                dsBHGoc);
        lvBaiHatGoc.setAdapter(adapterBHGoc);
        adapterBHGoc.notifyDataSetChanged();
        chuoitimkiem =newText;
        return true;
    }
}
