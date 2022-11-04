package com.example.a06ejer_lista_compra_recyclerview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.a06ejer_lista_compra_recyclerview.adapters.ProductoAdapter;
import com.example.a06ejer_lista_compra_recyclerview.modelos.Producto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.example.a06ejer_lista_compra_recyclerview.databinding.ActivityMainBinding;

import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Producto> productosList;
    private ProductoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        productosList = new ArrayList<>();
        //crearTodos();
        
        //linkear main con adapter(no se dice así pero me aclaro)
        adapter = new ProductoAdapter(productosList,R.layout.productos_view_model,MainActivity.this);
        binding.contentMain.contenedor.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);
        

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                crateProducto("CREAR PRODUCTO").show();



            }
        });
    }

    private AlertDialog crateProducto(String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(titulo);
        builder.setCancelable(false);

        //Inflar y referenciar textViews
        View contenido = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_producto_alert_dialog, null);
        builder.setView(contenido);

        TextView txtNombre = contenido.findViewById(R.id.txtNombreAddProducto);
        TextView txtPrecio = contenido.findViewById(R.id.txtPrecioAddProducto);
        TextView txtCantidad = contenido.findViewById(R.id.txtCantidadAddProducto);
        TextView lblToTal = contenido.findViewById(R.id.lblTotalAddProducto);

        TextWatcher textWatcher = new TextWatcher() {
            /**
             * Al modificar un cuadro de texto
             * @param charSequence -> envia el contenido que habia antes del cambio
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * al modificar el cuadro de texto
             * @param charSequence -> Envia el texto actual despues de la modificacion
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * Se dispara al terminar la modificacion
             * @param editable -> encia el contenido final del cuadro de texto
             */
            @Override
            public void afterTextChanged(Editable editable) {

                try {

                    int cantidad = Integer.parseInt(txtCantidad.getText().toString());
                    float precio = Float.parseFloat(txtPrecio.getText().toString());

                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                    lblToTal.setText(numberFormat.format(cantidad*precio));

                }catch (NumberFormatException ex){



                }



            }
        };

        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);



        builder.setNegativeButton("CANCELAR",null);
        builder.setPositiveButton("AGREGAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty() ){
                    Producto p = new Producto(txtNombre.getText().toString(),
                            Float.parseFloat(txtPrecio.getText().toString()),
                            Integer.parseInt(txtCantidad.getText().toString()));
                    productosList.add(p);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "FALTAN DATOS", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return builder.create();


    }

    private void crearTodos() {

        for (int i = 0; i < 100 ; i++) {
            productosList.add(new Producto("Producto "+i,5,i));
        }
        
        
    }

}