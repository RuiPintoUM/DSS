package uminho.dss.sistema.data;

import uminho.dss.sistema.business.Funcionario;
import uminho.dss.sistema.business.Mecanico;
import uminho.dss.sistema.business.Utilizador;

import java.sql.*;
import java.util.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UtilizadorDAO implements Map<String, Utilizador> {
    private static UtilizadorDAO singleton = null;

    private UtilizadorDAO() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Tabela Mecanicos
            String sql = "CREATE TABLE IF NOT EXISTS mecanicos (" +
                    "id varchar(10) NOT NULL PRIMARY KEY," +
                    "nome varchar(45) NOT NULL," +
                    "especialidade varchar(45) NOT NULL," +
                    "horario varchar(45) NOT NULL," +
                    "id_posto varchar(10))";
            stm.executeUpdate(sql);

            // Tabela Funcionarios
            sql = "CREATE TABLE IF NOT EXISTS funcionarios (" +
                    "id varchar(10) NOT NULL PRIMARY KEY," +
                    "nome varchar(45) NOT NULL)";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            // Erro a criar tabela...
            e.printStackTrace();
        }
    }

    public void printTables() {
        System.out.println("Mecanicos Table:");
        printTable("mecanicos");

        System.out.println("\nFuncionarios Table:");
        printTable("funcionarios");
    }

    private void printTable(String tableName) {
        String query = "SELECT * FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            // Print table data
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error printing table: " + tableName, e);
        }
    }

    public static UtilizadorDAO getInstance() {
        if (singleton == null) {
            singleton = new UtilizadorDAO();
        }
        return singleton;
    }

    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM utilizadores")) {
            if (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (SQLException e) {
            // Error querying the database...

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
             ResultSet rs = stm.executeQuery("SELECT IdUtil FROM utilizadores WHERE IdUtil='" + key.toString() + "'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter utilizador", e);
        }
        return r;
    }

    public boolean containsValue(Object value) {
        Utilizador utilizador = (Utilizador) value;
        return this.containsKey(utilizador.getId());
    }

    public Utilizador get(Object key) {
        Utilizador utilizador = null;
        String queryMecanico = "SELECT id, nome, especialidade, horario, id_posto FROM mecanicos WHERE id = ?";
        String queryFuncionario = "SELECT id, nome FROM funcionarios WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement pstmtMecanico = conn.prepareStatement(queryMecanico);
             PreparedStatement pstmtFuncionario = conn.prepareStatement(queryFuncionario)) {

            pstmtMecanico.setString(1, key.toString());
            pstmtFuncionario.setString(1, key.toString());

            try (ResultSet rsMecanico = pstmtMecanico.executeQuery()) {
                if (rsMecanico.next()) {
                    // Found in mecanicos table
                    String id = rsMecanico.getString("id");
                    String nome = rsMecanico.getString("nome");
                    String especialidade = rsMecanico.getString("especialidade");
                    String horario = rsMecanico.getString("horario");
                    String idPosto = rsMecanico.getString("id_posto");

                    utilizador = new Mecanico(id, nome, especialidade, horario, idPosto);
                }
            }

            // If not found in mecanicos table, try funcionarios table
            if (utilizador == null) {
                try (ResultSet rsFuncionario = pstmtFuncionario.executeQuery()) {
                    if (rsFuncionario.next()) {
                        // Found in funcionarios table
                        String id = rsFuncionario.getString("id");
                        String nome = rsFuncionario.getString("nome");
                        utilizador = new Funcionario(id, nome);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter utilizador", e);
        }
        return utilizador;
    }

    public Utilizador put(String key, Utilizador utilizador) {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            stm.executeUpdate(
                    "INSERT INTO utilizadores " +
                            "VALUES ('" + utilizador.getId() + "', '" + utilizador.getNome() + "') " +
                            "ON DUPLICATE KEY UPDATE Nome=VALUES(Nome)");

            if (utilizador instanceof Mecanico) {
                Mecanico mecanico = (Mecanico) utilizador;
                stm.executeUpdate(
                        "INSERT INTO mecanicos " +
                                "VALUES ('" + mecanico.getId() + "', '" + mecanico.getEspecialidade() + "', '" +
                                mecanico.getHorarioLaboral() + "', '" + mecanico.getIdPosto() + "') " +
                                "ON DUPLICATE KEY UPDATE Especialidade=VALUES(Especialidade), " +
                                "HorarioLaboral=VALUES(HorarioLaboral), IdPosto=VALUES(IdPosto)");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return utilizador;
    }



    public Utilizador remove(Object key) {
        Utilizador utilizador = this.get(key);

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement removeUtilizadorStatement = conn.prepareStatement("DELETE FROM utilizadores WHERE IdUtil=?")) {

            // Remove the user
            removeUtilizadorStatement.setString(1, key.toString());
            removeUtilizadorStatement.executeUpdate();

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return utilizador;
    }

    // Add other methods as needed...

    public void putAll(Map<? extends String, ? extends Utilizador> utilizadores) {
        for (Utilizador utilizador : utilizadores.values()) {
            put(utilizador.getId(), utilizador);
        }
    }

    public void clear() {
        throw new UnsupportedOperationException("Not implemented!");
    }

    public Map<String, Mecanico> getMecanicos() {
        Map<String, Mecanico> mecanicos = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM mecanicos")) {

            while (rs.next()) {
                Mecanico mecanico = new Mecanico(
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("especialidade"),
                        rs.getString("horario"),
                        rs.getString("id_posto")
                );
                mecanicos.put(mecanico.getId(), mecanico);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mecanicos;
    }

    public Map<String, Funcionario> getFuncionarios() {
        Map<String, Funcionario> funcionarios = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM funcionarios")) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("id"),
                        rs.getString("nome")
                );
                funcionarios.put(funcionario.getId(), funcionario);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return funcionarios;
    }


    public Set<String> keySet() { throw new NullPointerException("Not implemented!"); }

    public Collection<Utilizador> values()  { throw new NullPointerException("Not implemented!"); }

    public Set<Entry<String, Utilizador>> entrySet() { throw new NullPointerException("Not implemented!"); }

}
