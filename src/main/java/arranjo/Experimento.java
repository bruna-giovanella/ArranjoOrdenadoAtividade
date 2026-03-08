package arranjo;

import java.util.Random;

/**
 * Experimento científico para análise de desempenho do ArranjoOrdenado.
 * Avalia as operações de inserção e exclusão com diferentes tipos de entrada
 * e diferentes ordenações (crescente / decrescente).
 */
public class Experimento {

    private static final int CAPACIDADE = 100_000;
    private static final int EXECUCOES  = 100;

    // -----------------------------------------------------------------------
    //  Ponto de entrada
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("=======================================================");
        System.out.println("  EXPERIMENTO CIENTÍFICO - ARRANJO ORDENADO");
        System.out.println("=======================================================\n");

        System.out.println(">>> EXPERIMENTO 1: INSERÇÃO\n");
        ResultadoExperimento[][] resInsercao = experimentoInsercao();
        imprimirTabelaInsercao(resInsercao);

        System.out.println("\n>>> EXPERIMENTO 2: EXCLUSÃO\n");
        ResultadoExperimento[][] resExclusao = experimentoExclusao();
        imprimirTabelaExclusao(resExclusao);

        System.out.println("\n>>> EXTRA: WILCOXON (inserção crescente vs decrescente)\n");
        wilcoxonInsercao(resInsercao);
    }

    // -----------------------------------------------------------------------
    //  Experimento de Inserção
    // -----------------------------------------------------------------------

    /**
     * Executa 100 rodadas para cada combinação de tipo-de-entrada × ordenação.
     *
     * @return matriz [tipoEntrada][tipoOrdenacao]  (3×2)
     */
    public static ResultadoExperimento[][] experimentoInsercao() {
        // Dimensões: [0=crescente,1=decrescente,2=aleatorio] × [0=crescente,1=decrescente]
        long[][][] tempos = new long[3][2][EXECUCOES];

        for (int exec = 0; exec < EXECUCOES; exec++) {
            int[] aleatorioCrescente  = gerarAleatorio(CAPACIDADE);
            int[] aleatorioDecrescente = gerarAleatorio(CAPACIDADE);

            for (int ord = 0; ord < 2; ord++) {
                boolean crescente = (ord == 0);

                // -- inserção em ordem crescente --
                ArranjoOrdenado a1 = new ArranjoOrdenado(CAPACIDADE, crescente);
                long t1 = System.nanoTime();
                for (int i = 0; i < CAPACIDADE; i++) a1.inserir(i);
                tempos[0][ord][exec] = System.nanoTime() - t1;

                // -- inserção em ordem decrescente --
                ArranjoOrdenado a2 = new ArranjoOrdenado(CAPACIDADE, crescente);
                long t2 = System.nanoTime();
                for (int i = CAPACIDADE - 1; i >= 0; i--) a2.inserir(i);
                tempos[1][ord][exec] = System.nanoTime() - t2;

                // -- inserção aleatória --
                int[] vet = (ord == 0) ? aleatorioCrescente : aleatorioDecrescente;
                ArranjoOrdenado a3 = new ArranjoOrdenado(CAPACIDADE, crescente);
                long t3 = System.nanoTime();
                for (int v : vet) a3.inserir(v);
                tempos[2][ord][exec] = System.nanoTime() - t3;
            }
        }

        return construirResultados(tempos);
    }

    // -----------------------------------------------------------------------
    //  Experimento de Exclusão
    // -----------------------------------------------------------------------

    /**
     * Executa 100 rodadas de exclusão para cada combinação.
     *
     * @return matriz [tipoEntrada][tipoOrdenacao]  (3×2)
     */
    public static ResultadoExperimento[][] experimentoExclusao() {
        long[][][] tempos = new long[3][2][EXECUCOES];

        for (int exec = 0; exec < EXECUCOES; exec++) {
            int[] aleatorio = gerarAleatorio(CAPACIDADE);

            for (int ord = 0; ord < 2; ord++) {
                boolean crescente = (ord == 0);

                // -- exclusão após inserção crescente --
                ArranjoOrdenado a1 = new ArranjoOrdenado(CAPACIDADE, crescente);
                for (int i = 0; i < CAPACIDADE; i++) a1.inserir(i);
                long t1 = System.nanoTime();
                for (int i = 0; i < CAPACIDADE; i++) a1.remover(i);
                tempos[0][ord][exec] = System.nanoTime() - t1;

                // -- exclusão após inserção decrescente --
                ArranjoOrdenado a2 = new ArranjoOrdenado(CAPACIDADE, crescente);
                for (int i = CAPACIDADE - 1; i >= 0; i--) a2.inserir(i);
                long t2 = System.nanoTime();
                for (int i = CAPACIDADE - 1; i >= 0; i--) a2.remover(i);
                tempos[1][ord][exec] = System.nanoTime() - t2;

                // -- exclusão após inserção aleatória (guarda os valores) --
                ArranjoOrdenado a3 = new ArranjoOrdenado(CAPACIDADE, crescente);
                for (int v : aleatorio) a3.inserir(v);
                long t3 = System.nanoTime();
                for (int v : aleatorio) a3.remover(v);
                tempos[2][ord][exec] = System.nanoTime() - t3;
            }
        }

        return construirResultados(tempos);
    }

    // -----------------------------------------------------------------------
    //  Auxiliares estatísticos
    // -----------------------------------------------------------------------

    private static ResultadoExperimento[][] construirResultados(long[][][] tempos) {
        ResultadoExperimento[][] res = new ResultadoExperimento[3][2];
        for (int tipo = 0; tipo < 3; tipo++) {
            for (int ord = 0; ord < 2; ord++) {
                double media = media(tempos[tipo][ord]);
                double desvio = desvioPadrao(tempos[tipo][ord], media);
                res[tipo][ord] = new ResultadoExperimento(
                        tempos[tipo][ord].clone(), media, desvio);
            }
        }
        return res;
    }

    public static double media(long[] valores) {
        double soma = 0;
        for (long v : valores) soma += v;
        return soma / valores.length;
    }

    public static double desvioPadrao(long[] valores, double media) {
        double soma = 0;
        for (long v : valores) soma += Math.pow(v - media, 2);
        return Math.sqrt(soma / valores.length);
    }

    // -----------------------------------------------------------------------
    //  Geração de dados
    // -----------------------------------------------------------------------

    public static int[] gerarAleatorio(int n) {
        Random rng = new Random(42);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rng.nextInt(n * 10);
        return arr;
    }

    // -----------------------------------------------------------------------
    //  Wilcoxon simplificado (signed-rank)
    // -----------------------------------------------------------------------

    /**
     * Teste de Wilcoxon entre as duas ordenações para inserção crescente (tipo 0).
     * Compara os 100 tempos de "ordenação crescente" vs "ordenação decrescente".
     */
    public static void wilcoxonInsercao(ResultadoExperimento[][] res) {
        long[] a = res[0][0].getTempos(); // crescente / ord-crescente
        long[] b = res[0][1].getTempos(); // crescente / ord-decrescente

        double[] diferencas = new double[a.length];
        for (int i = 0; i < a.length; i++) diferencas[i] = a[i] - b[i];

        // Calcula ranks das diferenças não nulas
        double[] ranks = calcularRanks(diferencas);
        double Wpos = 0, Wneg = 0;
        for (int i = 0; i < diferencas.length; i++) {
            if (diferencas[i] > 0) Wpos += ranks[i];
            else if (diferencas[i] < 0) Wneg += ranks[i];
        }
        double W = Math.min(Wpos, Wneg);
        int n = (int) java.util.Arrays.stream(diferencas).filter(d -> d != 0).count();

        // Aproximação normal para n > 20
        double media  = n * (n + 1.0) / 4.0;
        double desvio = Math.sqrt(n * (n + 1.0) * (2 * n + 1.0) / 24.0);
        double z = (W - media) / desvio;

        System.out.printf("Wilcoxon W = %.2f,  z = %.4f%n", W, z);
        System.out.printf("|z| = %.4f → %s%n", Math.abs(z),
                Math.abs(z) > 1.96 ? "Diferença SIGNIFICATIVA (α=0.05)" : "Sem diferença significativa (α=0.05)");
    }

    private static double[] calcularRanks(double[] diferencas) {
        int n = diferencas.length;
        double[] absDif = new double[n];
        for (int i = 0; i < n; i++) absDif[i] = Math.abs(diferencas[i]);

        // Índices ordenados por |diferença|
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        java.util.Arrays.sort(idx, (x, y) -> Double.compare(absDif[x], absDif[y]));

        double[] ranks = new double[n];
        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && absDif[idx[j]] == absDif[idx[i]]) j++;
            double rankMedio = (i + 1 + j) / 2.0;
            for (int k = i; k < j; k++) ranks[idx[k]] = rankMedio;
            i = j;
        }
        return ranks;
    }

    // -----------------------------------------------------------------------
    //  Impressão
    // -----------------------------------------------------------------------

    private static void imprimirTabelaInsercao(ResultadoExperimento[][] res) {
        String[] linhas = {"Inserção crescente", "Inserção decrescente", "Inserção aleatória"};
        System.out.printf("%-25s | %-30s | %-30s%n",
                "Inserção", "Ordenação Crescente (ms)", "Ordenação Decrescente (ms)");
        System.out.println("-".repeat(90));
        for (int i = 0; i < 3; i++) {
            System.out.printf("%-25s | %s | %s%n",
                    linhas[i],
                    formatarResultado(res[i][0]),
                    formatarResultado(res[i][1]));
        }
    }

    private static void imprimirTabelaExclusao(ResultadoExperimento[][] res) {
        String[] linhas = {"Exclusão (ins. crescente)", "Exclusão (ins. decrescente)", "Exclusão (ins. aleatória)"};
        System.out.printf("%-28s | %-30s | %-30s%n",
                "Exclusão", "Ordenação Crescente (ms)", "Ordenação Decrescente (ms)");
        System.out.println("-".repeat(93));
        for (int i = 0; i < 3; i++) {
            System.out.printf("%-28s | %s | %s%n",
                    linhas[i],
                    formatarResultado(res[i][0]),
                    formatarResultado(res[i][1]));
        }
    }

    private static String formatarResultado(ResultadoExperimento r) {
        double mediaMs  = r.getMedia()  / 1_000_000.0;
        double desvioMs = r.getDesvio() / 1_000_000.0;
        return String.format("%.2f +/- %.4f", mediaMs, desvioMs);
    }

    // -----------------------------------------------------------------------
    //  Inner class de resultado
    // -----------------------------------------------------------------------

    public static class ResultadoExperimento {
        private final long[]  tempos;
        private final double  media;
        private final double  desvio;

        public ResultadoExperimento(long[] tempos, double media, double desvio) {
            this.tempos = tempos;
            this.media  = media;
            this.desvio = desvio;
        }

        public long[]  getTempos() { return tempos; }
        public double  getMedia()  { return media;  }
        public double  getDesvio() { return desvio; }
    }
}
