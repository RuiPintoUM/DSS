package uminho.dss.sistema.business;
public class Mecanico extends Utilizador {

    private String especialidade;
    private String horarioLaboral;
    private String idPosto;

    public Mecanico(String id, String nome, String especialidade, String horarioLaboral, String idPosto) {
        super(id, nome);
        this.especialidade = especialidade;
        this.horarioLaboral = horarioLaboral;
        this.idPosto = idPosto;
    }

    public String getHorarioLaboral() {
        return this.horarioLaboral;
    }

    public void setHorarioLaboral(String horarioLaboral) {
        this.horarioLaboral = horarioLaboral;
    }

    public String getEspecialidade() {
        return this.especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getIdPosto() {
        return this.idPosto;
    }

    public void setIdPosto(String idPosto) {
        this.idPosto = idPosto;
    }

    @Override
    public String toString() {
        return "Mecanico{" +
                "horarioLaboral='" + horarioLaboral + '\'' +
                ", especialidade='" + especialidade + '\'' +
                "} " + super.toString();
    }
}
