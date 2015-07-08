package cl.inacap.unidad3.tarea3.clases;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cl.inacap.unidad3.tarea3.activity.R;

public class Producto {
	public int id_producto;
	public String nombre_producto;
	public int precio_producto;
	
	private Context context;
	
	private String TABLE = "producto";
	private String[] COLUMNS = new String[] { "id_producto","nombre_producto","precio_producto" };
	
	//Se genera y obtiene la lista de productos del string-array productos
	public ArrayList<Producto> listaProductos(Context mContext)
	{
		this.context = mContext;
		
		ArrayList<Producto> lista = new ArrayList<Producto>();
		
		//Creamos la query para obtener los registros
        String query = "SELECT * FROM producto";
 
        //ejecutamos la query
        SQLiteDatabase db = new BaseDatos(this.context).getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        
        if (cursor.moveToFirst()) {
            do {
            	
            	Producto producto = new Producto();
            	producto.id_producto = cursor.getInt(0);
            	producto.nombre_producto = cursor.getString(1);
            	producto.precio_producto = cursor.getInt(2);
				
				lista.add(producto);
            	
            } while (cursor.moveToNext());
        }
		
		return lista;
	}	
	
	//forma String de la clase para general el listview
	public String toString()
	{
		return this.nombre_producto; 
	}
	
	//forma String de la clase para general el listview
	public Producto getProducto(int id)
	{
		// creamos conexion
        SQLiteDatabase db = new BaseDatos(this.context).getWritableDatabase();
 
        // ejecutar query
        Cursor cursor = db.query(TABLE, COLUMNS, " id_producto = ?", new String[] { String.valueOf(id) }, null, null, null, null);
 
        if (cursor != null)
            cursor.moveToFirst();
 
        //creamos el objeto
        Producto producto = new Producto();
        producto.id_producto = cursor.getInt(0);
        producto.nombre_producto = cursor.getString(1);
        producto.precio_producto = cursor.getInt(2);
		
		return producto;

	}
}
