package ar6t.nguyenlinh.android.com.karaoke.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeoutException;

import ar6t.nguyenlinh.android.com.karaoke.app.MainActivity;
import ar6t.nguyenlinh.android.com.karaoke.app.R;
import ar6t.nguyenlinh.android.com.karaoke.model.Music;

/**
 * Created by GameNet on 05/01/2017.
 */

public class MusicAdapter extends ArrayAdapter<Music> {
    Activity context;
    int resource;
    List<Music> objects;
    public MusicAdapter(Activity context, int resource, List<Music> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource= resource;
        this.objects = objects;
    }

    private int LayViTriBatDau(String chuoi, String chuoitimkiem){
        int vitri = 0;
        vitri = chuoi.indexOf(chuoitimkiem);
        return vitri;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource,null);
        TextView txtMa = (TextView) row.findViewById(R.id.txtMa);
        TextView txtTen = (TextView) row.findViewById(R.id.txtTenBaiHat);
        TextView txtCaSi = (TextView) row.findViewById(R.id.txtCaSi);
        ImageButton btnLike = (ImageButton) row.findViewById(R.id.btnLike);
        ImageButton btnDisLike = (ImageButton) row.findViewById(R.id.btnDislike);

        final Music music = this.objects.get(position);
        txtTen.setText(music.getTenBaiHat());
        txtMa.setText(music.getMaBaiHat());
        txtCaSi.setText(music.getCaSi());
        if(music.isThich()){
            btnLike.setVisibility(View.INVISIBLE);
            btnDisLike.setVisibility(View.VISIBLE);
        } else {
            btnLike.setVisibility(View.VISIBLE);
            btnDisLike.setVisibility(View.INVISIBLE);
        }
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyThich(music);
            }
        });
        btnDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyKhongThich(music);
            }
        });


        return row;

    }

    private void xuLyKhongThich(Music music) {
        ContentValues row = new ContentValues();
        row.put("YEUTHICH",0);
        MainActivity.database.update("ArirangSongList",
                row,
                "MABH=?",
                new String[]{music.getMaBaiHat()});
    }


    private void xuLyThich(Music music) {

        ContentValues row = new ContentValues();
        row.put("YEUTHICH",1);
        MainActivity.database.update("ArirangSongList",
                row,
                "MABH=?",
                new String[]{music.getMaBaiHat()});
    }
}
