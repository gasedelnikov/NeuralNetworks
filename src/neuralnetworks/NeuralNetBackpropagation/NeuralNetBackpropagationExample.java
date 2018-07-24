package neuralnetworks.NeuralNetBackpropagation;

import neuralnetworks.Data.LettersArray;
import neuralnetworks.NNObjectInterface;
import neuralnetworks.NeuralNetworksInterface;
import neuralnetworks.utils.Functions;

/**
 *
 * @author g_sedelnikov
 */
public class NeuralNetBackpropagationExample {
     
    public static void start(){
        if  (1 == 2) {
            startTriplex();
        }
        else {
            startLettersExample();
        }            
    }
    
    /**
     * Пример работы нейронной сети с тремя входными нейронами, двумя скрытыми и одним выходным. Обучается и проверяется на двух состояниях.
     */
    public static void startTriplex(){
        int outputCount                 = 1;
        int inputCount                  = 3;
        int hiddenCount                 = 2;
        double startWeightsRandomCoef   = 0.1;
        int iterationCount              = 10000;         
       
        NeuralNetworksInterface nnb = new NeuralNetBackpropagation(inputCount, hiddenCount, outputCount, startWeightsRandomCoef);        
        double[] s0, result;
        for (int i = 0; i < iterationCount; i++){     
            s0 = new double[]{1d, 1d, 0d};         
            nnb.teach(s0, new double[]{0});
           
            s0 = new double[]{1, 0, 1}; 
            nnb.teach(s0, new double[]{1});          
        }        
        
        s0 = new double[]{1, 0, 1};         
        result = nnb.getPrediction(s0);
        System.out.print(String.format("%.3f",result[0]) + "(1); ");             

        s0 = new double[]{1d, 1d, 0d};   
        result = nnb.teach(s0, new double[]{1});          
        System.out.println(String.format("%.3f", result[0])+ "(0); ");                
    }    
    
    /**
     * Пример работы нейронной сети по распознаванию букв. Буква представляется набором пикселей (1 – наличие пикселя, 0 - отсутствие). 
     * Входной сигнал – одномерный массив размера X*Y, гда X и Y количество пикселей по горизонтали и вертикали соответственно.
     * Сеть обучается на заранее подготовленных корректных и битых «состояниях» (буквах). 
     * По мимо проверки обучения на обучаемой выборке, проводится проверка на «состояниях» с «инвертированными» пикселями.
     */
    public static void startLettersExample(){
        int outputCount                 = 29;
        NNObjectInterface[] letters     = LettersArray.getLettersAndBrokenLetters(outputCount);
        int inputCount                  = letters[0].getData().length;
        int hiddenCount                 = 30;
        double startWeightsRandomCoef   = 0.1;
        int mixingCount                 = 100;    
        int iterationCount              = 1000;         
       
        NeuralNetworksInterface nnb = new NeuralNetBackpropagation(inputCount, hiddenCount, outputCount, startWeightsRandomCoef);
        
        for (int i=0; i< iterationCount; i++){
            Functions.mixUpArray(letters, mixingCount);
            for (NNObjectInterface letter : letters) {
                double[] expectedData = new double[outputCount];
                expectedData[letter.getIndex()] = 1.0d; 
                
                nnb.teach(letter.getData(), expectedData);
            }
        } 

//************************     
        letters = LettersArray.getLetters(outputCount);
        Functions.mixUpArray(letters, mixingCount);
        check(nnb, letters, "Correct letters test");
        
//************************        
        letters  = LettersArray.getBrokenLetters(outputCount);    
        Functions.mixUpArray(letters, mixingCount);        
        check(nnb, letters, "Prepared broken letters test");    
        
//************************        
        int invertPixelCount = 5;
        for (NNObjectInterface letter: letters){
            letter.breakData(invertPixelCount);
        }        
        check(nnb, letters, "Broken letters test; invertPixel = " + String.format("%.2f", 100.0d*invertPixelCount/letters.length)+"%");            
       
//************************        
        invertPixelCount = 10;
        letters = LettersArray.getLetters(outputCount);
        Functions.mixUpArray(letters, mixingCount);          
        for (NNObjectInterface letter: letters){
            letter.breakData(invertPixelCount);
        }        
        check(nnb, letters, "Broken letters test; invertPixel = " + String.format("%.2f", 100.0d*invertPixelCount/letters.length)+"%");           
//        printLetter(7, letter);
     
}    
    
    public static void printLetter(int indexSep, NNObjectInterface letter){
        for (int i=0; i < letter.getData().length; i++){
            if ((i!=0) && (i / indexSep) == (1d * i / indexSep)){
                System.out.println("|");
            }
            String text = (letter.getData()[i] == letter.getCorrectData()[i])?((letter.getData()[i] < 1)?" ":"1"):"0";
            System.out.print(text);
        }
        System.out.println("|");
        System.out.println();        
    }      
    
    public static void check(NeuralNetworksInterface nnb, NNObjectInterface[] letters, String testName){    
        int outputCount = letters.length;        
        int cntSucsses = 0;
        int cntErr = 0;        
        double deltaSucsses = 0.0d;
        double maxErrValue = 0.0d;
        double minSucssesValue = 1.0d;        
        int cntSucssesMax = 0;
        int cntErrMax = 0;          
        for (int i=0; i < outputCount; i++){  
            NNObjectInterface letter = letters[i]; 
            double[] result = nnb.getPrediction(letter.getData());
            double maxValue = Double.NEGATIVE_INFINITY;
            int curIndex = 0;            
            for (int j=0; j< result.length; j++){
                if (j == letter.getIndex()){
                    minSucssesValue = Math.min(minSucssesValue, result[j]); 
                    if (result[j] > 0.5d){
                        cntSucsses++;
                        deltaSucsses += 1 - result[j];
                    }
                    else {
                        cntErr++;
                        deltaSucsses += 1 - result[j];                        
                    }
                }
                else{
                    maxErrValue = Math.max(maxErrValue, result[j]);
                    if (result[j] > 0.5d){
                        cntErr++;  
                    }                    
                }
                
                if (result[j] > maxValue){
                    curIndex = j;
                    maxValue = result[j];
                }                
            }

            if (curIndex == letter.getIndex()){
               cntSucssesMax ++;
            }
            else{
               cntErrMax++;                   
            }
        }
        System.out.println(testName); 
        System.out.println("  cntSucssesMax = "+cntSucssesMax+"; cntErr = "+cntErrMax);
        System.out.println("  cntSucsses = "+cntSucsses+"; cntErr = "+cntErr);
        System.out.println("  deltaSucsses = "+String.format("%.3f",deltaSucsses/outputCount)+"; maxErrValue = "+String.format("%.3f",maxErrValue) + "; minSucssesValue = " +String.format("%.3f",minSucssesValue) );
        System.out.println();
    }     
    
    public static void printResult(NeuralNetworksInterface nnb, NNObjectInterface[] letters){
        int outputCount = letters.length;
        for (int i=0; i < outputCount; i++){
            double[] result = nnb.getPrediction(letters[i].getData());  
            System.out.print(letters[i].getCode() + ": ");               
            for (int j=0; j < outputCount; j++){
              System.out.print(String.format(" %.3f", result[j]));                
            } 
            System.out.println(" Index = " + letters[i].getIndex());            
        }            
    }
}
