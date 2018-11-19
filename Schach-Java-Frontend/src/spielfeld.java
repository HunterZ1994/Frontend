import java.io.*;
import java.util.Scanner;

import schach.backend.BackendSpielAdminStub;
import schach.backend.BackendSpielStub;
import schach.daten.Xml;

/*To-do-Liste
 
 * 1. spiel immer aktuell halten(if server schickt was?)
1.( spiel speichern )
2. spiel laden
3.Zug historie anzeigen
4. Gehe zu Zug nr.xx
5. farbzüge überprüfen!!!!!
b7

5. 1

*/
public class spielfeld {
	static int id;
	
	static PrintStream originalStream = System.out;		//ausgabe auf der konsole

	static PrintStream dummyStream = new PrintStream(new OutputStream(){	//ausgabe in einem stream, der nirgendwo ausegegeben wird xD
	    public void write(int b) {	//muss man haben
	        // NO-OP
	    }
	});

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setOut(originalStream);
		
		
		
		
		deco();
		System.out.println("Was möchten sie tun?");
		System.out.println("1 Neues Spiel");
		System.out.println("2 Laden");
		Scanner eingabe = new Scanner(System.in);
		int wahl = eingabe.nextInt();

		System.out.println("Auf welcher ID möchten Sie spielen?");

		id = eingabe.nextInt();
		
		System.setOut(dummyStream);
		
		String url = "http://www.game-engineering.de:8080/rest/";
		boolean logging = true;
		BackendSpielAdminStub admin = new BackendSpielAdminStub(url, logging);
		BackendSpielStub spiel = new BackendSpielStub(url, logging);
		Xml.toD(admin.neuesSpiel(id));
		feld schachbrett = new feld();
		
		 
		

