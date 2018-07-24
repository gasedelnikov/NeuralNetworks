package neuralnetworks.NeuralNetworksSelfInvented;

import neuralnetworks.Data.LettersArray;
import neuralnetworks.Data.Letter;

/**
 *
 * @author g_sedelnikov
 */
public class NeuralNetworksSelfInvented {
    public static final int lettersCount = 29;
    public static final int lettersMixing  = 2 * lettersCount;
    
    public static final int pixelsXSize  = 7;
    public static final int pixelsYSize  = 8;
    public static final int pixelsCount  = NeuralNetworksSelfInvented.pixelsXSize * NeuralNetworksSelfInvented.pixelsYSize;
    public static final int maxCycles     = 100;
    public static final Letter[] lettersModel =  LettersArray.getLetters(lettersCount);
    public static final double startW_RandomCoef = 0.01;

    public static void start(){
        Letter[] letters =  LettersArray.getLetters(lettersCount);//NeuralNetworks.lettersCount = letters.length;
        double[][] w = getStartW();

        int cycleIndex = 0;
        int errCount;
        do {
            getMixingLetters(letters, NeuralNetworksSelfInvented.lettersMixing);
            errCount = getCycle(w, letters);
            System.out.println("  Cycle = " + cycleIndex + " errCount = " + errCount);
            cycleIndex++;
        }
        while((errCount > 0) && (cycleIndex <  NeuralNetworksSelfInvented.maxCycles));

      
//        System.out.println("Тест");
//        letters =  Data.getBrokenLetters(lettersCount);//NeuralNetworks.lettersCount = letters.length;
//        cycleIndex = 0;
//        errCount = 1;
//        do {
//            getMixingLetters(letters, NeuralNetworks.lettersMixing);
//            errCount = getCycle(w, letters);
//            System.out.println("  Cycle = " + cycleIndex + " errCount = " + errCount);
//            cycleIndex++;
//        }
//        while((errCount > 0) && (cycleIndex <  NeuralNetworks.maxCycles));        
        
        errCount = getCycle(w, LettersArray.getBrokenLetters(lettersCount));       
        System.out.println("Тест; Ошибок = " + errCount + " ("+  String.format("%.1f", 100-100.0*errCount/lettersCount) + "%)" );
//        getTest(w, Data.getLetters(lettersCount));       
    }    
    
    private static int getCycle(double[][] w, Letter[] letters){
        int iter = 0;
        int errCount = 0;
        for (Letter letter: letters){
            double[] s = new double[lettersCount];
            double[] data = letter.getData();
            int index = getS(w, s, data);
            resizeW(w, data, letter, index);
            printCycleRes(++iter, w, s, letter, index);
            if (index != letter.getIndex()){
                errCount ++;
            }
        }  
        return errCount;
    }
    
    private static double[][] getStartW(){
        double[][] w = new double[lettersCount][pixelsCount];
        if (NeuralNetworksSelfInvented.startW_RandomCoef != 0){
            for (int i=0; i < lettersCount; i++){
                for (int j=0; j < pixelsCount; j++){
                    w[i][j] = NeuralNetworksSelfInvented.startW_RandomCoef * Math.random();
                }
            } 
        }
        return w;
    }
    
    private static int getS(double[][] w, double[] s, double[] data){
        for (int i=0; i < lettersCount; i++){
            for (int j=0; j < pixelsCount; j++){
                s[i] += w[i][j] * data[j];
            }
        }

        double max = Double.NEGATIVE_INFINITY;
        int index = -1;
        for (int i=0; i < lettersCount; i++){
            if (s[i] > max){
                max = s[i];
                index = i;
            }
        }
        return index;
    }
    
    private static void resizeW(double[][] w, double[] data, Letter letter, int index){
//        int inc = (index == letter.index)?+1:-1;
        if (index != letter.getIndex()){
            for (int j=0; j < pixelsCount; j++){
                w[index][j]             -= data[j] ;
                w[letter.getIndex()][j] += data[j] ;
//                    if (w[index][j] < -5){
//                        w[index][j] = -5;
//                    }
//                    if (w[index][j] > 5){
//                        w[index][j] = 5;
//                    }                
            }
        }    
    }
    
    private static void printCycleRes(int iter, double[][] w, double[] s, Letter letter, int index){
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
    
    private static void printLetters(double[][] w){
        for (double[] w1 : w) {
            System.out.println();
            printLetter(NeuralNetworksSelfInvented.pixelsXSize, w1);
        }             
    }

    private static void printLetter(int indexSep, double[] l){
        for (int j=0; j < l.length; j++){
            if ((j!=0) && (j / indexSep) == (1d * j / indexSep)){
                System.out.println();
            }
            System.out.print(" " + ((l[j] >= 0)?" ":"") + String.format("%.3f", l[j]));
        }
        System.out.println();
    }    

    private static void getMixingLetters(Letter[] data, int mixing){
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
