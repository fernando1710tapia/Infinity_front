/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author david
 */
public class UsoSello implements Serializable{

    private static final long serialVersionUID = 1L;
    
    protected UsoSelloPK usoselloPK;

    private Date fecha;

    private String nombreconductor;

    private String nombrecliente;

    private String np6;

    private String np5;

    private String np4;

    private String np3;

    private String np2;
    
    private String np1;

    private BigInteger sello16;

    private BigInteger sello15;

    private BigInteger sello14;

    private BigInteger sello13;

    private BigInteger sello12;

    private BigInteger sello11;

    private BigInteger sello10;

    private BigInteger sello9;

    private BigInteger sello8;

    private BigInteger sello7;

    private BigInteger sello6;

    private BigInteger sello5;

    private BigInteger sello4;

    private BigInteger sello3;

    private BigInteger sello1;

    private BigInteger sello2;

    private String usuarioactual;
    
    private Timestamp fechahoraregistro;

    public UsoSello() {
    }

    public UsoSello(UsoSelloPK usoselloPK, Date fecha, String nombreconductor, String nombrecliente, String np6, String np5, String np4, String np3, String np2, String np1,BigInteger sello16, BigInteger sello15, BigInteger sello14, BigInteger sello13, BigInteger sello12, BigInteger sello11, BigInteger sello10, BigInteger sello9, BigInteger sello8, BigInteger sello7, BigInteger sello6, BigInteger sello5, BigInteger sello4, BigInteger sello3, BigInteger sello1, BigInteger sello2, String usuarioactual) {
        this.usoselloPK = usoselloPK;
        this.fecha = fecha;
        this.nombreconductor = nombreconductor;
        this.nombrecliente = nombrecliente;
        this.np6 = np6;
        this.np5 = np5;
        this.np4 = np4;
        this.np3 = np3;
        this.np2 = np2;
        this.np1 = np1;
        this.sello16 = sello16;
        this.sello15 = sello15;
        this.sello14 = sello14;
        this.sello13 = sello13;
        this.sello12 = sello12;
        this.sello11 = sello11;
        this.sello10 = sello10;
        this.sello9 = sello9;
        this.sello8 = sello8;
        this.sello7 = sello7;
        this.sello6 = sello6;
        this.sello5 = sello5;
        this.sello4 = sello4;
        this.sello3 = sello3;
        this.sello1 = sello1;
        this.sello2 = sello2;
        this.usuarioactual = usuarioactual;
    }

    public UsoSelloPK getUsoselloPK() {
        return usoselloPK;
    }

    public void setUsoselloPK(UsoSelloPK usoselloPK) {
        this.usoselloPK = usoselloPK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombreconductor() {
        return nombreconductor;
    }

    public void setNombreconductor(String nombreconductor) {
        this.nombreconductor = nombreconductor;
    }

    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente;
    }

    public String getNp6() {
        return np6;
    }

    public void setNp6(String np6) {
        this.np6 = np6;
    }

    public String getNp5() {
        return np5;
    }

    public void setNp5(String np5) {
        this.np5 = np5;
    }

    public String getNp4() {
        return np4;
    }

    public void setNp4(String np4) {
        this.np4 = np4;
    }

    public String getNp3() {
        return np3;
    }

    public void setNp3(String np3) {
        this.np3 = np3;
    }

    public String getNp2() {
        return np2;
    }

    public void setNp2(String np2) {
        this.np2 = np2;
    }

    public BigInteger getSello16() {
        return sello16;
    }

    public void setSello16(BigInteger sello16) {
        this.sello16 = sello16;
    }

    public BigInteger getSello15() {
        return sello15;
    }

    public void setSello15(BigInteger sello15) {
        this.sello15 = sello15;
    }

    public BigInteger getSello14() {
        return sello14;
    }

    public void setSello14(BigInteger sello14) {
        this.sello14 = sello14;
    }

    public BigInteger getSello13() {
        return sello13;
    }

    public void setSello13(BigInteger sello13) {
        this.sello13 = sello13;
    }

    public BigInteger getSello12() {
        return sello12;
    }

    public void setSello12(BigInteger sello12) {
        this.sello12 = sello12;
    }

    public BigInteger getSello11() {
        return sello11;
    }

    public void setSello11(BigInteger sello11) {
        this.sello11 = sello11;
    }

    public BigInteger getSello10() {
        return sello10;
    }

    public void setSello10(BigInteger sello10) {
        this.sello10 = sello10;
    }

    public BigInteger getSello9() {
        return sello9;
    }

    public void setSello9(BigInteger sello9) {
        this.sello9 = sello9;
    }

    public BigInteger getSello8() {
        return sello8;
    }

    public void setSello8(BigInteger sello8) {
        this.sello8 = sello8;
    }

    public BigInteger getSello7() {
        return sello7;
    }

    public void setSello7(BigInteger sello7) {
        this.sello7 = sello7;
    }

    public BigInteger getSello6() {
        return sello6;
    }

    public void setSello6(BigInteger sello6) {
        this.sello6 = sello6;
    }

    public BigInteger getSello5() {
        return sello5;
    }

    public void setSello5(BigInteger sello5) {
        this.sello5 = sello5;
    }

    public BigInteger getSello4() {
        return sello4;
    }

    public void setSello4(BigInteger sello4) {
        this.sello4 = sello4;
    }

    public BigInteger getSello3() {
        return sello3;
    }

    public void setSello3(BigInteger sello3) {
        this.sello3 = sello3;
    }

    public BigInteger getSello1() {
        return sello1;
    }

    public void setSello1(BigInteger sello1) {
        this.sello1 = sello1;
    }

    public BigInteger getSello2() {
        return sello2;
    }

    public void setSello2(BigInteger sello2) {
        this.sello2 = sello2;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public String getNp1() {
        return np1;
    }

    public void setNp1(String np1) {
        this.np1 = np1;
    }

    public Timestamp getFechahoraregistro() {
        return fechahoraregistro;
    }

    public void setFechahoraregistro(Timestamp fechahoraregistro) {
        this.fechahoraregistro = fechahoraregistro;
    }

    
    
}
