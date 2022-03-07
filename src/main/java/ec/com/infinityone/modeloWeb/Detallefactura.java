/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author HP
 */
public class Detallefactura implements Serializable{

    private BigDecimal volumennaturalrequerido;

    private BigDecimal volumennaturalautorizado;

    private BigDecimal precioproducto;

    private BigDecimal subtotal;

    private String usuarioactual;

    private String ruccomercializadora;

    private String nombreproducto;

    private String codigoimpuesto;


    private String nombreimpuesto;
    
    private String codigoprecio;

    private Boolean seimprime;

    private BigDecimal valordefecto;
    
    protected DetallefacturaPK detallefacturaPK;
    
    private Factura factura;
    
    private String codigomedida;
    
    public Detallefactura() {
    }

    public Detallefactura(DetallefacturaPK detallefacturaPK) {
        this.detallefacturaPK = detallefacturaPK;
    }

    public Detallefactura(DetallefacturaPK detallefacturaPK, BigDecimal volumennaturalrequerido, BigDecimal volumennaturalautorizado, BigDecimal precioproducto, BigDecimal subtotal, String usuarioactual) {
        this.detallefacturaPK = detallefacturaPK;
        this.volumennaturalrequerido = volumennaturalrequerido;
        this.volumennaturalautorizado = volumennaturalautorizado;
        this.precioproducto = precioproducto;
        this.subtotal = subtotal;
        this.usuarioactual = usuarioactual;
    }

    public Detallefactura(String codigoabastecedora, String codigocomercializadora, String numeronotapedido, String numero, String codigoproducto) {
        this.detallefacturaPK = new DetallefacturaPK(codigoabastecedora, codigocomercializadora, numeronotapedido, numero, codigoproducto);
    }

    public DetallefacturaPK getDetallefacturaPK() {
        return detallefacturaPK;
    }

    public void setDetallefacturaPK(DetallefacturaPK detallefacturaPK) {
        this.detallefacturaPK = detallefacturaPK;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallefacturaPK != null ? detallefacturaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallefactura)) {
            return false;
        }
        Detallefactura other = (Detallefactura) object;
        if ((this.detallefacturaPK == null && other.detallefacturaPK != null) || (this.detallefacturaPK != null && !this.detallefacturaPK.equals(other.detallefacturaPK))) {
            return false;
        }
        return true;
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

    public BigDecimal getPrecioproducto() {
        return precioproducto;
    }

    public void setPrecioproducto(BigDecimal precioproducto) {
        this.precioproducto = precioproducto;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public String getRuccomercializadora() {
        return ruccomercializadora;
    }

    public void setRuccomercializadora(String ruccomercializadora) {
        this.ruccomercializadora = ruccomercializadora;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }

    public String getCodigoimpuesto() {
        return codigoimpuesto;
    }

    public void setCodigoimpuesto(String codigoimpuesto) {
        this.codigoimpuesto = codigoimpuesto;
    }

    public String getNombreimpuesto() {
        return nombreimpuesto;
    }

    public void setNombreimpuesto(String nombreimpuesto) {
        this.nombreimpuesto = nombreimpuesto;
    }

    public Boolean getSeimprime() {
        return seimprime;
    }

    public void setSeimprime(Boolean seimprime) {
        this.seimprime = seimprime;
    }

    public BigDecimal getValordefecto() {
        return valordefecto;
    }

    public void setValordefecto(BigDecimal valordefecto) {
        this.valordefecto = valordefecto;
    }
    
    public String getCodigoprecio() {
        return codigoprecio;
    }

    public void setCodigoprecio(String codigoprecio) {
        this.codigoprecio = codigoprecio;
    }

    public String getCodigomedida() {
        return codigomedida;
    }

    public void setCodigomedida(String codigomedida) {
        this.codigomedida = codigomedida;
    }
    
    
}
