package scmaccess.model.objects;
import java.util.List;

public class SmeProperties {
	private String name;
	private String mainPath;
	private List<Version> versions;

	
	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setMainPath(String mainPath) {
		this.mainPath = mainPath ;
	}

	public String getMainPath() {
		return mainPath;
	}

	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}

	public List<Version> getVersions() {
		return versions;
	}
	
	public String toString(){
		return this.name;
	}
}
