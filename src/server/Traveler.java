package server;

import graph.Link;
import graph.Node;

import java.util.ArrayList;
import java.util.Scanner;

public class Traveler {

    private Node currentNode;
    private Integer cost = 0;
    private ArrayList<Link> links;

    public Traveler(Node currentNode) {
        this.sc = new Scanner(System.in);
        this.currentNode = currentNode;
        System.out.printf("Traveler has been created. Starting at: %d\n", currentNode.getId());
    }

    public void nextStep() {
        System.out.printf("Current travel cost: %d\n", cost);

        printPossibleRoutes();
        travelTo(input());
    }

    private void printPossibleRoutes() {
       this.links = this.currentNode.getLinks();
        int c = 0;
        for(Link l : links) {
            System.out.printf("%d/ -> %d (%d)\n", c++, l.getDestination().getId(), l.getTravelCost());
        }
    }

    private Scanner sc;
    private int input() {
        System.out.println("Vybor: ");
        return sc.nextInt();
    }

    public void travelTo(int id) {
        Link l = this.links.get(id);
        System.out.printf("Traveling to: %d\n\n", l.getDestination().getId());
        this.cost += l.getTravelCost();
        this.currentNode = l.getDestination();
    }
}