		switch (wahl) {
		case 1:
			spielen(id, spiel, schachbrett, admin);
			eingabe.close();
			break;

		case 2:
			System.out.println("Lade Spieldaten...");
			load(admin, id);
			break;
		default:
			System.out.println("Ungültige Eingabe.");
			String[] t = new String[1];
			main(t);

		}

	}

	public static void save(BackendSpielAdminStub spiel, int id) {
		System.out.println("Daten werden gespeichert. Bitte schalten sie die Konsole nicht aus...");
		spiel.speichernSpiel(id, "spiel");

	}

	/**
	 * 
	 * @param spiel
	 */
	public static void load(BackendSpielAdminStub spiel, int id) {
		System.setOut(originalStream);
		spiel.ladenSpiel(id, "spiel");
	}

	public static boolean fertig(String meldung) {
		if (meldung.contains("SchwarzSchachMatt")) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("!!!SCHWARZ HAT GEWONNEN!!!");
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
			return true;
		}
		if (meldung.contains("WeissSchachMatt")) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("!!!WEISS HAT GEWONNEN!!!");
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
			return true;
		}
		return false;
	}

	/**
	 * fuuuuu
	 * 
	 * @param id_spiel
	 * @param spiel
	 * @param schachbrett
	 * @param admin
	 */
	public static void spielen(int id, BackendSpielStub spiel, feld schachbrett, BackendSpielAdminStub admin) {
		
		
		String alteDaten=spiel.getSpielDaten(id);
		Scanner eingabe = new Scanner(System.in);
		
		while (true) {
			if(!alteDaten.equals(spiel.getSpielDaten(id))) {
				schachbrett.print(schachbrett, spiel, id);
				alteDaten=spiel.getSpielDaten(id);
			}
			
			System.setOut(dummyStream);
			String daten = spiel.getSpielDaten(id);
			System.setOut(originalStream);
			matt(daten);
			if (fertig(daten)) {
				break;
			}
			
			schachbrett.print(schachbrett, spiel, id);
			
			System.out.println("Weiß am Zug.");

			String zug = ziehen(eingabe, spiel, admin, id);
			System.out.println(spiel.getSpielDaten(id));

			System.setOut(dummyStream);
			daten = spiel.getSpielDaten(id);
			System.setOut(originalStream);
			matt(daten);
			if (fertig(daten)) {
				break;
			}

			while (zug.contains("D_Fehler")) {
				System.out.println("Ungültiger Zug, Weiß bitte nochmal ziehen.");
				zug = ziehen(eingabe, spiel, admin, id);
			}

			System.setOut(dummyStream);
			daten = spiel.getSpielDaten(id);
			System.setOut(originalStream);
			matt(daten);
			if (fertig(daten)) {
				break;
			}

			schachbrett.print(schachbrett, spiel, id);
			//spiel.getSpielDaten(id);
			System.out.println("Schwarz am Zug.");
			zug = ziehen(eingabe, spiel, admin, id);
			System.out.println(spiel.getSpielDaten(id));
			
			System.setOut(dummyStream);
			daten = spiel.getSpielDaten(id);
			System.setOut(originalStream);
			matt(daten);
			if (fertig(daten)) {
				break;
			}

			while (zug.contains("D_Fehler")) {
				System.out.println("Ungültiger Zug, Schwarz bitte nochmal ziehen.");
				zug = ziehen(eingabe, spiel, admin, id);
			}
			}
		}
	

	public static void matt(String getDaten) {
		if (weissMatt(getDaten)) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("!!!WEISS IST MATT!!!");
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!");

		}
		if (schwarzMatt(getDaten)) {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("!!!SCHWARZ IST MATT!!!");
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!");

		}

	}

	public static boolean weissMatt(String getDaten) {
		if (getDaten.contains("SchwarzImSchach"))
			return true;
		return false;
	}

	public static boolean schwarzMatt(String getDaten) {
		if (getDaten.contains("WeissImSchach"))
			return true;
		return false;
	}

	public static String ziehen(Scanner eingabe, BackendSpielStub spiel, BackendSpielAdminStub admin, int id) {

		System.out.println("Welche Figur soll ziehen?");
		String von = eingabe.nextLine();
		if (von.contains("save")) {
			save(admin, id);
			System.out.println("Welche Figur soll ziehen?");
			von = eingabe.nextLine();
		} else if (von.contains("Zughistorie")) {
			System.out.println(zugHistorie(spiel, id));
			System.out.println("Welche Figur soll ziehen?");
			von = eingabe.nextLine();
		}
		System.out.println("Wohin soll sie ziehen?");
		String nach = eingabe.nextLine();
		System.setOut(dummyStream);
		
		String zug=spiel.ziehe(id, von, nach);
		System.setOut(originalStream);
		
	
		return zug;// XMLtoD?!?!

	}

	public static String zugHistorie(BackendSpielStub spiel, int id) {
		return spiel.getZugHistorie(id);

	}

	public static void getFigurDatenUndSetze(String posAufSchachMatrix, feld schachbrett, BackendSpielStub spiel,
			int id) {

		// extrahiert zuerst aus dem String der bei getFigur geliefert wird die
		// einzelnen daten
		// Xml.toD(spiel.getFigur(5, posAufSchachMatrix));
		String figurDaten = spiel.getFigur(id, posAufSchachMatrix); // holt FigurDaten
		char[] figurDatenArray = figurDaten.toCharArray(); // speichertFigurDaten in array
		String posVonRestAnfrage = figurDatenArray[161] + "" + figurDatenArray[162]; // speichert nochmal position
		char typVonRestAnfrage = figurDatenArray[190];
		

		// mit diesen Daten (pos, typ, farbe) ruft man dann die setzen methdoe auf

		figurSetzen(schachbrett, posVonRestAnfrage, typVonRestAnfrage, weiss(figurDatenArray, typVonRestAnfrage));

	}

	public static void figurSetzen(feld schachbrett, String einePos, char AnfangsBuchstabeTyp, boolean schwarz) {
		// je nach typ wird die richtige methode mit der position und farbe aufgerufen

		String typ = welcherTyp(AnfangsBuchstabeTyp);

		if (typ == "Turm") {

			setzeTurm(schachbrett, einePos, schwarz);

		} else if (typ == "Bauer") {
			setzeBauer(schachbrett, einePos, schwarz);
		} else if (typ == "Dame") {
			setzeDame(schachbrett, einePos, schwarz);
		} else if (typ == "Läufer") {
			setzeLäufer(schachbrett, einePos, schwarz);
		} else if (typ == "König") {
			setzeKönig(schachbrett, einePos, schwarz);
		} else if (typ == "Springer") {
			setzeSpringer(schachbrett, einePos, schwarz);
		} /*
			 * else if(typ =="Leer") { zelleLeeren(schachbrett, einePos); }
			 */
	}

	// brauchen wir gar nicht mehr, wollte sie aber noch nicht löschen
	public static void StringZuArray(String eingabe, feld schachbrett) { // A1 -->65 49 a1 -->97 49

		if (eingabe.charAt(0) > 90) {
			schachbrett.schachmatrix[7 - (eingabe.charAt(1) - 49)][eingabe.charAt(0) - 97] = "X_|";
		} else {
			schachbrett.schachmatrix[7 - (eingabe.charAt(1) - 49)][eingabe.charAt(0) - 65] = "X_|";
		}
	}

