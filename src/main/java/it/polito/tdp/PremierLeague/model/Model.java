package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	PremierLeagueDAO dao = new PremierLeagueDAO();
	Graph<Team, DefaultWeightedEdge> grafo;
	Map<Team,Posizione> classifica;
	
	public Model() {
		classifica = new HashMap<>();
	}
	
	public List<Team> getTeams() {
		return dao.listAllTeams(classifica);
	}
	public String creaGrafo() {
		
		grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.listAllTeams(classifica));
		this.generaClassifica(this.classifica);
		for(Team t : this.grafo.vertexSet()) {
			for(Team t2 : this.grafo.vertexSet()) {
				if(classifica.get(t).punteggio>classifica.get(t2).punteggio) {
					Graphs.addEdgeWithVertices(this.grafo, t, t2, 
					(classifica.get(t).punteggio-classifica.get(t2).punteggio));
				}
				
			}
		}
		
		return String.format("Grafo creato con %d vertici e %d archi\n", 
				this.grafo.vertexSet().size(), 
				this.grafo.edgeSet().size());
	}
	
	public void generaClassifica(Map<Team,Posizione> classifica) {
		
		List<Match> matches = new ArrayList<>(dao.listAllMatches());
		for(Posizione p : classifica.values()) {
			for(Match m : matches) {
				if((p.getT().getTeamID()==m.teamHomeID && m.resultOfTeamHome==1) ||
					(p.getT().getTeamID()==m.teamAwayID && m.resultOfTeamHome==-1)	) {
					p.setPunteggio(3);
				}
				else if((p.getT().getTeamID()==m.teamHomeID && m.resultOfTeamHome==0) ||
						(p.getT().getTeamID()==m.teamAwayID && m.resultOfTeamHome==0)){
					p.setPunteggio(1);
				}
			}
		}
		
		
		
	}

	public String stampaClassifica(Team selezionato) {
		String res="Con Punteggio migliore:\n";
		List<Posizione> migliori = new ArrayList<>();
		List<Posizione> peggiori= new ArrayList<>();
		
		for(Posizione p : classifica.values()) {
			if(!p.getT().equals(classifica.get(selezionato)) && p.getPunteggio()>classifica.get(selezionato).getPunteggio()) {
				Posizione p1 = new Posizione(p.getT(),p.getPunteggio()-classifica.get(selezionato).getPunteggio() );
				migliori.add(p1);
			}else if(!p.getT().equals(classifica.get(selezionato)) && p.getPunteggio()<classifica.get(selezionato).getPunteggio()) {
				
				Posizione p1 = new Posizione(p.getT(),Math.abs(p.getPunteggio()-classifica.get(selezionato).getPunteggio()) );
				peggiori.add(p1);
			}
		}
		Collections.sort(migliori);
		Collections.sort(peggiori);
		
		for(Posizione p : migliori) {
			res = res + p.getT().getName()+" "+p.getPunteggio()+"\n";
		}
		res = res+ "Con Punteggio Peggiore:\n";
		for(Posizione p : peggiori) {
			res = res + p.getT().getName()+" "+p.getPunteggio()+"\n";
		}
		return res;
	}
	
}
