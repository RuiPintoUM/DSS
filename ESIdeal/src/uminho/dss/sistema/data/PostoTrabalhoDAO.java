package uminho.dss.sistema.data;

import uminho.dss.sistema.business.PostoTrabalho;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.LocalTime;

public class PostoTrabalhoDAO implements Map<String, PostoTrabalho> {
    private static uminho.dss.sistema.data.PostoTrabalhoDAO singleton = null;

    private PostoTrabalhoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            // Criar a tabela de serviços
            String createServicosTable = "CREATE TABLE IF NOT EXISTS postoServicos (" +
                    "IdServico varchar(10) NOT NULL PRIMARY KEY," +
                    "IdCliente varchar(10) NOT NULL PRIMARY KEY," +
                    "Matricula varchar(100) NOT NULL)";
            stm.executeUpdate(createServicosTable);

            // Criar a tabela de postoTrabalho
            String createPostoTrabalhoTable = "CREATE TABLE IF NOT EXISTS postosTrabalho (" +
                    "Id varchar(10) NOT NULL PRIMARY KEY," +
                    "Especialidade varchar(45) NOT NULL," +
                    "IdServico varchar(10) REFERENCES servicos(IdServico)," +
                    "Disponivel boolean DEFAULT true," +
                    "HorarioInicio varchar(5) DEFAULT '09:00'," +
                    "HorarioFim varchar(5) DEFAULT '18:00')";
            stm.executeUpdate(createPostoTrabalhoTable);
        } catch (SQLException e) {
            // Erro a criar tabela...

            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static uminho.dss.sistema.data.PostoTrabalhoDAO getInstance() {
        if (uminho.dss.sistema.data.PostoTrabalhoDAO.singleton == null) {
            uminho.dss.sistema.data.PostoTrabalhoDAO.singleton = new uminho.dss.sistema.data.PostoTrabalhoDAO();
        }
        return uminho.dss.sistema.data.PostoTrabalhoDAO.singleton;
    }

    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM postosTrabalho")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
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
                     stm.executeQuery("SELECT Id FROM postosTrabalho WHERE Id='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    public boolean containsValue(Object value) {
        PostoTrabalho t = (PostoTrabalho) value;
        return this.containsKey(t.getId());
    }

    public PostoTrabalho get(Object key) {
        PostoTrabalho pt = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM postosTrabalho WHERE Id='" + key + "'")) {
            if (rs.next()) {
                List<List<String>> listaServicos = getServicosDoPostoTrabalho(rs.getString("Id"), conn);

                pt = new PostoTrabalho(

                        rs.getString("Id"),
                        rs.getString("Especialidade"),
                        listaServicos,
                        rs.getBoolean("Disponivel"),
                        rs.getTime("HorarioInicio").toLocalTime(),
                        rs.getTime("HorarioFim").toLocalTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pt;
    }

    public Map<String, PostoTrabalho> getAllPostosTrabalho() {
        Map<String, PostoTrabalho> postosTrabalhoMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM postosTrabalho")) {

            while (rs.next()) {
                String idPosto = rs.getString("Id");
                List<List<String>> servicos = getServicosDoPostoTrabalho(idPosto, conn);

                // Use getString to retrieve the time as a string
                String horarioInicioString = rs.getString("HorarioInicio");
                String horarioFimString = rs.getString("HorarioFim");

                // Parse the time strings into LocalTime
                LocalTime horarioInicio = LocalTime.parse(horarioInicioString);
                LocalTime horarioFim = LocalTime.parse(horarioFimString);

                PostoTrabalho postoTrabalho = new PostoTrabalho(
                        idPosto,
                        rs.getString("Especialidade"),
                        servicos,
                        rs.getBoolean("Disponivel"),
                        horarioInicio,
                        horarioFim
                );

                postosTrabalhoMap.put(idPosto, postoTrabalho);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return postosTrabalhoMap;
    }



    private List<List<String>> getServicosDoPostoTrabalho(String postoTrabalhoId, Connection conn) throws SQLException {
        List<List<String>> servicos = new ArrayList<>();

        String sql = "SELECT IdServico, IdCliente, Matricula FROM postoServicos WHERE IdServico=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, postoTrabalhoId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    List<String> servico = new ArrayList<>();
                    servico.add(rs.getString("IdServico"));
                    servico.add(rs.getString("IdCliente"));
                    servico.add(rs.getString("Matricula"));
                    servicos.add(servico);
                }
            }
        }

        return servicos;
    }

    public PostoTrabalho put(String key, PostoTrabalho postoTrabalho) {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Atualizar o postoTrabalho
            stm.executeUpdate(
                    "INSERT INTO postosTrabalho (Id, Especialidade, Disponivel, HorarioInicio, HorarioFim) " +
                            "VALUES ('" + postoTrabalho.getId() + "', '" + postoTrabalho.getEspecialidade() + "', " +
                            postoTrabalho.getDisponivel() + ", '" +
                            postoTrabalho.getHoraInicio().toString() + "', '" + postoTrabalho.getHoraFim().toString() + "') " +
                            "ON DUPLICATE KEY UPDATE Especialidade=VALUES(Especialidade), " +
                            "Disponivel=VALUES(Disponivel), " +
                            "HorarioInicio=VALUES(HorarioInicio), HorarioFim=VALUES(HorarioFim)");

            // Deletar serviços associados ao postoTrabalho
            stm.executeUpdate("DELETE FROM postoServicos WHERE IdPostoTrabalho = '" + key + "'");

            // Inserir novos serviços associados ao postoTrabalho
            List<List<String>> servicos = postoTrabalho.getListaServicos();
            for (List<String> servico : servicos) {
                stm.executeUpdate(
                        "INSERT INTO postoServicos (IdServico, IdCliente, Matricula, IdPostoTrabalho) " +
                                "VALUES ('" + servico.get(0) + "', '" + servico.get(1) + "', '" + servico.get(2) + "', '" + key + "') " +
                                "ON DUPLICATE KEY UPDATE IdCliente=VALUES(IdCliente), Matricula=VALUES(Matricula)");
            }

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
        }
        return postoTrabalho;
    }


    public PostoTrabalho remove(Object key) {
        PostoTrabalho postoTrabalho = this.get(key);

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement removeServicosStatement = conn.prepareStatement("DELETE FROM postoServicos WHERE IdPostoTrabalho=?");
             PreparedStatement removePostoTrabalhoStatement = conn.prepareStatement("DELETE FROM postosTrabalho WHERE Id=?")) {

            // Remover os serviços associados ao postoTrabalho
            removeServicosStatement.setString(1, key.toString());
            removeServicosStatement.executeUpdate();

            // Remover o postoTrabalho
            removePostoTrabalhoStatement.setString(1, key.toString());
            removePostoTrabalhoStatement.executeUpdate();

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return postoTrabalho;
    }

    public void putAll(Map<? extends String, ? extends PostoTrabalho> clientes) { throw new NullPointerException("Not implemented!"); }

    public void clear() { throw new NullPointerException("Not implemented!"); }

    public Set<String> keySet() { throw new NullPointerException("Not implemented!"); }

    public Collection<PostoTrabalho> values()  { throw new NullPointerException("Not implemented!"); }

    public Set<Entry<String, PostoTrabalho>> entrySet() { throw new NullPointerException("Not implemented!"); }

}