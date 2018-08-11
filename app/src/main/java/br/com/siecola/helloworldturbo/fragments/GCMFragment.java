package br.com.siecola.helloworldturbo.fragments;

import android.app.Fragment;

import br.com.siecola.helloworldturbo.gcm.GCMRegisterEvents;

import java.io.IOException;

import br.com.siecola.helloworldturbo.R;
import br.com.siecola.helloworldturbo.gcm.GCMRegister;
import br.com.siecola.helloworldturbo.gcm.GCMRegisterEvents;
import br.com.siecola.helloworldturbo.models.ProductInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GCMFragment extends Fragment implements GCMRegisterEvents {

    private String registrationID;
    private GCMRegister gcmRegister;
    private Button btnUnregister;
    private Button btnRegister;
    private Button btnClearMessage;
    private EditText edtSenderID;
    private TextView txtRegistrationID;

    private TextView txtProductId;
    private TextView txtProductName;
    private TextView txtProductModel;
    private TextView txtProductCode;
    private TextView txtProductPrice;

    private ProductInfo productInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gcm, container,
                false);

        getActivity().setTitle("GCM");

        edtSenderID = (EditText) rootView.findViewById(R.id.edtSenderID);
        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        btnUnregister = (Button) rootView.findViewById(R.id.btnUnregister);
        btnClearMessage = (Button) rootView.findViewById(
                R.id.btnClearMessage);
        txtRegistrationID = (TextView) rootView.findViewById(
                R.id.txtRegistrationID);

        txtProductId = (TextView) rootView.findViewById(
                R.id.txtProductId);
        txtProductName = (TextView) rootView.findViewById(
                R.id.txtProductName);
        txtProductModel = (TextView) rootView.findViewById(
                R.id.txtProductModel);
        txtProductCode = (TextView) rootView.findViewById(
                R.id.txtProductCode);
        txtProductPrice = (TextView) rootView.findViewById(
                R.id.txtProductPrice);

        if (gcmRegister == null)
            gcmRegister = new GCMRegister(getActivity(), this);

        edtSenderID.setText(gcmRegister.getSenderId());

        if (!gcmRegister.isRegistrationExpired()) {
            registrationID = gcmRegister.getCurrentRegistrationId();
            setForRegistered(registrationID);
        } else {
            setForUnregistered();
        }

        Bundle bundle = this.getArguments();
        if ((bundle != null) && (bundle.containsKey("productInfo"))) {
            productInfo = (ProductInfo) bundle.getSerializable(
                    "productInfo");

            txtProductId.setText(Long.toString(productInfo.getId()));
            //txtProductName.setText(productInfo.getModel());
            //txtProductModel.setText(productInfo.getModel());
            txtProductCode.setText(Integer.toString(
                    productInfo.getCode()));
            txtProductPrice.setText(Float.toString(
                    productInfo.getPrice()));
        }

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationID = gcmRegister.getRegistrationId(
                        edtSenderID.getText().toString());
                if ((registrationID == null) ||
                        (registrationID.length() == 0)) {
                    Toast.makeText(getActivity(),
                            "Dispositivo ainda não registrado na nuvem. " +
                                    "Tentando...",
                            Toast.LENGTH_SHORT).show();
                    setForUnregistered();
                }
                else {
                    Toast.makeText(getActivity(),
                            "Dispositivo já registrado na nuvem.",
                            Toast.LENGTH_SHORT).show();
                    setForRegistered(registrationID);
                }
            }
        });

        btnUnregister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gcmRegister.unRegister();
            }
        });

        btnClearMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtProductId.setText("");
                txtProductName.setText("");
                txtProductModel.setText("");
                txtProductCode.setText("");
                txtProductPrice.setText("");
            }
        });

        return rootView;
    }

    @Override
    public void gcmRegisterFinished(String registrationID) {
        Toast.makeText(getActivity(),
                "Dispositivo registrado na nuvem com sucesso.",
                Toast.LENGTH_SHORT).show();
        setForRegistered(registrationID);
    }

    @Override
    public void gcmRegisterFailed(IOException ex) {
        Toast.makeText(getActivity(),
                "Falha ao registrar dispositivo na nuvem. " +
                        ex.getMessage(), Toast.LENGTH_SHORT).show();
        setForUnregistered();
    }

    @Override
    public void gcmUnregisterFinished() {
        Toast.makeText(getActivity(),
                "Dispositivo desregistrado da nuvem.",
                Toast.LENGTH_SHORT).show();
        setForUnregistered();
    }

    @Override
    public void gcmUnregisterFailed(IOException ex) {
        Toast.makeText(getActivity(),
                "Falha ao desregistrar o dispositivo na nuvem.",
                Toast.LENGTH_SHORT).show();
    }

    private void setForRegistered (String regID) {
        txtRegistrationID.setText(regID);
        btnUnregister.setEnabled(true);
        btnRegister.setEnabled(false);
    }

    private void setForUnregistered () {
        txtRegistrationID.setText("");
        btnUnregister.setEnabled(false);
        btnRegister.setEnabled(true);
    }
}