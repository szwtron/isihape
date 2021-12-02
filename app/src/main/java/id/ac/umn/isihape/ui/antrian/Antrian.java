package id.ac.umn.isihape.ui.antrian;

import java.io.Serializable;

public class Antrian implements Serializable {
    private String uid;

    public Antrian() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Antrian(String uid) {
        this.uid = uid;
    }

}
