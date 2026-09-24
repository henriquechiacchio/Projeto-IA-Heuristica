Mapa e Estrutura

Projeto Java/Swing sem dependências externas.

## Como executar

No terminal, dentro da pasta `src`:

```bash
javac *.java
java Main
```

A janela do mapa será aberta.

Clique em qualquer ponto A até U. O nome do ponto selecionado será mostrado no terminal e o ponto ficará destacado.

## Classes

- `No.java`: representa cada ponto do mapa e guarda suas coordenadas e arestas de saída.
- `Aresta.java`: representa uma conexão direcionada e seu custo.
- `Grafo.java`: cadastra os 21 pontos e as conexões do mapa.
- `MapaPanel.java`: desenha o mapa e trata o clique nos pontos.
- `Main.java`: inicia a aplicação.

## Integração

O A* poderá receber um `Grafo` e usar:

```java
No inicio = grafo.buscarNo("A");

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

\n## Observação sobre as direções\n\nAs conexões foram cadastradas respeitando as setas da Figura 1. Quando uma estrada é bidirecional, são criadas duas arestas, uma em cada sentido. A interface também desenha uma pequena seta preta indicando a direção de cada aresta.\n
