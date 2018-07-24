package neuralnetworks.Data;

import neuralnetworks.NNObjectInterface;

/**
 *
 * @author g_sedelnikov
 */
public class Letter implements NNObjectInterface{
    private int index;
    private String code;
    private String name;
    private double[] data;
    private double[] correctData;    

    public Letter(int index, String code, String name, double[] data) {
        this.code = code;
        this.name = name;
        this.data = data;    
        this.index = index;
        this.correctData = new double[data.length];            
        System.arraycopy(data, 0, this.correctData, 0, data.length);
    }
    
    public Letter(Letter letter) {
        this.code = letter.code;
        this.name = letter.name;
        this.index = letter.index;
        this.data = new double[letter.getData().length];
        System.arraycopy(letter.getData()       , 0, this.data          , 0, letter.getData().length);
        System.arraycopy(letter.getCorrectData(), 0, this.correctData   , 0, letter.getCorrectData().length);        
    }    
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public double[] getCorrectData() {
        return correctData;
    }

    public void setCorrectData(double[] correctData) {
        this.correctData = correctData;
    }

    @Override
    public void breakData(int invertPixelCount){
        int realInvertPixel = 0;
        if (invertPixelCount >= data.length){
            invertPixelCount = data.length;
        }
        
        while (realInvertPixel < invertPixelCount){
            int pixelIndex = (int)(data.length * Math.random());
            if ((pixelIndex >= 0) && (pixelIndex < data.length) ){
                if ((data[pixelIndex] == 0.0d) || (data[pixelIndex] == 1.0d)){
                   realInvertPixel ++;
                   data[pixelIndex] += 0.1d;
                }
            }
        }
        for (int i=0; i < data.length; i++){
            if (data[i] == 0.1d){
                data[i] = 1.0d;
            }
            else {
                if (data[i] == 1.1d){
                    data[i] = 0.0d;
                }   
            }  
        }
    }      
}
