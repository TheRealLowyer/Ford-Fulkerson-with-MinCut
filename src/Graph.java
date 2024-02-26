import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Graph {
	private HashSet<Vertice> nodes;
    public Graph(){
        nodes = new HashSet<>();
    }
    
    public HashSet<Vertice> getNodes() {
		return nodes;
	}

	public void setNodes(HashSet<Vertice> nodes) {
		this.nodes = nodes;
	}
	public void addNode(Vertice newNode){
        nodes.add(newNode);
    }
}
