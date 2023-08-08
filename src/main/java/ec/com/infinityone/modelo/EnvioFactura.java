/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.util.List;

/**
 *
 * @author HP
 */
public class EnvioFactura {
    
    private Factura factura;
    private List<Detallefactura> detalle;
    private String ensri;
    
    public EnvioFactura() {
    }

    /**
     * @return the factura
     */
    public Factura getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    /**
     * @return the detalle
     */
    public List<Detallefactura> getDetalle() {
        return detalle;
    }

    /**
     * @param detalle the detalle to set
     */
    public void setDetalle(List<Detallefactura>  detalle) {
        this.detalle = detalle;
    }

    public String getEnsri() {
        return ensri;
    }

    public void setEnsri(String ensri) {
        this.ensri = ensri;
    }
}
