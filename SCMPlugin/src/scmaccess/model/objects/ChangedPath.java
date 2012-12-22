package scmaccess.model.objects;

public class ChangedPath {
	private Element element;
	private int typeOfChange;
	private ElementProperty elementProperty; //the properties of the element in this moment of change
	
	public ElementProperty getElementProperty() {
		return elementProperty;
	}
	public void setElementProperty(ElementProperty elementProperty) {
		this.elementProperty = elementProperty;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public int getTypeOfChange() {
		return typeOfChange;
	}
	public void setTypeOfChange(int typeOfChange) {
		this.typeOfChange = typeOfChange;
	} 
}
