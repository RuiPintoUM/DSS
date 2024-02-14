package uminho.dss.sistema.business;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cliente {
    private String id;
    private String nif;
    private String morada;
    private String telefone;
    private String email;
    private Map<String, Carro> veiculos;

    // Construtor
    public Cliente(String id, String nif, String morada, String telefone, String email, Map<String, Carro> veiculos) {
        this.id = id;
        this.nif = nif;
        this.morada = morada;
        this.telefone = telefone;
        this.email = email;
        this.veiculos = veiculos;
    }

    // Métodos getters e setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Carro> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(Map<String, Carro> veiculos) {
        this.veiculos = veiculos;
    }

    // Adiciona um veículo ao cliente
    public void adicionarVeiculo(Carro carro) {
        this.veiculos.put(carro.getMatricula(), carro);
    }

    public Carro getCarro(String matricula){
        return this.veiculos.get(matricula);
    }
}
