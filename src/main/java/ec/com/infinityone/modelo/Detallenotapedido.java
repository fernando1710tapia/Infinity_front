/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author HP
 */

public class Detallenotapedido implements Serializable {
    
    protected DetallenotapedidoPK detallenotapedidoPK;
    
    private BigDecimal volumennaturalrequerido;
    
    private BigDecimal volumennaturalautorizado;

    private String usuarioactual;
     
    //private String medida;

    //private String producto;
    
    private Medida medida;

    private Producto producto;
    
    public Detallenotapedido() {
    }

    public Detallenotapedido(DetallenotapedidoPK detallenotapedidoPK) {
        this.detallenotapedidoPK = detallenotapedidoPK;
    }

    public Detallenotapedido(DetallenotapedidoPK detallenotapedidoPK, BigDecimal volumennaturalrequerido, BigDecimal volumennaturalautorizado, String usuarioactual) {
        this.detallenotapedidoPK = detallenotapedidoPK;
        this.volumennaturalrequerido = volumennaturalrequerido;
        this.volumennaturalautorizado = volumennaturalautorizado;
        this.usuarioactual = usuarioactual;
    }

    public Detallenotapedido(String codigoabastecedora, String codigocomercializadora, String numero, String codigoproducto, String codigomedida) {
        this.detallenotapedidoPK = new DetallenotapedidoPK(codigoabastecedora, codigocomercializadora, numero, codigoproducto, codigomedida);
    }

    public DetallenotapedidoPK getDetallenotapedidoPK() {
        return detallenotapedidoPK;
    }

    public void setDetallenotapedidoPK(DetallenotapedidoPK detallenotapedidoPK) {
        this.detallenotapedidoPK = detallenotapedidoPK;
    }

    public BigDecimal getVolumennaturalrequerido() {
        return volumennaturalrequerido;
    }

    public void setVolumennaturalrequerido(BigDecimal volumennaturalrequerido) {
        this.volumennaturalrequerido = volumennaturalrequerido;
    }

    public BigDecimal getVolumennaturalautorizado() {
        return volumennaturalautorizado;
    }

    public void setVolumennaturalautorizado(BigDecimal volumennaturalautorizado) {
        this.volumennaturalautorizado = volumennaturalautorizado;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

//    public String getMedida() {
//        return medida;
//    }
//
//    public void setMedida(String medida) {
//        this.medida = medida;
//    }
//
//    public String getProducto() {
//        return producto;
//    }
//
//    public void setProducto(String producto) {
//        this.producto = producto;
//    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida codMedida) {
        this.medida = codMedida;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto codProducto) {
        this.producto = codProducto;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallenotapedidoPK != null ? detallenotapedidoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallenotapedido)) {
            return false;
        }
        Detallenotapedido other = (Detallenotapedido) object;
        if ((this.detallenotapedidoPK == null && other.detallenotapedidoPK != null) || (this.detallenotapedidoPK != null && !this.detallenotapedidoPK.equals(other.detallenotapedidoPK))) {
            return false;
        }
        return true;
    }

}
