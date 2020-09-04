package ibao.alertbus.ecosac.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ibao.alertbus.ecosac.models.vo.ViajeVO;

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
        for(ViajeVO v:viajeVOList){
            if(v.getId()==viajeVO.getId()){
                viajeVOList.remove(v);
                break;
            }
        }
        set(viajeVOList);
    }

    public static void updateViaje(ViajeVO viajeVO){
        for(ViajeVO v:viajeVOList){
            if(v.getId()==viajeVO.getId()){
                v.setId(viajeVO.getId());
                v.setIdWeb(viajeVO.getIdWeb());
                v.sethInicio(viajeVO.gethInicio());
                v.sethFin(viajeVO.gethFin());
                v.setPlaca(viajeVO.getPlaca());
                v.setRuta(viajeVO.getRuta());
                v.setCapacidad(viajeVO.getCapacidad());
                v.setEmpresa(viajeVO.getEmpresa());
                v.setProveedor(viajeVO.getProveedor());
                v.setStatus(viajeVO.getStatus());
                v.setConductor(viajeVO.getConductor());
                v.sethProgramada(viajeVO.gethProgramada());
                v.setNumPasajerosRegistrados(viajeVO.getNumPasajerosRegistrados());
                v.setNumRestriccionesRegistradas(viajeVO.getNumRestriccionesRegistradas());
                v.sethConfirmado(viajeVO.gethConfirmado());
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