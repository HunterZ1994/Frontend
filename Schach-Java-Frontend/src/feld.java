
public class feld {
	public String[][] schachmatrix; // auf der schachmatrix wird alles dargestellt
	final int width = 8;
	final int height = 8;

	feld() { // erstellt ganz am anfang das spielfeld

		this.schachmatrix = new String[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i % 2 == 0 && (j % 2 == 0)) {
					this.schachmatrix[i][j] = "|__"; // ~jedes (un)gerade feld jeder (un)gerade zeile
				} // wird schwarz/weiß
				if (i % 2 == 1 && (j % 2 == 0)) {
					this.schachmatrix[i][j] = "|#_";
				}
				if (i % 2 == 0 && (j % 2 == 1)) {
					this.schachmatrix[i][j] = "|#_";
				}
				if (i % 2 == 1 && (j % 2 == 1)) {
					this.schachmatrix[i][j] = "|__";
				}

			}
		}

	}

	public void print(feld schachbrett,  int id) { // diese print()methode beachtet die aktuelle
																			// belegung

		String[][] neueMatrix = new String[8][8]; // hier wird eine neue MAtrix erstellt, die dann
		this.schachmatrix = neueMatrix; // gleich mit der leeren Belegung gefüllt wird
										// auf dieses leere Schachmatrix werden die aktuellen Figuren
		for (int i = 0; i < width; i++) { // draufgesetzt, so bekommt man keine Probleme, die fortgerückte
			for (int j = 0; j < height; j++) { // Figur auf ihrer "von" Position zu löschen
				if (i % 2 == 0 && (j % 2 == 0)) {
					this.schachmatrix[i][j] = "|__";
				}
				if (i % 2 == 1 && (j % 2 == 0)) {
					this.schachmatrix[i][j] = "|#_";
				}
				if (i % 2 == 0 && (j % 2 == 1)) {
					this.schachmatrix[i][j] = "|#_";
				}
				if (i % 2 == 1 && (j % 2 == 1)) {
					this.schachmatrix[i][j] = "|__";
				}

			}
		}

		for (int i = 1; i < 9; i++) { // hier wird jede einzelne Position durchgegeangen
			for (int j = 0; j < 8; j++) { // mit ihr wirdgetFigurDatenUndSetze aufgerufen
				char x = (char) (j + 97); // wodurch auf jede Zelle die richtige Figur gesetzt wird
				int y = i;
				String pos = "" + x + "" + y;
				System.setOut(spielfeld.dummyStream);
				spielfeld.getFigurDatenUndSetze(pos, schachbrett,  id);
				//spielfeld.getFigurDatenUndSetze(pos, schachbrett, spiel, id);
				System.setOut(spielfeld.originalStream);
				System.out.print(".");
			}
		}
		System.out.println();
		if(!spielfeld.schwarzAmZug())
			System.out.println("Schwarz ist am Zug.");
		if(spielfeld.schwarzAmZug())
			System.out.println("Weiß ist am Zug.");
		
		for (int i = 0; i < width; i++) { // Das eigentliche printen
			for (int j = 0; j < height; j++) {
				System.out.print(this.schachmatrix[i][j]);
			}
			int row = 8 - i;
			switch (row) {
			case 8:
				System.out.println("|" + row + "   Sie haben folgende Operationen zur Auswahl:");
				break;
			case 7:
				System.out.println("|" + row + "   1. save");
				break;
			case 6:
				System.out.println("|" + row + "   2. historie");
				break;
			case 5:
				System.out.println("|" + row + "   3. Mit Figur ziehen");
				break;
			case 4:
				System.out.println("|" + row +"    4. update(bei 2 Sp auf 2 Geräten");
				break;
			case 3:
				System.out.println("|" + row);
				break;
			case 2:
				System.out.println("|" + row);
				break;
			case 1:
				System.out.println("|" + row);
				break;

			}
		}
		System.out.println("-------------------------");
		System.out.println("|A||B||C||D||E||F||G||H|");

	}
	
	
	public feld getSchachbrett () {
		return this;
	}

}
