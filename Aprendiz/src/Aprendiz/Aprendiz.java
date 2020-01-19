package Aprendiz;

import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import weka.core.*;
import weka.classifiers.trees.*;
import weka.classifiers.functions.*;

public class Aprendiz {
    public static float mediana(Vector<Float> numeros){
        int a[] = new int[numeros.size()];
        for(int i = 0; i < numeros.size(); i++) {
            a[i] = (int) ( (numeros.elementAt(i)).floatValue()*1000);
        }
        Arrays.sort(a);
        return a[ a.length/2]/1000f;
    }
    
    public static void main(String [] args){
        try{
            String  FicheroCasosEntrenamiento =System.getProperty("user.dir")+"/lanzamientos.arff";
            Instances  casosEntrenamiento  = new Instances ( new BufferedReader(new FileReader(FicheroCasosEntrenamiento)));
            casosEntrenamiento.setClassIndex(  casosEntrenamiento.numAttributes() - 2 );  //OJO: aprende  v = f (angulo, distancia)
            MultilayerPerceptron conocimiento = new MultilayerPerceptron();
            //M5P conocimiento = new M5P();   //àra regresión, o J48 para clasificación
            //SMOreg conocimiento = new SMOreg();   //àra regresión, o J48 para clasificación
            
            //CONSTRUCCIÓN DE CIENTOS DE CASOS APRENDIENDO  distancia =  v*v*Math.sin(2*angulo)/10f);
            float _45gradosRad= (float) (Math.PI / 4f);
            float angulo_inicial= 0.3f *_45gradosRad;
            float angulo_final =   1.5f *_45gradosRad;
            float deltaAngulo =    0.1f* _45gradosRad;
            for (float a=angulo_inicial; a<angulo_final ; a= a+ deltaAngulo ){
                for (float v=0f; v<=10; v= v+ 2f ){   //z es la velocidad
                    Instance casoAdecidir = new Instance(casosEntrenamiento.numAttributes());
                    casoAdecidir.setDataset(casosEntrenamiento);
                    casoAdecidir.setValue(0,  a);
                    casoAdecidir.setValue(1,  v  );
                    casoAdecidir.setValue(2,   (float) v*v*Math.sin(2*a)/10f);
                    casosEntrenamiento.add(casoAdecidir);
                }}
            
            //APRENDIZAJE DE LOS CASOS  -- construccion de un árbol de regresión
            conocimiento.buildClassifier(casosEntrenamiento);
            
            //EVALUACION DEL MODELO GENERADO: ERRORES DEL MODELO CON LOS CASOS
            //System.out.println("árbol generado"+conocimiento);
            /*Evaluation evaluador = new Evaluation(casosEntrenamiento);
            evaluador.crossValidateModel(  conocimiento, casosEntrenamiento, 10, new Random(1));
            System.out.println(String.format("ErrorCuadraticoMedio=%6.2f unidades",evaluador.rootMeanSquaredError()));
            */
            
            //RESOLUCION DE DOS INCOGNITAS  ANGULO Y VELOCIDAD (la  funcion velocidad ya se aprendió)
            float vel_Oraculo=5f;            //El oraculo quiere saber si predice bien Vo= 8.5 m/s. Calcula la distancia que deberia recorrer
            System.out.println("Velocidad a predecir  = "+vel_Oraculo+" m/s           (para luego buscar su angulo)");
            float distancia=    vel_Oraculo * vel_Oraculo*(float) Math.sin( 2f *  _45gradosRad )/10f;         //con un angulo "cualquiera" =45º
            System.out.println("Distancia correspondiente= "+distancia+ " m          (calculada por formula)\n");
            
            //El NPC no conoce el angulo (hace un FOR).  ejecuta  arbol aprendido:   velocidad =  f ( angulo, distancia )
            Vector<Float> velocidades = new Vector<Float>();
            Vector<Float> angulos = new Vector<Float>();
            System.out.println("Prediciendo Vo = f (distancia, angulo)    (variando el angulo)");
            for (float angulo=angulo_inicial; angulo< angulo_final; angulo += deltaAngulo){  //
                Instance casoAdecidir = new Instance(casosEntrenamiento.numAttributes());
                casoAdecidir.setDataset(casosEntrenamiento);
                casoAdecidir.setValue(0,  angulo);
                casoAdecidir.setValue(2,  distancia);  //alcanzara disntancia de 2.5 deberia dar v=5
                float  prediccion = (float) conocimiento.classifyInstance(casoAdecidir);
                angulos.add(angulo);
                velocidades.add(new Float( prediccion));
                System.out.println(String.format("Angulo= %3.2f    Vo = %3.2f  error= %3.2f",angulo*45f/_45gradosRad,prediccion,(prediccion-vel_Oraculo) ) );
            }
            
            float mediana= mediana(velocidades);
            
            float anguloInferidoRads=0;
            for (int i=0; i<angulos.size(); i++) {
                if ( (velocidades.elementAt(i).floatValue()>= (mediana*0.999f)) &&   (velocidades.elementAt(i).floatValue()<= (mediana *1.001f)))
                    anguloInferidoRads=angulos.elementAt(i).floatValue();
            }
            
            
            float distanciaFinalAlcanzada=    mediana*mediana*(float) Math.sin( 2f * anguloInferidoRads )/10f;         //con un angulo "cualquiera" =45º
            System.out.println(String.format("\nLanzamiento con V0=%3.3f y angulo=%3.3f y...", mediana, anguloInferidoRads*180/3.1416f));
            System.out.println(String.format("Distancia alcanzada = %3.3f   (error distancia= %3.3f)",distanciaFinalAlcanzada,(distancia-distanciaFinalAlcanzada)));
            
        } catch(Exception e){       System.out.println("error");  }
    }
}