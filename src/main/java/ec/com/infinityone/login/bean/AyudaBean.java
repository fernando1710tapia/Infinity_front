/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.infinityone.login.bean;

import ec.com.infinityone.configuration.Fichero;
import ec.com.infinityone.modeloWeb.Usuario;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author SonyVaio
 */
@Named
@SessionScoped
public class AyudaBean implements Serializable {

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
    private String direcMedia;

    private boolean actualizarD;

    private TreeNode root;

    @PostConstruct
    public void init() {
        actualizarD = false;
        crearAtbol();
    }

    public void crearAtbol() {
        root = new DefaultTreeNode("Files", null);
        TreeNode node0 = new DefaultTreeNode("Seguridad", root);
        TreeNode node1 = new DefaultTreeNode("Catálogo", root);
        TreeNode node2 = new DefaultTreeNode("Monitor Comercial", root);
        TreeNode node3 = new DefaultTreeNode("Actor Comercial", root);
        TreeNode node4 = new DefaultTreeNode("Pedidos y Facturación", root);
        TreeNode node5 = new DefaultTreeNode("Precios y Facturación", root);
        TreeNode node6 = new DefaultTreeNode("Reportes", root);

//        TreeNode node00 = new DefaultTreeNode("Menús", node0);
//        TreeNode node01 = new DefaultTreeNode("Permisos", node0);
//        TreeNode node02 = new DefaultTreeNode("Usuarios", node0);
        node0.getChildren().add(new DefaultTreeNode("Menús"));
        node0.getChildren().add(new DefaultTreeNode("Permisos"));
        node0.getChildren().add(new DefaultTreeNode("Usuarios"));

        node1.getChildren().add(new DefaultTreeNode("Tipo de Cliente"));
        node1.getChildren().add(new DefaultTreeNode("Banco"));
        node1.getChildren().add(new DefaultTreeNode("Terminal"));
        node1.getChildren().add(new DefaultTreeNode("Dirección INEN"));
        node1.getChildren().add(new DefaultTreeNode("Forma de Pago"));
        node1.getChildren().add(new DefaultTreeNode("Medida"));
        node1.getChildren().add(new DefaultTreeNode("Producto"));
        node1.getChildren().add(new DefaultTreeNode("Área Mercadeo"));

        node2.getChildren().add(new DefaultTreeNode("Monitor Comercial"));

        node3.getChildren().add(new DefaultTreeNode("Abastecedora"));
        node3.getChildren().add(new DefaultTreeNode("Cliente"));
        node3.getChildren().add(new DefaultTreeNode("Cliente Producto"));
        node3.getChildren().add(new DefaultTreeNode("Comercializadora"));
        node3.getChildren().add(new DefaultTreeNode("Comercializadora Producto"));
        node3.getChildren().add(new DefaultTreeNode("Numeración Documento"));
        node3.getChildren().add(new DefaultTreeNode("Total Garantizado"));
        node3.getChildren().add(new DefaultTreeNode("Cliente Garantia"));

        node4.getChildren().add(new DefaultTreeNode("Consulta Facturas Cliente"));
        node4.getChildren().add(new DefaultTreeNode("Pago de Factura"));
        node4.getChildren().add(new DefaultTreeNode("Notas de Pedido"));
        node4.getChildren().add(new DefaultTreeNode("Facturación"));
        node4.getChildren().add(new DefaultTreeNode("Refacturación"));
        node4.getChildren().add(new DefaultTreeNode("Nota de Crédito"));
        node4.getChildren().add(new DefaultTreeNode("Prórrogas de Facturas"));

        node5.getChildren().add(new DefaultTreeNode("Facturador Despachador"));
        node5.getChildren().add(new DefaultTreeNode("Gravamen"));
        node5.getChildren().add(new DefaultTreeNode("Lista de Precios"));
        node5.getChildren().add(new DefaultTreeNode("Precio"));
        node5.getChildren().add(new DefaultTreeNode("Rubro Tercero"));
        node5.getChildren().add(new DefaultTreeNode("Cliente Rubro Tercero"));
        node5.getChildren().add(new DefaultTreeNode("Fecha Festiva"));

        node6.getChildren().add(new DefaultTreeNode("Reporte Precios"));

        direcMedia = Fichero.getCARPETAAYUDA()+ "\\CLIENTE_2\\cliente.MP4";
    }

    public void mostarPantalla() {
        PrimeFaces.current().executeScript("PF('ayuda').show()");
    }

    public TreeNode getRoot() {
        return root;
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

    public String getDirecMedia() {
        return direcMedia;
    }

    public void setDirecMedia(String direcMedia) {
        this.direcMedia = direcMedia;
    }   

    public boolean isActualizarD() {
        return actualizarD;
    }

    public void setActualizarD(boolean actualizarD) {
        this.actualizarD = actualizarD;
    }

}
