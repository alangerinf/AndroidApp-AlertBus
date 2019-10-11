package ibao.alanger.alertbus.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ibao.alanger.alertbus.models.vo.PasajeroVO;
import ibao.alanger.alertbus.models.vo.ViajeVO;

public class PageViewModelViajesActuales extends ViewModel {

    private static MutableLiveData<List<ViajeVO>> viajeVOListLiveData;

    private static List<ViajeVO> viajeVOList;

    public LiveData<List<ViajeVO>> getViajeVOList(){
        init();
        return viajeVOListLiveData;
    }

    public static void set(List<ViajeVO> index) {
        init();
        viajeVOList =  index;
        viajeVOListLiveData.setValue(viajeVOList);
    }

    public static void addViaje(ViajeVO viajeVO){
        init();
        viajeVOList.add(viajeVO);
        set(viajeVOList);
    }

    public static void removeViaje(ViajeVO viajeVO){
        viajeVOList.remove(viajeVO);
        set(viajeVOList);
    }

    public static void updateViaje(ViajeVO viajeVO){
        for(ViajeVO v:viajeVOList){
            if(v.getId()==viajeVO.getId()){
                viajeVOList.remove(viajeVO);
                viajeVOList.add(viajeVO);
                break;
            }
        }
        set(viajeVOList);
    }

    public static void viajeToStatus3(int id){
        for(ViajeVO v:viajeVOList){
            if(v.getId()==id){
                v.setStatus(3);
                break;
            }
        }
        set(viajeVOList);
    }

    public static void viajeToStatus2(int id){
        for(ViajeVO v:viajeVOList){
            if(v.getId()==id){
                v.setStatus(2);
                break;
            }
        }
        set(viajeVOList);
    }

    public static void viajeToStatus1(int id){
        for(ViajeVO v:viajeVOList){
            if(v.getId()==id){
                v.setStatus(1);
                break;
            }
        }
        set(viajeVOList);
    }

    private static void init(){
        if(viajeVOListLiveData==null){
            viajeVOListLiveData = new MutableLiveData<>();
            viajeVOList = new ArrayList<>();
        }
    }
}