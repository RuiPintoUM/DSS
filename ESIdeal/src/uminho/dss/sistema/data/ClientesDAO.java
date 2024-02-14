package uminho.dss.sistema.data;

import uminho.dss.sistema.business.Carro;
import uminho.dss.sistema.business.Cliente;
import uminho.dss.sistema.business.Servico;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ClientesDAO implements Map<String, Cliente> {
    private static uminho.dss.sistema.data.ClientesDAO singleton = null;

    private ClientesDAO() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
                    "Id varchar(10) NOT NULL PRIMARY KEY," +
                    "Nif varchar(10) NOT NULL," +
                    "Morada varchar(45) DEFAULT NULL," +
                    "Telefone varchar(15) DEFAULT NULL," +
                    "Email varchar(45) DEFAULT NULL)";
            stm.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS carros (" +
                    "Matricula varchar(7) NOT NULL PRIMARY KEY," +
                    "Marca varchar(45) NOT NULL," +
                    "TipoVeiculo varchar(45) NOT NULL," +
                    "ClienteId varchar(10)," +
                    "FOREIGN KEY (ClienteId) REFERENCES clientes(Id))";
            stm.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS servicos (" +
                    "Id varchar(10) NOT NULL PRIMARY KEY," +
                    "Estado varchar(45) DEFAULT NULL," +
                    "CarroMatricula varchar(7)," +
                    "FOREIGN KEY (CarroMatricula) REFERENCES carros(Matricula))";
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static uminho.dss.sistema.data.ClientesDAO getInstance() {
        if (uminho.dss.sistema.data.ClientesDAO.singleton == null) {
            uminho.dss.sistema.data.ClientesDAO.singleton = new uminho.dss.sistema.data.ClientesDAO();
        }
        return uminho.dss.sistema.data.ClientesDAO.singleton;
    }

    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM clientes")) {
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
                     stm.executeQuery("SELECT Id FROM clientes WHERE Id='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    public boolean containsValue(Object value) {
        Cliente t = (Cliente) value;
        return this.containsKey(t.getId());
    }

    public Cliente get(Object key) {
        Cliente cliente = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM clientes WHERE Id='" + key + "'")) {
            if (rs.next()) {
                Map<String, Carro> carros = getCarrosCliente(key.toString(), stm);
                cliente = new Cliente(
                        rs.getString("Id"),
                        rs.getString("Nif"),
                        rs.getString("Morada"),
                        rs.getString("Telefone"),
                        rs.getString("Email"),
                        carros
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return cliente;
    }

    public Map<String, Cliente> getAllClientes() {
        Map<String, Cliente> clientesMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM clientes")) {

            while (rs.next()) {
                String clientId = rs.getString("Id");
                Map<String, Carro> carros = getCarrosCliente(clientId, stm);

                Cliente cliente = new Cliente(
                        clientId,
                        rs.getString("Nif"),
                        rs.getString("Morada"),
                        rs.getString("Telefone"),
                        rs.getString("Email"),
                        carros
                );

                clientesMap.put(clientId, cliente);
            }

        } catch (SQLException e) {
            System.out.println("ClienteDAO getAllClientes");
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return clientesMap;
    }

    private Map<String, Carro> getCarrosCliente(String clienteId, Statement stm) throws SQLException {
        Map<String, Carro> carros = new HashMap<>();
        String sql = "SELECT * FROM carros WHERE ClienteId='" + clienteId + "'";
        try (ResultSet rsCarros = stm.executeQuery(sql)) {
            while (rsCarros.next()) {
                Map<String, Servico> servicos = getServicosCarro(rsCarros.getString("Matricula"), stm);
                Carro carro = new Carro(
                        rsCarros.getString("Matricula"),
                        rsCarros.getString("Marca"),
                        rsCarros.getString("TipoVeiculo"),
                        servicos
                );
                carros.put(carro.getMatricula(), carro);
            }
        }
        return carros;
    }

    private Map<String, Servico> getServicosCarro(String matricula, Statement stm) throws SQLException {
        Map<String, Servico> servicos = new HashMap<>();
        String sql = "SELECT * FROM servico WHERE CarroMatricula='" + matricula + "'";
        try (ResultSet rsServicos = stm.executeQuery(sql)) {
            while (rsServicos.next()) {
                java.sql.Date dataSql = rsServicos.getDate("EstimativaEntrega");

                Servico servico = new Servico(
                        rsServicos.getString("Id"),
                        rsServicos.getString("Estado"),
                        rsServicos.getString("Nome")
                );
                servicos.put(servico.getId(), servico);
            }
        }
        return servicos;
    }


    public Cliente put(String key, Cliente cliente) {
        Cliente res = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Atualizar o cliente
            stm.executeUpdate(
                    "INSERT INTO clientes " +
                            "VALUES ('" + cliente.getId() + "', '" + cliente.getNif() + "', '" + cliente.getMorada() + "', '" +
                            cliente.getTelefone() + "', '" + cliente.getEmail() + "') " +
                            "ON DUPLICATE KEY UPDATE Nif=VALUES(Nif), Morada=VALUES(Morada), " +
                            "Telefone=VALUES(Telefone), Email=VALUES(Email)");

            // Atualizar os carros do cliente
            Map<String, Carro> oldCarros = getCarrosCliente(key, stm);
            Map<String, Carro> newCarros = cliente.getVeiculos();

            // Carros que entram, em relação ao que está na BD
            newCarros.keySet().removeAll(oldCarros.keySet());

            // Carros que saem, em relação ao que está na BD
            oldCarros.keySet().removeAll(cliente.getVeiculos().keySet());

            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO carros VALUES (?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE Marca=VALUES(Marca), TipoVeiculo=VALUES(TipoVeiculo)")) {
                pstm.setString(4, cliente.getId());  // Definir a referência ao cliente

                // Remover os carros que saem
                for (Carro carro : oldCarros.values()) {
                    pstm.setString(1, carro.getMatricula());
                    pstm.setString(2, carro.getMarca());
                    pstm.setString(3, carro.getTipoVeiculo());
                    pstm.executeUpdate();
                }

                // Adicionar os carros que entram
                for (Carro carro : newCarros.values()) {
                    pstm.setString(1, carro.getMatricula());
                    pstm.setString(2, carro.getMarca());
                    pstm.setString(3, carro.getTipoVeiculo());
                    pstm.executeUpdate();
                }
            }

            // Atualizar os serviços associados aos carros do cliente
            for (Carro carro : cliente.getVeiculos().values()) {
                Map<String, Servico> oldServicos = getServicosCarro(carro.getMatricula(), stm);
                Map<String, Servico> newServicos = carro.getServicosAtribuidos();

                // Serviços que entram, em relação ao que está na BD
                newServicos.keySet().removeAll(oldServicos.keySet());

                // Serviços que saem, em relação ao que está na BD
                oldServicos.keySet().removeAll(carro.getServicosAtribuidos().keySet());

                try (PreparedStatement pstm = conn.prepareStatement(
                        "INSERT INTO servico VALUES (?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE EstimativaEntrega=VALUES(EstimativaEntrega), " +
                                "Estado=VALUES(Estado)")) {
                    pstm.setString(1, cliente.getId() + "-" + carro.getMatricula());  // Usar uma combinação única de Cliente e Carro
                    pstm.setObject(2, java.sql.Date.valueOf(LocalDate.now()));  // Ajuste para EstimativaEntrega padrão
                    pstm.setString(3, "Pendente");  // Ajuste para Estado padrão

                    // Remover os serviços que saem
                    for (Servico servico : oldServicos.values()) {
                        pstm.setString(1, servico.getId());
                        pstm.setObject(2, java.sql.Date.valueOf(servico.getEstimativaEntrega()));
                        pstm.setString(3, servico.getEstado());
                        pstm.executeUpdate();
                    }

                    // Adicionar os serviços que entram
                    for (Servico servico : newServicos.values()) {
                        pstm.setString(1, servico.getId());
                        pstm.setObject(2, java.sql.Date.valueOf(servico.getEstimativaEntrega()));
                        pstm.setString(3, servico.getEstado());
                        pstm.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    public Cliente remove(Object key) {
        Cliente cliente = this.get(key);

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement removeCarrosStatement = conn.prepareStatement("DELETE FROM carros WHERE ClienteId=?");
             PreparedStatement removeServicosStatement = conn.prepareStatement("DELETE FROM servico WHERE CarroMatricula=?");
             PreparedStatement removeClienteStatement = conn.prepareStatement("DELETE FROM clientes WHERE Id=?")) {

            // Remover os serviços associados aos carros do cliente
            for (Carro carro : cliente.getVeiculos().values()) {
                removeServicosStatement.setString(1, carro.getMatricula());
                removeServicosStatement.executeUpdate();
            }

            // Remover os carros do cliente
            removeCarrosStatement.setString(1, key.toString());
            removeCarrosStatement.executeUpdate();

            // Remover o cliente
            removeClienteStatement.setString(1, key.toString());
            removeClienteStatement.executeUpdate();

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return cliente;
    }

    public void putAll(Map<? extends String, ? extends Cliente> clientes) {
        for (Cliente cliente : clientes.values()) {
            put(cliente.getId(), cliente);
        }
    }

    public void clear() { throw new NullPointerException("Not implemented!"); }

    public Set<String> keySet() {
        throw new NullPointerException("Not implemented!");
    }

    public Collection<Cliente> values() {
        List<Cliente> clienteList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM clientes")) {

            while (rs.next()) {
                Map<String, Carro> carros = getCarrosCliente(rs.getString("Id"), stm);
                Cliente cliente = new Cliente(
                        rs.getString("Id"),
                        rs.getString("Nif"),
                        rs.getString("Morada"),
                        rs.getString("Telefone"),
                        rs.getString("Email"),
                        carros
                );
                clienteList.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }

        return clienteList;
    }


    public Set<Entry<String, Cliente>> entrySet() { throw new NullPointerException("Not implemented!"); }

}