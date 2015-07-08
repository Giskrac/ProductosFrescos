package cl.inacap.unidad3.tarea3.clases;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Posicion {
	public double id_usuario;
	public double latitud;
	public double longitud;
	public String direccion;
	
	//forma String de la clase para general el listview
	public String toString()
	{
		return "LAT:" + this.latitud + "\nLON:" + this.longitud + "\nDIR:" + this.direccion; 
	}
	
	public void addPosition(Context context, Posicion posicion)
	{		 
        // creamos conexion
        SQLiteDatabase db = new BaseDatos(context).getWritableDatabase();
 
        // crear los valores a insertar
        ContentValues values = new ContentValues();
        values.put("latitud", posicion.latitud);
        values.put("longitud", posicion.longitud);
        values.put("direccion", posicion.direccion);
 
        // ejecutar query
        db.insert("posicion", null, values);
 
        // cerramos la db
        db.close(); 
	}
	
}
