/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.com.infinityone.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Modelo para la asignación de Petro Terminal
 */
public class Asignacionpetroterminal implements Serializable {

    private AsignacionpetroterminalPK asignacionpetroterminalPK;

    // EXTRA (ext)
    private BigDecimal eppasiginicialsolicitadoext;
    private BigDecimal eppasiginicialautorizadoext;
    private BigDecimal eppasigadicionalsolicitadoext;
    private BigDecimal eppasigadicionalautorizadoext;
    private BigDecimal eppasigtotalsolicitadoext;
    private BigDecimal eppasigtotalautorizadoext;

    // DIESEL (die)
    private BigDecimal eppasiginicialsolicitadodie;
    private BigDecimal eppasiginicialautorizadodie;
    private BigDecimal eppasigadicionalsolicitadodie;
    private BigDecimal eppasigadicionalautorizadodie;
    private BigDecimal eppasigtotalsolicitadodie;
    private BigDecimal eppasigtotalautorizadodie;

    // SÚPER (sup)
    private BigDecimal eppasiginicialsolicitadosup;
    private BigDecimal eppasiginicialautorizadosup;
    private BigDecimal eppasigadicionalsolicitadosup;
    private BigDecimal eppasigadicionalautorizadosup;
    private BigDecimal eppasigtotalsolicitadosup;
    private BigDecimal eppasigtotalautorizadosup;

    private String usuarioactual;
    private Terminal terminal;

    public Asignacionpetroterminal() {
        this.asignacionpetroterminalPK = new AsignacionpetroterminalPK();
        inicializarConCeros();
    }

    public Asignacionpetroterminal(AsignacionpetroterminalPK asignacionpetroterminalPK) {
        this.asignacionpetroterminalPK = asignacionpetroterminalPK;
        inicializarConCeros();
    }

    public void inicializarConCeros() {
        this.eppasiginicialsolicitadoext = BigDecimal.ZERO;
        this.eppasiginicialautorizadoext = BigDecimal.ZERO;
        this.eppasigadicionalsolicitadoext = BigDecimal.ZERO;
        this.eppasigadicionalautorizadoext = BigDecimal.ZERO;
        this.eppasigtotalsolicitadoext = BigDecimal.ZERO;
        this.eppasigtotalautorizadoext = BigDecimal.ZERO;

        this.eppasiginicialsolicitadodie = BigDecimal.ZERO;
        this.eppasiginicialautorizadodie = BigDecimal.ZERO;
        this.eppasigadicionalsolicitadodie = BigDecimal.ZERO;
        this.eppasigadicionalautorizadodie = BigDecimal.ZERO;
        this.eppasigtotalsolicitadodie = BigDecimal.ZERO;
        this.eppasigtotalautorizadodie = BigDecimal.ZERO;

        this.eppasiginicialsolicitadosup = BigDecimal.ZERO;
        this.eppasiginicialautorizadosup = BigDecimal.ZERO;
        this.eppasigadicionalsolicitadosup = BigDecimal.ZERO;
        this.eppasigadicionalautorizadosup = BigDecimal.ZERO;
        this.eppasigtotalsolicitadosup = BigDecimal.ZERO;
        this.eppasigtotalautorizadosup = BigDecimal.ZERO;
    }

    /**
     * Calcula los totales (Inicial + Adicional) para cada producto
     */
    public void calcularTotales() {
        BigDecimal iniSolExt = this.eppasiginicialsolicitadoext != null ? this.eppasiginicialsolicitadoext : BigDecimal.ZERO;
        BigDecimal adiSolExt = this.eppasigadicionalsolicitadoext != null ? this.eppasigadicionalsolicitadoext : BigDecimal.ZERO;
        this.eppasigtotalsolicitadoext = iniSolExt.add(adiSolExt);

        BigDecimal iniAutExt = this.eppasiginicialautorizadoext != null ? this.eppasiginicialautorizadoext : BigDecimal.ZERO;
        BigDecimal adiAutExt = this.eppasigadicionalautorizadoext != null ? this.eppasigadicionalautorizadoext : BigDecimal.ZERO;
        this.eppasigtotalautorizadoext = iniAutExt.add(adiAutExt);

        BigDecimal iniSolDie = this.eppasiginicialsolicitadodie != null ? this.eppasiginicialsolicitadodie : BigDecimal.ZERO;
        BigDecimal adiSolDie = this.eppasigadicionalsolicitadodie != null ? this.eppasigadicionalsolicitadodie : BigDecimal.ZERO;
        this.eppasigtotalsolicitadodie = iniSolDie.add(adiSolDie);

        BigDecimal iniAutDie = this.eppasiginicialautorizadodie != null ? this.eppasiginicialautorizadodie : BigDecimal.ZERO;
        BigDecimal adiAutDie = this.eppasigadicionalautorizadodie != null ? this.eppasigadicionalautorizadodie : BigDecimal.ZERO;
        this.eppasigtotalautorizadodie = iniAutDie.add(adiAutDie);

        BigDecimal iniSolSup = this.eppasiginicialsolicitadosup != null ? this.eppasiginicialsolicitadosup : BigDecimal.ZERO;
        BigDecimal adiSolSup = this.eppasigadicionalsolicitadosup != null ? this.eppasigadicionalsolicitadosup : BigDecimal.ZERO;
        this.eppasigtotalsolicitadosup = iniSolSup.add(adiSolSup);

        BigDecimal iniAutSup = this.eppasiginicialautorizadosup != null ? this.eppasiginicialautorizadosup : BigDecimal.ZERO;
        BigDecimal adiAutSup = this.eppasigadicionalautorizadosup != null ? this.eppasigadicionalautorizadosup : BigDecimal.ZERO;
        this.eppasigtotalautorizadosup = iniAutSup.add(adiAutSup);
    }

