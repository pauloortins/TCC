package dubaj.internacionalizacao;

public class IdiomaPortugues implements IIdioma {

	@Override
	public String getConfigureConnection() {
		// TODO Auto-generated method stub
		return "Configure a conex�o";
	}

	@Override
	public String getSCM() {
		// TODO Auto-generated method stub
		return "SCM:";
	}
	
	@Override
	public String getSubVersion() {
		// TODO Auto-generated method stub
		return "SubVersion";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "Url:";
	}

	@Override
	public String getUser() {
		// TODO Auto-generated method stub
		return "Usu�rio:";
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return "Senha:";
	}

	@Override
	public String getConnect() {
		// TODO Auto-generated method stub
		return "Connect";
	}

	@Override
	public String getConnectionOK() {
		// TODO Auto-generated method stub
		return "Conex�o OK";
	}

	@Override
	public String getConnectionRefused() {
		// TODO Auto-generated method stub
		return "Conex�o Negada";
	}

	@Override
	public String getSelectVersions() {
		// TODO Auto-generated method stub
		return "Selecionar Vers�es";
	}

	@Override
	public String getDestinationFolder() {
		// TODO Auto-generated method stub
		return "Pasta de Destino:";
	}

	@Override
	public String getBrowser() {
		// TODO Auto-generated method stub
		return "Escolher";
	}

	@Override
	public String getNumberOfRevisionsOfRepository() {
		// TODO Auto-generated method stub
		return "N�mero de revis�es do reposit�rio:";
	}

	@Override
	public String getInitialRevision() {
		// TODO Auto-generated method stub
		return "Revis�o Inicial:";
	}

	@Override
	public String getFinalRevision() {
		// TODO Auto-generated method stub
		return "Revis�o Final:";
	}

	@Override
	public String getSCMData() {
		// TODO Auto-generated method stub
		return "Dados da SCM:";
	}

	@Override
	public String getStartDownload() {
		// TODO Auto-generated method stub
		return "Fazer Download";
	}

	@Override
	public String getDownloadFinished() {
		// TODO Auto-generated method stub
		return "Download Finalizado";
	}

	@Override
	public String getDestinationFolderRequired() {
		// TODO Auto-generated method stub
		return "Pasta de Destino � Obrigat�ria.";
	}

	@Override
	public String getInitialRevisionRequired() {
		// TODO Auto-generated method stub
		return "Revis�o Inicial � Obrigat�ria.";
	}

	@Override
	public String getFinalRevisionRequired() {
		// TODO Auto-generated method stub
		return "Revis�o Final � Obrigat�ria";
	}

	@Override
	public String getInitialRevisionInvalid() {
		// TODO Auto-generated method stub
		return "Revis�o Inicial est� invalida.";
	}

	@Override
	public String getFinalRevisionInvalid() {
		// TODO Auto-generated method stub
		return "Revis�o Final est� invalida.";
	}

	@Override
	public String getFinalRevisionLesserThanInitialRevision() {
		// TODO Auto-generated method stub
		return "Revis�o Final deve ser maior que a Revis�o Inicial";
	}

	@Override
	public String getFinalRevisionLesserThanZeroOrGreaterThanNumberOfRevisions() {
		// TODO Auto-generated method stub
		return "Revis�o Final deve ser maior que zero e menor que o n�mero de revis�es do reposit�rio.";
	}

	@Override
	public String getInitialRevisionLesserThanZeroOrGreaterThanNumberOfRevisions() {
		// TODO Auto-generated method stub
		return "Revis�o Inicial deve ser maior que zero e menor que o n�mero de revis�es do reposit�rio.";
	}

	@Override
	public String getDubaJReport() {
		// TODO Auto-generated method stub
		return "Relat�rio DubaJ";
	}

	@Override
	public String getReport() {
		// TODO Auto-generated method stub
		return "Relat�rio:";
	}

	@Override
	public String getExportToCSV() {
		// TODO Auto-generated method stub
		return "Exportar para CSV";
	}

	@Override
	public String getFileSaveSucessful() {
		// TODO Auto-generated method stub
		return "Arquivo Salvo com Sucesso !";
	}

	@Override
	public String getClassesChanges() {
		// TODO Auto-generated method stub
		return "Mudan�as de Classe: ";
	}

	@Override
	public String getMethodsChanges() {
		// TODO Auto-generated method stub
		return "Mudan�as de M�todo: ";
	}

	@Override
	public String getAttributesChanges() {
		// TODO Auto-generated method stub
		return "Mudan�as de Atributo: ";
	}

	@Override
	public String getDeltaLOC() {
		// TODO Auto-generated method stub
		return "Delta LOC: ";
	}
	
	@Override
	public String getNumberOfCommits() {
		// TODO Auto-generated method stub
		return "N�mero de Commits: ";
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return "Classe";
	}

	@Override
	public String getSumOfChanges() {
		// TODO Auto-generated method stub
		return "Soma das Mudan�as";
	}

	@Override
	public String getLinesOfCode() {
		// TODO Auto-generated method stub
		return "Linhas de C�digo: ";
	}

	@Override
	public String getDeltaLOCRelativo() {
		// TODO Auto-generated method stub
		return "Delta LOC Relativo: ";
	}

}
