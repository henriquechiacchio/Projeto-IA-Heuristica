public class Heuristica {

    //Calcula a distância heurística h(n) entre o nó atual e o destino.
    public static double calcular(TipoHeuristica tipo, double x1, double y1, double x2, double y2) {
        if (tipo == null) return 0.0;
        
        switch (tipo) {
            case MANHATTAN:
                return manhattan(x1, y1, x2, y2);
            case EUCLIDIANA:
                return euclidiana(x1, y1, x2, y2);
            case CHEBYSHEV:
                return chebyshev(x1, y1, x2, y2);
            default:
                return 0.0;
        }
    }

    //Distância de Manhattan: |x1 - x2| + |y1 - y2|
    public static double manhattan(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    //Distância Euclidiana: sqrt((x1 - x2)^2 + (y1 - y2)^2)
    public static double euclidiana(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //Distância de Chebyshev: max(|x1 - x2|, |y1 - y2|)
    public static double chebyshev(double x1, double y1, double x2, double y2) {
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
}