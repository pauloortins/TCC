package dubaj.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Metrica extends ModelBase {
	
	private String urlProjetoAntiga;
	private String urlProjetoAtual;
	private int numeroRevisaoInicial;
	private int numeroRevisaoFinal;
	private int mudancasClasse;
	private int mudancasMetodos;
	private int mudancasAtributos;
	private int codeChurn;
	private int linhasCodigo;
	
	public String getUrlProjetoAntiga() {
		return urlProjetoAntiga;
	}

	public void setUrlProjetoAntiga(String urlProjetoAntiga) {
		this.urlProjetoAntiga = urlProjetoAntiga;
	}

	public String getUrlProjetoAtual() {
		return urlProjetoAtual;
	}

	public void setUrlProjetoAtual(String urlProjetoAtual) {
		this.urlProjetoAtual = urlProjetoAtual;
	}

	public int getNumeroRevisaoInicial() {
		return numeroRevisaoInicial;
	}
	
	public void setNumeroRevisaoInicial(int numeroRevisaoInicial) {
		this.numeroRevisaoInicial = numeroRevisaoInicial;
	}
	
	public int getNumeroRevisaoFinal() {
		return numeroRevisaoFinal;
	}
	
	public void setNumeroRevisaoFinal(int numeroRevisaoFinal) {
		this.numeroRevisaoFinal = numeroRevisaoFinal;
	}
	
	public int getMudancasClasse() {
		return mudancasClasse;
	}
	
	public void setMudancasClasse(int mudancasClasse) {
		this.mudancasClasse = mudancasClasse;
	}
	
	public int getMudancasMetodos() {
		return mudancasMetodos;
	}
	
	public void setMudancasMetodos(int mudancasMetodos) {
		this.mudancasMetodos = mudancasMetodos;
	}
	
	public int getMudancasAtributos() {
		return mudancasAtributos;
	}
	
	public void setMudancasAtributos(int mudancasAtributos) {
		this.mudancasAtributos = mudancasAtributos;
	}
	
	public int getCodeChurn() {
		return codeChurn;
	}
	
	public void setCodeChurn(int codeChurn) {
		this.codeChurn = codeChurn;
	}
	
	public static List<Metrica> ObterPorFiltro(Metrica filter) {
		return null;
	}
	
	public static List<Metrica> SelectByFilter(Metrica metrica) {
		
		List<Metrica> list = new ArrayList<Metrica>();
		
		try
        {
            stmt = getConnection().createStatement();
            
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT ");
            sql.append("urlProjetoAntiga, ");
            sql.append("urlProjetoAtual,");
            sql.append("numeroRevisaoInicial, ");
            sql.append("numeroRevisaoFinal, ");
            sql.append("mudancasClasse, ");
            sql.append("mudancasMetodos, ");
            sql.append("mudancasAtributos, ");
            sql.append("codeChurn, ");
            sql.append("linhasCodigo ");
            sql.append("FROM metricas ");
            sql.append("where 1=1 ");
            
            if (metrica.getUrlProjetoAntiga() != null && !metrica.getUrlProjetoAntiga().equals("")) {
            	sql.append(" AND urlProjetoAntiga = '" + metrica.getUrlProjetoAntiga() + "' ");
            }
            
            if (metrica.getUrlProjetoAtual() != null && !metrica.getUrlProjetoAtual().equals("")) {
            	sql.append(" AND urlProjetoAtual = '" + metrica.getUrlProjetoAtual() + "' ");
            }
            
            if (metrica.getNumeroRevisaoInicial() != 0) {
            	sql.append(" AND numeroRevisaoInicial = " + metrica.getNumeroRevisaoInicial());
            }
            
            if (metrica.getNumeroRevisaoFinal() != 0) {
            	sql.append(" AND numeroRevisaoFinal = " + metrica.getNumeroRevisaoFinal());
            }
            
            ResultSet results = stmt.executeQuery(sql.toString());	
            
            while(results.next())
            {
            	Metrica metricaResult = new Metrica();
            	metricaResult.urlProjetoAntiga = results.getString("urlProjetoAntiga");
            	metricaResult.urlProjetoAtual = results.getString("urlProjetoAtual");
            	metricaResult.numeroRevisaoInicial = results.getInt("numeroRevisaoInicial");
            	metricaResult.numeroRevisaoFinal = results.getInt("numeroRevisaoFinal");
            	metricaResult.mudancasClasse = results.getInt("mudancasClasse");
            	metricaResult.mudancasMetodos = results.getInt("mudancasMetodos");
            	metricaResult.mudancasAtributos = results.getInt("mudancasAtributos");
            	metricaResult.codeChurn = results.getInt("codeChurn");
            	metricaResult.linhasCodigo = results.getInt("linhasCodigo");
            	
                list.add(metricaResult);
            }
            
            results.close();
            stmt.close();
            
            return list;
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        
		return list;
	}
	
	public static void Insert(Metrica metrica) {
		try
        {
            stmt = getConnection().createStatement();
            
            StringBuilder sql = new StringBuilder();
            
            sql.append("INSERT INTO metricas ");
            sql.append("( ");
            sql.append("urlProjetoAntiga, ");
            sql.append("urlProjetoAtual,");
            sql.append("numeroRevisaoInicial, ");
            sql.append("numeroRevisaoFinal, ");
            sql.append("mudancasClasse, ");
            sql.append("mudancasMetodos, ");
            sql.append("mudancasAtributos, ");
            sql.append("codeChurn, ");
            sql.append("linhasCodigo ");
            sql.append(") ");
            sql.append("values ");
            sql.append("( ");
            sql.append("'" + metrica.urlProjetoAntiga + "', ");
            sql.append("'" + metrica.urlProjetoAtual + "', ");
            sql.append(metrica.numeroRevisaoInicial + ", ");
            sql.append(metrica.numeroRevisaoFinal + ", ");
            sql.append(metrica.mudancasClasse + ", ");
            sql.append(metrica.mudancasMetodos + ", ");
            sql.append(metrica.mudancasAtributos + ", ");
            sql.append(metrica.codeChurn + ", ");
            sql.append(metrica.linhasCodigo);
            sql.append(")");
            
            stmt.execute(sql.toString());
            
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
	}
	
	public static void deleteAll() {
		try
        {
            stmt = getConnection().createStatement();
            
            StringBuilder sql = new StringBuilder();
            
            sql.append("DELETE FROM metricas ");
            
            stmt.execute(sql.toString());
            
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
	}

	public int getLinhasCodigo() {
		return linhasCodigo;
	}

	public void setLinhasCodigo(int linhasCodigo) {
		this.linhasCodigo = linhasCodigo;
	}
}
