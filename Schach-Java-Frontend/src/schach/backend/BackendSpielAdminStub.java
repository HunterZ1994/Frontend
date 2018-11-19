package schach.backend;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.ws.rs.client.Client;

import interfaces.iBackendSpielAdmin;
import schach.daten.D;
import schach.daten.Xml;
import utils.BackendUtils;

public class BackendSpielAdminStub implements iBackendSpielAdmin {
	private static final String urlUnterPfad = "schach/spiel/admin/";
	private String url;
	private Client client;
	private boolean logging;

	public BackendSpielAdminStub(String url,boolean logging) {
		if (url.endsWith("/"))
			this.url = url + urlUnterPfad;
		else
			this.url = url + "/" + urlUnterPfad;
		this.logging=logging;
		try {
			this.client = BackendUtils.ignoreSSLClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getXmlvonRest(String pfad) {
		String anfrage = url + pfad;
		if (logging)
			System.out.println("CLIENT ANFRAGE: " + anfrage);
		String s = client.target(anfrage).request().accept("application/xml").get(String.class);
		if (logging) {
			ArrayList<D> daten = Xml.toArray(s);
			System.out.println(daten);
		}
		return s;
	}

	@Override
	public String neuesSpiel(int id) {
		return getXmlvonRest("neuesSpiel/"+id);
	}

	@Override
	public String speichernSpiel(int id,String pfad) {
		try {
			return getXmlvonRest("speichernSpiel"+"/"+id+"/"+URLEncoder.encode(""+pfad,"ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String ladenSpiel(int id,String pfad) {
		try {
			return getXmlvonRest("ladenSpiel"+"/"+id+"/"+URLEncoder.encode("" + pfad, "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
