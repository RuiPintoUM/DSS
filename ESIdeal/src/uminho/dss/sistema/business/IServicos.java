package uminho.dss.sistema.business;

// **Interface Servicos**
public interface IServicos {

    public String iniciarServico(String idCliente, String idServico, String matricula);

    public String removerServico(String matricula, String idCliente, String nomeServico);

    public String concluirServico(String idCliente, String idServico, String matricula);

    public void atribuirServico(String idCliente, String matricula, String tipoServico);

    public Servico agendarServico(String matricula, String idCliente, String nomeServico);
}
