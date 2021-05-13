package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;


public class Model {
    HashMap<Country,Country> predecessore;
	BordersDAO dao=new BordersDAO();	
	Graph<Country,DefaultEdge> grafo=new SimpleGraph(DefaultEdge.class);
	public Model() {
	}
	public Graph<Country,DefaultEdge> creaGrafo(Integer anno) {
		
	
		Graphs.addAllVertices(grafo, dao.loadAllCountries().values());
		for(Border b:dao.getCountryPairs(anno)) {
		grafo.addEdge(b.getC1(), b.getC2());
	}
		return grafo;}
	
	public int getNConfini(Country c) {
		return this.grafo.degreeOf(c);
	}
	
	
	public Graph<Country, DefaultEdge> getGrafo() {
		return grafo;
	}
	public void setGrafo(Graph<Country, DefaultEdge> grafo) {
		this.grafo = grafo;
	}
	
	
	public int getNumberOfConnectedComponents(){
		ConnectivityInspector<Country, DefaultEdge> ci = 
				new ConnectivityInspector<Country, DefaultEdge>(this.grafo) ;

		return ci.connectedSets().size();
	}
	
	public Country trovaPaese(String nome) {
		Country cc=null;
		for(Country c:dao.loadAllCountries().values())
			if(c.getNome().compareTo(nome)==0)
				cc=c;
		return cc;
		
	}
	
	public List<Country> confiniRaggiungibili(Country partenza){
		this.predecessore=new HashMap<Country,Country>();
		predecessore.put(partenza, null);
		BreadthFirstIterator<Country,DefaultEdge> bfv=new BreadthFirstIterator(grafo,partenza);
		bfv.addTraversalListener(new TraversalListener<Country,DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				DefaultEdge arco=e.getEdge();
				Country a=grafo.getEdgeSource(arco);
				Country b=grafo.getEdgeTarget(arco);
				if(!predecessore.containsKey(a) && predecessore.containsKey(b))
					predecessore.put(b, a);
				else if(!predecessore.containsKey(b) && predecessore.containsKey(a))
					predecessore.put(a, b);
				
					
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Country> e) {
				// TODO Auto-generated method stub
				
			}
			
	});
	List<Country> result=new LinkedList<Country>();
	while(bfv.hasNext()) {
		result.add(bfv.next());
	}
		
	return result;
	
	}
}
