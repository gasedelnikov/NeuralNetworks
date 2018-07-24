package neuralnetworks.NeuralNetBackpropagation;

import neuralnetworks.NeuralNetworksInterface;
import neuralnetworks.utils.Functions;

/**
 * Нейронная сеть с обучением с помощью алгоритма обратного распространения (backpropagation) с одним "скрытым" слоем
 * 
 * @author g_sedelnikov
 */
public class NeuralNetBackpropagation implements NeuralNetworksInterface{
    private static final double startWeightsRandomCoef = 0.1d; // коэф. стартовой генерации весовых коэффициентов
    
    private final int inputCount;           // Количество "нейронов" входного слоя
    private final int hiddenCount;          // Количество "нейронов" скрытого слоя
    private final int outputCount;          // Количество "нейронов" выходного слоя     
    
    private int iteratinosOfTeachingCount = 0;   // Количество проведенных итераций обучения
    private double learning_rate = 0.5d;    // the ratio (percentage) influences the speed and quality of learning
    
    private final double[][] weights_lev_1; // Массив весовых коэффициентов для входного слоя
    private final double[][] weights_lev_2; // Массив весовых коэффициентов для скрытого слоя
    private final double[] signal_lev1;     // Массив уровней сигнала срытого слоя
    private final double[] signal_lev2;     // Массив уровней сигнала выходного слоя  

    /**
     * Конструктор класса NeuralNetBackpropagation
     *
     * @param inputCount Количество "нейронов" входного слоя
     * @param hiddenCount Количество "нейронов" скрытого слоя
     * @param outputCount Количество "нейронов" выходного слоя
     */
    public NeuralNetBackpropagation(int inputCount, int hiddenCount, int outputCount){
        this(inputCount, hiddenCount, outputCount, startWeightsRandomCoef);
    }    

    /**
     * Конструктор класса NeuralNetBackpropagation
     *
     * @param inputCount Количество "нейронов" входного слоя
     * @param hiddenCount Количество "нейронов" скрытого слоя
     * @param outputCount Количество "нейронов" выходного слоя
     * @param startWeightsRandomCoef (startWeightsRandomCoef / 2) - Модуль максимального значения при стартовой генерации весовых коэффициентов
     */
    public NeuralNetBackpropagation(int inputCount, int hiddenCount, int outputCount, double startWeightsRandomCoef){
        this.inputCount  = inputCount;
        this.hiddenCount = hiddenCount;
        this.outputCount = outputCount;
        
        this.weights_lev_1 = Functions.getRandomArray(hiddenCount, inputCount, startWeightsRandomCoef);
        this.weights_lev_2 = Functions.getRandomArray(outputCount, hiddenCount, startWeightsRandomCoef); 

        this.signal_lev1 = new double[hiddenCount];
        this.signal_lev2 = new double[outputCount];
    }
    
    /**
     * Конструктор класса NeuralNetBackpropagation
     *
     * @param inputCount Количество "нейронов" входного слоя
     * @param hiddenCount Количество "нейронов" скрытого слоя
     * @param outputCount Количество "нейронов" выходного слоя
     * @param weights_lev_1 Массив весовых коэффициентов для входного слоя
     * @param weights_lev_2 Массив весовых коэффициентов для скрытого слоя
     */
    public NeuralNetBackpropagation(int inputCount, int hiddenCount, int outputCount, double[][] weights_lev_1, double[][] weights_lev_2){
        checkStartWeightsLengts(hiddenCount,  inputCount, weights_lev_1, "weights_lev_1");
        checkStartWeightsLengts(outputCount, hiddenCount, weights_lev_2, "weights_lev_2");        
        
        this.inputCount  = inputCount;
        this.hiddenCount = hiddenCount;
        this.outputCount = outputCount;
        
        this.weights_lev_1 = weights_lev_1;
        this.weights_lev_2 = weights_lev_2;
    
        this.signal_lev1 = new double[hiddenCount];
        this.signal_lev2 = new double[outputCount];  
    }    

