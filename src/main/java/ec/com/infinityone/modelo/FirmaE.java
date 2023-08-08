/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
public class FirmaE implements Serializable {

    private Long fir_codigo;

    private String emp_codigoempresa;

    private int fir_tipo;

    private String fir_descripcion;

    private String fir_ubicacion;

    private String fir_dirgenerada;

    private String fir_dirfirmada;

    private String fir_dirautorizada;

    private String fir_dirrechazada;

    private String fir_clave;

    private String fir_estado;

    private String fir_fechacaduca;

    private String fir_usuariocrea;

    private String fir_fechacrea;

    private String fir_ipcrea;

    private int fir_timer;

    private String fir_correoalertas;

    public FirmaE() {
    }

    public FirmaE(Long codigo) {
        this.fir_codigo = codigo;
    }

    public Long getFir_codigo() {
        return fir_codigo;
    }

    public void setFir_codigo(Long fir_codigo) {
        this.fir_codigo = fir_codigo;
    }

    public String getEmp_codigoempresa() {
        return emp_codigoempresa;
    }

    public void setEmp_codigoempresa(String emp_codigoempresa) {
        this.emp_codigoempresa = emp_codigoempresa;
    }

    public int getFir_tipo() {
        return fir_tipo;
    }

    public void setFir_tipo(int fir_tipo) {
        this.fir_tipo = fir_tipo;
    }

    public String getFir_descripcion() {
        return fir_descripcion;
    }

    public void setFir_descripcion(String fir_descripcion) {
        this.fir_descripcion = fir_descripcion;
    }

    public String getFir_ubicacion() {
        return fir_ubicacion;
    }

    public void setFir_ubicacion(String fir_ubicacion) {
        this.fir_ubicacion = fir_ubicacion;
    }

    public String getFir_dirgenerada() {
        return fir_dirgenerada;
    }

    public void setFir_dirgenerada(String fir_dirgenerada) {
        this.fir_dirgenerada = fir_dirgenerada;
    }

    public String getFir_dirfirmada() {
        return fir_dirfirmada;
    }

    public void setFir_dirfirmada(String fir_dirfirmada) {
        this.fir_dirfirmada = fir_dirfirmada;
    }

    public String getFir_dirautorizada() {
        return fir_dirautorizada;
    }

    public void setFir_dirautorizada(String fir_dirautorizada) {
        this.fir_dirautorizada = fir_dirautorizada;
    }

    public String getFir_dirrechazada() {
        return fir_dirrechazada;
    }

    public void setFir_dirrechazada(String fir_dirrechazada) {
        this.fir_dirrechazada = fir_dirrechazada;
    }

    public String getFir_clave() {
        return fir_clave;
    }

    public void setFir_clave(String fir_clave) {
        this.fir_clave = fir_clave;
    }

    public String getFir_estado() {
        return fir_estado;
    }

    public void setFir_estado(String fir_estado) {
        this.fir_estado = fir_estado;
    }

    public String getFir_fechacaduca() {
        return fir_fechacaduca;
    }

    public void setFir_fechacaduca(String fir_fechacaduca) {
        this.fir_fechacaduca = fir_fechacaduca;
    }

    public String getFir_usuariocrea() {
        return fir_usuariocrea;
    }

    public void setFir_usuariocrea(String fir_usuariocrea) {
        this.fir_usuariocrea = fir_usuariocrea;
    }

    public String getFir_fechacrea() {
        return fir_fechacrea;
    }

    public void setFir_fechacrea(String fir_fechacrea) {
        this.fir_fechacrea = fir_fechacrea;
    }

    public String getFir_ipcrea() {
        return fir_ipcrea;
    }

    public void setFir_ipcrea(String fir_ipcrea) {
        this.fir_ipcrea = fir_ipcrea;
    }

    public int getFir_timer() {
        return fir_timer;
    }

    public void setFir_timer(int fir_timer) {
        this.fir_timer = fir_timer;
    }

    public String getFir_correoalertas() {
        return fir_correoalertas;
    }

    public void setFir_correoalertas(String fir_correoalertas) {
        this.fir_correoalertas = fir_correoalertas;
    }

}
