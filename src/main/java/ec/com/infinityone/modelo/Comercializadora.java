/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo; 

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Paul
 */

public class Comercializadora implements Serializable {
    
    private String nombre;
    
    private boolean activo;
    
    private String codigoarch;
    
    private String codigostc;
   
    private String clavestc;
    
    private String ruc;
   
    private String nombrecorto;
   
    private String direccion;
    
    private String identificacionrepresentantelega;
   
    private String nombrerepresentantelegal;
   
    private String telefono1;
    
    private String telefono2;
    
    private String correo1;
    
    private String correo2;
    
    private String tipoplazocredito;
   
    private String cuentadebito;
    
    private String tipocuentadebito;
        
    private String establecimientofac;
    
    private String puntoventafac;
    
    private String usuarioactual;
    
    private String establecimientondb;
    
    private String puntoventandb;
    
    private String establecimientoncr;
    
    private String puntoventancr;
    
    private String prefijonpe;
    
    private String clavewsepp;
    
    private Boolean esagenteretencion;
    
    private String obligadocontabilidad;
    
    private String leyendaagenteretencion;
    
    private Character ambientesri;
    
    private Character tipoemision;
    
    private String codigo;
    
    private String escontribuyenteespacial;
    
    private Short diasplazocredito;
    
    private BigDecimal tasainteres;
    
    private Date fechavencimientocontr;
    
    private Date fehainiciocontrato;
  
    private Abastecedora codigoabastecedora;
    
    private Banco codigobancodebito;
    
    private List<Numeracion> numeracionList;
   
    private List<Notapedido> notapedidoList;

    public Comercializadora() {
    }

    public Comercializadora(String codigo) {
        this.codigo = codigo;
    }

    public Comercializadora(String codigo, String nombre, boolean activo, String codigoarch, String codigostc, String ruc, String usuarioactual, String prefijonpe, String clavewsepp) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.activo = activo;
        this.codigoarch = codigoarch;
        this.codigostc = codigostc;
        this.ruc = ruc;
        this.usuarioactual = usuarioactual;
        this.prefijonpe = prefijonpe;
        this.clavewsepp = clavewsepp;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getEscontribuyenteespacial() {
        return escontribuyenteespacial;
    }

    public void setEscontribuyenteespacial(String escontribuyenteespacial) {
        this.escontribuyenteespacial = escontribuyenteespacial;
    }


    public Short getDiasplazocredito() {
        return diasplazocredito;
    }

    public void setDiasplazocredito(Short diasplazocredito) {
        this.diasplazocredito = diasplazocredito;
    }


    public BigDecimal getTasainteres() {
        return tasainteres;
    }

    public void setTasainteres(BigDecimal tasainteres) {
        this.tasainteres = tasainteres;
    }

    public Date getFechavencimientocontr() {
        return fechavencimientocontr;
    }

    public void setFechavencimientocontr(Date fechavencimientocontr) {
        this.fechavencimientocontr = fechavencimientocontr;
    }

    public Date getFehainiciocontrato() {
        return fehainiciocontrato;
    }

    public void setFehainiciocontrato(Date fehainiciocontrato) {
        this.fehainiciocontrato = fehainiciocontrato;
    }

/*
    public Object getLogo() {
        return logo;
    }

    public void setLogo(Object logo) {
        this.logo = logo;
    }

*/
    public Abastecedora getCodigoabastecedora() {
        return codigoabastecedora;
    }

    public void setCodigoabastecedora(Abastecedora codigoabastecedora) {
        this.codigoabastecedora = codigoabastecedora;
    }

    public Banco getCodigobancodebito() {
        return codigobancodebito;
    }

