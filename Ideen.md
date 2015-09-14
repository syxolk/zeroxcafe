# Einleitung #

Hier entsteht eine Liste von Ideen und Vorschlägen für diese App.


# Ausstehend #

  * Eingabe-Basis kann auf der Tastatur geändert werden.
  * Einstellung, ob Buchstaben in Hex-Werten groß oder klein geschrieben werden sollen
  * Dialog mit Spinnern zur Auswahl der Ein- und Ausgabe-Basis.
  * Einstellungsmenü mit Korrektheit des Ergebnisses (Algorithmus, Anzahl Nachkommastellen)
  * Button zum Tauschen von Ein- und Ausgabe-Basis
  * 'Ist-Gleich'-Button auf Tastatur statt automatischem Rechnen
  * Rechenschritte als HTML speichern oder sharen
  * neue TestCases für MathUtils (z.B. Basis 3) und die anderen Utils-Klassen

# Erledigt #

  * Anleitung zur händischen Umrechnung von Zahlen zw. verschiedenen Basen mit Prüf-Methode, Modulo-Methode und 4-Bit-to-Hex-Methode.
  * Automatische Erkennung von Perioden nach dem Trennpunkt
  * beliebe Basen sollen auswählbar sein
  * Auf Android 2.3.3 wird der Überstrich (Periode) nicht angezeigt. (Fix: anderes Überstrich-Zeichen)
  * Hilfe: Erklärung, was der Überstrich macht, hinzufügen.
  * Auf dem S4 wird die Tastatur nicht korrekt angezeigt. (Fix: horizonalGap-Hack)
  * Berechnungen in einen Background-Thread verschieben.
  * Erklärung, was die '...' 3-Punkte machen, hinzufügen
  * bessere Pos1/Und Icons
  * Tastatur wird auf Android 2.3.3 nicht korrekt dargestellt (Fix: eigene XML-Datei für v14)
  * Step-by-Step Anleitung zur Umrechnung mit selbst definierbaren Beispielen
  * in Help-Activity ein Link zum Mailen an die Entwickler
  * in der Help-Activity die Version anzeigen
  * bei der Anzeige der Rechenschritte die Periode erkennen

# Verworfen #

  * Die Ausgabe erfolgt stets in allen verfügbaren Basen (2,8,10,16) (Grund: es gibt mittlerweile mehr als nur 4 Basen...)
  * Activity-Status: save/restore hinzufügen (Grund: wird automatisch erledigt)
  * Loading Spinner anzeigen, wenn berechnet wird (Grund: aufgrund verkürzter Berechnungszeit unnötig geworden)