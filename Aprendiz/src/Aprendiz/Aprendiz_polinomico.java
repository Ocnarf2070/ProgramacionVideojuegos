package Aprendiz;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import weka.core.*;
import weka.classifiers.trees.*;

public class Aprendiz_polinomico {
    public static float mediana(Vector<Float> numeros){
        int a[] = new int[numeros.size()];
        for(int i = 0; i < numeros.size(); i++){
            a[i] = (int) ( (numeros.elementAt(i)).floatValue()*1000);
            
        }
        Arrays.sort(a);
        return a[ a.length/2]/1000f;
    }
    
    public static void main(String [] args){
        try{
            String  FicheroCasosEntrenamiento =System.getProperty("user.dir")+"/lanzamientos.arff";
            Instances  casosEntrenamiento  = new Instances ( new BufferedReader(new FileReader(FicheroCasosEntrenamiento)));
            casosEntrenamiento.setClassIndex(  casosEntrenamiento.numAttributes() - 1 );  //OJO: aprende  v = f (angulo, distancia)
            M5P conocimiento = new M5P();   //àra regresión, o J48 para clasificación
            //MultilayerPerceptron conocimiento = new MultilayerPerceptron();
            //SMOreg conocimiento = new SMOreg();   //àra regresión, o J48 para clasificación
            //RBFNetwork conocimiento = new RBFNetwork();   //àra regresión, o J48 para clasificación
            
            //CONSTRUCCIÓN DE CIENTOS DE CASOS APRENDIENDO   con observaciones de distancia =  v*v*Math.sin(2*angulo)/10f);
            float _45gradosRad= (float) (Math.PI / 4f);
            float angulo_inicial= 0.5f *_45gradosRad;
            float angulo_final =   1.5f *_45gradosRad;
            float deltaAngulo =    0.1f* _45gradosRad;
            for (float a=angulo_inicial; a<angulo_final ; a += deltaAngulo ){
                for (float v=0f; v<=10; v += 2f ){   //z es la velocidad
                    Instance casoAdecidir = new Instance(casosEntrenamiento.numAttributes());
                    casoAdecidir.setDataset(casosEntrenamiento);
                    casoAdecidir.setValue(0,  a);
                    casoAdecidir.setValue(1,  v  );
                    casoAdecidir.setValue(2,   v*v*Math.sin(2*a)/10f);
                    casosEntrenamiento.add(casoAdecidir);
                }}
            
            //APRENDIZAJE DE LOS CASOS  -- construccion de un árbol de regresión
            conocimiento.buildClassifier(casosEntrenamiento);
            
            //RESOLUCION DE DOS INCOGNITAS  ANGULO Y VELOCIDAD (la  funcion velocidad ya se aprendió)
            float vel_Oraculo=5.0f;            //El oraculo quiere saber si predice bien Vo= 8.5 m/s. Calcula la distancia que deberia recorrer
            System.out.println("Velocidad a predecir  = "+vel_Oraculo+" m/s           (para luego buscar su angulo)");
            float distancia=    vel_Oraculo * vel_Oraculo*(float) Math.sin( 2f *  _45gradosRad )/10f;         //con un angulo "cualquiera" =45º
            float menorErrorDistancia=1e10f;
            float mejorVelocidad =0;
            float mejorAngulo=0;
            System.out.println("DISTANCIA OBJETIVO = "+distancia+ " m                (calculada por formula)\n");
            
            
            //El NPC no conoce ni angulo ni velocidad (hace dos FOR anidados)
            
            System.out.println("Prediciendo Vo = f (distancia, angulo)    (variando el angulo)");
            for (float angulo=angulo_inicial; angulo< angulo_final; angulo += deltaAngulo){  //
                for (float v=0f; v<=10; v += 0.2f ){
                    Instance casoAdecidir = new Instance(casosEntrenamiento.numAttributes());
                    casoAdecidir.setDataset(casosEntrenamiento);
                    casoAdecidir.setValue(0,  angulo);
                    casoAdecidir.setValue(1,  v);
                    float  prediccion = (float) conocimiento.classifyInstance(casoAdecidir);
                    if (Math.abs(prediccion-distancia)<menorErrorDistancia){
                        menorErrorDistancia=Math.abs(prediccion-distancia);
                        mejorVelocidad = v;
                        mejorAngulo = angulo;
                    }
                }}
            
            float distanciaFinalAlcanzada=   mejorVelocidad*mejorVelocidad*(float) Math.sin( 2f * mejorAngulo )/10f;         //con un angulo "cualquiera" =45º
            System.out.println(String.format("\nLanzamiento con V0=%3.3f y angulo=%3.3f y...", mejorVelocidad, mejorAngulo*180/3.1416f));
            System.out.println(String.format("Distancia alcanzada = %3.3f   (error distancia= %3.3f)",distanciaFinalAlcanzada,(distancia-distanciaFinalAlcanzada)));
        } catch(Exception e){       System.out.println("error");  }
    }}