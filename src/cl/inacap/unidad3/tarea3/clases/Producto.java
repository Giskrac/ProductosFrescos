package cl.inacap.unidad3.tarea3.clases;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import cl.inacap.unidad1.tarea2.activity.R;

public class Producto {
	public int id_producto;
	public String nombre_producto;
	public int precio_producto;
	
	//Se genera y obtiene la lista de productos del string-array productos
	public ArrayList<Producto> listaProductos(Context context)
	{
		ArrayList<Producto> lista = new ArrayList<Producto>();
		
		String productos[] = context.getResources().getStringArray(R.array.productos_array);
		
		int cantidad = productos.length;
		
		for(int i =0; i < cantidad; i++)		
		{
			
			try {
				
				JSONObject jsonClientes = new JSONObject(productos[i]);
				
				Producto cliente = new Producto();
				cliente.id_producto = jsonClientes.getInt("id_producto");
				cliente.nombre_producto = jsonClientes.getString("nombre_producto");
				cliente.precio_producto = jsonClientes.getInt("precio_producto");
				
				lista.add(cliente);
				
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			
			
		}
		
		return lista;
	}	
	
	//forma String de la clase para general el listview
	public String toString()
	{
		return this.nombre_producto; 
	}
}
