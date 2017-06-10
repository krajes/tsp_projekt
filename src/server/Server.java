/**
 * Do zrobienia pozostalo:
 * Klient/serwer
 * Wczytywanie danych wejsciowych
 * Formatowanie danych wyjsciowych
 *
 * Algorytm dziala na zasadzie naiwnej eliminacji (poki co na sztywno start w wierzcholku 0).
 * Generowane sa rozne cykle, zliczane sa ich koszty. Po kazdej iteracji sprawdzamy, czy koszt cyklu
 * jest mniejszy lub rowny od najlepszego (tzn. najmniejszego). Jesli tak, to ustawiamy cykl jako najlepszy (najkrotszy).
 * Wykonujemy ta petle pewna ilosc razy (poki co ustawione na sztywno: 1000). Dzieki temu uzyskujemy calkiem dobry wynik
 * w dosc krotkim czasie.
 */
package server;

import graph.Graph;
import java.util.Random;

public class Server {

    public static Random rg = new Random();
    public static void main(String[] args) {

        // Logic
        Graph g = new Graph();

        for(int i = 0; i < 13; i++)
            g.addNode(100 + rg.nextInt(900), 100 + rg.nextInt(600));

        for(int inner = 0; inner < g.getNodeList().size(); inner++)
            for(int outter = 0; outter < g.getNodeList().size(); outter++)
                if(inner != outter)
                    g.connectNodes(inner, outter,
                            g.getNode(inner).getCoordinates().distance(g.getNode(outter).getCoordinates()),
                            true);

        // Wyswietlanie (tylko do debugu)
        Display display = new Display();
        display.attachGraph(g);

    }
}