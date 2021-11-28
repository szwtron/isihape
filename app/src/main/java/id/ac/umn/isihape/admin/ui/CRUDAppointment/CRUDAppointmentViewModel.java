package id.ac.umn.isihape.admin.ui.CRUDAppointment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CRUDAppointmentViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CRUDAppointmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}