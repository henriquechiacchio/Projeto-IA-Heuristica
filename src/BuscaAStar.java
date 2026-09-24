import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;

public class BuscaAStar {
    public List<No> buscar(TipoHeuristica tipoHeuristica, No origem, No destino) {
    return executar(tipoHeuristica, origem, destino).getCaminho();
    }

    public ResultadoBuscaAStar executar(
        TipoHeuristica tipoHeuristica,
        No origem,
        No destino
    ) {
        validarEntrada(tipoHeuristica, origem, destino);

        Map<No, Double> custos = new HashMap<>();
        Map<No, No> predecessores = new HashMap<>();
        List<RegistroBuscaAStar> registros = new ArrayList<>();
        PriorityQueue<Estado> fronteira = new PriorityQueue<>(
                Comparator.comparingDouble(Estado::getCustoEstimado)
        );

        custos.put(origem, 0.0);
        fronteira.add(new Estado(origem, 0.0, heuristica(tipoHeuristica, origem, destino)));

        while (!fronteira.isEmpty()) {
            Estado estadoAtual = fronteira.poll();
            No atual = estadoAtual.getNo();

            if (estadoAtual.getCustoAcumulado() > custos.getOrDefault(atual, Double.POSITIVE_INFINITY)) {
                continue;
            }

            if (atual == destino) {
                return new ResultadoBuscaAStar(
                        reconstruirCaminho(predecessores, destino),
                        registros
                );
            }

            for (Aresta aresta : atual.getArestas()) {
                No vizinho = aresta.getDestino();
                double novoCusto = custos.get(atual) + aresta.getDistancia();
                double estimativa = heuristica(tipoHeuristica, vizinho, destino);
                List<No> caminhoTentativa = reconstruirCaminho(predecessores, atual);
                caminhoTentativa.add(vizinho);
                registros.add(new RegistroBuscaAStar(
                        caminhoTentativa,
                        novoCusto,
                        estimativa,
                        novoCusto + estimativa
                ));

                if (novoCusto < custos.getOrDefault(vizinho, Double.POSITIVE_INFINITY)) {
                    custos.put(vizinho, novoCusto);
                    predecessores.put(vizinho, atual);

                    fronteira.add(new Estado(vizinho, novoCusto, estimativa));
                }
            }
        }

        return new ResultadoBuscaAStar(Collections.emptyList(), registros);
    }

    public double calcularCusto(List<No> caminho) {
        if (caminho == null || caminho.size() < 2) {
            return 0.0;
        }

        double custoTotal = 0.0;

        for (int indice = 0; indice < caminho.size() - 1; indice++) {
            No atual = caminho.get(indice);
            No proximo = caminho.get(indice + 1);
            custoTotal += custoDaAresta(atual, proximo);
        }

        return custoTotal;
    }

    private double heuristica(TipoHeuristica tipoHeuristica, No atual, No destino) {
        return Heuristica.calcular(
                tipoHeuristica,
                atual.getX(),
                atual.getY(),
                destino.getX(),
                destino.getY()
        );
    }

    private List<No> reconstruirCaminho(Map<No, No> predecessores, No destino) {
        LinkedList<No> caminho = new LinkedList<>();
        No atual = destino;

        while (atual != null) {
            caminho.addFirst(atual);
            atual = predecessores.get(atual);
        }

        return caminho;
    }

    private int custoDaAresta(No origem, No destino) {
        for (Aresta aresta : origem.getArestas()) {
            if (aresta.getDestino() == destino) {
                return aresta.getDistancia();
            }
        }

        throw new IllegalArgumentException(
                "Não existe aresta entre " + origem.getNome() + " e " + destino.getNome()
        );
    }

    private void validarEntrada(TipoHeuristica tipoHeuristica, No origem, No destino) {
        if (tipoHeuristica == null) {
            throw new IllegalArgumentException("O tipo de heurística não pode ser nulo");
        }

        if (origem == null || destino == null) {
            throw new IllegalArgumentException("Origem e destino não podem ser nulos");
        }
    }

    private static class Estado {
        private final No no;
        private final double custoAcumulado;
        private final double custoEstimado;

        private Estado(No no, double custoAcumulado, double heuristica) {
            this.no = no;
            this.custoAcumulado = custoAcumulado;
            this.custoEstimado = custoAcumulado + heuristica;
        }

        private No getNo() {
            return no;
        }

        private double getCustoAcumulado() {
            return custoAcumulado;
        }

        private double getCustoEstimado() {
            return custoEstimado;
        }
    }
}