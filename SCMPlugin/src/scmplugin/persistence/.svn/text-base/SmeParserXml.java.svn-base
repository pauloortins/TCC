package scmplugin.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import scmaccess.model.objects.InfoSCM;
import scmaccess.model.objects.Version;

public class SmeParserXml extends DefaultHandler {
	private Stack<String> elementStack = new Stack<String>();
	private List<InfoSCM> list = new ArrayList<InfoSCM>();
	private InfoSCM info;
	private int order;
	public SmeParserXml() {
		super();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.elementStack.push(qName);
		if (qName.equalsIgnoreCase("Sme")) {
			info = new InfoSCM();
		}
		if (qName.equalsIgnoreCase("SmeVersions")) {
			info.getSme().setVersions(new ArrayList< Version>());
			order = 1;
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {

		String value = new String(ch, start, length).trim();
		if (value.length() == 0)
			return; // ignore white space

		if ("Name".equalsIgnoreCase(currentElement())) {
			info.getSme().setName(value);
		} else if ("MainPath".equalsIgnoreCase(currentElement())) {
			info.getSme().setMainPath(value);
		} else if ("SCMType".equalsIgnoreCase(currentElement())) {
			if (value.equalsIgnoreCase("Subversion"))
				info.setSCMTypes(0);
			else if (value.equalsIgnoreCase("GIT"))
				info.setSCMTypes(1);
			else
				info.setSCMTypes(-1);
		} else if ("Url".equalsIgnoreCase(currentElement())) {
			info.setUrl(value);
		} else if ("User".equalsIgnoreCase(currentElement())) {
			info.setUser(value);
		} else if ("Password".equalsIgnoreCase(currentElement())) {
			info.setPassword(value);
		} else if ("Version".equalsIgnoreCase(currentElement())) {
			List<Version> lista = info.getSme().getVersions();
			lista.add(new Version(value,order));
			info.getSme().setVersions(lista);
			order++;
		}
	}

	private String currentElement() {
		return this.elementStack.peek();
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		this.elementStack.pop();
		if (qName.equalsIgnoreCase("Sme")) {
			list.add(info);
			info = null;
		}
	}

	public List<InfoSCM> getAllSme() {
		return list;
	}
}
