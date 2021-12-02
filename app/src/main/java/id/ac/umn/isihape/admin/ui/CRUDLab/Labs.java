package id.ac.umn.isihape.admin.ui.CRUDLab;

import java.io.Serializable;

public class Labs implements Serializable {
    private String nama, harga, dekkripsi;

    public Labs() {

    }

    public Labs(String nama, String harga, String dekkripsi) {
        this.nama = nama;
        this.harga = harga;
        this.dekkripsi = dekkripsi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDekkripsi() {
        return dekkripsi;
    }

    public void setDekkripsi(String dekkripsi) {
        this.dekkripsi = dekkripsi;
    }
}
