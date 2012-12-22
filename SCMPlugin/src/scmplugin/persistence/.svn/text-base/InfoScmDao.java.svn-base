package scmplugin.persistence;

import java.io.IOException;
import java.util.List;

import scmaccess.model.objects.InfoSCM;

public class InfoScmDao {

	private ManipularXML xml;

	public InfoScmDao(){
		xml = new ManipularXML();
	}

	public void save(InfoSCM info) throws IOException{
		xml.adicionarConteudo(info);
	}
	
	public void renameDir(String path, String newNameDir){
		ManipularXML.renameDiretorio(path, newNameDir);
	}
	
	public List<InfoSCM> getAllInfoScm(){
		return xml.getAllInfoScm();
	}

	public void edit(InfoSCM info,InfoSCM newInfo) throws IOException {
		List<InfoSCM> lista = getAllInfoScm();
		for(InfoSCM infoIndex: lista){
			if(infoIndex.getSme().getName().equals(info.getSme().getName())){
				infoIndex.getSme().setName(newInfo.getSme().getName());
				infoIndex.getSme().setVersions(newInfo.getSme().getVersions());
				break;
			}
		}
		xml.editarConteudo(lista);
	}
	
	public void delete(InfoSCM info) throws IOException {
		
		InfoSCM deleted = null;
		List<InfoSCM> lista = getAllInfoScm();
		
		for(InfoSCM infoIndex: lista){
			if(infoIndex.getSme().getName().equals(info.getSme().getName()))
			{
				deleted = infoIndex;
			}
		}
		
		lista.remove(deleted);
		xml.editarConteudo(lista);
	}
}
