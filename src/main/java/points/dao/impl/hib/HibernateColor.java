package points.dao.impl.hib;

import java.awt.Color;

/**
 * A color implementation suitable for object-relational
 * mapping using Hibernate. 
 */
class HibernateColor {

	public HibernateColor() { }

	public HibernateColor(int rgb) {
		this.rgb = rgb;
	}
	
	public int getRgb() { 
		return rgb;
	}
	
	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

	private int rgb;

	public Color toColor() {
		return new Color(rgb);
	}

}
