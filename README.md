Mapa e Estrutura

Projeto Java/Swing sem dependências externas.

## Como executar

No terminal, na raiz do projeto:

```bash
javac -d bin src/*.java
java -cp bin Main
```

A janela do mapa será aberta.

Clique em qualquer ponto A até U. O nome do ponto selecionado será mostrado no terminal e o ponto ficará destacado.

## Classes

Os arquivos-fonte ficam em `src/` e os arquivos compilados são gerados em `bin/`.

- `No.java`: representa cada ponto do mapa e guarda suas coordenadas e arestas de saída.
- `Aresta.java`: representa uma conexão direcionada e seu custo.
- `Grafo.java`: cadastra os 21 pontos e as conexões do mapa.
- `MapaPanel.java`: desenha o mapa e trata o clique nos pontos.
- `Main.java`: inicia a aplicação.
- `BuscaAStar.java`: encontra um caminho usando o algoritmo A*.
- `ResultadoBuscaAStar.java`: agrupa o caminho final e o histórico da busca.
- `RegistroBuscaAStar.java`: representa cada caminho candidato analisado.

## Busca A*

A busca recebe o tipo de heurística, o nó de origem e o nó de destino:

```java
No inicio = grafo.buscarNo("A");
No fim = grafo.buscarNo("U");

BuscaAStar busca = new BuscaAStar();
List<No> caminho = busca.buscar(
        TipoHeuristica.MANHATTAN,
        inicio,
        fim
);

double custo = busca.calcularCusto(caminho);
```

O resultado é uma lista ordenada de nós, começando na origem e terminando no destino. Se não houver caminho, o resultado será uma lista vazia.

Durante a busca, cada estado armazena:

- `g(n)`: custo real acumulado desde a origem;
- `h(n)`: estimativa calculada pela heurística escolhida;
- `f(n) = g(n) + h(n)`: prioridade do estado na fronteira.

Quando um vizinho é encontrado com custo menor, a busca atualiza seu custo e seu predecessor. Ao alcançar o destino, os predecessores são percorridos de trás para frente para reconstruir o caminho.

As arestas do grafo são direcionadas, portanto a busca respeita exatamente as setas cadastradas. Para garantir que A* sempre retorne o caminho de menor custo, a heurística deve ser admissível, isto é, não pode superestimar o custo restante. Como os custos deste mapa nem sempre correspondem à distância geométrica entre as coordenadas, a heurística escolhida pode produzir caminhos diferentes.

Para acessar os vizinhos diretamente:

```java
for (Aresta aresta : inicio.getArestas()) {
    No vizinho = aresta.getDestino();
    int custo = aresta.getDistancia();
}
```

As coordenadas ficam disponíveis em:

```java
no.getX();
no.getY();
```

Essas coordenadas podem ser usadas posteriormente pelas heurísticas.

## Uso pela interface

Na janela do mapa:

1. Escolha a heurística no seletor.
2. Clique no nó de origem.
3. Clique no nó de destino.

O mapa destaca a origem em verde, o destino em laranja e o caminho escolhido em verde. A saída de texto apresenta o caminho com o custo acumulado em cada nó, por exemplo:

```text
A(0) -> B(1) -> C(4) -> D(5) -> E(7)
```

Além do caminho selecionado, a saída lista todos os caminhos candidatos examinados. Cada registro mostra `g`, o custo acumulado até o último nó, `h`, a estimativa da heurística, e `f = g + h`, o valor usado para ordenar a fronteira.

O método `executar` disponibiliza esses dados para outras interfaces:

```java
ResultadoBuscaAStar resultado = busca.executar(
    TipoHeuristica.MANHATTAN,
    inicio,
    fim
);

List<RegistroBuscaAStar> tentativas = resultado.getRegistros();
```

\n## Observação sobre as direções\n\nAs conexões foram cadastradas respeitando as setas da Figura 1. Quando uma estrada é bidirecional, são criadas duas arestas, uma em cada sentido. A interface também desenha uma pequena seta preta indicando a direção de cada aresta.\n
