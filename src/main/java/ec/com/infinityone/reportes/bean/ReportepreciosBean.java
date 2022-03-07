/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.reportes.bean;

import ec.com.infinityone.modeloWeb.Listaprecio;
import ec.com.infinityone.preciosyfacturacion.servicios.ListaprecioServicio;
import ec.com.infinityone.reusable.ReusableBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author HP
 */
@Named
@ViewScoped
public class ReportepreciosBean extends ReusableBean implements Serializable{
    
    
    /*
    Variable que trae los mètodos de lista Precios
    */
    @Inject
    private ListaprecioServicio listaPrecioServicio;
    /*
    Varaibale Lista Precio
    */
    private List<Listaprecio> listaListaprecios;
    /*
    Varaibale Lista Precio
    */
    private Listaprecio listaPrecio;
    /*
    Varaibale para almacenar el codigo de la listaPrecio
    */
    private long codigoListaprecio;
    /**
     * Constructor por defecto
     */
    public ReportepreciosBean() {
    }

    @PostConstruct
    /**
     * Funcion para inicializar variables
     */
    public void init() {
        listaPrecio = new Listaprecio();
        codigoListaprecio = 0;
        obtenerListaPrecio();
    }
    
    public void obtenerListaPrecio() {
        listaListaprecios = new ArrayList<>();
        listaListaprecios = this.listaPrecioServicio.obtenerListaprecio();
    }
    
    public void selecionarListaPrecio(){
        if(listaPrecio != null){
            codigoListaprecio = listaPrecio.getListaprecioPK().getCodigo();
        }
            
    }

    public List<Listaprecio> getListaListaprecios() {
        return listaListaprecios;
    }

    public void setListaListaprecios(List<Listaprecio> listaListaprecios) {
        this.listaListaprecios = listaListaprecios;
    }

    public Listaprecio getListaPrecio() {
        return listaPrecio;
    }

    public void setListaPrecio(Listaprecio listaPrecio) {
        this.listaPrecio = listaPrecio;
    }
    
    
}
