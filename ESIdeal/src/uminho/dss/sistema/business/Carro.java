package uminho.dss.sistema.business;
import java.util.HashMap;
import java.util.Map;

public class Carro {
    private String matricula;
    private String marca;
    private String tipoVeiculo;
    private Map<String, Servico> servicosAtribuidos;

    // Construtor
    public Carro(String matricula, String marca, String tipoVeiculo, Map<String, Servico> servicos) {
        this.matricula = matricula;
        this.marca = marca;
        this.tipoVeiculo = tipoVeiculo;
        this.servicosAtribuidos = new HashMap<>();
    }

    // Métodos getters e setters
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public Map<String, Servico> getServicosAtribuidos() {
        return servicosAtribuidos;
    }

    public void setServicosAtribuidos(Map<String, Servico> servicosAtribuidos) {
        this.servicosAtribuidos = servicosAtribuidos;
    }

    // Adiciona um serviço atribuído ao carro
    public void adicionarServicoAtribuido(Servico servico) {
        this.servicosAtribuidos.put(servico.getId(), servico);
    }

    // Outros métodos, se necessário
}

