package ibao.alertbus.ecosac.views.Trasbordo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ibao.alertbus.ecosac.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TListViajes extends Fragment {

    public TListViajes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_t_list_viajes, container, false);
    }
}
