package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	
	public Model() {
		
		dao = new FoodDao();
		idMap = new HashMap<>();
		
	}
	
	
	public List<Food> getVertici(Integer n){
		
		return dao.getVertici(n);
	}

	
	public void creaGrafo(Integer n) {
		
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici 
		Graphs.addAllVertices(this.grafo, dao.getVertici(n));
		
		for(Food f: this.grafo.vertexSet()) {
			
			idMap.put(f.getFood_code(), f);
		}
		
		
		//archi
		
		/*a. Gli archi rappresentano la “differenza di grassi”: per ogni
			coppia di cibi, esiste un arco che va dal cibo con il valor
			medio di grassi maggiore (calcolato tenendo conto di tutte le sue porzioni,
			campo “saturated_fats”) verso il cibo con il valor medio di grassi minore.
		b. 	Il peso degli archi è dato dalla differenza dei due valori medi.
		 	Se la differenza è pari a 0, l’arco non va inserito.
		 * 
		 */
		
		for(Adiacenza a: dao.getArchi(idMap)) {
			
			if(idMap.containsKey(a.getF1().getFood_code()) && idMap.containsKey(a.getF2().getFood_code())) {
				
				if(a.getPeso() > 0) {
					Graphs.addEdgeWithVertices(this.grafo, a.getF1(), a.getF2(), a.getPeso());
				}
				else if(a.getPeso() < 0) {
					Graphs.addEdgeWithVertices(this.grafo, a.getF2(), a.getF1(), a.getPeso()*(-1));
				}
			}
			
			
		}
		
		
	}
	
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public Graph<Food,DefaultWeightedEdge> getGrafo(){
		
		return grafo;
	}
	
	
	//l’elenco dei 5 cibi aventi la “differenza di grassi” minima tra quelli
	//successivi al cibo selezionato nella tendina del punto b. Deve
	//essere visualizzato il nome del cibo ed il numero di “Grassi” di differenza.
	
	public List<CiboEGrassi> getCibiPocoGrassi(Food inizio){
		
		List<CiboEGrassi> result = new ArrayList<>();
		
		for(Food f: Graphs.successorListOf(this.grafo, inizio)) {
			
			result.add(new CiboEGrassi(f,this.grafo.getEdgeWeight(this.grafo.getEdge(inizio, f))));
			
		}
		
		Collections.sort(result);
		
		if(result.size() > 5) {
			List<CiboEGrassi> cinque = new ArrayList<>();
		
			for(int i=0; i<5; i++) {
			
				cinque.add(result.get(i));
			}
		
			return cinque;
		}
		
		return result;
	}
	
}
