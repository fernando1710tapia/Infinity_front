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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author HP
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
    private String productoSinFe;

    private boolean actualizarD;

    private TreeNode root;

    @PostConstruct
    public void init() {
        actualizarD = false;
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

        TreeNode node00 = new DefaultTreeNode("Menús", node0);
        TreeNode node01 = new DefaultTreeNode("Permisos", node0);
        TreeNode node02 = new DefaultTreeNode("Usuarios", node0);

        node00.getChildren().add(new DefaultTreeNode("Expenses.doc"));
        node00.getChildren().add(new DefaultTreeNode("Resume.doc"));
        node01.getChildren().add(new DefaultTreeNode("Invoices.txt"));

        TreeNode node10 = new DefaultTreeNode("Meeting", node1);
        TreeNode node11 = new DefaultTreeNode("Product Launch", node1);
        TreeNode node12 = new DefaultTreeNode("Report Review", node1);

        TreeNode node20 = new DefaultTreeNode("Al Pacino", node2);
        TreeNode node21 = new DefaultTreeNode("Robert De Niro", node2);

        node20.getChildren().add(new DefaultTreeNode("Scarface"));
        node20.getChildren().add(new DefaultTreeNode("Serpico"));

        node21.getChildren().add(new DefaultTreeNode("Goodfellas"));
        node21.getChildren().add(new DefaultTreeNode("Untouchables"));
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

    public String getProductoSinFe() {
        return productoSinFe;
    }

    public void setProductoSinFe(String productoSinFe) {
        this.productoSinFe = productoSinFe;
    }

    public boolean isActualizarD() {
        return actualizarD;
    }

    public void setActualizarD(boolean actualizarD) {
        this.actualizarD = actualizarD;
    }

}
