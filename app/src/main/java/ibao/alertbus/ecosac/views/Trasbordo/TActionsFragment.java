package ibao.alertbus.ecosac.views.Trasbordo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ibao.alertbus.ecosac.R;

import static ibao.alertbus.ecosac.views.TrasbordoActivity.manager;

/**
 * A simple {@link Fragment} subclass.
 */
public class TActionsFragment extends Fragment {

    Button btnLeerQR;
    Button btnVerViajes;

    public TActionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnLeerQR = getActivity().findViewById(R.id.btnLeerQR);
        btnVerViajes = getActivity().findViewById(R.id.btnVerViajes);

        btnLeerQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction().show(manager.findFragmentById(R.id.TLeerQRFragment))
                        .hide(manager.findFragmentById(R.id.TActionsFragment))
                        .hide(manager.findFragmentById(R.id.TListViajes))
                        .commit();
            }
        });

        btnVerViajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.beginTransaction().show(manager.findFragmentById(R.id.TListViajes))
                        .hide(manager.findFragmentById(R.id.TActionsFragment))
                        .hide(manager.findFragmentById(R.id.TLeerQRFragment))
                        .commit();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_t_actions, container, false);
    }
}
