package dubaj.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ModelBase {

	private static String dbURL = "jdbc:derby:myDB;create=true;";
	private static Connection conn = null;
	protected static Statement stmt = null;

	protected static Connection getConnection() {

		if (conn == null) {

			try {
				Class.forName("org.apache.derby.jdbc.EmbeddedDriver")
						.newInstance();
				// Get a connection
				conn = DriverManager.getConnection(dbURL);
				
				createDefaultTables();
				
			} catch (Exception except) {
				except.printStackTrace();
			}

			
		}

		return conn;
	}

	private static void createDefaultTables() {
		// TODO Auto-generated method stub
		createMetricasTable();
		
	}

	private static void createMetricasTable() {
		// TODO Auto-generated method stub
		try
        {
            stmt = getConnection().createStatement();
            stmt.execute("create table metricas(" +
            				"urlProjetoAntiga varchar(400), " +
            				"urlProjetoAtual varchar(400)," +
            				"numeroRevisaoInicial integer, " +
            				"numeroRevisaoFinal integer, " +
            				"mudancasClasse integer, " +
            				"mudancasMetodos integer, " +
            				"mudancasAtributos integer, " +
            				"codeChurn integer," +
            				"linhasCodigo integer)");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            // Caso a tabela já exista
        }
	}
}
