package neuralnetworks;

/**
 *
 * @author gasedelnikov
 */
public interface NNObjectInterface {
 
    /**
     * @return Code of Object 
     */
    public String getCode();
    
    /**
     * @return Name of Object 
     */    
    public String getName();
    
    /**
     * @return Input data (Correct) for NeuralNetworks 
     */      
    public double[] getCorrectData();

    /**
     * @return Index of Object for Teaching NeuralNetworks
     */     
    public int getIndex();
    
    /**
     * break Correct Data for Testing or Teaching NeuralNetworks
     * 
     * @param invertPixelCount
     */
    public void breakData(int invertPixelCount);   
    
    /**
     * @return Input data (broken) for NeuralNetworks 
     */    
    public double[] getData();    
}
