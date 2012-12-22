package dubaj.tr;

import dubaj.tr.Relatorio.TipoDeclaracao;

public class MudancaVO {
	
	private String nomeNovo;
	
	private String nomeAntigo;
	
	private TipoDeclaracao tipoElemento;

	public MudancaVO(String nomeNovo, String nomeAntigo, TipoDeclaracao tipoElemento) {
		this.nomeAntigo = nomeAntigo;
		this.nomeNovo = nomeNovo;
		this.tipoElemento = tipoElemento;
	}
	
	public String getNomeNovo() {
		return nomeNovo;
	}

	public void setNomeNovo(String nomeNovo) {
		this.nomeNovo = nomeNovo;
	}

	public String getNomeAntigo() {
		return nomeAntigo;
	}

	public void setNomeAntigo(String nomeAntigo) {
		this.nomeAntigo = nomeAntigo;
	}

	public TipoDeclaracao getTipoElemento() {
		return tipoElemento;
	}

	public void setTipoElemento(TipoDeclaracao tipoElemento) {
		this.tipoElemento = tipoElemento;
	}
	
	

}
