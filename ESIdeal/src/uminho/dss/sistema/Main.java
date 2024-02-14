package uminho.dss.sistema;

import uminho.dss.sistema.business.OficinaFacade;
import uminho.dss.sistema.business.ServicosFacade;
import uminho.dss.sistema.business.SistemaFacade;
import uminho.dss.sistema.ui.Menus;

/**
 *
 * @author DSS
 * @version 20201206
 */
public class Main {

    /**
     * O método main cria a aplicação e invoca o método run()
     */
    public static void main(String[] args) {

        OficinaFacade oficinaFacade = new OficinaFacade();
        ServicosFacade servicosFacade = new ServicosFacade();

        SistemaFacade sistemaFacade = new SistemaFacade(oficinaFacade, servicosFacade);

        Menus menu = new Menus(sistemaFacade);

        try {
            menu.menuPrincipal();
        } catch (Exception e) {
            System.out.println("Erro fatal: "+e.getMessage()+" ["+ e +"]");
        }
    }


}