package uminho.dss.sistema.business;

public interface ISistema {
    public String registarEntrada(String idMecanico);

    public String registarSaida(String idMecanico);

    public String iniciarServico(String idMecanico, String idServico, String idCliente, String matricula);

    public String concluirServico(String idCliente, String matricula, String idServico);

    public String consultarServico(String idMecanico);

    public void atribuirServico(String idCliente, String Matricula, String tripoServico);

    public void agendarServico(String idMecanico, String idCliente, String matricula);

    public void removerServico(String idCliente, String matricula, String idServico); // jê
}
