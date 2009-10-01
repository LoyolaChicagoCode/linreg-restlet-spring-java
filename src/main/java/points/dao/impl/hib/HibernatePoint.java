package points.dao.impl.hib;

import java.awt.Color;

import points.domain.impl.DefaultPoint;

/**
 * A Point implementation suitable for object-relational
 * mapping using Hibernate. 
 */
class HibernatePoint extends DefaultPoint {

	private HibernateColor hibernateColor;

	public HibernatePoint() { 
		super(0, 0, Color.BLACK);
	}
	
	public HibernatePoint(double x, double y, HibernateColor hibernateColor) {
		super(x, y, hibernateColor.toColor());
		this.hibernateColor = hibernateColor;
	}

	public void setHibernateColor(HibernateColor hibernateColor) {
		this.hibernateColor = hibernateColor;
		super.setColor(hibernateColor.toColor());
	}

	public HibernateColor getHibernateColor() {
		return hibernateColor;
	}

	@Override
	public void setColor(Color color) {
		throw new UnsupportedOperationException(
				"should use setHibernateColor instead");
	}
}
