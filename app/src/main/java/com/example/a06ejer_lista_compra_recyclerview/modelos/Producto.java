package com.example.a06ejer_lista_compra_recyclerview.modelos;

import java.io.Serializable;

public class Producto implements Serializable {

    private String nombre;
    private float precio;
    private int cantidad;
    private float importeTotal;

    public Producto(String nombre, float precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        importeTotal = precio*cantidad;
    }

    public Producto() {
    }

    private void actualizaTotal(){
        this.importeTotal = this.precio*this.cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
        actualizaTotal();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(float importeTotal) {
        this.importeTotal = importeTotal;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", importeTotal=" + importeTotal +
                '}';
    }
}
