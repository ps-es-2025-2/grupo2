// Código para colar em Produto.java
public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private double precoVenda;

    public Produto(int id, String nome, String descricao, double precoVenda) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.precoVenda = precoVenda;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }
}