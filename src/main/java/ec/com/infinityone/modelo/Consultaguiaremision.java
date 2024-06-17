/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

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

    private String numerosri;

    private String cedulaconductor;

    private String nombreconductor;

    private String observacion;

    private String codigocliente;

    private int compartimento1;

    private int compartimento2;

    private int compartimento3;

    private int compartimento4;

    private int compartimento5;

    private int compartimento6;

    private int selloinicial;

    private int sellofinal;
    
    private int cantidadsellos;

    private String numerofactura;

    private String horaautorizacion;

    private Date fechafactura;

    private String numeroautorizacion;

    private String fechaautorizacion;

    private String direstablecimiento;

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

    public String getNumerosri() {
        return numerosri;
    }

    public void setNumerosri(String numerosri) {
        this.numerosri = numerosri;
    }

    public String getCedulaconductor() {
        return cedulaconductor;
    }

    public void setCedulaconductor(String cedulaconductor) {
        this.cedulaconductor = cedulaconductor;
    }

    public String getNombreconductor() {
        return nombreconductor;
    }

    public void setNombreconductor(String nombreconductor) {
        this.nombreconductor = nombreconductor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCodigocliente() {
        return codigocliente;
    }

    public void setCodigocliente(String codigocliente) {
        this.codigocliente = codigocliente;
    }

    public int getCompartimento1() {
        return compartimento1;
    }

    public void setCompartimento1(int compartimento1) {
        this.compartimento1 = compartimento1;
    }

    public int getCompartimento2() {
        return compartimento2;
    }

    public void setCompartimento2(int compartimento2) {
        this.compartimento2 = compartimento2;
    }

    public int getCompartimento3() {
        return compartimento3;
    }

    public void setCompartimento3(int compartimento3) {
        this.compartimento3 = compartimento3;
    }

    public int getCompartimento4() {
        return compartimento4;
    }

    public void setCompartimento4(int compartimento4) {
        this.compartimento4 = compartimento4;
    }

    public int getCompartimento5() {
        return compartimento5;
    }

    public void setCompartimento5(int compartimento5) {
        this.compartimento5 = compartimento5;
    }

    public int getCompartimento6() {
        return compartimento6;
    }

    public void setCompartimento6(int compartimento6) {
        this.compartimento6 = compartimento6;
    }

    public int getSelloinicial() {
        return selloinicial;
    }

    public void setSelloinicial(int selloinicial) {
        this.selloinicial = selloinicial;
    }

    public int getSellofinal() {
        return sellofinal;
    }
    

    public void setSellofinal(int sellofinal) {
        this.sellofinal = sellofinal;
    }
    
    public int getCantidadsellos() {
        return sellofinal - selloinicial;
    }
    public void setCantidadsellos(int cantidadSellos) {
        this.cantidadsellos = sellofinal - selloinicial;
    }

    public String getNumerofactura() {
        return numerofactura;
    }

    public void setNumerofactura(String numerofactura) {
        this.numerofactura = numerofactura;
    }

    public String getHoraautorizacion() {
        return horaautorizacion;
    }

    public void setHoraautorizacion(String horaautorizacion) {
        this.horaautorizacion = horaautorizacion;
    }

    public Date getFechafactura() {
        return fechafactura;
    }

    public void setFechafactura(Date fechafactura) {
        this.fechafactura = fechafactura;
    }

    public String getNumeroautorizacion() {
        return numeroautorizacion;
    }

    public void setNumeroautorizacion(String numeroautorizacion) {
        this.numeroautorizacion = numeroautorizacion;
    }

    public String getFechaautorizacion() {
        return fechaautorizacion;
    }

    public void setFechaautorizacion(String fechaautorizacion) {
        this.fechaautorizacion = fechaautorizacion;
    }

    public String getDirestablecimiento() {
        return direstablecimiento;
    }

    public void setDirestablecimiento(String direstablecimiento) {
        this.direstablecimiento = direstablecimiento;
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
