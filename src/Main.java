
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public static void main(String[] args) {
        String archivoCSV = "src/spheres2d50.csv";
        String archivoNuevosPuntosCSV = "src/spheres2d70.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(archivoCSV));
        BufferedReader brNuevosPuntos = new BufferedReader(new FileReader(archivoNuevosPuntosCSV))) {

        double[][] dataset = leerDataset(br);
        int[] clases = obtenerClases(dataset);
        int numColumnas = dataset[0].length;

        // Métodos para particionar el dataset
        List<double[][]> particiones = new ArrayList<>();
        //particiones.add(particionarAleatoriamente(dataset, clases));
        //particiones.add(particionarEstratificadamente(dataset, clases));
        //particiones.add(particionarTemporalmente(dataset));
        particiones.addAll(particionarValidacionCruzada(dataset, clases));
        //particiones.add(particionarPorCaracteristicasSignificativas(dataset));

        // Entrenar el perceptrón para cada partición
        for (double[][] particion : particiones) {
        int numEntradas = numColumnas - 1;
        double[][] entradas = new double[particion.length][numEntradas];
        int[] salidasDeseadas = new int[particion.length];
        for (int i = 0; i < particion.length; i++) {
        for (int j = 0; j < numEntradas; j++) {
        entradas[i][j] = particion[i][j];
        }
        salidasDeseadas[i] = (int) particion[i][numEntradas];
        }
        PerceptronSimple perceptron = new PerceptronSimple(numEntradas, 0.1);
        perceptron.entrenar(entradas, salidasDeseadas, 1000);

        // Leer nuevos puntos
        String linea;
        while ((linea = brNuevosPuntos.readLine()) != null) {
        String[] nuevoPuntoStr = linea.split(",");
        double[] nuevoPunto = new double[numEntradas];
        for (int i = 0; i < numEntradas; i++) {
        nuevoPunto[i] = Double.parseDouble(nuevoPuntoStr[i]);
        }
        System.out.println("Clasificación del nuevo punto: " + perceptron.clasificar(nuevoPunto));
        }
        }
        } catch (IOException e) {
        e.printStackTrace();
        }
        }

// Métodos auxiliares para la lectura de datos y particionamiento
private static double[][] leerDataset(BufferedReader br) throws IOException {
        List<double[]> datos = new ArrayList<>();
        String linea;
        while ((linea = br.readLine()) != null) {
        String[] partes = linea.split(",");
        double[] fila = new double[partes.length];
        for (int i = 0; i < partes.length; i++) {
        fila[i] = Double.parseDouble(partes[i]);
        }
        datos.add(fila);
        }
        return datos.toArray(new double[0][]);
        }

private static int[] obtenerClases(double[][] dataset) {
        int[] clases = new int[dataset.length];
        for (int i = 0; i < dataset.length; i++) {
        clases[i] = (int) dataset[i][dataset[i].length - 1];
        }
        return clases;
        }

private static double[][] particionarAleatoriamente(double[][] dataset, int[] clases) {
        Random random = new Random();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < dataset.length; i++) {
        indices.add(i);
        }
        Collections.shuffle(indices, random);

        int numEntrenamiento = (int) (dataset.length * 0.8);
        double[][] particion = new double[numEntrenamiento][];
        for (int i = 0; i < numEntrenamiento; i++) {
        particion[i] = dataset[indices.get(i)];
        }
        return particion;
        }

private static double[][] particionarEstratificadamente(double[][] dataset, int[] clases) {
        int numEntrenamiento = (int) (dataset.length * 0.8);
        Map<Integer, List<Integer>> clasesIndices = new HashMap<>();
        for (int i = 0; i < dataset.length; i++) {
        int clase = clases[i];
        clasesIndices.putIfAbsent(clase, new ArrayList<>());
        clasesIndices.get(clase).add(i);
        }

        List<Integer> indices = new ArrayList<>();
        for (List<Integer> lista : clasesIndices.values()) {
        Collections.shuffle(lista);
        indices.addAll(lista.subList(0, numEntrenamiento / clasesIndices.size()));
        }

        double[][] particion = new double[numEntrenamiento][];
        for (int i = 0; i < numEntrenamiento; i++) {
        particion[i] = dataset[indices.get(i)];
        }
        return particion;
        }

private static double[][] particionarTemporalmente(double[][] dataset) {
        int numEntrenamiento = (int) (dataset.length * 0.8);
        return Arrays.copyOfRange(dataset, 0, numEntrenamiento);
        }

private static List<double[][]> particionarValidacionCruzada(double[][] dataset, int[] clases) {
        Random random = new Random();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < dataset.length; i++) {
        indices.add(i);
        }
        Collections.shuffle(indices, random);

        List<double[][]> particiones = new ArrayList<>();
        int k = 5; // Número de folds
        int foldSize = dataset.length / k;
        for (int i = 0; i < k; i++) {
        double[][] fold = new double[foldSize][];
        for (int j = 0; j < foldSize; j++) {
        fold[j] = dataset[indices.get(i * foldSize + j)];
        }
        particiones.add(fold);
        }
        return particiones;
        }

private static double[][] particionarPorCaracteristicasSignificativas(double[][] dataset) {
        // Implementación de partición por características significativas
        return null; // Aquí va la implementación
        }