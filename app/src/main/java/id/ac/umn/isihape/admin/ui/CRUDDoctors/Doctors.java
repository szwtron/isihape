package id.ac.umn.isihape.admin.ui.CRUDDoctors;

import java.io.Serializable;

public class Doctors implements Serializable {
    private String name;
    private String nik;
    private String harga;
    private String spesialis;
    private int backgroundColor;

    public Doctors() {

    }

    public Doctors(String name, String nik, String harga, String spesialis, int backgroundColor) {
        this.name = name;
        this.nik = nik;
        this.harga = harga;
        this.spesialis = spesialis;
        this.backgroundColor = backgroundColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public void setSpesialis(String spesialis) {
        this.spesialis = spesialis;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int setBackgroundColor(int backgroundColor) {
        return this.backgroundColor = backgroundColor;
    }

    public String toString() { return this.getNik() + " => " + this.getName() + " => " + this.getHarga(); }
}
