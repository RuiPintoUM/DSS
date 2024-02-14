package uminho.dss.sistema.data;

import uminho.dss.sistema.business.TipoDeServico;

import java.sql.*;
import java.util.*;

public class TipoServicoDAO implements Map<String, TipoDeServico> {
    private static TipoServicoDAO singleton = null;

    private TipoServicoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            String createServicosTable = "CREATE TABLE IF NOT EXISTS tipoServico (" +
                    "Nome varchar(45) NOT NULL PRIMARY KEY," +
                    "Gasoleo boolean NOT NULL," +
                    "Gasolina boolean NOT NULL," +
                    "Eletrico boolean NOT NULL)";
            stm.executeUpdate(createServicosTable);
        } catch (SQLException e) {
            // Erro a criar tabela...
            e.printStackTrace();
        }
    }

    public static TipoServicoDAO getInstance() {
        if (singleton == null) {
            singleton = new TipoServicoDAO();
        }
        return singleton;
    }

    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM tipoServico")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
        }
        return i;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT IdServico FROM tipoServico WHERE IdServico='" + key.toString() + "'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NumberFormatException();
        }
        return r;
    }

    public boolean containsValue(Object value) {
        TipoDeServico tipoServico = (TipoDeServico) value;
        return this.containsKey(tipoServico.getNome());
    }

    public TipoDeServico get(Object key) {
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("A chave deve ser uma String representando o nome do serviço.");
        }

        String nomeServico = (String) key;
        TipoDeServico tipoServico = null;

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM tipoServico WHERE Nome='" + nomeServico + "'")) {

            if (rs.next()) {
                tipoServico = new TipoDeServico(
                        rs.getString("Nome"),
                        rs.getBoolean("Gasoleo"),
                        rs.getBoolean("Gasolina"),
                        rs.getBoolean("Eletrico")
                );
            }
        } catch (SQLException e) {
            // Erro ao obter dados...
            e.printStackTrace();
        }

        return tipoServico;
    }

    public TipoDeServico put(String key, TipoDeServico value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Chave e valor não podem ser nulos.");
        }

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Inserir ou atualizar o registro na tabela tipoServico
            stm.executeUpdate(
                    "INSERT INTO tipoServico (Nome, Gasoleo, Gasolina, Eletrico) VALUES " +
                            "('" + key + "', " + value.getDiesel() + ", " + value.getGasolina() + ", " + value.getEletrico() + ") " +
                            "ON DUPLICATE KEY UPDATE Gasoleo=VALUES(Gasoleo), Gasolina=VALUES(Gasolina), Eletrico=VALUES(Eletrico)");

        } catch (SQLException e) {
            // Erro ao inserir ou atualizar dados...
            e.printStackTrace();
        }

        return value;
    }

    public TipoDeServico remove(Object key) {
        TipoDeServico tipoServico = this.get(key);

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement removeTipoServicoStatement = conn.prepareStatement("DELETE FROM tipoServico WHERE IdServico=?")) {

            // Remover o tipoServico
            removeTipoServicoStatement.setString(1, key.toString());
            removeTipoServicoStatement.executeUpdate();

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
        }

        return tipoServico;
    }

    public void putAll(Map<? extends String, ? extends TipoDeServico> tipoServico) { throw new NullPointerException("Not implemented!"); }

    public void clear() { throw new NullPointerException("Not implemented!"); }

    public Set<String> keySet() { throw new NullPointerException("Not implemented!"); }

    public Collection<TipoDeServico> values()  { throw new NullPointerException("Not implemented!"); }

    public Set<Entry<String, TipoDeServico>> entrySet() { throw new NullPointerException("Not implemented!"); }

}