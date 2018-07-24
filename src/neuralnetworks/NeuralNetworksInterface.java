package neuralnetworks;

/**
 *
 * @author g_sedelnikov
 */
public interface NeuralNetworksInterface {
    
    /**
     * Set learning rate - this ratio (percentage) influences the speed and quality of learning
     *
     * @param learning_rate
     */
    public void setLearning_rate(double learning_rate);

    /**
     * Get learning rate - this ratio (percentage) influences the speed and quality of learning
     * 
     * @return learning rate
     */
    public double getLearning_rate();
    
    /**
     * Получение количества проведенных итераций обучения.
     * @return
     */
    public int getIteratinosOfTeachingCount();
    
    /**
     * Метод получения предсказания 
     *
     * @param inData Массив (размер = кол-ву входных нейронов) значений [0, 1]
     * @return Массив (размер = кол-ву выходных нейронов) значений [0, 1]
     */
    public double[] getPrediction(double[] inData);
    
    /**
     *
     * @param inData массив (размер = кол-ву входных нейронов) значений [0, 1]
     * @param expected Массив (размер = кол-ву выходных нейронов) значений [0, 1]
     * @return Массив (размер = кол-ву выходных нейронов) значений [0, 1]
     */
    public double[] teach(double[] inData, double[] expected);    
    
}
