package ibao.alertbus.ecosac.viajeEnCurso;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import ibao.alertbus.ecosac.models.vo.PasajeroVO;
import ibao.alertbus.ecosac.models.vo.ViajeVO;

public class PageViewModelViaje extends ViewModel {

    private static MutableLiveData<ViajeVO> viajeVOMutableLiveData;


    private LiveData<ViajeVO> viajeVOLiveData = Transformations.map(viajeVOMutableLiveData, new Function<ViajeVO, ViajeVO>() {
        @Override
        public ViajeVO apply(ViajeVO input) {
            return input;
        }
    });

    public static void set(ViajeVO index) {
        viajeVOMutableLiveData.setValue(index);
    }

    public static void addPasajero(PasajeroVO pasa){
        viajeVOMutableLiveData.getValue().getPasajeroVOList().add(0,pasa);
        viajeVOMutableLiveData.setValue(viajeVOMutableLiveData.getValue());
    }

    public static void addPasajero(int pos, PasajeroVO pasa){
        viajeVOMutableLiveData.getValue().getPasajeroVOList().add(pos,pasa);
        viajeVOMutableLiveData.setValue(viajeVOMutableLiveData.getValue());
    }

    public static void removePasajero(PasajeroVO tareoDetalleVO){

        viajeVOMutableLiveData.getValue().getPasajeroVOList().remove(tareoDetalleVO);
        viajeVOMutableLiveData.setValue(viajeVOMutableLiveData.getValue());
    }

    public static PasajeroVO removePasajero(int pos){

        PasajeroVO pasajeroVO = viajeVOMutableLiveData.getValue().getPasajeroVOList().remove(pos);
        viajeVOMutableLiveData.setValue(viajeVOMutableLiveData.getValue());
        return  pasajeroVO;
    }


    public LiveData<ViajeVO> get() {
        return viajeVOLiveData;
    }

    public static void init(){
        viajeVOMutableLiveData = new MutableLiveData<>();
    }

}