package neuralnetworks.XXX;

import neuralnetworks.Data.LettersArray;
import neuralnetworks.Data.Letter;

/**
 *
 * @author g_sedelnikov
 */
public class NeuralNetworks02 {
    public static final int lettersCount = 29;
    public static final int weightsCount = lettersCount;
    public static final int lettersMixing  = 0 * lettersCount;
    
    public static final int pixelsXSize  = 7;
    public static final int pixelsYSize  = 8;
    public static final int pixelsCount  = NeuralNetworks02.pixelsXSize * NeuralNetworks02.pixelsYSize;
    public static final int maxCycles     = 2;
    public static final Letter[] lettersModel =  LettersArray.getLetters(lettersCount);

    public static final double startW_RandomCoef = 0.01d; 
    
    public static final double learning_rate = 0.5d;

    public static final int inputCount      = pixelsCount;
    public static final int hiddenCount     = 30;
    public static final int outputCount     = 29;        
    
//    public static final int inputCount      = 3;
//    public static final int hiddenCount     = 2;
//    public static final int outputCount     = 1;//lettersCount; 

    public static final double[][] weights_lev_1 = new double[hiddenCount][inputCount];
    public static final double[][] weights_lev_2 = new double[outputCount][hiddenCount];
    
    public static final double[] s1 = new double[hiddenCount];
    public static final double[] s2 = new double[outputCount];    
        
    public static void start(){
        Letter[] letters =  LettersArray.getLetters(lettersCount);//NeuralNetworks.lettersCount = letters.length;
        funcLocal.getStartWeights(weights_lev_1, startW_RandomCoef);
        funcLocal.getStartWeights(weights_lev_2, startW_RandomCoef);        
     
        double[] inData;

        for (int i=0; i< 1000; i++){
            for (int j=0; j<outputCount; j++){
                double[] expectedData = new double[outputCount]; 
                expectedData[j] = 1.0d;                
                inData = letters[j].getData();   
                forward(inData);       
                backward(inData, s2, expectedData); 
                
                System.out.print(letters[j].getCode());
                for (int z=0; z<outputCount; z++){
                    System.out.print("  " + String.format("%.3f", s2[z]));    
                }
                System.out.println();
            }
            
            System.out.println();
        }    
        
        
    }    
    
    public static void startXXX(){
        funcLocal.getStartWeights(weights_lev_1, startW_RandomCoef);
        funcLocal.getStartWeights(weights_lev_2, startW_RandomCoef);
 
        double[] s0;
        for (int i = 0; i < 10000; i++){     
            System.out.print(" " + i);            
            
            s0 = new double[]{1d, 1d, 0d};//new double[hiddenCount];
            forward(s0);
            backward(s0, s2, new double[]{0, 1});
            System.out.print("(-) " + s2[0]);   // + "  " + s2[1]
           
            s0 = new double[]{1, 0, 1};//new double[hiddenCount];
            forward(s0); backward(s0, s2, new double[]{1, 0});            
            System.out.print("  (+) " + s2[0] );  // + "  " + s2[1]    
            
            System.out.println();
        }
     
    }    
    
    private static void forward(double[] data){
        for (int i=0; i < hiddenCount; i++){
            s1[i] = 0.0d;
            for (int j=0; j < inputCount; j++){
                s1[i] += weights_lev_1[i][j] * data[j];
            }
            s1[i] = funcLocal.sigmoid(s1[i]);
        }

        for (int i=0; i < outputCount; i++){
            s2[i] = 0.0d;
            for (int j=0; j < hiddenCount; j++){
                s2[i] += weights_lev_2[i][j] * s1[j];
            }
            s2[i] = funcLocal.sigmoid(s2[i]);
        }
    }
    
    private static void backward(double[] inData, double[] actual, double[] expected){
        double[] weights_delta = new double[outputCount];
        for (int i=0; i < outputCount; i++){
            double error = actual[i] - expected[i];
            weights_delta[i] = error * actual[i] * (1.0d - actual[i]);
            
            for (int j=0; j < hiddenCount; j++){
                weights_lev_2[i][j] = weights_lev_2[i][j] - s1[j] * weights_delta[i] * learning_rate;
            }            
        }        
    
        for (int i=0; i < hiddenCount; i++){
            double error = 0;
            for (int j=0; j < outputCount; j++){
                error += weights_delta[j] * weights_lev_2[j][i]; 
            } 
            double weights_delta_1 = error * s1[i] * (1.0d - s1[i]);
            
            for (int j=0; j < inputCount; j++){
                weights_lev_1[i][j] = weights_lev_1[i][j] - inData[j] * weights_delta_1 * learning_rate;
            }             
        }        
    }
}
