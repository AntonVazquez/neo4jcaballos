/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.prueba02;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import static org.neo4j.driver.Values.parameters;


/**
 *
 * @author anton
 */
public class Neo4jClass implements AutoCloseable {
    
    private Driver driver;
    
    public static void main (String args []){
        Buscadorpre b = new Buscadorpre();
        b.setVisible(true);
    }
    
    public Neo4jClass(String uri, String user, String password) {
        // Abrimos conexion
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
        
    }
    
    public void inserCaballo(String nombreCaballo, String fechaNacimiento, String chip ) {
    Session sesion = driver.session();
    String strTrans =  sesion.writeTransaction(new TransactionWork<String>() {
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("CREATE(c:caballo{nombreCaballo:$nombreCaballo,fechaNacimiento:$fechaNacimiento,chip:$chip}) return  c.nombreCaballo", parameters("nombreCaballo",nombreCaballo,"fechaNacimiento",fechaNacimiento,"chip",chip));
            if (resultado.list().size() > 0) {
                System.out.println("nodo insertado");
            } else {
                System.out.println("nodo no insertado");
            }
            return "";
        }
    });
    }
    
     
    
      public void insertGanaderia (String nombreGanaderia, String fundacion ) {
    Session sesion = driver.session();
    String strTrans =  sesion.writeTransaction(new TransactionWork<String>() {
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("CREATE(g:ganaderia {nombreGanaderia: $nombreGanaderia,fundacion:$fundacion})", parameters("nombreGanaderia",nombreGanaderia,"fundacion",fundacion));
            if (resultado.list().size() > 0) {
                System.out.println("nodo insertado");
            } else {
                System.out.println("nodo no insertado");
            }
            return "";
        }
    });
  
    }
        public void inserConcurso(String nombreConcurso, int numeroParticipantes, String importanciaConcurso ) {
    Session sesion = driver.session();
    String ganador = "no";
    String strTrans =  sesion.writeTransaction(new TransactionWork<String>() {
        @Override
        public String execute(Transaction tx) {
            Result resultado = (Result) tx.run("CREATE(k:concurso {nombreConcurso: $nombreConcurso, numeroParticipantes:$numeroParticipantes, importanciaConcurso: $importanciaConcurso,ganador:$ganador})", parameters("nombreConcurso",nombreConcurso,"numeroParticipantes",numeroParticipantes,"importanciaConcurso",importanciaConcurso,"ganador",ganador));
            if (resultado.list().size() > 0) {
                System.out.println("nodo insertado");
            } else {
                System.out.println("nodo no insertado");
            }
            return "";
        }
    });
   
    sesion.close();
    }
   
        public void relacionarcaballoGanaderia(String chip, String nombreGanaderia) {
    Session sesion = driver.session();
    String strTrans =  sesion.writeTransaction(new TransactionWork<String>() {
        @Override
        public String execute(Transaction tx) {
           Result resultado = (Result) tx.run("MATCH(c:caballo{chip:$chip}),(j:ganaderia{nombreGanaderia:$nombreGanaderia}) CREATE (c)-[:pertenece]->(j)", parameters("chip",chip,"nombreGanaderia",nombreGanaderia));
            if (resultado.list().size() > 0) {
                System.out.println("nodo insertado");
            } else {
                System.out.println("nodo no insertado");
            }
            return "";
        }
    });
        }
        
         public void relacionarcaballoConcurso(String chip, String nombreConcurso) {
    Session sesion = driver.session();
    String strTrans =  sesion.writeTransaction(new TransactionWork<String>() {
        @Override
        public String execute(Transaction tx) {
           
            Result resultado = (Result) tx.run("MATCH (c:caballo{chip:$chip}),(k:concurso{nombreConcurso:$nombreConcurso})"+
            "CREATE (c)-[:participa]->(k)",parameters("chip",chip,"nombreConcurso",nombreConcurso));
           
             return "";
        }
    });
        }
         
         public List<Record> obtenerCaballo(){
        Session sesion = driver.session();
        Transaction tx = sesion.beginTransaction();
        
        Result resultado=(Result)tx.run("MATCH (c:caballo) RETURN c.nombreCaballo,c.fechaNacimiento,c.chip");
        List<Record> lista= resultado.list();
        while(resultado.hasNext()){
            Record r = resultado.next();
            lista.add(r);
        }
       
        return lista;
       }
          public List<Record> obtenerGanaderia(){
        Session sesion = driver.session();
        Transaction tx = sesion.beginTransaction();
        
        Result resultado=(Result) tx.run("MATCH (g:ganaderia) RETURN g.nombreGanaderia");
        List<Record> lista= resultado.list();
        while(resultado.hasNext()){
             Record r = resultado.next();
            lista.add(r);
        }
       
        return lista;
       }
             public List<Record> obtenerConcurso(){
        Session sesion = driver.session();
        Transaction tx = sesion.beginTransaction();
        
        Result resultado=(Result) tx.run("MATCH (k:concurso{ganador:'no'})  RETURN k.nombreConcurso,k.importanciaConcurso");
        List<Record> lista= resultado.list();
        while(resultado.hasNext()){
             Record r = resultado.next();
            lista.add(r);
        }
       
        return lista;
       }
             public List<Record> obtenertodosConcursos(){
        Session sesion = driver.session();
        Transaction tx = sesion.beginTransaction();
        
        Result resultado=(Result) tx.run("MATCH (k:concurso)  RETURN k.nombreConcurso,k.numeroParticipantes,k.ganador");
        List<Record> lista= resultado.list();
        while(resultado.hasNext()){
             Record r = resultado.next();
            lista.add(r);
        }
       
        return lista;
       }

            public void eliminarCaballo(String nombreCaballo){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("MATCH(c:caballo) WHERE c.nombreCaballo='" + nombreCaballo + "' DETACH DELETE c");
                return "";
                        }
            });
            sesion.close(); 
           }
           
            public void eliminarGanaderia(String nombreGanaderia){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                String consulta = "MATCH(g:ganaderia) WHERE g.nombreGanaderia='" + nombreGanaderia + "' DETACH DELETE g";
                System.out.println(consulta);
                Result resultado = (Result) tx.run(consulta);
                return "";
                        }
            });
            sesion.close(); 
           }
            
             public void eliminarConcurso(String nombreConcurso){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("MATCH(k:concurso) WHERE k.nombreConcurso='" + nombreConcurso + "' DETACH DELETE k");
                return "";
                        }
            });
            sesion.close(); 
           }
             
             public void eliminarTodo(){
        Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("MATCH(r) DETACH DELETE r");
                return "";
                        }
            });
            sesion.close();
        } 
             
             public void asignarGanador(String nombre,String concurso){
                 Session sesion = driver.session();
        String strTrans;
        strTrans = sesion.writeTransaction(new TransactionWork<String>(){
            @Override
            public String execute (Transaction tx) {
                Result resultado = (Result) tx.run("MATCH (c:caballo{nombreCaballo:$nombre}),(j:concurso{nombreConcurso:$nombreconcurso}) SET j.ganador=$nombre CREATE (c)-[:gano]->(j)",parameters("nombre",nombre,"nombreconcurso",concurso));
                return "";
                        }
            });
            sesion.close();
             }
            
             
             
         

           
}
             
