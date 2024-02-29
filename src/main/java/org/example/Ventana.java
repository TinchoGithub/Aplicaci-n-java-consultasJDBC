package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ventana {
    public static void main(String[] args){

        JFrame mi_marco = new Marco_aplicacion();
        mi_marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mi_marco.setVisible(true);

    }
}

class Marco_aplicacion extends JFrame{

    private JComboBox secciones;
    private JComboBox paises;
    private JTextArea resultado;
    private Connection miConexion;
    private PreparedStatement enviaConsultaTipo;
    private final String consultaTipo = "SELECT nombre, tipo, precio, origen FROM productos WHERE tipo=?";


    public Marco_aplicacion(){
        setTitle("CONSULTA BBDD");
        setBounds(500, 300, 400, 400);
        setLayout(new BorderLayout());
        JPanel menus = new JPanel();
        menus.setLayout(new FlowLayout());
        secciones = new JComboBox<>();
        secciones.setEditable(false);
        secciones.addItem("Todos");
        paises = new JComboBox<>();
        paises.setEditable(false);
        paises.addItem("Todos");
        resultado = new JTextArea(4, 50);
        resultado.setEditable(false);
        add(resultado);
        menus.add(secciones);
        menus.add(paises);
        add(menus, BorderLayout.NORTH);
        add(resultado, BorderLayout.CENTER);
        JButton botonConsulta = new JButton("Consulta");

        botonConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ejecutaConsulta();
            }
        });

        add(botonConsulta, BorderLayout.SOUTH);



        //------------CONEXIÓN CON BBDD----------------

        try{
            miConexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda_prueba", "root", "MartinSQL");
            Statement sentencia = miConexion.createStatement();

            //-----CARGA JCOMBOBOX TIPO-----

            String consulta = "SELECT DISTINCTROW tipo FROM productos";
            ResultSet rs = sentencia.executeQuery(consulta);
            while (rs.next()){
                secciones.addItem(rs.getString(1));
            }
            rs.close();

            //-----CARGA JCOMBOBOX PAISES-----

            consulta = "SELECT DISTINCTROW origen FROM productos";
            rs = sentencia.executeQuery(consulta);
            while (rs.next()){
                paises.addItem(rs.getString(1));
            }
            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void ejecutaConsulta(){

        ResultSet rs = null;
        try{
            String seccion = (String) secciones.getSelectedItem();
            enviaConsultaTipo = miConexion.prepareStatement(consultaTipo);
            enviaConsultaTipo.setString(1, seccion);
            rs = enviaConsultaTipo.executeQuery();

            while (rs.next()){

                resultado.append(rs.getString(1));
                resultado.append(", ");
                resultado.append(rs.getString(2));
                resultado.append(", ");
                resultado.append(rs.getString(3));
                resultado.append(", ");
                resultado.append(rs.getString(4));


                resultado.append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
