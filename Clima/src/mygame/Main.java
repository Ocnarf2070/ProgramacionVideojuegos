/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.trees.*;
import weka.core.*;

/**
 *
 * @author franc
 */
public class Main {
     public static void main(String [] args){
         try {
             Random rnd = new Random();
             String  FicheroCasosEntrenamiento =System.getProperty("user.dir")+"/clima.arff";
             Instances  casosEntrenamiento  = new Instances ( new BufferedReader(new FileReader(FicheroCasosEntrenamiento)));
             casosEntrenamiento.setClassIndex(  casosEntrenamiento.numAttributes() - 1 );
             J48 conocimiento = new J48();
             conocimiento.buildClassifier(casosEntrenamiento);
             //System.out.println(conocimiento.graph());
             
             String [] tipo_de_dia = {"soleado","nublado","lluvioso"};
             int humedad;
             String vientos;
             
             for(int i=0;i<10;i++){
                 String dia = tipo_de_dia[rnd.nextInt(3)];
                 humedad = rnd.nextInt(100);
                 vientos=rnd.nextBoolean()?"TRUE":"FALSE";
                 Instance casoAdecidir = new Instance(casosEntrenamiento.numAttributes());
                 casoAdecidir.setDataset(casosEntrenamiento);
                 casoAdecidir.setValue(0, dia);
                 casoAdecidir.setValue(2, humedad);
                 casoAdecidir.setValue(3, vientos);
                 String prediccion = conocimiento.classifyInstance(casoAdecidir)==0?"Si":"No";
                 System.out.println("Tipo de dia: "+dia+
                         " Humedad: "+humedad+
                         " Viento: "+vientos+
                         " Jugar? "+prediccion);
             }
         } catch (FileNotFoundException ex) {
             Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
             Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         } catch (Exception ex) {
             Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
}
