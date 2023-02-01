/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modeloWeb;

import java.io.Serializable;

/**
 *
 * @author SonyVaio
 */
public class Pagocheque implements Serializable {
    
    protected PagochequePK pagochequePK;

    private byte[] imagencheque;

    private String urlimagencheque;

    public Pagocheque() {
    }

    public Pagocheque(PagochequePK pagochequePK) {
        this.pagochequePK = pagochequePK;
    }

    public Pagocheque(String codigoabastecedora, String codigocomercializadora, String numero, String codigobanco) {
        this.pagochequePK = new PagochequePK(codigoabastecedora, codigocomercializadora, numero, codigobanco);
    }

    public PagochequePK getPagochequePK() {
        return pagochequePK;
    }

    public void setPagochequePK(PagochequePK pagochequePK) {
        this.pagochequePK = pagochequePK;
    }

    public byte[] getImagencheque() {
        return imagencheque;
    }

    public void setImagencheque(byte[] imagencheque) {
        this.imagencheque = imagencheque;
    }

    public String getUrlimagencheque() {
        return urlimagencheque;
    }

    public void setUrlimagenchequeS(String urlimagencheque) {
        this.urlimagencheque = urlimagencheque;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagochequePK != null ? pagochequePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pagocheque)) {
            return false;
        }
        Pagocheque other = (Pagocheque) object;
        if ((this.pagochequePK == null && other.pagochequePK != null) || (this.pagochequePK != null && !this.pagochequePK.equals(other.pagochequePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.com.infinity.modelo.Pagocheque[ pagochequePK=" + pagochequePK + " ]";
    }

}
