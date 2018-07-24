package neuralnetworks.utils;


/**
 *
 * @author g_sedelnikov
 */
public class Functions {
         
    public static double[][] getRandomArray(int length1, int length2, double randomCoef){
        double[][] result = new double[length1][length2];
        if (randomCoef != 0){
            for (int i=0; i < length1; i++){
                for (int j=0; j < length2; j++){
                  result[i][j] = randomCoef * (Math.random() - 0.5d);
                }  
            } 
        }
        return result;
    }     
    
    public static void mixUpArray(Object[] InData, int mixing) {
        for (int i=0; i < mixing; i++){
            int i1 = (int) Math.round(InData.length * Math.random()) ;
            int i2 = (int) Math.round(InData.length * Math.random()) ;
            if ((i1 >= 0) && (i2 >= 0) && (i1 < InData.length) && (i2 < InData.length) ){
                if (i1 == i2){
                    if ((i1 & 1) == 1){
                        i1 = 0;
                    } 
                    else{
                        i2 = InData.length-1;
                    }
                }    
                Object letter = InData[i1];
                InData[i1] = InData[i2];
                InData[i2] = letter;
            }
        } 
    }    
    
}
