package client;
import bridge.NodeConnectionInfo;
import exceptions.DuplicatedNodeException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class InputProcessor {

    public static String pathToFile;
    private static ArrayList<Integer> nodeList;
    private static ArrayList<NodeConnectionInfo> nodeConnectionInfos;
    private static final String nodeTag = "nodes";

    public static void parseFile(String path) {
        parseFile(path, null);
    }

    // Uwaga, test bardzo bardzo. NIE COMMITOWAC DO REPO KLIENTA, BO PRZYPAL.
    public static void parseFile(String path, JFrame parent) {

        try {

            FileReader r = new FileReader(path);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(r);

            InputProcessor.nodeList = new ArrayList<>();
            InputProcessor.nodeConnectionInfos = new ArrayList<>();

            // Wierzcholki
            JSONArray nodes = (JSONArray) jsonObject.get("nodes");
            Iterator i = nodes.iterator();
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                nodeList.add(Integer.parseInt((String)innerObj.get("id")));
            }

            // Polaczenia
            JSONArray indices = (JSONArray) jsonObject.get("indices");
            i = indices.iterator();
            while(i.hasNext()) {
                JSONObject inner = (JSONObject) i.next();
                NodeConnectionInfo nodeConnectionInfo = new NodeConnectionInfo(Integer.parseInt((String)inner.get("source")));

                JSONArray listArray = (JSONArray) inner.get("list");
                Iterator listIterator = listArray.iterator();

                while(listIterator.hasNext()) {
                    JSONObject listInnerObject = (JSONObject) listIterator.next();
                    nodeConnectionInfo.addConnection(
                            Integer.parseInt((String)listInnerObject.get("destination")),
                            Integer.parseInt((String)listInnerObject.get("cost"))
                    );
                }

                InputProcessor.nodeConnectionInfos.add(nodeConnectionInfo);
            }

            r.close();
        } catch (FileNotFoundException e) {
            if(parent != null) JOptionPane.showMessageDialog(parent, String.format("Plik (%s) nie został znaleziony.", path));
            e.printStackTrace();
        } catch (ParseException e) {
            if(parent != null) JOptionPane.showMessageDialog(parent, String.format("Wystąpił błąd podczas parsowania pliku."));
            e.printStackTrace();
        } catch (IOException e) {
            if(parent != null) JOptionPane.showMessageDialog(parent, String.format("Wystąpił błąd wejścia/wyjścia."));
            e.printStackTrace();
        } catch (DuplicatedNodeException e) {
            if (parent != null)
                JOptionPane.showMessageDialog(parent, String.format("Wystąpił błąd w pliku - duplikat węzła w połączeniach."));
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> getNodeList() throws NullPointerException {
        if(InputProcessor.nodeList == null) throw new NullPointerException();
        return InputProcessor.nodeList;
    }

    public static ArrayList<NodeConnectionInfo> getNodeConnectionInfo() {
        return InputProcessor.nodeConnectionInfos;
    }
}
