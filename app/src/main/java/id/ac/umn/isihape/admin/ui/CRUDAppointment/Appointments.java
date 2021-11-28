package id.ac.umn.isihape.admin.ui.CRUDAppointment;

import java.io.Serializable;

public class Appointments implements Serializable {
    private String nama;
    private String tanggal;
    private String dokter;

    public Appointments() {

    }

    public Appointments(String nama, String tanggal, String dokter) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.dokter = dokter;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String toString() { return this.getTanggal() + " => " + this.getNama() + " => " + this.getDokter(); }
}
