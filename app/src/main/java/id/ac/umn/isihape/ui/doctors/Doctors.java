package id.ac.umn.isihape.ui.doctors;

import java.io.Serializable;

public class Doctors implements Serializable {
    private String nama;
    private String spesialis;
    private String alamat;

    public Doctors() {

    }

    public Doctors(String tanggal, String dokter, String waktu) {
        this.nama = tanggal;
        this.spesialis = dokter;
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
}