    public BigDecimal getTotalSolPorProducto(String producto) {
        if (producto == null) return BigDecimal.ZERO;
        if ("EXTRA".equalsIgnoreCase(producto)) {
            return this.eppasigtotalsolicitadoext != null ? this.eppasigtotalsolicitadoext : BigDecimal.ZERO;
        } else if ("DIESEL".equalsIgnoreCase(producto)) {
            return this.eppasigtotalsolicitadodie != null ? this.eppasigtotalsolicitadodie : BigDecimal.ZERO;
        } else if ("SUPER".equalsIgnoreCase(producto)) {
            return this.eppasigtotalsolicizadosup() != null ? this.eppasigtotalsolicizadosup() : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalAutoPorProducto(String producto) {
        if (producto == null) return BigDecimal.ZERO;
        if ("EXTRA".equalsIgnoreCase(producto)) {
            return this.eppasigtotalautorizadoext != null ? this.eppasigtotalautorizadoext : BigDecimal.ZERO;
        } else if ("DIESEL".equalsIgnoreCase(producto)) {
            return this.eppasigtotalautorizadodie != null ? this.eppasigtotalautorizadodie : BigDecimal.ZERO;
        } else if ("SUPER".equalsIgnoreCase(producto)) {
            return this.eppasigtotalautorizadosup != null ? this.eppasigtotalautorizadosup : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal eppasigtotalsolicizadosup() {
        return this.eppasigtotalsolicitadosup;
    }

    // Getters y Setters

    public AsignacionpetroterminalPK getAsignacionpetroterminalPK() {
        return asignacionpetroterminalPK;
    }

    public void setAsignacionpetroterminalPK(AsignacionpetroterminalPK asignacionpetroterminalPK) {
        this.asignacionpetroterminalPK = asignacionpetroterminalPK;
    }

    public BigDecimal getEppasiginicialsolicitadoext() {
        return eppasiginicialsolicitadoext;
    }

    public void setEppasiginicialsolicitadoext(BigDecimal eppasiginicialsolicitadoext) {
        this.eppasiginicialsolicitadoext = eppasiginicialsolicitadoext;
    }

    public BigDecimal getEppasiginicialautorizadoext() {
        return eppasiginicialautorizadoext;
    }

    public void setEppasiginicialautorizadoext(BigDecimal eppasiginicialautorizadoext) {
        this.eppasiginicialautorizadoext = eppasiginicialautorizadoext;
    }

    public BigDecimal getEppasigadicionalsolicitadoext() {
        return eppasigadicionalsolicitadoext;
    }

    public void setEppasigadicionalsolicitadoext(BigDecimal eppasigadicionalsolicitadoext) {
        this.eppasigadicionalsolicitadoext = eppasigadicionalsolicitadoext;
    }

    public BigDecimal getEppasigadicionalautorizadoext() {
        return eppasigadicionalautorizadoext;
    }

    public void setEppasigadicionalautorizadoext(BigDecimal eppasigadicionalautorizadoext) {
        this.eppasigadicionalautorizadoext = eppasigadicionalautorizadoext;
    }

    public BigDecimal getEppasigtotalsolicitadoext() {
        return eppasigtotalsolicitadoext;
    }

    public void setEppasigtotalsolicitadoext(BigDecimal eppasigtotalsolicitadoext) {
        this.eppasigtotalsolicitadoext = eppasigtotalsolicitadoext;
    }

    public BigDecimal getEppasigtotalautorizadoext() {
        return eppasigtotalautorizadoext;
    }

    public void setEppasigtotalautorizadoext(BigDecimal eppasigtotalautorizadoext) {
        this.eppasigtotalautorizadoext = eppasigtotalautorizadoext;
    }

    public BigDecimal getEppasiginicialsolicitadodie() {
        return eppasiginicialsolicitadodie;
    }

    public void setEppasiginicialsolicitadodie(BigDecimal eppasiginicialsolicitadodie) {
        this.eppasiginicialsolicitadodie = eppasiginicialsolicitadodie;
    }

    public BigDecimal getEppasiginicialautorizadodie() {
        return eppasiginicialautorizadodie;
    }

    public void setEppasiginicialautorizadodie(BigDecimal eppasiginicialautorizadodie) {
        this.eppasiginicialautorizadodie = eppasiginicialautorizadodie;
    }

    public BigDecimal getEppasigadicionalsolicitadodie() {
        return eppasigadicionalsolicitadodie;
    }

    public void setEppasigadicionalsolicitadodie(BigDecimal eppasigadicionalsolicitadodie) {
        this.eppasigadicionalsolicitadodie = eppasigadicionalsolicitadodie;
    }

    public BigDecimal getEppasigadicionalautorizadodie() {
        return eppasigadicionalautorizadodie;
    }

    public void setEppasigadicionalautorizadodie(BigDecimal eppasigadicionalautorizadodie) {
        this.eppasigadicionalautorizadodie = eppasigadicionalautorizadodie;
    }

    public BigDecimal getEppasigtotalsolicitadodie() {
        return eppasigtotalsolicitadodie;
    }

    public void setEppasigtotalsolicitadodie(BigDecimal eppasigtotalsolicitadodie) {
        this.eppasigtotalsolicitadodie = eppasigtotalsolicitadodie;
    }

    public BigDecimal getEppasigtotalautorizadodie() {
        return eppasigtotalautorizadodie;
    }

    public void setEppasigtotalautorizadodie(BigDecimal eppasigtotalautorizadodie) {
        this.eppasigtotalautorizadodie = eppasigtotalautorizadodie;
    }

    public BigDecimal getEppasiginicialsolicitadosup() {
        return eppasiginicialsolicitadosup;
    }

    public void setEppasiginicialsolicitadosup(BigDecimal eppasiginicialsolicitadosup) {
        this.eppasiginicialsolicitadosup = eppasiginicialsolicitadosup;
    }

    public BigDecimal getEppasiginicialautorizadosup() {
        return eppasiginicialautorizadosup;
    }

    public void setEppasiginicialautorizadosup(BigDecimal eppasiginicialautorizadosup) {
        this.eppasiginicialautorizadosup = eppasiginicialautorizadosup;
    }

    public BigDecimal getEppasigadicionalsolicizadosup() {
        return eppasigadicionalsolicitadosup;
    }

    public BigDecimal getEppasigadicionalsolicitadosup() {
        return eppasigadicionalsolicitadosup;
    }

    public void setEppasigadicionalsolicitadosup(BigDecimal eppasigadicionalsolicitadosup) {
        this.eppasigadicionalsolicitadosup = eppasigadicionalsolicitadosup;
    }

    public BigDecimal getEppasigadicionalautorizadosup() {
        return eppasigadicionalautorizadosup;
    }

    public void setEppasigadicionalautorizadosup(BigDecimal eppasigadicionalautorizadosup) {
        this.eppasigadicionalautorizadosup = eppasigadicionalautorizadosup;
    }

    public BigDecimal getEppasigtotalsolicitadosup() {
        return eppasigtotalsolicitadosup;
    }

    public void setEppasigtotalsolicitadosup(BigDecimal eppasigtotalsolicitadosup) {
        this.eppasigtotalsolicitadosup = eppasigtotalsolicitadosup;
    }

    public BigDecimal getEppasigtotalautorizadosup() {
        return eppasigtotalautorizadosup;
    }

    public void setEppasigtotalautorizadosup(BigDecimal eppasigtotalautorizadosup) {
        this.eppasigtotalautorizadosup = eppasigtotalautorizadosup;
    }

    public String getUsuarioactual() {
        return usuarioactual;
    }

    public void setUsuarioactual(String usuarioactual) {
        this.usuarioactual = usuarioactual;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }
}
