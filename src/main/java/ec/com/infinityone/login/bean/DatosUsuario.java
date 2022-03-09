/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.login.bean;

import ec.com.infinityone.modeloWeb.Usuario;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author HP
 */
@Named
@SessionScoped
public class DatosUsuario implements Serializable {

    /*
    Variable que alamcena un usuario
     */
    private Usuario user;
    /*
    Variable que almacena el nombre del usuaio conectado
     */
    private String userConected;
    /*
    variable que alamacena los productos asfalto
     */
    private String productoSinFe;

    @PostConstruct
    public void init() {

    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getUserConected() {
        return userConected;
    }

    public void setUserConected(String userConected) {
        this.userConected = userConected;
    }

    public String getProductoSinFe() {
        return productoSinFe;
    }

    public void setProductoSinFe(String productoSinFe) {
        this.productoSinFe = productoSinFe;
    }
    
    

}
