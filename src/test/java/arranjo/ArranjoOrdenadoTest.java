package arranjo;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArranjoOrdenado - Testes Unitários")
class ArranjoOrdenadoTest {

    @Test
    @DisplayName("Deve criar arranjo crescente vazio")
    void deveCriarArranjoCresenteVazio() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        assertTrue(a.estaVazio());
        assertEquals(0, a.getTamanho());
        assertEquals(10, a.getCapacidade());
        assertTrue(a.isCrescente());
    }

    @Test
    @DisplayName("Deve criar arranjo decrescente vazio")
    void deveCriarArranjoDecrescenteVazio() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, false);
        assertTrue(a.estaVazio());
        assertFalse(a.isCrescente());
    }

    @Test
    @DisplayName("Deve lançar exceção para capacidade inválida")
    void deveLancarExcecaoCapacidadeInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ArranjoOrdenado(0, true));
        assertThrows(IllegalArgumentException.class, () -> new ArranjoOrdenado(-1, true));
    }

    @Test
    @DisplayName("Inserção crescente - elementos em ordem crescente")
    void insercaoCrescente_elementosOrdemCrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(1); a.inserir(3); a.inserir(5);
        int[] arr = a.toArray();
        assertArrayEquals(new int[]{1, 3, 5}, arr);
    }

    @Test
    @DisplayName("Inserção crescente - elementos em ordem decrescente")
    void insercaoCrescente_elementosOrdemDecrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(5); a.inserir(3); a.inserir(1);
        int[] arr = a.toArray();
        assertArrayEquals(new int[]{1, 3, 5}, arr);
    }

    @Test
    @DisplayName("Inserção crescente - elementos fora de ordem")
    void insercaoCrescente_elementosForaDeOrdem() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(4); a.inserir(1); a.inserir(7); a.inserir(2);
        int[] arr = a.toArray();
        assertArrayEquals(new int[]{1, 2, 4, 7}, arr);
    }

    @Test
    @DisplayName("Inserção crescente - elementos duplicados")
    void insercaoCrescente_elementosDuplicados() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(3); a.inserir(3); a.inserir(3);
        assertEquals(3, a.getTamanho());
        int[] arr = a.toArray();
        assertArrayEquals(new int[]{3, 3, 3}, arr);
    }

    @Test
    @DisplayName("Inserção decrescente - elementos em ordem crescente")
    void insercaoDecrescente_elementosOrdemCrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, false);
        a.inserir(1); a.inserir(3); a.inserir(5);
        int[] arr = a.toArray();
        assertArrayEquals(new int[]{5, 3, 1}, arr);
    }

    @Test
    @DisplayName("Inserção decrescente - elementos em ordem decrescente")
    void insercaoDecrescente_elementosOrdemDecrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, false);
        a.inserir(5); a.inserir(3); a.inserir(1);
        int[] arr = a.toArray();
        assertArrayEquals(new int[]{5, 3, 1}, arr);
    }

    @Test
    @DisplayName("Inserção decrescente - elementos fora de ordem")
    void insercaoDecrescente_elementosForaDeOrdem() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, false);
        a.inserir(4); a.inserir(1); a.inserir(7); a.inserir(2);
        int[] arr = a.toArray();
        assertArrayEquals(new int[]{7, 4, 2, 1}, arr);
    }

    @Test
    @DisplayName("Remoção - elemento existente crescente")
    void remocao_elementoExistenteCrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(1); a.inserir(3); a.inserir(5);
        assertTrue(a.remover(3));
        assertArrayEquals(new int[]{1, 5}, a.toArray());
    }

    @Test
    @DisplayName("Remoção - elemento existente decrescente")
    void remocao_elementoExistenteDecrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, false);
        a.inserir(1); a.inserir(3); a.inserir(5);
        assertTrue(a.remover(3));
        assertArrayEquals(new int[]{5, 1}, a.toArray());
    }

    @Test
    @DisplayName("Remoção - elemento não existente")
    void remocao_elementoNaoExistente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(1); a.inserir(3);
        assertFalse(a.remover(99));
        assertEquals(2, a.getTamanho());
    }

    @Test
    @DisplayName("Remoção - primeiro e último elemento")
    void remocao_primeiroEUltimoElemento() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(1); a.inserir(3); a.inserir(5);
        assertTrue(a.remover(1));
        assertTrue(a.remover(5));
        assertArrayEquals(new int[]{3}, a.toArray());
    }

    @Test
    @DisplayName("Busca - elemento encontrado crescente")
    void busca_elementoEncontradoCrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(1); a.inserir(3); a.inserir(5);
        assertEquals(1, a.buscar(3)); // índice 1
    }

    @Test
    @DisplayName("Busca - elemento não encontrado")
    void busca_elementoNaoEncontrado() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(1); a.inserir(3);
        assertEquals(-1, a.buscar(99));
    }

    @Test
    @DisplayName("Contem - verificação correta")
    void contem_verificacaoCorreta() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(42);
        assertTrue(a.contem(42));
        assertFalse(a.contem(0));
    }

    @Test
    @DisplayName("Deve lançar exceção ao inserir em arranjo cheio")
    void deveLancarExcecaoAoInserirEmCheio() {
        ArranjoOrdenado a = new ArranjoOrdenado(3, true);
        a.inserir(1); a.inserir(2); a.inserir(3);
        assertTrue(a.estaCheia());
        assertThrows(IllegalStateException.class, () -> a.inserir(4));
    }

    @Test
    @DisplayName("Obter elemento com índice inválido lança exceção")
    void obterIndiceInvalidoLancaExcecao() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(10);
        assertThrows(IndexOutOfBoundsException.class, () -> a.obter(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> a.obter(1));
    }

    @Test
    @DisplayName("Limpar deve esvaziar o arranjo")
    void limparDeveEsvaziarArranjo() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(1); a.inserir(2);
        a.limpar();
        assertTrue(a.estaVazio());
        assertEquals(0, a.getTamanho());
    }

    @Test
    @DisplayName("toString deve representar o arranjo corretamente")
    void toStringDeveRepresentarArranjo() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        assertEquals("[]", a.toString());
        a.inserir(1); a.inserir(2);
        assertEquals("[1, 2]", a.toString());
    }

    @Test
    @DisplayName("Inserção de 1000 elementos mantém ordenação crescente")
    void insercaoGrandeVolumeCrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(1000, true);
        java.util.Random rng = new java.util.Random(0);
        for (int i = 0; i < 1000; i++) a.inserir(rng.nextInt(10_000));
        int[] arr = a.toArray();
        for (int i = 1; i < arr.length; i++) {
            assertTrue(arr[i - 1] <= arr[i],
                    "Quebra de ordem na posição " + i + ": " + arr[i - 1] + " > " + arr[i]);
        }
    }

    @Test
    @DisplayName("Inserção de 1000 elementos mantém ordenação decrescente")
    void insercaoGrandeVolumeDecrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(1000, false);
        java.util.Random rng = new java.util.Random(0);
        for (int i = 0; i < 1000; i++) a.inserir(rng.nextInt(10_000));
        int[] arr = a.toArray();
        for (int i = 1; i < arr.length; i++) {
            assertTrue(arr[i - 1] >= arr[i],
                    "Quebra de ordem na posição " + i + ": " + arr[i - 1] + " < " + arr[i]);
        }
    }
}
