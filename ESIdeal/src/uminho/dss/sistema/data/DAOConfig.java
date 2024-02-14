package uminho.dss.sistema.data;

/**
 * Classe que representa a configuração da base de dados.
 *
 * @author DSS
 * @version 20230915
 */

public class DAOConfig {
    static final String USERNAME = "jfc";
    static final String PASSWORD = "jfc";
    private static final String DATABASE = "esideal";
    private static final String DRIVER = "jdbc:mysql";
    static final String URL = DRIVER+"://localhost:3306/"+DATABASE;
}