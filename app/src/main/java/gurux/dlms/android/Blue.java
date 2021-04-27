package gurux.dlms.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Set;

public class Blue extends Fragment {
    CheckBox enable_bt, visible_bt;
    ImageView search_bt;
    TextView name_bt;
    ListView listView;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;

    public Blue() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blue, container, false);
        enable_bt = view.findViewById(R.id.enable_bt);
        visible_bt = view.findViewById(R.id.visible_bt);
        search_bt = view.findViewById(R.id.search_bt);
        name_bt = view.findViewById(R.id.name_bt);
        listView = view.findViewById(R.id.list_view);

        name_bt.setText(getLocalBluetoothName());

        BA = BluetoothAdapter.getDefaultAdapter();
        if (BA == null) {
            Toast.makeText(getActivity(), "Bluetooth not supported", Toast.LENGTH_SHORT).show();



        }
        if (BA.isEnabled()) {
            enable_bt.setChecked(true);
        }
        enable_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    BA.disable();
                    Toast.makeText(getActivity(), "turned off", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intentOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intentOn, 0);
                    Toast.makeText(getActivity(), "Turned on", Toast.LENGTH_SHORT).show();
                }
            }
        });
        visible_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(getVisible, 0);
                    Toast.makeText(getActivity(), "Visible for 2 mins", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list();
            }
        });

        return view;
    }

    /**
     * Search obis code.
     */
    private void list() {
        pairedDevices = BA.getBondedDevices();
        ArrayList list = new ArrayList();
        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
        }
        Toast.makeText(getActivity(), "Showing Devices", Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }
    public String getLocalBluetoothName() {
        if (BA == null) {
            BA = BluetoothAdapter.getDefaultAdapter();
        }
        String name1;
        name1 = BA.getName();
        if (name1 == null) {
            name1 = BA.getAddress();
        }
        return name1;
    }

}
