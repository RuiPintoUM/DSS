package uminho.dss.sistema.business;

import java.util.List;

public class SistemaFacade{
    private ServicosFacade servicosFacade;
    private OficinaFacade oficinaFacade;

    public SistemaFacade(OficinaFacade oficinaFacade, ServicosFacade servicosFacade) {
        this.oficinaFacade = new OficinaFacade();
        this.servicosFacade = new ServicosFacade();
    }

    public String registarEntrada(String idMecanico) {
        return oficinaFacade.registarEntrada(idMecanico);
    }

    public String registarSaida(String idMecanico) {
        return oficinaFacade.registarSaida(idMecanico);
    }

    public String iniciarServico(String idCliente, String idServico,  String matricula){
        return servicosFacade.iniciarServico(idCliente, idServico, matricula);
    }

    public String concluirServico(String idCliente, String idServico,  String matricula) {
        return servicosFacade.concluirServico(idCliente, idServico, matricula);
    }

    public String consultaServicos(String idMecanico) {
        List<List<String>> listaServicos = oficinaFacade.consultaServicos(idMecanico);

        return servicosFacade.consultaServicos(listaServicos);
    }

    public String removerServico(String matricula, String idCliente, String idServico) {

        return servicosFacade.removerServico(matricula,idCliente,idServico);
    }

    public String atribuirServico(String idCliente, String matricula, String tipoServico) {
        return servicosFacade.atribuirServico(idCliente, matricula, tipoServico);
    }

    public int agendarServico(String matricula, String idCliente, String nomeServico) {
        List<Object> info = servicosFacade.agendarServico(matricula, idCliente, nomeServico);
        Servico servInfo = (Servico) info.get(0); // servico
        String idClienteInfo = (String) info.get(1);
        String matriculaCarroInfo = (String) info.get(2);
        TipoDeServico tipoServ = (TipoDeServico) info.get(3);
        return oficinaFacade.agendarServico(servInfo, idClienteInfo, matriculaCarroInfo, tipoServ);
    }
}
