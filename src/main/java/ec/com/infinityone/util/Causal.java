package ec.com.infinityone.util;

public class Causal {
	private String id;
	private String descripcion;

	public Causal(String id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}
	// Getters y Setters...
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}