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
    
    private BigDecimal compartimento1;
    private BigDecimal compartimento2;
    private BigDecimal compartimento3;
    private BigDecimal compartimento4;
    private BigDecimal compartimento5;
    private BigDecimal compartimento6;
    private BigDecimal compartimento7;
    private BigDecimal compartimento8;
    private BigDecimal compartimento9;
    private BigDecimal compartimento10;
    private Integer selloinicial;
    private Integer sellofinal;
    
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

    public BigDecimal getCompartimento1() {
        return compartimento1;
    }

    public void setCompartimento1(BigDecimal compartimento1) {
        this.compartimento1 = compartimento1;
    }

    public BigDecimal getCompartimento2() {
        return compartimento2;
    }

    public void setCompartimento2(BigDecimal compartimento2) {
        this.compartimento2 = compartimento2;
    }

    public BigDecimal getCompartimento3() {
        return compartimento3;
    }

    public void setCompartimento3(BigDecimal compartimento3) {
        this.compartimento3 = compartimento3;
    }

    public BigDecimal getCompartimento4() {
        return compartimento4;
    }

    public void setCompartimento4(BigDecimal compartimento4) {
        this.compartimento4 = compartimento4;
    }

    public BigDecimal getCompartimento5() {
        return compartimento5;
    }

    public void setCompartimento5(BigDecimal compartimento5) {
        this.compartimento5 = compartimento5;
    }

    public BigDecimal getCompartimento6() {
        return compartimento6;
    }

    public void setCompartimento6(BigDecimal compartimento6) {
        this.compartimento6 = compartimento6;
    }

    public BigDecimal getCompartimento7() {
        return compartimento7;
    }

    public void setCompartimento7(BigDecimal compartimento7) {
        this.compartimento7 = compartimento7;
    }

    public BigDecimal getCompartimento8() {
        return compartimento8;
    }

    public void setCompartimento8(BigDecimal compartimento8) {
        this.compartimento8 = compartimento8;
    }

    public BigDecimal getCompartimento9() {
        return compartimento9;
    }

    public void setCompartimento9(BigDecimal compartimento9) {
        this.compartimento9 = compartimento9;
    }

    public BigDecimal getCompartimento10() {
        return compartimento10;
    }

    public void setCompartimento10(BigDecimal compartimento10) {
        this.compartimento10 = compartimento10;
    }

    public Integer getSelloinicial() {
        return selloinicial;
    }

    public void setSelloinicial(Integer selloinicial) {
        this.selloinicial = selloinicial;
    }

    public Integer getSellofinal() {
        return sellofinal;
    }

    public void setSellofinal(Integer sellofinal) {
        this.sellofinal = sellofinal;
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
