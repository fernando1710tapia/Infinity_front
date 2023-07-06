/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

/**
 *
 * @author HP
 */
public class EnvioRefactura {
    
    private Factura factura;
    private Detallefactura detalleFactura;   
    private String ensri; 
    private Cliente clienteRefactura;

    public EnvioRefactura() {
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Detallefactura getDetalleFactura() {
        return detalleFactura;
    }

    public void setDetalleFactura(Detallefactura detalleFactura) {
        this.detalleFactura = detalleFactura;
    }

    public String getEnsri() {
        return ensri;
    }

    public void setEnsri(String ensri) {
        this.ensri = ensri;
    }

    public Cliente getClienteRefactura() {
        return clienteRefactura;
    }

    public void setClienteRefactura(Cliente clienteRefactura) {
        this.clienteRefactura = clienteRefactura;
    }
    
}
