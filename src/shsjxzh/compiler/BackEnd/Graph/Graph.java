package shsjxzh.compiler.BackEnd.Graph;

import shsjxzh.compiler.IR.Value.VirtualRegister;

import java.util.*;

public class Graph {
    public Map<VirtualRegister, GraphNode> Vr2NodeMap = new HashMap<>();

    public Set<GraphNode> graphNodeSet = new LinkedHashSet<>();

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

    public void addAllocNode(VirtualRegister vrReg){
        if (vrReg != null) {
            GraphNode graphNode = Vr2NodeMap.get(vrReg);
            if (graphNode == null){
                graphNode = new GraphNode(vrReg);
                Vr2NodeMap.put(vrReg, graphNode);
                graphNodeSet.add(graphNode);
            }
        }
    }

    public void addNonAllocNode(VirtualRegister vrReg){
        if (vrReg != null) {
            GraphNode graphNode = Vr2NodeMap.get(vrReg);
            if (graphNode == null){
                graphNode = new GraphNode(vrReg);
                Vr2NodeMap.put(vrReg, graphNode);
            }
        }
    }

}
