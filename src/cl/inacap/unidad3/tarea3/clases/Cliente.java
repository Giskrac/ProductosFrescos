package cl.inacap.unidad3.tarea3.clases;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Cliente {
	
	private String TAG = "Cliente Class";
	
	public int id_cliente;
	public String nombre_cliente;
	public String direccion_cliente;
	public String telefono_cliente;
	public int id_usuario;
	
	private Context context;
	private int usuario_id;
	
	private String TABLE = "cliente";
	private String[] COLUMNS = new String[] { "id_cliente","nombre_cliente","direccion_cliente","telefono_cliente","id_usuario","visible" };
	
	//Se genera y obtiene la lista de clientes como base de datos de todos los clientes
	private ArrayList<Cliente> ListarClientes()	{	
		
		ArrayList<Cliente> lista = new ArrayList<Cliente>();
		
		//Creamos la query para obtener los registros
        String query = "SELECT * FROM cliente WHERE id_usuario="+this.usuario_id+" AND visible=1";
 
        //ejecutamos la query
        SQLiteDatabase db = new BaseDatos(this.context).getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);    
        
        if (cursor.moveToFirst()) {
            do {
            	
				Cliente cliente = new Cliente();
				cliente.id_cliente = cursor.getInt(0);
				cliente.nombre_cliente = cursor.getString(1);
				cliente.direccion_cliente = cursor.getString(2);
				cliente.telefono_cliente = cursor.getString(3);
				cliente.id_usuario = cursor.getInt(4);
				
				lista.add(cliente);
            	
            } while (cursor.moveToNext());
        }
        
        db.close();
		
		return lista;

	}
	
	
	//Entrega los clientes asignados a un usuario y visibles o activos
	public ArrayList<Cliente> MisClientes(Context mContext, int id_usuario)
	{
		this.context = mContext;
		this.usuario_id = id_usuario;
		
		ArrayList<Cliente> lista = ListarClientes();
		
		//se crea variable para recojer mis clientes
		ArrayList<Cliente> misClientes = new ArrayList<Cliente>();
		
		//se calcula la cantidad de objetos en la lista para no hacerlo en cada ciclo del FOR
		int totalClientes = lista.size();
		
		//se Busca a los clientes asignados al usuario actual
		for(int i = 0; i < totalClientes ; i++)
		{
			misClientes.add(lista.get(i));
		}
		
		return misClientes;
		
	}

	//forma String de la clase para general el listview
	public String toString()
	{
		return this.nombre_cliente + "\n" + this.direccion_cliente + "\n" + this.telefono_cliente; 
	}
	
	public Cliente MostrarCliente(int id)
	{		
		// creamos conexion
        SQLiteDatabase db = new BaseDatos(context).getWritableDatabase();
 
        // ejecutar query
        Cursor cursor = db.query(TABLE, COLUMNS, " id_cliente = ?", new String[] { String.valueOf(id) }, null, null, null, null);
 
        if (cursor != null)
            cursor.moveToFirst();
 
        //creamos el objeto
        Cliente cliente = new Cliente();
        cliente.id_cliente = cursor.getInt(0);
        cliente.nombre_cliente = cursor.getString(1);
        cliente.direccion_cliente = cursor.getString(2);
        cliente.telefono_cliente = cursor.getString(3);
        cliente.id_usuario = cursor.getInt(4);
		
		return cliente;
	}
	
	public ArrayList<Cliente> agregarCliente(Cliente cliente)
	{
		 
        // creamos conexion
        SQLiteDatabase db = new BaseDatos(context).getWritableDatabase();
 
        // crear los valores a insertar
        ContentValues values = new ContentValues();
        values.put("nombre_cliente", cliente.nombre_cliente);
        values.put("direccion_cliente", cliente.direccion_cliente);
        values.put("telefono_cliente", cliente.telefono_cliente);
        values.put("id_usuario", cliente.id_usuario);
        values.put("visible", 1);
 
        // ejecutar query
        db.insert(TABLE, null, values);
 
        // cerramos la db
        db.close(); 
		
		return ListarClientes();
	}
	
	public ArrayList<Cliente> borrarCliente(int id)
	{
		
		// creamos conexion
        SQLiteDatabase db = new BaseDatos(context).getWritableDatabase();
	 
        // crear los valores a eliminar
	    ContentValues values = new ContentValues();
	    values.put("visible", 0); 
	 
	    // ejecutar query
	    db.update(TABLE, values, "id_cliente = ?", new String[] { String.valueOf(id) });
	 
	    // cerramos la db
	    db.close();
		
		return ListarClientes();
	}	
	
	public ArrayList<Cliente> actualizarCliente(Cliente cliente, int id)
	{
		
		// creamos conexion
        SQLiteDatabase db = new BaseDatos(context).getWritableDatabase();
	 
        // crear los valores a actualizar
	    ContentValues values = new ContentValues();
	    values.put("nombre_cliente", cliente.nombre_cliente); 
	    values.put("direccion_cliente", cliente.direccion_cliente);
	    values.put("telefono_cliente", cliente.telefono_cliente);
	 
	    // ejecutar query
	    db.update(TABLE, values, "id_cliente = ?", new String[] { String.valueOf(id) });
	 
	    // cerramos la db
	    db.close();
		
		return ListarClientes();
	}

	
}
