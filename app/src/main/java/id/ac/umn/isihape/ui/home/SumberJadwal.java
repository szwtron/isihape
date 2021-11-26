package id.ac.umn.isihape.ui.home;

import java.io.Serializable;

public class SumberJadwal implements Serializable {
    private String tanggal;
    private String dokter;
    private String waktu;

    public SumberJadwal() {

    }

    public SumberJadwal(String tanggal, String dokter, String waktu) {
        this.tanggal = tanggal;
        this.dokter = dokter;
        this.waktu = waktu;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getDokter() {
        return dokter;
    }

    public void setDokter(String dokter) {
        this.dokter = dokter;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String toString() { return this.getTanggal() + " => " + this.getWaktu() + " => " + this.getDokter(); }
}