    @Override
    public void setLearning_rate(double learning_rate) {
        this.learning_rate = learning_rate;
    }

    @Override
    public double getLearning_rate() {
        return this.learning_rate;
    }
    
    @Override
    public int getIteratinosOfTeachingCount() {
        return this.iteratinosOfTeachingCount;
    }
    
    @Override
    public double[] getPrediction(double[] inData){
        if (inData.length != inputCount) throwInArrayErr("inData.length must be equal to inputCount");        
        for (int i=0; i < hiddenCount; i++){
            signal_lev1[i] = 0.0d;
            for (int j=0; j < inputCount; j++){
                signal_lev1[i] += weights_lev_1[i][j] * inData[j];
            }
            signal_lev1[i] = sigmoid(signal_lev1[i]);
        }

        for (int i=0; i < outputCount; i++){
            signal_lev2[i] = 0.0d;
            for (int j=0; j < hiddenCount; j++){
                signal_lev2[i] += weights_lev_2[i][j] * signal_lev1[j];
            }
            signal_lev2[i] = sigmoid(signal_lev2[i]);
        }
        return signal_lev2;
    }
    
    @Override
    public double[] teach(double[] inData, double[] expected) {
        if (inData.length   != inputCount)  throwInArrayErr("inData.length must be equal to inputCount");
        if (expected.length != outputCount) throwInArrayErr("expected.length must be equal to outputCount");
        
        getPrediction(inData);
        
        iteratinosOfTeachingCount++;
        double[] weights_delta = new double[outputCount];
        for (int i=0; i < outputCount; i++){
            double error = signal_lev2[i] - expected[i];
            weights_delta[i] = error * sigmoidDif(signal_lev2[i]);
            
            for (int j=0; j < hiddenCount; j++){
                weights_lev_2[i][j] = weights_lev_2[i][j] - signal_lev1[j] * weights_delta[i] * learning_rate;
            }            
        }        
    
        for (int i=0; i < hiddenCount; i++){
            double error = 0;
            for (int j=0; j < outputCount; j++){
                error += weights_delta[j] * weights_lev_2[j][i]; 
            } 
            double weights_delta_1 = error * sigmoidDif(signal_lev1[i]);
            
            for (int j=0; j < inputCount; j++){
                weights_lev_1[i][j] = weights_lev_1[i][j] - inData[j] * weights_delta_1 * learning_rate;
            }             
        }
        return signal_lev2;        
    }
    
    
    /**
     * Функция активации - Сигмоидальная функция
     *
     * @param x - аргумент функции
     * @return значение функции активации 
     */            
    private double sigmoid(double x){
         return  1.0d / (1.0d + Math.exp(-x));            
    }
    
    /**
     * Производная Функции активации
     *
     * @param sx - значение функции активации (для ускорения вычислений)
     * @return значение функции активации 
     */      
    private double sigmoidDif(double sx){
         return  sx * (1.0d - sx);            
    }        
    
    /**
     * Метод проверки входных данных на предмет соответствия соответствующих размеров массивов. 
     * В случае ошибки будет сгенерирован RuntimeException 
     *
     * @param length1 - ожидаемый размер массива первой размерности
     * @param length2 - ожидаемый размер массива первой размерности  
     * @param inArray - для успешной работы ожидается массив [length1][length2]
     * @param arrayName - доплнительный теккст сообщения 

     * @return значение функции активации 
     */      
    private void checkStartWeightsLengts(int length1, int length2, double[][] inArray, String arrayName) {
        if (inArray.length == length1){
            for (int i=0; i < length1; i++){
                if (inArray[i].length == length2){
                    throwInArrayErr(arrayName + ": illegal second length for index = "+i+"!");
                }
            } 
        }
        else{
            throwInArrayErr(arrayName + ": illegal first length!");
        }
    }    
    
    private void throwInArrayErr(String text) throws   IllegalArgumentException {
         throw new IllegalArgumentException(text);
    }      

    
}
