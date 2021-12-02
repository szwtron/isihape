package id.ac.umn.isihape.admin.ui.CRUDAppointment;

import java.io.Serializable;

public class Appointments implements Serializable {
    private String nama;
    private String tanggal;
    private String dokter;
    private String status;
    private String typeHolder;
    private int backgroundColor;

    public Appointments() {

    }

    public Appointments(String nama, String tanggal, String dokter, String typeHolder, String status, int backgroundColor) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.dokter = dokter;
        this.status = status;
        this.typeHolder = typeHolder;
        this.backgroundColor = backgroundColor;
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

    public String getType() {
        return typeHolder;
    }

    public void setType(String typeHolder) {
        this.typeHolder = typeHolder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int setBackgroundColor(int backgroundColor) {
        return this.backgroundColor = backgroundColor;
    }

    public String toString() { return this.getTanggal() + " => " + this.getNama() + " => " + this.getDokter(); }
}
