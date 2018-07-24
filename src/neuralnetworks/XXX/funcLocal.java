package neuralnetworks.XXX;

import neuralnetworks.Data.Letter;
import static neuralnetworks.XXX.NeuralNetworks02.lettersCount;
import static neuralnetworks.XXX.NeuralNetworks02.lettersModel;
import static neuralnetworks.XXX.NeuralNetworks02.pixelsCount;

/**
 *
 * @author g_sedelnikov
 */
public class funcLocal {
         
    public static double sigmoid(double x){
         return  1.0d / (1.0d + Math.exp(-x));            
    }    
    
    public static void getStartWeights(double[][] inArray, double randomCoef){
        if (randomCoef != 0){
            for (int i=0; i < inArray.length; i++){
                for (int j=0; j < inArray[i].length; j++){
                  inArray[i][j] = randomCoef * Math.random();
                }  
            } 
        }
    }    
    
    public static void printCycleRes(int iter, double[][] w, double[] s, Letter letter, int index){
        double summ = 0;
        for (int i=0; i < lettersCount; i++){
            for (int j=0; j < pixelsCount; j++){
                summ += Math.abs(w[i][j]);
            }
        }   
//        System.out.println();
        if (letter.getIndex() != index){
            String text = "    ";
//            String ww =  + iter + " " + ((index == letter.getIndex())?"+":"-");
//            System.out.print(ww + " index = " + index + "("+letter.getIndex()+"); summ =" + String.format("%.2f",summ)  + " ");
//            System.out.print(" s+ = " + String.format("%.3f", s[letter.getIndex()]) + " " + letter.getCode());
            text += letter.getCode() + "(" + lettersModel[index].getCode()+")";
            text += " s+ = " + String.format("%.3f", s[letter.getIndex()]);
            text += " s- = " + String.format("%.3f", s[index]);         
            System.out.println(text);
        }
//        for (int i=0; i < lettersCount; i++){
//            System.out.print(" s"+i+" = " + String.format("%.3f", s[i]));
//        }  
    }   
    
    
    public static void printLetters(double[][] w){
        for (double[] w1 : w) {
            System.out.println();
            printLetter(NeuralNetworks02.pixelsXSize, w1);
        }             
    }

    public static void printLetter(int indexSep, double[] l){
        for (int j=0; j < l.length; j++){
            if ((j!=0) && (j / indexSep) == (1d * j / indexSep)){
                System.out.println();
            }
            System.out.print(" " + ((l[j] >= 0)?" ":"") + String.format("%.3f", l[j]));
        }
        System.out.println();
    }    

    public static void getMixingLetters(Letter[] data, int mixing){
        for (int i=0; i < mixing; i++){
            int i1 = (int) Math.round(data.length * Math.random()) ;
            int i2 = (int) Math.round(data.length * Math.random()) ;
            if ((i1 >= 0) && (i2 >= 0) && (i1 < data.length) && (i2 < data.length) ){
                if (i1 == i2){
                    if ((i1 & 1) == 1){
                        i1 = 0;
                    } 
                    else{
                        i2 = data.length-1;
                    }
                }    
                Letter letter = data[i1];
                data[i1] = data[i2];
                data[i2] = letter;
            }

        } 
    }    
    
}
