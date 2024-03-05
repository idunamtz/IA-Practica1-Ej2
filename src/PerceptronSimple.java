import java.util.Random;

public class PerceptronSimple {
    private double[] pesos;
    private double tasaAprendizaje;

    public PerceptronSimple(int numEntradas, double tasaAprendizaje) {
        // Inicializar los pesos aleatoriamente
        Random rnd = new Random();
        pesos = new double[numEntradas];
        for (int i = 0; i < numEntradas; i++) {
            pesos[i] = rnd.nextDouble() * 2 - 1; // pesos entre -1 y 1
        }

        this.tasaAprendizaje = tasaAprendizaje;
    }

    // Función de activación (sigmoide)
    private double funcionActivacion(double suma) {
        return 1 / (1 + Math.exp(-suma));
    }

    // Derivada de la función de activación (sigmoide)
    private double derivadaFuncionActivacion(double salida) {
        return salida * (1 - salida);
    }

    // Entrenamiento del perceptrón
    public void entrenar(double[][] entradas, int[] salidasDeseadas, int maxIteraciones) {
        int numEntradas = pesos.length;
        int numPatrones = entradas.length;

        for (int iteracion = 0; iteracion < maxIteraciones; iteracion++) {
            for (int p = 0; p < numPatrones; p++) {
                // Calcular la salida del perceptrón
                double suma = 0;
                for (int i = 0; i < numEntradas; i++) {
                    suma += pesos[i] * entradas[p][i];
                }
                double salidaCalculada = funcionActivacion(suma);

                // Calcular el error
                double error = salidasDeseadas[p] - salidaCalculada;

                // Actualizar los pesos
                for (int i = 0; i < numEntradas; i++) {
                    pesos[i] += tasaAprendizaje * error * derivadaFuncionActivacion(salidaCalculada) * entradas[p][i];
                }
            }
        }
    }

    // Clasificación de una nueva entrada
    public double clasificar(double[] entrada) {
        double suma = 0;
        for (int i = 0; i < pesos.length; i++) {
            suma += pesos[i] * entrada[i];
        }
        return funcionActivacion(suma);
    }
}