package uminho.dss.sistema.business;
public class TipoDeServico {
    
    private String nome;
    private boolean diesel;
    private boolean gasolina;
    private boolean eletrico;


    public TipoDeServico(String nome, boolean gasoleo, boolean gasolina, boolean eletrico) {
        this.nome = nome;
        this.diesel = gasoleo;
        this.gasolina = gasolina;
        this.eletrico = eletrico;
    }

    public boolean compativel(String especialidade) {
        if(especialidade.equals("diesel")) {
            if(this.diesel && !this.gasolina && !this.eletrico) return true;
        } else if(especialidade.equals("gasolina")) {
            if(this.gasolina && !this.diesel && !this.eletrico) return true;// 1 segundo
        } else if(especialidade.equals("eletrico")) {
            if(this.eletrico && !this.diesel && !this.gasolina) return true;
        }
        return false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean getDiesel() {
        return diesel;
    }

    public void setDiesel(boolean gasoleo) {
        this.diesel = gasoleo;
    }


    public boolean getGasolina() {
        return gasolina;
    }

    public void setGasolina(boolean gasolina) {
        this.gasolina = gasolina;
    }

    public boolean getEletrico() {
        return eletrico;
    }

    public void setEletrico(boolean eletrico) {
        this.eletrico = eletrico;
    }

}