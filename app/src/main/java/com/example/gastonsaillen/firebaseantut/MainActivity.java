package com.example.gastonsaillen.firebaseantut;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference mRootReference;
    Button mButtonSubirDatosFirebase;
    EditText mEditTextDatoNombreUsuario,mEditTextDatoApellidoUsuario,mEditTextDatoTelefonoUsuario,mEditTextDatoDireccionUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonSubirDatosFirebase = findViewById(R.id.btnSubirDatos);
        mButtonSubirDatosFirebase.setOnClickListener(this);
        mEditTextDatoNombreUsuario = findViewById(R.id.etNombre);
        mEditTextDatoApellidoUsuario = findViewById(R.id.etApellido);
        mEditTextDatoTelefonoUsuario = findViewById(R.id.etTelefono);
        mEditTextDatoDireccionUsuario = findViewById(R.id.etDireccion);

        mRootReference = FirebaseDatabase.getInstance().getReference();

        solicitarDatosFirebase();




    }

    private void solicitarDatosFirebase() {
        mRootReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){

                    mRootReference.child("Usuario").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userPojo user = snapshot.getValue(userPojo.class);
                            String nombre = user.getNombre();
                            String apellido = user.getApellido();
                            int telefono = user.getTelefono();
                            String direccion = user.getDireccion();

                            Log.e("NombreUsuario:",""+nombre);
                            Log.e("ApellidoUsuario:",""+apellido);
                            Log.e("TelefonoUsuario:",""+telefono);
                            Log.e("DireccionUsuario:",""+direccion);
                            Log.e("Datos:",""+snapshot.getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cargarDatosFirebase(String nombre, String apellido, int telefono, String direccion) {

        Map<String, Object> datosUsuario = new HashMap<>();
        datosUsuario.put("nombre", nombre);
        datosUsuario.put("apellido", apellido);
        datosUsuario.put("telefono", telefono);
        datosUsuario.put("direccion", direccion);

        mRootReference.child("Usuario").push().setValue(datosUsuario);
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.btnSubirDatos:

                String nombre = mEditTextDatoNombreUsuario.getText().toString();
                String apellido = mEditTextDatoApellidoUsuario.getText().toString();
                int telefono = Integer.parseInt(mEditTextDatoTelefonoUsuario.getText().toString());
                String direccion = mEditTextDatoDireccionUsuario.getText().toString();
                cargarDatosFirebase(nombre, apellido, telefono, direccion);
                break;
        }

    }
}
