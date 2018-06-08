package shsjxzh.compiler.BackEnd.Graph;

import shsjxzh.compiler.IR.Value.VirtualRegister;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    public Map<VirtualRegister, GraphNode> Vr2NodeMap = new HashMap<>();

    /*private GraphNode getNode(VirtualRegister obj){
        GraphNode graphNode = Vr2NodeMap.get(obj);
        if (graphNode == null){
            graphNode = new GraphNode();
            Vr2NodeMap.put(obj, graphNode);
        }
        return graphNode;
    }*/

    public void addEdge(VirtualRegister a, VirtualRegister b){
        if(!a.equals(b)) {
            GraphNode A = Vr2NodeMap.get(a);
            GraphNode B = Vr2NodeMap.get(b);
            A.linkGraphNode(B);
        }
    }

    public void addNode(VirtualRegister obj){
        if (obj != null) {
            GraphNode graphNode = Vr2NodeMap.get(obj);
            if (graphNode == null){
                graphNode = new GraphNode();
                Vr2NodeMap.put(obj, graphNode);
            }
        }
    }
}
