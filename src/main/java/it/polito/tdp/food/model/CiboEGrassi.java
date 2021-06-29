package it.polito.tdp.food.model;

public class CiboEGrassi implements Comparable<CiboEGrassi>{

	private Food f;
	private double grassi;
	
	public CiboEGrassi(Food f, double grassi) {
		super();
		this.f = f;
		this.grassi = grassi;
	}

	public Food getF() {
		return f;
	}

	public void setF(Food f) {
		this.f = f;
	}

	public double getGrassi() {
		return grassi;
	}

	public void setGrassi(double grassi) {
		this.grassi = grassi;
	}

	@Override
	public int compareTo(CiboEGrassi other) {
		
		if(this.getGrassi() > other.getGrassi())
			return -1;
		
		if(this.getGrassi() < other.getGrassi())
			return +1;
		
		return 0;
	}
	
}
