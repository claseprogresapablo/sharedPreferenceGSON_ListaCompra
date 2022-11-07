package com.example.a06ejer_lista_compra_recyclerview.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a06ejer_lista_compra_recyclerview.R;
import com.example.a06ejer_lista_compra_recyclerview.modelos.Producto;

import java.text.NumberFormat;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoVH> {

    private List<Producto> objects; //objetos
    private int resource; //fila(xml)
    private Context context;



    public ProductoAdapter(List<Producto> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
    }

    /**
     * ALGO NO ME IMPORTA QUE. LLAMARA A ESTE METODO PARA CREAR UNA NUEVA FILA
     * @param parent
     * @param viewType
     * @return UN OBJETO ViewHolder
     */
    @NonNull
    @Override
    public ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ESTE METODO SE LLAMA CADA VEZ QUE HAY QUE CREAR UNA NUEVA FILA
        //Dar valores
        View productoView = LayoutInflater.from(context).inflate(resource, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        productoView.setLayoutParams(lp);
        return new ProductoVH(productoView);
    }

    /**
     * A partir de un ViewHolder -> Asignar Valores a los elemetos
     * @param holder -> Fila a configurar
     * @param position -> Elemto de la vista a monstrar
     */
    @Override
    public void onBindViewHolder(@NonNull ProductoVH holder, int position) {
        Producto producto = objects.get(position); //obtener objto que tengo que monstrar en este momento
        holder.lblProducto.setText(producto.getNombre());
        holder.lblCantidad.setText(""+ producto.getCantidad());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarDelete("Estas Seguro que quioeres eliminar?",producto).show();


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                editarProducto(producto.getNombre(),producto).show();

            }
        });

    }

    private AlertDialog editarProducto(String mensaje, Producto p) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mensaje);
        builder.setCancelable(false);


        View contenido = LayoutInflater.from(context).inflate(R.layout.edit_producto_alert_dialog, null);
        builder.setView(contenido);
        TextView txtCantidad = contenido.findViewById(R.id.txtCantidadEditProducto);
        TextView txtPrecio = contenido.findViewById(R.id.txtPrecioEditProducto);
        TextView txtImporteTotal = contenido.findViewById(R.id.txtImporteTotalEditProducto);


        txtImporteTotal.setText(String.valueOf(p.getImporteTotal()));
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        txtPrecio.setText(String.valueOf(p.getPrecio()));

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
                    txtImporteTotal.setText(numberFormat.format(cantidad*precio));

                }catch (NumberFormatException ex){



                }



            }
        };

        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);



        builder.setNegativeButton("CANCEL",null);
        builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                p.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                p.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                p.setImporteTotal(Integer.parseInt(txtCantidad.getText().toString()) * Float.parseFloat(txtPrecio.getText().toString()));
                notifyDataSetChanged();
            }
        });

        return builder.create();


    }

    private AlertDialog confirmarDelete(String mensaje,Producto p) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mensaje);
        builder.setCancelable(false);

        builder.setNegativeButton("NO",null);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                objects.remove(p);
                notifyDataSetChanged();
            }
        });

        return builder.create();
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    /**
     * OBJETO QUE SI INSTANCIA CADA QVEZ QUE TENGA QUE MONSTRAR UN PRODUCTO EN EL RECYCLER
     */
    public class ProductoVH extends RecyclerView.ViewHolder {

        /*La clase VH teiene un constructor que recive una vista que es el view model.xml
        y por lo tanto hay que mapear las variables de los txtAview btn etc que haya en el viewmodel.xml*/

        TextView lblProducto, lblCantidad;
        ImageButton btnDelete;

        public ProductoVH(@NonNull View itemView) {
            super(itemView);
            //como este constructoir va a inicializar los elementos hay que hacer aqui un finViewById

            lblCantidad = itemView.findViewById(R.id.txtCantidadModelView);
            lblProducto = itemView.findViewById(R.id.txtProductoModelView);
            btnDelete = itemView.findViewById(R.id.btnDeleteModelView);


        }
    }
}
