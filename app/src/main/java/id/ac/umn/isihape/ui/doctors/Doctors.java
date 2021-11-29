package id.ac.umn.isihape.ui.doctors;

import java.io.Serializable;

public class Doctors implements Serializable {
    private String nama;
    private String spesialis;
    private String harga;
    private String alamat;

    public Doctors() {

    }

    public Doctors(String nama, String dokter, String harga, String waktu) {
        this.nama = nama;
        this.spesialis = dokter;
        this.harga = harga;
        this.alamat = waktu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public void setSpesialis(String spesialis) {
        this.spesialis = spesialis;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
