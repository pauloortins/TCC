package dubaj.internacionalizacao;

public class IdiomaIngles implements IIdioma {

	@Override
	public String getConfigureConnection() {
		// TODO Auto-generated method stub
		return "Configure connection";
	}
	
	@Override
	public String getSCM()
	{
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
		return "User:";
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return "Password:";
	}
	
	@Override
	public String getConnect() {
		// TODO Auto-generated method stub
		return "Connect";
	}
	
	@Override
	public String getConnectionOK() {
		// TODO Auto-generated method stub
		return "Connection OK";
	}

	@Override
	public String getConnectionRefused() {
		// TODO Auto-generated method stub
		return "Connection Refused";
	}

	@Override
	public String getSelectVersions() {
		// TODO Auto-generated method stub
		return "Select Versions";
	}

	@Override
	public String getDestinationFolder() {
		// TODO Auto-generated method stub
		return "Destination Folder:";
	}

	@Override
	public String getBrowser() {
		// TODO Auto-generated method stub
		return "Browse";
	}

	@Override
	public String getNumberOfRevisionsOfRepository() {
		// TODO Auto-generated method stub
		return "Number of revisions of repository:";
	}

	@Override
	public String getInitialRevision() {
		// TODO Auto-generated method stub
		return "Initial Revision:";
	}

	@Override
	public String getFinalRevision() {
		// TODO Auto-generated method stub
		return "Final Revision:";
	}

	@Override
	public String getSCMData() {
		// TODO Auto-generated method stub
		return "SCM Data:";
	}

	@Override
	public String getStartDownload() {
		// TODO Auto-generated method stub
		return "Start Download";
	}

	@Override
	public String getDownloadFinished() {
		// TODO Auto-generated method stub
		return "Download Finished";
	}

	@Override
	public String getDestinationFolderRequired() {
		// TODO Auto-generated method stub
		return "Destination Folder is required.";
	}

	@Override
	public String getInitialRevisionRequired() {
		// TODO Auto-generated method stub
		return "Initial Revision is required.";
	}

	@Override
	public String getFinalRevisionRequired() {
		// TODO Auto-generated method stub
		return "Final Revision is required.";
	}

	@Override
	public String getInitialRevisionInvalid() {
		// TODO Auto-generated method stub
		return "Initial Revision is invalid.";
	}

	@Override
	public String getFinalRevisionInvalid() {
		// TODO Auto-generated method stub
		return "Final Revision is invalid.";
	}

	@Override
	public String getFinalRevisionLesserThanInitialRevision() {
		// TODO Auto-generated method stub
		return "Final Revision must be greater than Initial Revision.";
	}

	@Override
	public String getInitialRevisionLesserThanZeroOrGreaterThanNumberOfRevisions() {
		// TODO Auto-generated method stub
		return "Initial Revision must be greater than zero and smaller than repository number of revisions.";
	}
	
	@Override
	public String getFinalRevisionLesserThanZeroOrGreaterThanNumberOfRevisions() {
		// TODO Auto-generated method stub
		return "Final Revision must be greater than zero and smaller than repository number of revisions.";
	}

	@Override
	public String getDubaJReport() {
		// TODO Auto-generated method stub
		return "JCount Report";
	}

	@Override
	public String getReport() {
		// TODO Auto-generated method stub
		return "Relatório:";
	}

	@Override
	public String getExportToCSV() {
		// TODO Auto-generated method stub
		return "Exportar para CSV";
	}

	@Override
	public String getFileSaveSucessful() {
		// TODO Auto-generated method stub
		return "File save was sucessful !";
	}

	@Override
	public String getClassesChanges() {
		// TODO Auto-generated method stub
		return "Classes Changes: ";
	}

	@Override
	public String getMethodsChanges() {
		// TODO Auto-generated method stub
		return "Methods Changes: ";
	}

	@Override
	public String getAttributesChanges() {
		// TODO Auto-generated method stub
		return "Attributes Changes: ";
	}

	@Override
	public String getDeltaLOC() {
		// TODO Auto-generated method stub
		return "Delta LOC: ";
	}
	
	@Override
	public String getNumberOfCommits() {
		// TODO Auto-generated method stub
		return "Number Of Commits: ";
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return "Class";
	}

	@Override
	public String getSumOfChanges() {
		// TODO Auto-generated method stub
		return "Changes Sum";
	}

	@Override
	public String getLinesOfCode() {
		// TODO Auto-generated method stub
		return "Lines Of Code: ";
	}

	@Override
	public String getDeltaLOCRelativo() {
		// TODO Auto-generated method stub
		return "Relative Delta LOC: ";
	}
}
