package id.ac.umn.isihape.admin.ui.CRUDDoctors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CRUDDoctorsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CRUDDoctorsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is CRUD doctors fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}