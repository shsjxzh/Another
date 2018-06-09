package shsjxzh.compiler.BackEnd.Graph;

import shsjxzh.compiler.IR.Value.VirtualRegister;

import java.util.HashSet;
import java.util.Set;

public class GraphNode {
    public Set<GraphNode> neighbours = new HashSet<>();
    public int degree = 0;
    public int colour = -1;
    public VirtualRegister virReg;

    public GraphNode(VirtualRegister virReg) {
        this.virReg = virReg;
    }

    //for interference generation
    public void linkGraphNode(GraphNode other){
        this.neighbours.add(other);
        other.neighbours.add(this);
        ++this.degree;
        ++other.degree;
    }

    //for register allocator
    public boolean removed = false;
}
