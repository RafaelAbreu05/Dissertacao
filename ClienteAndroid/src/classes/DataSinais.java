package classes;

import android.graphics.PointF;

public class DataSinais {

	public static final int VALUE_X = 0;

	public static final int VALUE_Y = 1;

	public static final int VALUE_RSSI = 3;

	/**
	 * @uml.property name="x"
	 */
	protected int x;
	/**
	 * @uml.property name="y"
	 */
	protected int y;
	/**
	 * @uml.property name="rssi"
	 */
	protected double rssi;

	public DataSinais() {
	}

	public DataSinais(int x, int y, double rssi) {
		this.x = x;
		this.y = y;
		this.rssi = rssi;
	}

	/**
	 * @return
	 * @uml.property name="x"
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 * @uml.property name="x"
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return
	 * @uml.property name="y"
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 * @uml.property name="y"
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return
	 * @uml.property name="rssi"
	 */
	public double getRssi() {
		return rssi;
	}

	/**
	 * @param rssi
	 * @uml.property name="rssi"
	 */
	public void setRssi(double rssi) {
		this.rssi = rssi;
	}

	public PointF getPointF() {
		return new PointF(this.x, this.y);
	}
}
