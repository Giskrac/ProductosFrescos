package cl.inacap.unidad3.tarea3.clases;

public class Direcciones {
	public double latitud;
	public double longitud;
	public String direccion;
	
	//forma String de la clase para general el listview
	public String toString()
	{
		return "LAT:" + this.latitud + "\nLON:" + this.longitud + "\nDIR:" + this.direccion; 
	}
	
}
