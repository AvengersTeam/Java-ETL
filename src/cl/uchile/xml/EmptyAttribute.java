package cl.uchile.xml;

public class EmptyAttribute extends Attribute{

	public EmptyAttribute() {
		super("", "", "");
	}

	public boolean isEmpty(){
		return true;
	}
}
