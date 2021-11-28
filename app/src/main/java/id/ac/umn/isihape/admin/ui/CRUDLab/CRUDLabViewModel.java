package id.ac.umn.isihape.admin.ui.CRUDLab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CRUDLabViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CRUDLabViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}