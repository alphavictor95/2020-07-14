package it.polito.tdp.PremierLeague.model;

public class Posizione implements Comparable<Posizione> {
	
	Team t;
	int punteggio=0;
	
	public Posizione(Team t, int punteggio) {
		super();
		this.t = t;
		this.punteggio = punteggio;
	}
	public Team getT() {
		return t;
	}
	public void setT(Team t) {
		this.t = t;
	}
	public int getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(int aggiunta) {
		this.punteggio = this.punteggio+aggiunta;
	}
	@Override
	public int compareTo(Posizione o) {
		
		return this.punteggio-o.punteggio;
	}
	
	

}
