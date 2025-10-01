// Código para colar em Evento.java
import java.util.Date;

public class Evento {
    private int id;
    private String nome;
    private Date dataInicio;
    private Date dataFim;
    private String status;

    public Evento(int id, String nome, Date dataInicio, Date dataFim) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = "Planejado";
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Date getDataInicio() { return dataInicio; }
    public Date getDataFim() { return dataFim; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}