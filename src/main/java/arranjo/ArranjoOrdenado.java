package arranjo;

public class ArranjoOrdenado {

    private int[] dados;
    private int tamanho;
    private int capacidade;
    private boolean crescente;

    public ArranjoOrdenado(int capacidade, boolean crescente) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser maior que zero.");
        }
        this.capacidade = capacidade;
        this.crescente = crescente;
        this.dados = new int[capacidade];
        this.tamanho = 0;
    }

    public void inserir(int valor) {
        if (tamanho == capacidade) {
            throw new IllegalStateException("Arranjo cheio. Capacidade: " + capacidade);
        }

        int posicao = encontrarPosicaoInsercao(valor);

        // Desloca elementos para abrir espaço
        for (int i = tamanho; i > posicao; i--) {
            dados[i] = dados[i - 1];
        }

        dados[posicao] = valor;
        tamanho++;
    }

    private int encontrarPosicaoInsercao(int valor) {
        int inicio = 0;
        int fim = tamanho - 1;

        while (inicio <= fim) {
            int meio = inicio + (fim - inicio) / 2;

            if (crescente) {
                if (dados[meio] < valor) {
                    inicio = meio + 1;
                } else {
                    fim = meio - 1;
                }
            } else {
                if (dados[meio] > valor) {
                    inicio = meio + 1;
                } else {
                    fim = meio - 1;
                }
            }
        }

        return inicio;
    }

    public boolean remover(int valor) {
        int indice = buscar(valor);

        if (indice == -1) {
            return false;
        }

        for (int i = indice; i < tamanho - 1; i++) {
            dados[i] = dados[i + 1];
        }

        tamanho--;
        return true;
    }

    public int buscar(int valor) {
        int inicio = 0;
        int fim = tamanho - 1;

        while (inicio <= fim) {
            int meio = inicio + (fim - inicio) / 2;

            if (dados[meio] == valor) {
                return meio;
            }

            if (crescente) {
                if (dados[meio] < valor) {
                    inicio = meio + 1;
                } else {
                    fim = meio - 1;
                }
            } else {
                if (dados[meio] > valor) {
                    inicio = meio + 1;
                } else {
                    fim = meio - 1;
                }
            }
        }

        return -1;
    }

    public boolean contem(int valor) {
        return buscar(valor) != -1;
    }

    public int obter(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice + ". Tamanho: " + tamanho);
        }
        return dados[indice];
    }

    public int getTamanho() {
        return tamanho;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public boolean estaVazio() {
        return tamanho == 0;
    }

    public boolean estaCheia() {
        return tamanho == capacidade;
    }

    public boolean isCrescente() {
        return crescente;
    }

    public void limpar() {
        tamanho = 0;
    }

    public int[] toArray() {
        int[] resultado = new int[tamanho];
        System.arraycopy(dados, 0, resultado, 0, tamanho);
        return resultado;
    }

    @Override
    public String toString() {
        if (tamanho == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tamanho; i++) {
            sb.append(dados[i]);
            if (i < tamanho - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
