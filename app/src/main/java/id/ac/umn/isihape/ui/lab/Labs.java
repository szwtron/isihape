package id.ac.umn.isihape.ui.lab;

import java.io.Serializable;

public class Labs implements Serializable {
    private String nama;
    private String deskripsi;
    private String harga;

    public Labs() {
    }

    public Labs(String nama, String deskripsi, String harga) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
