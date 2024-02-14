package uminho.dss.sistema.business;

import uminho.dss.sistema.data.UtilizadorDAO;
import uminho.dss.sistema.data.PostoTrabalhoDAO;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// **Classe OficinaFacade**
public class OficinaFacade  { //implements IOficina
    private PostoTrabalhoDAO postoTrabalhoDAO;
    private UtilizadorDAO utilizadorDAO;

    public OficinaFacade() {
        this.postoTrabalhoDAO = PostoTrabalhoDAO.getInstance();
        this.utilizadorDAO = UtilizadorDAO.getInstance();
    }

    public String registarEntrada(String idMecanico){
        Mecanico mec = utilizadorDAO.getMecanicos().get(idMecanico);
        if (mec == null) {
            System.out.println("Não encontrou o mec");
            return "null";
        } else {
            System.out.println("Encontrei o mec");
            String idPosto = mec.getIdPosto();
            PostoTrabalho posto = postoTrabalhoDAO.getAllPostosTrabalho().get(idPosto);
            posto.setDisponivel(true);
            posto.setListaServicos(new ArrayList<>());
            return "naonull";
        }
    }

    public String registarSaida(String idMecanico) {
        Mecanico mec = utilizadorDAO.getMecanicos().get(idMecanico);
        if (mec == null) {
            System.out.println("Não encontrou o mec");
            return "null";
        } else {
            String idPosto = mec.getIdPosto();
            PostoTrabalho posto = postoTrabalhoDAO.getAllPostosTrabalho().get(idPosto);
            posto.setDisponivel(false);
            return "naonull";
        }
    }

    public int agendarServico(Servico serv, String idCliente, String matriculaCarro, TipoDeServico tipoServArg) {
        Map<String, Mecanico> lstMecanicos= utilizadorDAO.getMecanicos();
        Map<String, PostoTrabalho> lstPostosTrabalho = postoTrabalhoDAO.getAllPostosTrabalho();  // get postos trabalho
        Duration foundDuration = null;
        for(Mecanico mec : lstMecanicos.values()) {
            String postIdAtual = mec.getIdPosto();
            boolean found = false;
            for(PostoTrabalho pst : lstPostosTrabalho.values()) {
                boolean disponibilidade = pst.getDisponivel();
                Duration duration = Duration.between(pst.getHoraInicio(), pst.getHoraFim());
                int numServicos = pst.getListaServicos().size();
                if((((int) duration.toHours()) > numServicos) && disponibilidade && tipoServArg.compativel(pst.getEspecialidade())) {
                    // se ainda tiver horas e estiver disponivel
                    List<List<String>> lstServPosto = pst.getListaServicos();
                    List<String> infoServico = new ArrayList<>();
                    infoServico.add(serv.getId());
                    infoServico.add(idCliente);
                    infoServico.add(matriculaCarro);
                    lstServPosto.add(infoServico);
                    found = true;
                    foundDuration = duration;
                    pst.setListaServicos(lstServPosto);
                    postoTrabalhoDAO.put(pst.getId(), pst);
                    break;
                }
            }
            if(found) break;
        }
        if (foundDuration == null) return -1;
        return ((int) foundDuration.toHours());
    }



    public List<List<String>> consultaServicos(String idMecanico) {
        Utilizador user = utilizadorDAO.get(idMecanico);
        if(user instanceof Mecanico) {
            Mecanico mec = (Mecanico) user;
            String idPosto = mec.getIdPosto();
            PostoTrabalho posto = postoTrabalhoDAO.get(idPosto);

            return posto.getListaServicos();
        }
        return new ArrayList<>();
    }
}