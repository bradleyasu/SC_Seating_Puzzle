package com.showclix.seating;

public class Seat implements Comparable<Seat>{

	private int rowNumber;
	private int colNumber;

	private int distance;

	private boolean reserved = false;
	private boolean preReservation = false;
	

	public Seat(int rowNumber, int colNumber, int distance) {
		this.rowNumber = rowNumber;
		this.colNumber = colNumber;
		this.distance = distance;
	}
	

	public int getDistance() {
		return distance;
	}

	public int getColumn() {
		return colNumber;
	}

	public int getRow() {
		return rowNumber;
	}

	public String getSeatLabel() {
		return "R" + (rowNumber + 1) + "C" + (colNumber + 1);
	}

	public void setReserved() {
		reserved = true;
	}

	public void removeReservation() {
		reserved = false;
	}

	public void setPreReservation() {
		preReservation = true;
	}

	public void removePreReservation() {
		preReservation = false;
	}

	public boolean isReserved() {
		return reserved || preReservation;
	}

	public char getSeatStatusSymbol() {
		char symbol = '-';
		if (preReservation) {
			symbol = 'X';
		} else if (reserved) {
			symbol = 'O';
		}
		return symbol;
	}


	@Override
	public String toString() {
		return getSeatLabel();
	}
	
	@Override
	public int hashCode() {
		return getRow() << 16 & getColumn();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Seat)){
			return false;
		}
		return ((Seat)o).getColumn() == getColumn() && ((Seat)o).getRow() == getRow(); 
	}

	@Override
	public int compareTo(Seat s) {
		return getDistance() - s.getDistance();
	}
}
