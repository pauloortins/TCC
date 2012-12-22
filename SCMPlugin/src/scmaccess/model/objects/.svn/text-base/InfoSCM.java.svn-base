package scmaccess.model.objects;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InfoSCM {
	private String url = "";
	private String user = "";
	private String password = "";
	private int scmTypes;
	private SmeProperties sme;


	
	public InfoSCM() {
		sme = new SmeProperties();
	}

	public InfoSCM(int scmpType, String url, String user) {
		setSCMTypes(scmpType);
		setUrl(url);
		setUser(user);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPassword(String password) {
		this.password = md5(password);
	}

	public String getPassword() {
		return md5(password);
	}

	public void setSCMTypes(int i) {
		this.scmTypes = i;
	}

	public String getSCScmType() {
		switch (this.scmTypes) {
		case 0:
			return new String("Subversion");
		case 1:
			return new String("Git");
		default:
			// TODO
			return null;
		}
	}

	public SmeProperties getSme() {
		return this.sme;
	}

	public void setSme(SmeProperties sme) {
		this.sme = sme;
	}

	public static String md5(String senha) {
		String sen = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
		sen = hash.toString(16);
		return sen;
	}
}
