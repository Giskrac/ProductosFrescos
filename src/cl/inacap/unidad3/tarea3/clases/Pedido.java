package cl.inacap.unidad3.tarea3.clases;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cl.inacap.unidad3.tarea3.activity.ClientesActivity;


public class Pedido {

	public int id_pedido;
	public int id_cliente;
	public int id_usuario;
	public String nombre_cliente;
	public int id_producto;
	public String nombre_producto;
	public String fecha_entrega;
	public int cantidad;	
	public int precio;	
	public boolean visible;
	
	private Context context;
	private int cliente_id;
	private int usuario_id;
	
	private String TABLE = "pedido";
	private String[] COLUMNS = new String[] { "id_producto","cantidad","precio_producto" };	
	
	public Pedido() {}
	
	public Pedido(Context mContext, int mIdCliente, int MIdUsuario) {
		this.context = mContext;
		this.cliente_id = mIdCliente;
		this.usuario_id = MIdUsuario;
	}

	//Entrega los Pedidos asignados a un cliente
	public ArrayList<Pedido> ListarPedidos()
	{	
		ArrayList<Pedido> lista = new ArrayList<Pedido>();
		
		//Creamos la query para obtener los registros
        String query = 
        		"SELECT pe.id_pedido, pe.id_cliente, cl.nombre_cliente, pe.id_producto, pr.nombre_producto, pe.fecha_entrega, pe.cantidad, pr.precio_producto"
        				+ " FROM pedido AS pe, producto AS pr, cliente as cl"
        				+ " WHERE pr.id_producto=pe.id_producto"
        				+ " AND cl.id_cliente=pe.id_cliente"
        				+ " AND pe.id_cliente=" + this.cliente_id
        				+ " AND pe.id_usuario=" + this.usuario_id
        				+ " AND pe.visible=1";
 
        //ejecutamos la query
        SQLiteDatabase db = new BaseDatos(this.context).getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null); 
                
        if (cursor.moveToFirst()) {
            do {
            	
				Pedido pedido = new Pedido();
				
				pedido.id_pedido = cursor.getInt(0);
				pedido.id_cliente = cursor.getInt(1);
				pedido.nombre_cliente = cursor.getString(2);
				pedido.id_producto = cursor.getInt(3);
				pedido.nombre_producto = cursor.getString(4);
				pedido.fecha_entrega = cursor.getString(5);
				pedido.cantidad = cursor.getInt(6);
				pedido.precio = cursor.getInt(7);
				
				lista.add(pedido);
            	
            } while (cursor.moveToNext());
        }
        
        db.close();		

		return lista;
		
	}
	
	//entrega todos los pedidos
	public ArrayList<Pedido> InformeDePedidos(Context mContext, int mUsuarioId)	{	
		
		ArrayList<Pedido> lista = new ArrayList<Pedido>();
		
		//Creamos la query para obtener los registros
        String query = 
        		"SELECT pe.id_pedido, pe.id_cliente, cl.nombre_cliente, pe.id_producto, pr.nombre_producto, pe.fecha_entrega, pe.cantidad, pr.precio_producto"
        				+ " FROM pedido AS pe, producto AS pr, cliente as cl"
        				+ " WHERE pr.id_producto=pe.id_producto"
        				+ " AND cl.id_cliente=pe.id_cliente"
        				+ " AND pe.id_usuario=" + mUsuarioId
        				+ " AND pe.visible=1";
 
        //ejecutamos la query
        SQLiteDatabase db = new BaseDatos(mContext).getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null); 
                
        if (cursor.moveToFirst()) {
            do {
            	
				Pedido pedido = new Pedido();
				
				pedido.id_pedido = cursor.getInt(0);
				pedido.id_cliente = cursor.getInt(1);
				pedido.nombre_cliente = cursor.getString(2);
				pedido.id_producto = cursor.getInt(3);
				pedido.nombre_producto = cursor.getString(4);
				pedido.fecha_entrega = cursor.getString(5);
				pedido.cantidad = cursor.getInt(6);
				pedido.precio = cursor.getInt(7);
				
				lista.add(pedido);
            	
            } while (cursor.moveToNext());
        }
        
        db.close();		

		return lista;

	}

	//forma String de la clase para general el listview
	public String toString()
	{
		return this.nombre_producto + "\n" + this.cantidad + " x " + this.precio + "      " + (this.precio * this.cantidad); 
	}
	
	//muestra el pedido actual seleccionado
	public Pedido MostrarPedido(int id)
	{		
 
        String query = 
        		"SELECT pe.id_producto, pe.cantidad, pr.precio_producto"
        				+ " FROM pedido AS pe, producto AS pr"
        				+ " WHERE pr.id_producto=pe.id_producto"
        				+ " AND pe.id_pedido=" + id;
 
        //ejecutamos la query
        SQLiteDatabase db = new BaseDatos(this.context).getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null); 
        
        if (cursor != null)
            cursor.moveToFirst();
 
        //creamos el objeto
        Pedido pedido = new Pedido();
        pedido.id_producto = cursor.getInt(0);
        pedido.cantidad = cursor.getInt(1);
        pedido.precio = cursor.getInt(2);
		
		return pedido;
		
	}
	
	//agrega un pedido al pedido general y al actual
	public ArrayList<Pedido> agregarPedido(Pedido pedido)
	{		
		// creamos conexion
        SQLiteDatabase db = new BaseDatos(this.context).getWritableDatabase();
 
        // crear los valores a insertar
        ContentValues values = new ContentValues();
        values.put("id_cliente", this.cliente_id);
        values.put("id_usuario", this.usuario_id);
        values.put("id_producto", pedido.id_producto);
        values.put("fecha_entrega", pedido.fecha_entrega);
        values.put("cantidad", pedido.cantidad);
        values.put("visible", 1);
 
        // ejecutar query
        db.insert(TABLE, null, values);
 
        // cerramos la db
        db.close(); 
		
		return ListarPedidos();
		
	}
	
	// oculta el pedido en la lista general y lo elimina de la actual
	public ArrayList<Pedido> borrarPedido(int id)
	{
		
		// creamos conexion
        SQLiteDatabase db = new BaseDatos(this.context).getWritableDatabase();
	 
        // crear los valores a eliminar
	    ContentValues values = new ContentValues();
	    values.put("visible", 0); 
	 
	    // ejecutar query
	    db.update(TABLE, values, "id_pedido = ?", new String[] { String.valueOf(id) });
	 
	    // cerramos la db
	    db.close();
		
		return ListarPedidos();
	}	
	
	// actualiza el pedido del pedido general y del actual
	public ArrayList<Pedido> actualizarPedido(Pedido pedido, int id)
	{
		// creamos conexion
        SQLiteDatabase db = new BaseDatos(this.context).getWritableDatabase();
	 
        // crear los valores a actualizar
	    ContentValues values = new ContentValues();
        values.put("id_producto", pedido.id_producto);
        values.put("fecha_entrega", pedido.fecha_entrega);
        values.put("cantidad", pedido.cantidad);
	 
	    // ejecutar query
	    db.update(TABLE, values, "id_pedido = ?", new String[] { String.valueOf(id) });
	 
	    // cerramos la db
	    db.close();
	    
	    return ListarPedidos();

	}

	
}