//diese Methode wandeln den string zB b2 in eine array position um, überprüfen die farbe und
//schreiben das array dann dementsprechend um
	public static void setzeBauer(feld schachbrett, String pos, boolean schwarz) { // zerstückelt übergebenen string in
																					// array position
		int x, y;
		x = 7 - (pos.charAt(1) - 49);
		y = pos.charAt(0) - 97;
		if (schwarz)
			schachbrett.schachmatrix[x][y] = "|BW";
		if (!schwarz) {
			schachbrett.schachmatrix[x][y] = "|BS";
		}
	}

	public static void setzeTurm(feld schachbrett, String pos, boolean weiss) {

		int x, y; // Umwandlung vono String A1 nach int 01
		x = 7 - (pos.charAt(1) - 49);
		y = pos.charAt(0) - 97;

		if (weiss) {
			schachbrett.schachmatrix[x][y] = "|TW";

		} else if (!weiss) {

			schachbrett.schachmatrix[x][y] = "|TS";
		}

	}

	public static void setzeSpringer(feld schachbrett, String pos, boolean schwarz) {

		int x, y;
		x = 7 - (pos.charAt(1) - 49);
		y = pos.charAt(0) - 97;

		if (schwarz) {
			schachbrett.schachmatrix[x][y] = "|SW";
		} else if (!schwarz) {

			schachbrett.schachmatrix[x][y] = "|SS";
		}

	}

	public static void setzeLäufer(feld schachbrett, String pos, boolean schwarz) {

		int x, y;
		x = 7 - (pos.charAt(1) - 49);
		y = pos.charAt(0) - 97;

		if (schwarz) {
			schachbrett.schachmatrix[x][y] = "|LW";
		} else if (!schwarz) {

			schachbrett.schachmatrix[x][y] = "|LS";
		}

	}

	public static void setzeDame(feld schachbrett, String pos, boolean schwarz) {

		int x, y;
		x = 7 - (pos.charAt(1) - 49);
		y = pos.charAt(0) - 97;

		if (schwarz) {
			schachbrett.schachmatrix[x][y] = "|DW";
		} else if (!schwarz) {

			schachbrett.schachmatrix[x][y] = "|DS";
		}

	}

	public static void setzeKönig(feld schachbrett, String pos, boolean schwarz) {

		int x, y;
		x = 7 - (pos.charAt(1) - 49);
		y = pos.charAt(0) - 97;

		if (schwarz) {
			schachbrett.schachmatrix[x][y] = "|KW";
		} else if (!schwarz) {

			schachbrett.schachmatrix[x][y] = "|KS";
		}

	}

//entscheidet nach buchstabe welcher typ es ist
	public static String welcherTyp(char anfangsBuchstabe) {

		if (anfangsBuchstabe == 'B') {

			return "Bauer";
		} else if (anfangsBuchstabe == 'D') {
			return "Dame";
		} else if (anfangsBuchstabe == 'T')
			return "Turm";
		else if (anfangsBuchstabe == 'S')
			return "Springer";
		else if (anfangsBuchstabe == 'L')
			return "Läufer";
		else if (anfangsBuchstabe == 'K')
			return "König";
		else if (anfangsBuchstabe == 'r') {

			return "Leer";
		}
		return "Error";
	}

	public static boolean weiss(char[] figurDatenArray, char typ) {
		// da farbe erst nach typ kommt, und zB Laeufer länger ist als Turm, beginnt bei
		// jedem Typ die Farbe an
		// einer leicht unterschiedlichen Stelle
		if (typ == 'B') {
			char farbe = figurDatenArray[226];
			if (farbe == 't') // wenn farbe == t(rue) ist, ist sie weiss
				return true;
			return false;
		}

		else if (typ == 'S') {
			char farbe = figurDatenArray[229];
			if (farbe == 't')
				return true;
			return false;
		}

		else if (typ == 'T') {
			char farbe = figurDatenArray[225];
			if (farbe == 't')
				return true;
			return false;
		}

		else if (typ == 'K') {
			char farbe = figurDatenArray[227];
			if (farbe == 't')
				return true;
			return false;
		} else if (typ == 'D') {
			char farbe = figurDatenArray[225];
			if (farbe == 't')
				return true;
			return false;
		}

		return true;// nur weil sonst error
	}

	public static void deco() {
		System.out.println("                                                  _:_ ");
		System.out.println("                                                 '-.-' ");
		System.out.println("                                        ()      __.'.__ ");
		System.out.println("                                     .-:--:-.  |_______| ");
		System.out.println("                              ()     \\____/   \\=====/  ");
		System.out.println("                              /\\     {====}     )___( ");
		System.out.println("                   (\\=,     //\\\\    )__(     /_____\\ ");
		System.out.println("   __    |'-'-'|  //  .\\   (    )    /____\\    |   | ");
		System.out.println("  /  \\  |_____| (( \\  \\   )__(      |  |      |   | ");
		System.out.println("  \\_/    |===|   ))  `\\)  /____\\    |  |      |   | ");
		System.out.println(" /____\\  |   |  (/     \\   |  |      |  |      |   | ");
		System.out.println("  |  |    |   |   | _.-'|    |  |      |  |      |   | ");
		System.out.println("  |__|    )___(    )___(    /____\\   /____\\   /_____\\");
		System.out.println(" (====)  (=====)  (=====)  (======)  (======)  (=======) ");
		System.out.println(" }===={  }====={  }====={  }======{  }======{  }======={ ");
		System.out.println("(______)(_______)(_______)(________)(________)(_________)	");

	}
}