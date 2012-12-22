package scmplugin.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.xml.sax.SAXException;

import scmaccess.model.objects.InfoSCM;

public class ManipularXML {
	private File file;

	public ManipularXML() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IPath location = root.getLocation();
		String path = location.toPortableString();
		file = new File( path+"/" + "dataSme.xml");
	}

	private void writeFile(String conteudo) throws IOException {
		FileOutputStream os = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write("<?xml version = \"1.0\" ?>");
		bw.newLine();
		bw.write("<SmeRoot>");
		bw.newLine();
		bw.write(conteudo);
		bw.newLine();
		bw.write("</SmeRoot>");
		bw.flush();
		bw.close();
		os.close();
	}

	private String readFile() throws IOException {
		if (file.exists()) {
			FileInputStream in = new FileInputStream(file);
			InputStreamReader io = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(io);
			String next = "";
			StringBuffer conteudo = new StringBuffer("");
			br.readLine();
			br.readLine();
			while (br.ready()) {
				next = br.readLine();
				if (!next.equalsIgnoreCase("</SmeRoot>"))
					conteudo.append(next + "\n");
			}
			String retorno = conteudo.toString();
			return retorno;
		}
		return "";
	}

	public void adicionarConteudo(InfoSCM info) throws IOException {
		StringBuilder st = new StringBuilder();
		st.append("<Sme>\n");
		st.append("<Name>");
		st.append(info.getSme().getName());
		st.append("</Name>\n");
		st.append("<MainPath>");
		st.append(info.getSme().getMainPath());
		st.append("</MainPath>\n");
		st.append("<SCMType>");
		st.append(info.getSCScmType());
		st.append("</SCMType>\n");
		st.append("<Url>");
		st.append(info.getUrl());
		st.append("</Url>\n");
		st.append("<User>");
		st.append(info.getUser());
		st.append("</User>\n");
		if (info.getPassword() != "") {
			st.append("<Password>");
			st.append(info.getPassword());
			st.append("</Password>\n");
		}
		st.append("</Sme>");
		StringBuilder str = new StringBuilder(readFile());
		str.append(st.toString());
		writeFile(str.toString());
	}

	public List<InfoSCM> getAllInfoScm() {
		if (file.exists()) {
			try {
				SAXParserFactory saxFactory = SAXParserFactory.newInstance();
				SAXParser parserSax;
				parserSax = saxFactory.newSAXParser();
				SmeParserXml smeParse = new SmeParserXml();
				parserSax.parse(file, smeParse);
				return smeParse.getAllSme();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}
		return new ArrayList<InfoSCM>();
	}
	
	public static void renameDiretorio(String path, String reName){
		File fileDir = new File(path);
		if(fileDir.isDirectory()){
			fileDir.renameTo(new File(reName));
		}
	}

	public void editarConteudo(List<InfoSCM> lista) throws IOException {
		StringBuilder resultado = new StringBuilder();
		for(InfoSCM info: lista){
			StringBuilder st = new StringBuilder();
			st.append("<Sme>\n");
			st.append("<Name>");
			st.append(info.getSme().getName());
			st.append("</Name>\n");
			st.append("<MainPath>");
			st.append(info.getSme().getMainPath());
			st.append("</MainPath>\n");
			st.append("<SCMType>");
			st.append(info.getSCScmType());
			st.append("</SCMType>\n");
			st.append("<Url>");
			st.append(info.getUrl());
			st.append("</Url>\n");
			st.append("<User>");
			st.append(info.getUser());
			st.append("</User>\n");
			if (info.getPassword() != "") {
				st.append("<Password>");
				st.append(info.getPassword());
				st.append("</Password>\n");
			}
			
			st.append("</Sme>");
			resultado.append(st.toString());
		}
		writeFile(resultado.toString());
	}
}
