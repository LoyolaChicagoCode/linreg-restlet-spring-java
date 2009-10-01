package points.dao.impl.hib;

import java.awt.Color;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import points.dao.PointDAO;
import points.domain.Point;

/**
 * This class implements a data access object (DAO) that accesses Point objects
 * from the database using Hibernate. This DAO has to be context-aware for
 * access to the LocalSessionFactoryBean, which provides access to the hbm2ddl
 * methods for database schema creation etc.
 */
public class HibernatePointDAO extends HibernateDaoSupport implements PointDAO,
		ApplicationContextAware {

	private ApplicationContext context;

	private String sessionFactoryName;

	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
	}

	public void setSessionFactoryName(String sessionFactoryName) {
		this.sessionFactoryName = sessionFactoryName;
	}

	/**
	 * Obtains the LSFB through the Spring context
	 */
	protected LocalSessionFactoryBean getLsfb() {
		return (LocalSessionFactoryBean) context.getBean("&"
				+ sessionFactoryName);
	}

	/**
	 * Invoked automatically by Spring.
	 */
	@Override
	public void initDao() {
		try {
			// create the schema according to the
			// Hibernate mapping
			// but only if it does not exist yet
			// otherwise an exception occurs and
			// nothing happens
			getLsfb().createDatabaseSchema();
			// populate the tables
			// (be careful to include leading alpha value in colors)
			createColor(0xffff0000);
			createColor(0xff00ff00);
			createColor(0xff0000ff);
			// DONE consider removing the
			// pre-population
			// because it makes testing of this DAO
			// inconsistent
			// with testing of InMemoryPointDAO
			// createPoint(+2, +2.0, Color.BLUE);
			// createPoint(-2, +0.0, Color.BLUE);
			// createPoint(+1, +1.5, Color.GREEN);
			// createPoint(-4, -1.0, Color.RED);
			// createPoint(+4, +3.0, Color.RED);
		} catch (DataAccessException e) {
		}
	}

	/**
	 * Invoked by the user.
	 */
	@Override
	public void init() {
		// drop the schema in case it exists
		try {
			getLsfb().dropDatabaseSchema();
		} catch (DataAccessException e) {
		}
		// then recreate and populate the schema
		initDao();
	}

	protected Point createPoint(double x, double y,
			HibernateColor hibernateColor) {
		Point pt = new HibernatePoint(x, y, hibernateColor);
		getHibernateTemplate().save(pt);
		return pt;
	}

	protected void createColor(int rgb) {
		getHibernateTemplate().save(new HibernateColor(rgb));
	}

	@Override
	public Point create(double x, double y, Color color) {
		HibernateColor hibernateColor = (HibernateColor) getHibernateTemplate()
				.get(HibernateColor.class, color.getRGB());
		return createPoint(x, y, hibernateColor);
	}

	@Override
	public Point find(final int id) {
		return (Point) getHibernateTemplate().get(HibernatePoint.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Point> findAll() {
		// need join fetch to initialize
		// subobjects
		return getHibernateTemplate().find(
				"from points.dao.impl.hib.HibernatePoint "
						+ "as p join fetch p.hibernateColor");
	}

	@Override
	public void remove(final int id) {
		Object pt = getHibernateTemplate().get(HibernatePoint.class, id);
		getHibernateTemplate().delete(pt);
	}

	@Override
	public void update(final int id, final double x, final double y,
			final Color color) {
		HibernateTemplate template = getHibernateTemplate();
		HibernateColor hibernateColor = (HibernateColor) template.get(
				HibernateColor.class, color.getRGB());
		HibernatePoint pt = (HibernatePoint) template.get(HibernatePoint.class, id);
		pt.setX(x);
		pt.setY(y);
		pt.setHibernateColor(hibernateColor);
		template.update(pt);
	}

	@SuppressWarnings("unchecked")
	public Collection<Color> findColors() {
		Collection<Color> colors = getHibernateTemplate().find(
				"from points.dao.impl.hib.HibernateColor");
		return CollectionUtils.collect(colors, new Transformer() {
			public Object transform(Object input) {
				return ((HibernateColor) input).toColor();
			}
		});
	}
}