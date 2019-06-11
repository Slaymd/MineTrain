package fr.dariusmtn.minetrain.object;

public class PlayerEditor {

	private int phase = 0;
	private Object obj = null;
	
	public PlayerEditor(int phase, Line obj) {
		this.setPhase(phase);
		this.setEdit(obj);
	}
	
	public PlayerEditor(int phase, Station obj) {
		this.setPhase(phase);
		this.setEdit(obj);
	}

	/**
	 * @return the phase
	 */
	public int getPhase() {
		return phase;
	}

	/**
	 * @param phase the phase to set
	 */
	public void setPhase(int phase) {
		this.phase = phase;
	}

	/**
	 * @return the obj
	 */
	public Line getLine() {
		if(this.obj instanceof Line)
			return (Line)obj;
		return null;
	}
	
	public Station getStation() {
		if(this.obj instanceof Station)
			return (Station)obj;
		return null;
	}

	/**
	 * @param obj the obj to set
	 */
	public void setEdit(Object obj) {
		this.obj = obj;
	}
	
}
