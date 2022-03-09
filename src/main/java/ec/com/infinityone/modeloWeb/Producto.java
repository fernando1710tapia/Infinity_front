/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author HP
 */
public class Producto implements Serializable {

    private String codigo;

    private String nombre;

    private String codigoareamercadeo;

    private String codigostc;

    private String codigoarch;

    private String usuarioactual;
    
    private BigDecimal porcentajeivapresuntivo;
    
//    private List<Comercializadoraproducto> comercializadoraproductoList;
//
//    private List<Detallenotapedido> detallenotapedidoList;
// 
//    private List<Detallefactura> detallefacturaList;
    public Producto() {
    }

    public Producto(String codigo) {
        this.codigo = codigo;
    }

    public Producto(String codigo, String nombre, String codigostc, String usuarioactual) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.codigostc = codigostc;
        this.usuarioactual = usuarioactual;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigostc() {
        return codigostc;
    }

    public void setCodigostc(String codigostc) {
        this.codigostc = codigostc;
    }

    public String getCodigoarch() {
        return codigoarch;
    }

    public void setCodigoarch(String codigoarch) {
        this.codigoarch = codigoarch;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public String getCodigoareamercadeo() {
        return codigoareamercadeo;
    }

    public void setCodigoareamercadeo(String codigoareamercadeo) {
        this.codigoareamercadeo = codigoareamercadeo;
    }

    public BigDecimal getPorcentajeivapresuntivo() {
        return porcentajeivapresuntivo;
    }

    public void setPorcentajeivapresuntivo(BigDecimal porcentajeivapresuntivo) {
        this.porcentajeivapresuntivo = porcentajeivapresuntivo;
    }

//    public void setDetallenotapedidoList(List<Detallenotapedido> detallenotapedidoList) {
//        this.detallenotapedidoList = detallenotapedidoList;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Producto[ codigo=" + codigo + " ]";
    }

}
