/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.prueba02;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;

/**
 *
 * @author anton
 */
public class Concursos extends JFrame{
     static DefaultListModel modelog;
    static JList area;
    private Neo4jClass mineo = new Neo4jClass ("bolt://localhost:7687","neo4j","123");
    static Driver driver1 = GraphDatabase.driver("bolt://localhost:7687",AuthTokens.basic("neo4j","123"));
    
    public Concursos(){
        area = new JList();
        modelog = new DefaultListModel();
        add(area);
        
        setSize(400,800);
        cargarlista();
        setVisible(true);
        
        
        
    }
    public void cargarlista(){
        List<Record> listag;
      area.removeAll();
      modelog.removeAllElements();
      area.setModel(modelog);
      listag = mineo.obtenertodosConcursos();
      for (int i=0;i<listag.size();i++){
          Record p = listag.get(i);
          System.out.println(p);
          String añadir = quitarcomas("nombre: "+p.get("k.nombreConcurso")+"    participantes: "+p.get("k.numeroParticipantes")+"    ganador: ("+p.get("k.ganador").toString()+")");
          modelog.addElement(añadir);
          modelog.addElement(" ");
         System.out.println(añadir);
      }
       area.setModel(modelog);
    }
    
       public static String quitarcomas(String c){
        String resultado ="";
        for(int i=0;i<c.length();i++){
           if(c.charAt(i)!= '"'){
            resultado +=c.charAt(i);
        }
        
    }
    return resultado;
    }
}
