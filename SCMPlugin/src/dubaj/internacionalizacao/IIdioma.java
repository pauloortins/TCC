package dubaj.internacionalizacao;

public interface IIdioma {
	
	// Labels da tela ViewConfigureConnection
	public String getConfigureConnection();	
	public String getSCM();
	public String getSubVersion();
	public String getURL();
	public String getUser();
	public String getPassword();
	public String getConnect();
	public String getConnectionOK();
	public String getConnectionRefused();
	
	// Labels da tela ViewSelectVersions
	public String getSelectVersions();
	public String getDestinationFolder();
	public String getBrowser();
	public String getNumberOfRevisionsOfRepository();
	public String getInitialRevision();
	public String getFinalRevision();
	public String getSCMData();
	public String getStartDownload();
	public String getDownloadFinished();
	public String getDestinationFolderRequired();
	public String getInitialRevisionRequired();
	public String getFinalRevisionRequired();
	public String getInitialRevisionInvalid();
	public String getFinalRevisionInvalid();
	public String getFinalRevisionLesserThanInitialRevision();
	public String getInitialRevisionLesserThanZeroOrGreaterThanNumberOfRevisions();
	public String getFinalRevisionLesserThanZeroOrGreaterThanNumberOfRevisions();
	
	// Labels da tela ViewShowReport
	public String getDubaJReport();
	public String getReport();
	public String getExportToCSV();	
	public String getFileSaveSucessful();
	public String getClassesChanges();
	public String getMethodsChanges();
	public String getAttributesChanges();
	public String getDeltaLOC();
	public String getLinesOfCode();
	public String getDeltaLOCRelativo();
	public String getNumberOfCommits();
	
	// Labels do Arquivo CSV
	public String getClassName();
	public String getSumOfChanges();
	
	
}
