package ar6t.nguyenlinh.android.com.karaoke.model;

/**
 * Created by GameNet on 05/01/2017.
 */

public class Music {
    private  String maBaiHat;
    private String tenBaiHat;
    private String caSi;

    public Music() {
    }

    public Music(String maBaiHat, String tenBaiHat, String caSi, boolean thich) {
        this.maBaiHat = maBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.caSi = caSi;
        this.thich = thich;
    }

    public boolean isThich() {
        return thich;
    }

    public void setThich(boolean thich) {
        this.thich = thich;
    }

    public String getMaBaiHat() {
        return maBaiHat;
    }

    public void setMaBaiHat(String maBaiHat) {
        this.maBaiHat = maBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getCaSi() {
        return caSi;
    }

    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }

    private boolean thich;
}
