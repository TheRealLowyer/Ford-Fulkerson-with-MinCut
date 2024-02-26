import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class project5 {
	public static void main(String args[]) {
		ArrayList<Integer> troops= new ArrayList<>();		
		ArrayList<Vertice> nodes=new ArrayList<>();
		HashMap<String, Vertice> cars = new HashMap<String, Vertice>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line;
			int counter=0;
			while((line=reader.readLine()) != null) {
		          String[]input= line.split(" ");
		          if (counter==0) {
		        	  counter++;
		        	  continue;
		          }
		          else if(counter==1) {
		        	  for(int i =0; i<input.length;i++) {
		        		  troops.add(Integer.parseInt(input[i]));
		        	  }
		        	  counter++; 
		        	  continue;
		          }
		          else if(2<=counter && counter <=7) {
		        	  if(cars.containsKey(input[0])==true) {
		        		  
		        	  }
		        	  else {
		        		  Vertice home=new Vertice(input[0]);
		        		  nodes.add(home);
		        		  cars.put(input[0], home);
		        	  }
		        	  for(int i=1;i<input.length;i=i+2) {
		        		  Vertice home =cars.get(input[0]);
		        		  if (cars.containsKey(input[i])) {
		        			  Vertice komsu= cars.get(input[i]);
		        			  home.add(komsu, Integer.parseInt(input[i+1]));
		        		  }
		        		  else {
		        			  Vertice komsu= new Vertice(input[i]);
		        			  nodes.add(komsu);
		        			  cars.put(input[i], komsu);
		        			  home.add(komsu, Integer.parseInt(input[i+1]));
		        		  }		        		  
		        	  }
		        	  counter++;
		        	  continue;
		          }
		          else {
		        	  if(cars.containsKey(input[0])==true) {
		        		  
		        	  }
		        	  else {
		        		  Vertice home=new Vertice(input[0]);
		        		  nodes.add(home);
		        		  cars.put(input[0], home);
		        	  }
		        	  
		        	  for(int i=1;i<input.length;i=i+2) {
		        		  Vertice home =cars.get(input[0]);
		        		  if (cars.containsKey(input[i])) {
		        			  Vertice komsu= cars.get(input[i]);
		        			  home.add(komsu, Integer.parseInt(input[i+1]));
		        		  }
		        		  else {
		        			  Vertice komsu= new Vertice(input[i]);
		        			  nodes.add(komsu);
		        			  cars.put(input[i], komsu);
		        			  home.add(komsu, Integer.parseInt(input[i+1]));
		        		  }		        		  
		        	  }
		          }
			}
			reader.close();
		}catch(IOException e ) {
			e.printStackTrace();
		}
		Graph matrix= new Graph();
		Vertice sink= new Vertice("sink");
		for(int i=0; i<troops.size();i++) {
			String region ="r";
			String index=String.valueOf(i);
			String reg= region+index;					
			Vertice komsu = cars.get(reg);
			sink.add(komsu,troops.get(i));
		}
		nodes.add(sink);
		for(Vertice k : nodes) {
			matrix.addNode(k);
		}
		Vertice KL= cars.get("KL");
		Map<Vertice, Vertice> parent =new HashMap<>();
		int maxx = fordFulkersen(matrix, sink,KL);
		writing(maxx, args[1]);		
	}
	public static void writing(int max,String args) {
		try {
		      BufferedWriter myWriter = new BufferedWriter(new FileWriter(args));
		      String p= Integer.toString(max);
		      myWriter.write(p+"\n");
		      myWriter.close();		     
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	public static boolean bfs(Graph matrix, Vertice s, Vertice t, Map<Vertice, Vertice> parent) {
		HashSet<Vertice> nodes = matrix.getNodes();
		HashSet<Vertice> settledNodes = new HashSet<>();
		LinkedList<Vertice> queue= new LinkedList<Vertice>();
		queue.add(s);
		settledNodes.add(s);
		while(queue.size() !=0) {
			Vertice u = queue.poll();
			for( Vertice e:nodes) {
				Map<Vertice, Integer> adjacentNodes = u.getAdjacentNodes();
				// burada adjacentNodes.get(e)>0 ise idi 
				if(settledNodes.contains(e)==false && adjacentNodes.containsKey(e)==true) {
					if(adjacentNodes.get(e)>0) {
						String eName= e.getName();
						String tName= t.getName();
						if(eName.equals(tName)) {
							parent.put(e, u);
							return true;
						}
						parent.put(e, u);
						queue.add(e);
						settledNodes.add(e);
					}
				}
			}
		}
		
		return false;
	}
	public static int fordFulkersen(Graph matrix,Vertice s, Vertice t) {
		Graph residual = new Graph();
		residual= matrix;
		int max_flow=0;
		Map<Vertice, Vertice> parent =new HashMap<>();
		while(bfs(residual ,s ,t ,parent)==true) {
			int path_flow = Integer.MAX_VALUE;
			for(Vertice e=t ; e!=s ;  e= parent.get(e)) {
				Vertice u = parent.get(e);
				Map<Vertice, Integer> adjacentNodes = u.getAdjacentNodes();
				path_flow= Math.min(path_flow, adjacentNodes.get(e));
			}
			for(Vertice e=t ; e!=s ;  e= parent.get(e)) {
				Vertice u = parent.get(e);
				HashSet<Vertice> nodes = residual.getNodes();
				nodes.remove(u);
				nodes.remove(e);
				Map<Vertice, Integer> adjacentNodes = e.getAdjacentNodes();
				Map<Vertice, Integer> adjacentNodesOf = u.getAdjacentNodes();
				adjacentNodesOf.replace(e, adjacentNodesOf.get(e)-path_flow);
				u.setAdjacentNodes(adjacentNodesOf);
				nodes.add(u);
				if(adjacentNodes.containsKey(u)==true) {
					adjacentNodes.replace(u, adjacentNodes.get(u)+path_flow);
					nodes.add(e);
				}
				else {
					adjacentNodes.put(u, path_flow);
					e.setAdjacentNodes(adjacentNodes);
					nodes.add(e);
				}
				residual.setNodes(nodes);
			}	
			max_flow+=path_flow;
		}
		
		
		return max_flow;
		
	}
}
