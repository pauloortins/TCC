package scmaccess.model.objects;

public class Version implements Comparable<Version> {
	private String name;
	private int order;

	public Version(String name) {
		this.name = name;
	}

	public Version(String name, int ord) {
		this.name = name;
		this.order = ord;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int compareTo(Version o) {
		if (getOrder() < o.getOrder())
			return -1;
		if(getOrder() == o.getOrder())
			return 0;
		return 1;
	}

}