    public void setCodigobancodebito(Banco codigobancodebito) {
        this.codigobancodebito = codigobancodebito;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comercializadora)) {
            return false;
        }
        Comercializadora other = (Comercializadora) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCodigoarch() {
        return codigoarch;
    }

    public void setCodigoarch(String codigoarch) {
        this.codigoarch = codigoarch;
    }

    public String getCodigostc() {
        return codigostc;
    }

    public void setCodigostc(String codigostc) {
        this.codigostc = codigostc;
    }

    public String getClavestc() {
        return clavestc;
    }

    public void setClavestc(String clavestc) {
        this.clavestc = clavestc;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombrecorto() {
        return nombrecorto;
    }

    public void setNombrecorto(String nombrecorto) {
        this.nombrecorto = nombrecorto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getIdentificacionrepresentantelega() {
        return identificacionrepresentantelega;
    }

    public void setIdentificacionrepresentantelega(String identificacionrepresentantelega) {
        this.identificacionrepresentantelega = identificacionrepresentantelega;
    }

    public String getNombrerepresentantelegal() {
        return nombrerepresentantelegal;
    }

    public void setNombrerepresentantelegal(String nombrerepresentantelegal) {
        this.nombrerepresentantelegal = nombrerepresentantelegal;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getCorreo1() {
        return correo1;
    }

    public void setCorreo1(String correo1) {
        this.correo1 = correo1;
    }

    public String getCorreo2() {
        return correo2;
    }

    public void setCorreo2(String correo2) {
        this.correo2 = correo2;
    }

    public String getTipoplazocredito() {
        return tipoplazocredito;
    }

    public void setTipoplazocredito(String tipoplazocredito) {
        this.tipoplazocredito = tipoplazocredito;
    }

    public String getCuentadebito() {
        return cuentadebito;
    }

    public void setCuentadebito(String cuentadebito) {
        this.cuentadebito = cuentadebito;
    }

    public String getTipocuentadebito() {
        return tipocuentadebito;
    }

    public void setTipocuentadebito(String tipocuentadebito) {
        this.tipocuentadebito = tipocuentadebito;
    }

    public String getEstablecimientofac() {
        return establecimientofac;
    }

    public void setEstablecimientofac(String establecimientofac) {
        this.establecimientofac = establecimientofac;
    }

    public String getPuntoventafac() {
        return puntoventafac;
    }

    public void setPuntoventafac(String puntoventafac) {
        this.puntoventafac = puntoventafac;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public String getEstablecimientondb() {
        return establecimientondb;
    }

    public void setEstablecimientondb(String establecimientondb) {
        this.establecimientondb = establecimientondb;
    }

    public String getPuntoventandb() {
        return puntoventandb;
    }

    public void setPuntoventandb(String puntoventandb) {
        this.puntoventandb = puntoventandb;
    }

    public String getEstablecimientoncr() {
        return establecimientoncr;
    }

    public void setEstablecimientoncr(String establecimientoncr) {
        this.establecimientoncr = establecimientoncr;
    }

    public String getPuntoventancr() {
        return puntoventancr;
    }

    public void setPuntoventancr(String puntoventancr) {
        this.puntoventancr = puntoventancr;
    }

    public String getPrefijonpe() {
        return prefijonpe;
    }

    public void setPrefijonpe(String prefijonpe) {
        this.prefijonpe = prefijonpe;
    }

    public String getClavewsepp() {
        return clavewsepp;
    }

    public void setClavewsepp(String clavewsepp) {
        this.clavewsepp = clavewsepp;
    }

    public Boolean getEsagenteretencion() {
        return esagenteretencion;
    }

    public void setEsagenteretencion(Boolean esagenteretencion) {
        this.esagenteretencion = esagenteretencion;
    }

    public String getObligadocontabilidad() {
        return obligadocontabilidad;
    }

    public void setObligadocontabilidad(String obligadocontabilidad) {
        this.obligadocontabilidad = obligadocontabilidad;
    }

    public String getLeyendaagenteretencion() {
        return leyendaagenteretencion;
    }

    public void setLeyendaagenteretencion(String leyendaagenteretencion) {
        this.leyendaagenteretencion = leyendaagenteretencion;
    }

    public Character getAmbientesri() {
        return ambientesri;
    }

    public void setAmbientesri(Character ambientesri) {
        this.ambientesri = ambientesri;
    }

    public Character getTipoemision() {
        return tipoemision;
    }

    public void setTipoemision(Character tipoemision) {
        this.tipoemision = tipoemision;
    }
    
}
