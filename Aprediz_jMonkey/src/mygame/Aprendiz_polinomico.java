package mygame;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import weka.core.*;
import weka.classifiers.trees.*;
import weka.classifiers.functions.*;
public class Aprendiz_polinomico extends Thread implements Runnable{
  public void run(){      
     try{ 
        String  FicheroCasosEntrenamiento =System.getProperty("user.dir")+"/lanzamientos.arff";
         Instances  casosEntrenamiento  = new Instances ( new BufferedReader(new FileReader(FicheroCasosEntrenamiento)));
         casosEntrenamiento.setClassIndex(  casosEntrenamiento.numAttributes() - 1 );  //OJO: aprende  v = f (angulo, distancia)
         M5P conocimiento = new M5P();   //àra regresión, o J48 para clasificación
         //MultilayerPerceptron conocimiento = new MultilayerPerceptron();
         //SMOreg conocimiento = new SMOreg();   //àra regresión, o J48 para clasificación
         //RBFNetwork conocimiento = new RBFNetwork();   //àra regresión, o J48 para clasificación
         
         //CONSTRUCCIÓN DE CASOS DE APRENDIZAJE ; 
         float _1gradoRad= (float) (Math.PI / 180f);
         float angulo_inicial= 0;
         float angulo_final =   _1gradoRad; 
         float deltaAngulo =    0.3f* _1gradoRad;
         
         for (float a=angulo_inicial; a<angulo_final ; a= a+ deltaAngulo ){   
            //Condigurando variable1 de la Experiencia de Entrenamiento
            Main.velocidadRotacion= a;
            
            for (float v=0f; v<=10; v= v+ 2f ){   
                //Condigurando variable2 de la Experiencia de Entrenamiento
                 Main.app.velocidadDesplazamiento=v ;
            
                //Condicion para leer el efecto del PLAN. Ej. colisión con el suelo, etc. En este caso, espera de 1 segundo
                 Thread.sleep(1000);
            
                 //leyendo el resultado de la experiencia de entrenamiento. 
                 float posicionFinal= Main.app.geom.getLocalTranslation().x;
                 System.out.println(String.format("Con velocidad =%3.3f  anguloRotacion=%3.3f      ===1_seg===>    posFinal=%3.3f metros",v,a,posicionFinal));
                 Instance casoAdecidir = new Instance(casosEntrenamiento.numAttributes());
                 casoAdecidir.setDataset(casosEntrenamiento);   
                 casoAdecidir.setValue(0,  a);
                 casoAdecidir.setValue(1,  v  );
                 casoAdecidir.setValue(2,   posicionFinal);         
                 casosEntrenamiento.add(casoAdecidir);
                 Main.reiniciar=true;
        } }
  
            //APRENDIZAJE DE LOS CASOS  -- construccion de un árbol de regresión
            conocimiento.buildClassifier(casosEntrenamiento); 
            
            //RESOLUCION DE DOS INCOGNITAS  ANGULO Y VELOCIDAD (la  funcion velocidad ya se aprendió)
            float distanciaObjetivo=  7f;// 10e-3f;   //l_Oraculo quiere saber con que velocidad y angulo se llega a la distancia de 10 metros"
            float menorErrorDistancia=1e10f;
            float mejorVelocidad =0;
            float mejorAngulo=0;
            float mejorDistancia=0;
            System.out.println("\nDISTANCIA OBJETIVO (no ensayada durante el aprendizaje) = "+distanciaObjetivo+" metros");
 
           //El NPC no conoce ni angulo ni velocidad (hace dos FOR anidados) 
           for (float a=angulo_inicial; a< angulo_final; a=a+deltaAngulo){  // 
             for (float v=0f; v<=10; v= v+ 1f ){   
               Instance casoAdecidir = new Instance(casosEntrenamiento.numAttributes());
                casoAdecidir.setDataset(casosEntrenamiento);   
                casoAdecidir.setValue(0,  a);
                casoAdecidir.setValue(1,  v);        
                float  prediccion = (float) conocimiento.classifyInstance(casoAdecidir);
                if (Math.abs(prediccion-distanciaObjetivo)<menorErrorDistancia){
                    menorErrorDistancia=Math.abs(prediccion-distanciaObjetivo);
                    mejorVelocidad = v;
                    mejorAngulo = a;
                    mejorDistancia=prediccion;
                 System.out.println(String.format("mejor vel=%3.3f  mejorAng=%3.3f    mejor error distancia= %3.3f metros)", mejorVelocidad, mejorAngulo*180/3.1416f, menorErrorDistancia));
              }}}
        
        System.out.println(String.format("\nDistancia alcanzada despues de aprendiizaje= %3.3f ", mejorDistancia,(distanciaObjetivo-mejorDistancia)));        
        System.out.println(String.format("con velocidad Vo=%3.3f y angulo de rotacion=%3.3f    (error con distancia objetivo= %3.3f metros)", mejorVelocidad, mejorAngulo*180/3.1416f, menorErrorDistancia));
    } catch(Exception e){       System.out.println("error");} 
}}