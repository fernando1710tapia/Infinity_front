/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import ec.com.infinityone.bean.TerminalBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
public class Cliente implements Serializable {

    private String codigo;

    private String nombre;

    private boolean estado;

    private String codigoarch;

    private String codigostc;

    private String clavestc;

    private String codigocomercializadora;

    private String ruc;
    
    private String codigotipocliente;
    
    private String codigodireccioninen;       

    private String direccion;

    private String identificacionrepresentantelega;

    private String nombrearrendatario;

    private String nombrerepresentantelegal;

    private String escontribuyenteespacial;

    private String telefono1;

    private String telefono2;

    private String correo1;

    private String correo2;

    private String tipoplazocredito;

    private Short diasplazocredito;
    
    private String codigobancodebito;

    private BigDecimal tasainteres;

    private String cuentadebito;

    private String tipocuentadebito;

    private Boolean controlagarantia;

    private long codigolistaprecio;

    private String codigolistaflete;

    private Boolean aplicasubsidio2;

    private String centrocosto;

    private Date fehainscripcion;

    private Date fehainiciooperacion;

    private Date feharegistroarch;

    private Date fehavencimientocontrato;

    private String codigosupervisorzonal;

    private String usuarioactual;   

    private String nombrecomercial;    

    private Formapago codigoformapago;

    private Terminal codigoterminaldefecto;

    private int controldespacho; 

    public Cliente() {
    }

    public Cliente(String codigo) {
        this.codigo = codigo;
    }

    public Cliente(String codigo, String nombre, boolean estado, String codigoarch, String codigostc, String codigocomercializadora, String ruc, String direccion, long codigolistaprecio, String usuarioactual) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
        this.codigoarch = codigoarch;
        this.codigostc = codigostc;
        this.codigocomercializadora = codigocomercializadora;
        this.ruc = ruc;
        this.direccion = direccion;
        this.codigolistaprecio = codigolistaprecio;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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

    public String getCodigocomercializadora() {
        return codigocomercializadora;
    }

    public void setCodigocomercializadora(String codigocomercializadora) {
        this.codigocomercializadora = codigocomercializadora;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
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

    public String getNombrearrendatario() {
        return nombrearrendatario;
    }

    public void setNombrearrendatario(String nombrearrendatario) {
        this.nombrearrendatario = nombrearrendatario;
    }

    public String getNombrerepresentantelegal() {
        return nombrerepresentantelegal;
    }

    public void setNombrerepresentantelegal(String nombrerepresentantelegal) {
        this.nombrerepresentantelegal = nombrerepresentantelegal;
    }

    public String getEscontribuyenteespacial() {
        return escontribuyenteespacial;
    }

    public void setEscontribuyenteespacial(String escontribuyenteespacial) {
        this.escontribuyenteespacial = escontribuyenteespacial;
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

    public Boolean getControlagarantia() {
        return controlagarantia;
    }

    public void setControlagarantia(Boolean controlagarantia) {
        this.controlagarantia = controlagarantia;
    }

    public long getCodigolistaprecio() {
        return codigolistaprecio;
    }

    public void setCodigolistaprecio(long codigolistaprecio) {
        this.codigolistaprecio = codigolistaprecio;
    }

    public String getCodigolistaflete() {
        return codigolistaflete;
    }

    public void setCodigolistaflete(String codigolistaflete) {
        this.codigolistaflete = codigolistaflete;
    }

    public Boolean getAplicasubsidio2() {
        return aplicasubsidio2;
    }

    public void setAplicasubsidio2(Boolean aplicasubsidio2) {
        this.aplicasubsidio2 = aplicasubsidio2;
    }

    public String getCentrocosto() {
        return centrocosto;
    }

    public void setCentrocosto(String centrocosto) {
        this.centrocosto = centrocosto;
    }

    public Date getFehainscripcion() {
        return fehainscripcion;
    }

    public void setFehainscripcion(Date fehainscripcion) {
        this.fehainscripcion = fehainscripcion;
    }

    public Date getFehainiciooperacion() {
        return fehainiciooperacion;
    }

    public void setFehainiciooperacion(Date fehainiciooperacion) {
        this.fehainiciooperacion = fehainiciooperacion;
    }

    public Date getFeharegistroarch() {
        return feharegistroarch;
    }

    public void setFeharegistroarch(Date feharegistroarch) {
        this.feharegistroarch = feharegistroarch;
    }

    public Date getFehavencimientocontrato() {
        return fehavencimientocontrato;
    }

    public void setFehavencimientocontrato(Date fehavencimientocontrato) {
        this.fehavencimientocontrato = fehavencimientocontrato;
    }

    public String getCodigosupervisorzonal() {
        return codigosupervisorzonal;
    }

    public void setCodigosupervisorzonal(String codigosupervisorzonal) {
        this.codigosupervisorzonal = codigosupervisorzonal;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }   

    public Formapago getCodigoformapago() {
        return codigoformapago;
    }

    public void setCodigoformapago(Formapago codigoformapago) {
        this.codigoformapago = codigoformapago;
    }

    public Terminal getCodigoterminaldefecto() {
        return codigoterminaldefecto;
    }

    public void setCodigoterminaldefecto(Terminal codigoterminaldefecto) {
        this.codigoterminaldefecto = codigoterminaldefecto;
    }
    
    public String getCodigotipocliente() {
        return codigotipocliente;
    }

    public void setCodigotipocliente(String codigotipocliente) {
        this.codigotipocliente = codigotipocliente;
    }

    public String getCodigodireccioninen() {
        return codigodireccioninen;
    }

    public void setCodigodireccioninen(String codigodireccioninen) {
        this.codigodireccioninen = codigodireccioninen;
    }

    public String getCodigobancodebito() {
        return codigobancodebito;
    }

    public void setCodigobancodebito(String codigobancodebito) {
        this.codigobancodebito = codigobancodebito;
    }

    public String getNombrecomercial() {
        return nombrecomercial;
    }

    public void setNombrecomercial(String nombrecomercial) {
        this.nombrecomercial = nombrecomercial;
    }    

    public int getControldespacho() {
        return controldespacho;
    }

    public void setControldespacho(int controldespacho) {
        this.controldespacho = controldespacho;
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
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    public boolean isEstado(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
