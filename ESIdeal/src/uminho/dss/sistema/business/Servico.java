package uminho.dss.sistema.business;

import java.time.LocalDate;
import java.util.Date;

public class Servico {
    private String id;
    private LocalDate estimativaEntrega;
    private String estado;
    private String nome;

    // Construtor
    public Servico(String id, String estado, String nome) {
        this.id = id;
        this.estimativaEntrega = null;
        this.estado = estado;
        this.nome = nome;
    }

    public String toString() {
        return "id: " + id +
                "\nEstimativa de Entrega: " + estimativaEntrega +
                "\nEstado: " + estado;
    }

    // Métodos getters e setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getEstimativaEntrega() {
        return estimativaEntrega;
    }

    public void setEstimativaEntrega(LocalDate estimativaEntrega) {
        this.estimativaEntrega = estimativaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Outros métodos, se necessário
}

