package uminho.dss.sistema.business;

// **Interface Oficina**

public interface IOficina {
    public void registarEntrada(String idMecanico);

    public void registarSaida(String idMecanico);

    public void consultarServicoAtribuidos(String idMecanico);

    public void agendarServico(String idCliente, String matricula, String tipoServico);

    public void iniciarServico(String idMecanico, String idServico, String idCliente, String matricula);
}
