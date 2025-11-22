// Código para colar em Estoque.java
public class Estoque {
    private int id;
    private Produto produto;
    private Evento evento;
    private int quantidadeInicial;
    private int quantidadeAtual;
    private int nivelMinimoAlerta;

    public Estoque(int id, Produto produto, Evento evento, int quantidadeInicial, int nivelMinimoAlerta) {
        this.id = id;
        this.produto = produto;
        this.evento = evento;
        this.quantidadeInicial = quantidadeInicial;
        this.quantidadeAtual = quantidadeInicial;
        this.nivelMinimoAlerta = nivelMinimoAlerta;
    }

    public boolean darBaixaEstoque(int quantidade) {
        if (quantidade > 0 && this.quantidadeAtual >= quantidade) {
            this.quantidadeAtual -= quantidade;
            return true;
        }
        return false;
    }

    public void reporEstoque(int quantidade) {
        if (quantidade > 0) {
            this.quantidadeAtual += quantidade;
        }
    }

    // Getters
    public int getId() { return id; }
    public Produto getProduto() { return produto; }
    public Evento getEvento() { return evento; }
    public int getQuantidadeAtual() { return quantidadeAtual; }
    public boolean isAlertaEstoqueBaixo() { return this.quantidadeAtual <= this.nivelMinimoAlerta; }
}