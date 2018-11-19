
import schach.backend.BackendSpielAdminStub;
import schach.backend.BackendSpielStub;
import schach.daten.Xml;

public class ErsterTest {
	public static void main(String[] args) {
		int id_spiel=10;
		String url="http://www.game-engineering.de:8080/rest/";
		boolean logging=true;
	
		BackendSpielAdminStub admin=new BackendSpielAdminStub(url,logging);
		BackendSpielStub spiel=new BackendSpielStub(url,logging);

		Xml.toD(admin.neuesSpiel(id_spiel));
		Xml.toD(spiel.ziehe(id_spiel,"b2","b4"));		
		Xml.toD(spiel.getAktuelleBelegung(id_spiel));
	}
}
