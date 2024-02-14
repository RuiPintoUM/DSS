package uminho.dss.sistema.business;

import uminho.dss.sistema.data.ClientesDAO;
import uminho.dss.sistema.data.TipoServicoDAO;


import java.util.*;

public class ServicosFacade {

    private ClientesDAO clientesDAO;
    private TipoServicoDAO tipoServicoDAO;

    private Map<String,TipoDeServico> tipoServicoMAP; // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public ServicosFacade() {
        this.clientesDAO = ClientesDAO.getInstance();
        this.tipoServicoDAO = TipoServicoDAO.getInstance();
        this.tipoServicoMAP = new HashMap<>(); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public String iniciarServico(String idCliente, String idServico,  String matricula) {
        Cliente cliente = clientesDAO.get(idCliente);
        Carro carro = cliente.getVeiculos().get(matricula);
        Servico servico = carro.getServicosAtribuidos().get(idServico);
        if (servico == null) return "null";
        servico.setEstado("Inicializado");

        return "naonull";
    }

    public String concluirServico(String idCliente, String idServico,  String matricula) {
        Cliente cliente = clientesDAO.getAllClientes().get(idCliente);
        Carro carro = cliente.getVeiculos().get(matricula);
        Servico servico = carro.getServicosAtribuidos().get(idServico);
        if (servico == null) return "null";
        servico.setEstado("Concluído");

        if(servico.getNome().equals("checkUP")){
            return "checkup";
        }
        return "naonull";
    }

    public String consultaServicos(List<List<String>> listaServicos) {
        StringBuilder output = new StringBuilder();

        for (List<String> servico : listaServicos) {
            if (!servico.isEmpty()) {
                String idServico = servico.get(0);
                String idCliente = servico.get(1);
                String matricula = servico.get(2);
                Servico serv = clientesDAO.getAllClientes().get(idCliente).getCarro(matricula).getServicosAtribuidos().get(idServico);

                output.append(serv.toString());
                output.append("\n");
            }
        }
        return output.toString();
    }

    public boolean verificaServicoCarro(TipoDeServico servico, String tipoCarro) {

        switch (tipoCarro) {
            case "diesel":
                return servico.getDiesel();
            case "gasolina":
                return servico.getGasolina();
            case "eletrico":
                return servico.getEletrico();
            case "hibridoD":
                if(servico.getEletrico() || servico.getDiesel()){
                    return true;
                }
            case "hibridoG":
                if(servico.getEletrico() || servico.getGasolina()){
                    return true;
                }
        }
        return false;
    }

    public String atribuirServico(String idCliente, String matricula, String servico)
    {
        Cliente cliente = clientesDAO.get(idCliente);
       Carro carro = cliente.getCarro(matricula);
       String tipoVeiculo = carro.getTipoVeiculo();
       TipoDeServico tipoServico = this.tipoServicoMAP.get(servico);

       if(!verificaServicoCarro(tipoServico, tipoVeiculo)){
           return "null";
       }

       Collection<Cliente> listaClientes = clientesDAO.values();

       int id = 0;

       for (Cliente clienteElm : listaClientes) {
           for (Carro carroElm : clienteElm.getVeiculos().values()) {
               for (Servico servicoElm : carroElm.getServicosAtribuidos().values()) {
                   id = id + 1;
               }
           }
       }

       id = id + 1;

       Servico novo_servico = new Servico(String.valueOf(id), "atribuido", servico);

       carro.adicionarServicoAtribuido(novo_servico);

       clientesDAO.put(cliente.getId(),cliente);
       return "naonull";
    }

    public String removerServico(String matricula, String idCliente, String idServico) {
        Cliente cliente = clientesDAO.get(idCliente);
        Carro carro = cliente.getVeiculos().get(matricula);
        Servico servico = carro.getServicosAtribuidos().get(idServico);
        if (servico == null) return "null";
        servico.setEstado("Concluído");
        return "naonull";
    }

    public List<Object> agendarServico(String matricula, String idCliente, String nomeServico) {

        // sacar servico do cliente
        TipoDeServico tipoServ = this.tipoServicoMAP.get(nomeServico);
        List<Object> ret = new ArrayList<>();
        Cliente cli = clientesDAO.get(idCliente);
        Map<String, Carro> lstVeiculos = cli.getVeiculos();
        Carro car = lstVeiculos.get(matricula);
        Map<String, Servico> servAtrCarro = car.getServicosAtribuidos();
        Servico servicoAtrAoCliente = null;
        for(Servico serv : servAtrCarro.values())
            if(serv.getNome().equals(nomeServico)) {
                servicoAtrAoCliente = serv;
                break;
            }

        ret.add(servicoAtrAoCliente);
        ret.add(cli.getId());
        ret.add(car.getMatricula());
        ret.add(tipoServ);
        return ret;
    }
}
