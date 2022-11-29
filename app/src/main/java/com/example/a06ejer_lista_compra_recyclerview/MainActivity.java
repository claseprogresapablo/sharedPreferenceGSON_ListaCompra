package com.example.a06ejer_lista_compra_recyclerview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.a06ejer_lista_compra_recyclerview.adapters.ProductoAdapter;
import com.example.a06ejer_lista_compra_recyclerview.modelos.Producto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.a06ejer_lista_compra_recyclerview.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static ArrayList<Producto> productosList;
    private ProductoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static SharedPreferences sharedPreferences;
    private static Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        productosList = new ArrayList<>();
        //crearTodos();

        int columnas;
        //HORIZONTAL -> 2
        //VERTICAL -> 1
        //DESDE LAS CONFIGURACIONES DE LA ACTIVIDAD -> orientation // PORTRAIT(V) / LANDSCAPE(H)
         columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2; //OPERADOR TERNARIO
        //linkear main con adapter(no se dice así pero me aclaro)

        adapter = new ProductoAdapter(productosList,R.layout.productos_view_model,MainActivity.this);
        binding.contentMain.contenedor.setAdapter(adapter);
        layoutManager = new GridLayoutManager(MainActivity.this, columnas);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);


        sharedPreferences = getSharedPreferences(Constantes.DATOS,MODE_PRIVATE);
        gson = new Gson();
        cargarDatos();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                crateProducto(getString(R.string.alert_title_crear)).show();

            }
        });


    }


    private void cargarDatos(){


        if (sharedPreferences.contains(Constantes.COMPRA) && !sharedPreferences.getString(Constantes.COMPRA, "").isEmpty()){

            String compraSTR = sharedPreferences.getString(Constantes.COMPRA, "");
            Type tipo = new TypeToken< ArrayList<Producto> >(){}.getType();
            List<Producto> pro = new Gson().fromJson(compraSTR, tipo); // esto crea un arrayList a partir de los datos del json de las sp
            productosList.clear();
            productosList.addAll(pro);
            adapter.notifyItemRangeInserted(0,productosList.size());
        }


    }

    private void guardarEnSP() {
        String contactosSTR = gson.toJson(productosList);
        Log.d("JSON", contactosSTR);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constantes.COMPRA, contactosSTR);
        editor.apply();
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



        builder.setNegativeButton(getString(R.string.btn_alert_cancel),null);
        builder.setPositiveButton(getString(R.string.btn_alert_crear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty() ){
                    Producto p = new Producto(txtNombre.getText().toString(),
                            Float.parseFloat(txtPrecio.getText().toString()),
                            Integer.parseInt(txtCantidad.getText().toString()));
                    productosList.add(0,p);//asi añade al principio
                    adapter.notifyItemInserted(0); //muy importante esto npara que lo muestre
                    //adapter.notifyDataSetChanged();
                    guardarEnSP();
                } else {
                    Toast.makeText(MainActivity.this, "FALTAN DATOS", Toast.LENGTH_SHORT).show();
                }


            }
        });


        return builder.create();


    }










    /**
     * Se dispara ANTES de que se elimine la actividad
     * @param outState -> guardo los datos
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("LISTA",productosList);
    }

    /**
     * Se dispara despues de crear la actividad nueva
     * @param savedInstanceState -> recupero los datos
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Producto> tem = (ArrayList<Producto>) savedInstanceState.getSerializable("LISTA");
        productosList.addAll(tem);
        adapter.notifyItemRangeInserted(0,productosList.size());
    }

    private void crearTodos() {

        for (int i = 0; i < 100 ; i++) {
            productosList.add(new Producto("Producto "+i,5,i));
        }
        
        
    }

}