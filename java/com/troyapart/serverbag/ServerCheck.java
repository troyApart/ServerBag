package com.troyApart.serverbag;

import java.text.DecimalFormat;


public class ServerCheck {
	private int checkID;
	private String date, note;
	boolean isPM;
	private double checkCash, checkCredit, tipCash, tipCredit, creditTotal;

	public ServerCheck(int _checkID, String _date, double _checkCash, double _checkCredit,
			double _tipCash, double _tipCredit, boolean _isPM, String _note) {
		checkID = _checkID;
		date = _date;
		checkCash = _checkCash;
		checkCredit = _checkCredit;
		tipCash = _tipCash;
		tipCredit = _tipCredit;
		isPM = _isPM;
		note = _note;
		creditTotal = _checkCredit + _tipCredit;
	}

	public double getCreditTotal () {
		return creditTotal;
	}
	
	public int getCheckID() {
		return checkID;
	}

	public void setCheckID(int checkID) {
		this.checkID = checkID;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getCheckCash() {
		return checkCash;
	}

	public void setCheckCash(double checkCash) {
		this.checkCash = checkCash;
	}

	public double getCheckCredit() {
		return checkCredit;
	}

	public void setCheckCredit(double checkCredit) {
		this.checkCredit = checkCredit;
	}

	public double getTipCash() {
		return tipCash;
	}

	public void setTipCash(double tipCash) {
		this.tipCash = tipCash;
	}

	public double getTipCredit() {
		return tipCredit;
	}

	public void setTipCredit(double tipCredit) {
		this.tipCredit = tipCredit;
	}

	public boolean getIsPM() {
		return isPM;
	}

	public void setIsPM(boolean isPM) {
		this.isPM = isPM;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		if (note.isEmpty()) {
			if (isPM) {
				if (tipCash < 0) {
					return date + ", " + "Night" + ",\nCash: $" + df.format(checkCash) + ", Tip: -$" + df.format(Math.abs(tipCash)) + ",\nCredit: $" + df.format(checkCredit) + ", Tip: $" + df.format(tipCredit) + ", Credit Total = $" + df.format(creditTotal);
				} else {
					return date + ", " + "Night" + ",\nCash: $" + df.format(checkCash) + ", Tip: $" + df.format(tipCash) + ",\nCredit: $" + df.format(checkCredit) + ", Tip: $" + df.format(tipCredit) + ", Credit Total = $" + df.format(creditTotal);
				}
			} else {
				if (tipCash < 0) {
					return date + ", " + "Day" + ",\nCash: $" + df.format(checkCash) + ", Tip: -$" + df.format(Math.abs(tipCash)) + ",\nCredit: $" + df.format(checkCredit) + ", Tip: $" + df.format(tipCredit) + ", Credit Total = $" + df.format(creditTotal);
				} else {
					return date + ", " + "Day" + ",\nCash: $" + df.format(checkCash) + ", Tip: $" + df.format(tipCash) + ",\nCredit: $" + df.format(checkCredit) + ", Tip: $" + df.format(tipCredit) + ", Credit Total = $" + df.format(creditTotal);
				}
			}
		} else {
			if (isPM) {
				if (tipCash < 0) {
					return date + ", " + "Night" + ",\nCash: $" + df.format(checkCash) + ", Tip: -$" + df.format(Math.abs(tipCash)) + ",\nCredit: $" + df.format(checkCredit) + ", Tip: $" + df.format(tipCredit) + ", Credit Total = $" + df.format(creditTotal) + "\nNote: " + note;
				} else {
					return date + ", " + "Night" + ",\nCash: $" + df.format(checkCash) + ", Tip: $" + df.format(tipCash) + ",\nCredit: $" + df.format(checkCredit) + ", Tip: $" + df.format(tipCredit) + ", Credit Total = $" + df.format(creditTotal) + "\nNote: " + note;
				}
			} else {
				if (tipCash < 0) {
					return date + ", " + "Day" + ",\nCash: $" + df.format(checkCash) + ", Tip: -$" + df.format(Math.abs(tipCash)) + ",\nCredit: $" + df.format(checkCredit) + ", Tip: $" + df.format(tipCredit) + ", Credit Total = $" + df.format(creditTotal) + "\nNote: " + note;
				} else {
					return date + ", " + "Day" + ",\nCash: $" + df.format(checkCash) + ", Tip: $" + df.format(tipCash) + ",\nCredit: $" + df.format(checkCredit) + ", Tip: $" + df.format(tipCredit) + ", Credit Total = $" + df.format(creditTotal) + "\nNote: " + note;
				}
			}
		}
	}
}
