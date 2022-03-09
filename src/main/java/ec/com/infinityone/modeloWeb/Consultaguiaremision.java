/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author HP
 */
public class Consultaguiaremision implements Serializable {

    protected ConsultaguiaremisionPK consultaguiaremisionPK;

    private String codigoterminal;

    private String numerooe;
    
    private String codigoareamercadeo;

    private String codigoproducto;

    private String codigomedida;

    private String medida;

    private String producto;

    private BigDecimal volumenentregado;

    private String autotanque;

    private String estado;

    private boolean activo;

    private String usuarioactual;

    public Consultaguiaremision() {
    }

    public Consultaguiaremision(ConsultaguiaremisionPK consultaguiaremisionPK) {
        this.consultaguiaremisionPK = consultaguiaremisionPK;
    }

    public Consultaguiaremision(ConsultaguiaremisionPK consultaguiaremisionPK, String codigoterminal, String numerooe, BigDecimal volumenentregado, String autotanque, boolean activo) {
        this.consultaguiaremisionPK = consultaguiaremisionPK;
        this.codigoterminal = codigoterminal;
        this.numerooe = numerooe;
        this.volumenentregado = volumenentregado;
        this.autotanque = autotanque;
        this.activo = activo;
    }

    public Consultaguiaremision(String codigocomercializadora, String numero, String fecha, Date fecharecepcion) {
        this.consultaguiaremisionPK = new ConsultaguiaremisionPK(codigocomercializadora, numero, fecha, fecharecepcion);
    }

    public ConsultaguiaremisionPK getConsultaguiaremisionPK() {
        return consultaguiaremisionPK;
    }

    public void setConsultaguiaremisionPK(ConsultaguiaremisionPK consultaguiaremisionPK) {
        this.consultaguiaremisionPK = consultaguiaremisionPK;
    }

    public String getCodigoterminal() {
        return codigoterminal;
    }

    public void setCodigoterminal(String codigoterminal) {
        this.codigoterminal = codigoterminal;
    }

    public String getNumerooe() {
        return numerooe;
    }

    public void setNumerooe(String numerooe) {
        this.numerooe = numerooe;
    }

    public String getCodigoareamercadeo() {
        return codigoareamercadeo;
    }

    public void setCodigoareamercadeo(String codigoareamercadeo) {
        this.codigoareamercadeo = codigoareamercadeo;
    }

    public String getCodigoproducto() {
        return codigoproducto;
    }

    public void setCodigoproducto(String codigoproducto) {
        this.codigoproducto = codigoproducto;
    }

    public String getCodigomedida() {
        return codigomedida;
    }

    public void setCodigomedida(String codigomedida) {
        this.codigomedida = codigomedida;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public BigDecimal getVolumenentregado() {
        return volumenentregado;
    }

    public void setVolumenentregado(BigDecimal volumenentregado) {
        this.volumenentregado = volumenentregado;
    }

    public String getAutotanque() {
        return autotanque;
    }

    public void setAutotanque(String autotanque) {
        this.autotanque = autotanque;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (consultaguiaremisionPK != null ? consultaguiaremisionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consultaguiaremision)) {
            return false;
        }
        Consultaguiaremision other = (Consultaguiaremision) object;
        if ((this.consultaguiaremisionPK == null && other.consultaguiaremisionPK != null) || (this.consultaguiaremisionPK != null && !this.consultaguiaremisionPK.equals(other.consultaguiaremisionPK))) {
            return false;
        }
        return true;
    }
    
}
