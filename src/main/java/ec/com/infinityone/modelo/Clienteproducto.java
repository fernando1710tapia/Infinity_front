/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.modelo;

/**
 *
 * @author HP
 */
public class Clienteproducto {
    protected ClienteproductoPK clienteproductoPK;

    private boolean activo;

    private String usuarioactual;

    private Cliente cliente;

    private Producto producto;
    
    public Clienteproducto() {
    }

    public Clienteproducto(ClienteproductoPK clienteproductoPK) {
        this.clienteproductoPK = clienteproductoPK;
    }

    public Clienteproducto(ClienteproductoPK clienteproductoPK, boolean activo, String usuarioactual) {
        this.clienteproductoPK = clienteproductoPK;
        this.activo = activo;
        this.usuarioactual = usuarioactual;
    }

    public Clienteproducto(String codigocomercializadora, String codigocliente, String codigo) {
        this.clienteproductoPK = new ClienteproductoPK(codigocomercializadora, codigocliente, codigo);
    }

    public ClienteproductoPK getClienteproductoPK() {
        return clienteproductoPK;
    }

    public void setClienteproductoPK(ClienteproductoPK clienteproductoPK) {
        this.clienteproductoPK = clienteproductoPK;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clienteproductoPK != null ? clienteproductoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clienteproducto)) {
            return false;
        }
        Clienteproducto other = (Clienteproducto) object;
        if ((this.clienteproductoPK == null && other.clienteproductoPK != null) || (this.clienteproductoPK != null && !this.clienteproductoPK.equals(other.clienteproductoPK))) {
            return false;
        }
        return true;
    }

    
}
