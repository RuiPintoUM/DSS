package uminho.dss.sistema.business;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// **Classe PostoTrabalho**
public class PostoTrabalho {
    private String id;
    private String especialidade;
    private List<List<String>> listaServicos;
    private Boolean disponivel;
    private LocalTime horaInicio;
    private LocalTime horaFim;

    public PostoTrabalho(String id, String especialidade) {
        this.id = id;
        this.especialidade = especialidade;
        this.listaServicos = new ArrayList<>();
        this.disponivel = true;
        this.horaInicio = LocalTime.of(8, 0);
        this.horaFim = LocalTime.of(18, 0);
    }

    public PostoTrabalho(String id, String especialidade, List<List<String>> listaServicos, Boolean disponivel, LocalTime horaInicio, LocalTime horaFim) {
        this.id = id;
        this.especialidade = especialidade;
        this.listaServicos = listaServicos;
        this.disponivel = disponivel;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public List<List<String>> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<List<String>> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

}