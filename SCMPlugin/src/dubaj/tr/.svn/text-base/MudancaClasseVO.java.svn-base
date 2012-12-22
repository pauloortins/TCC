package dubaj.tr;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dubaj.tr.contagem.metodo.MudancaMetodo;
import dubaj.util.Util;

public class MudancaClasseVO {

	private String nomClasse = new String();
	private int numeroMudancasAtributo = 0;
	private int numeroMudancasClasse = 0;
	private int numeroCodeChurn = 0;
	private int numeroLinesOfCode = 0;
	private int numeroCommits = 0;
	ArrayList<MudancaMetodo> listaDeMudancasMetodo = new ArrayList<MudancaMetodo>();
	
	public MudancaClasseVO()
	{
		numeroMudancasAtributo = 0;
		numeroMudancasClasse = 0;
		numeroCodeChurn = 0;
		numeroLinesOfCode = 0;
		numeroCommits = 1;
		listaDeMudancasMetodo = new ArrayList<MudancaMetodo>();
		nomClasse = new String();
	}
	
	public String getNomClasse() {
		return nomClasse;
	}

	public void setNomClasse(String nomClasse) {
		this.nomClasse = nomClasse;
	}
	
	public int calcularNumeroCommits()
	{
		return numeroCommits;
	}
	
	public void incrementarNumeroCommits()
	{
		this.numeroCommits++;
	}

	public int calcularMudancasClasse() {
		return numeroMudancasClasse;
	}

	public int calcularMudancasMetodo() {
		int somatorioMudancas = 0;

		for (MudancaMetodo mudancaMetodo : listaDeMudancasMetodo) {
			somatorioMudancas += mudancaMetodo.contar(listaDeMudancasMetodo);
		}

		return somatorioMudancas;
	}

	public int calcularMudancasAtributo() {
		return numeroMudancasAtributo;
	}

	public int calcularMudancasTotais() {
		return calcularMudancasClasse() + calcularMudancasMetodo()
				+ calcularMudancasAtributo();
	}

	public void calcularCodeChurn(File fromFile, File toFile) {
		try {
			int numLinhasArquivoOrigem = Util
					.contarQuantidadeLinhasArquivo(fromFile);
			int numLinhasArquivoDestino = Util
					.contarQuantidadeLinhasArquivo(toFile);

			numeroCodeChurn = Math.abs(numLinhasArquivoDestino - numLinhasArquivoOrigem);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public int obterCodeChurn()
	{
		return numeroCodeChurn;
	}

	public void incrementarMudancasMetodo(MudancaMetodo mudancaDeMetodo) {
		listaDeMudancasMetodo.add(mudancaDeMetodo);
	}
	
	public void incrementarMudancasMetodo(List<MudancaMetodo> listaDeMudancas) {
		listaDeMudancasMetodo.addAll(listaDeMudancas);
	}

	public void incrementarMudancasAtributo() {
		numeroMudancasAtributo++;
	}

	public void incrementarMudancasClasse() {
		numeroMudancasClasse++;
	}
	
	public void setNumeroMudancasAtributo(int numeroMudancasAtributo)
	{
		this.numeroMudancasAtributo = numeroMudancasAtributo;
	}
	
	public void setNumeroMudancasCodeChurn(int numeroCodeChurn) {
		this.numeroCodeChurn = numeroCodeChurn;
	}
	
	public List<MudancaMetodo> getListaDeMudancasMetodo()
	{
		return listaDeMudancasMetodo;
	}
	
	public void setNumeroMudancasClasse(int numeroMudancasClasse){
		this.numeroMudancasClasse = numeroMudancasClasse;
	}

	public void calcularLinesOfCode(File to) {
		try {
			numeroLinesOfCode = Util
					.contarQuantidadeLinhasArquivo(to);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getNumeroLinesOfCode() {
		return numeroLinesOfCode;
	}

	public void setNumeroLinesOfCode(int numeroLinesOfCode) {
		this.numeroLinesOfCode = numeroLinesOfCode;
	}
}
