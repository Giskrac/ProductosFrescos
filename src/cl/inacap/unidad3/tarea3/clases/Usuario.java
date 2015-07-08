package cl.inacap.unidad3.tarea3.clases;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import cl.inacap.unidad3.tarea3.activity.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Usuario {
	
	public String TAG = "Usuario Class";
	
	public int id_usuario;
	public String nombre_usuario;
	public String login_usuario;
	public String contrasena;
	
	private Context context;
	
	//Se genera y obtiene la lista de usuarios
	public ArrayList<Usuario> listaUsuarios()
	{
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		
		//Creamos la query para obtener los registros
        String query = "SELECT * FROM usuario";
 
        //ejecutamos la query
        SQLiteDatabase db = new BaseDatos(context).getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        Usuario usuario = null;
        
        if (cursor.moveToFirst()) {
            do {
            	
            	usuario = new Usuario();
				usuario.id_usuario = cursor.getInt(0);
				usuario.nombre_usuario = cursor.getString(1);
				usuario.login_usuario = cursor.getString(2);
				usuario.contrasena = cursor.getString(3);
				
				lista.add(usuario);
            	
            } while (cursor.moveToNext());
        }
	
		return lista;
	}
	
	//Se realiza la validacion del Login de usuario
	public int validarLogin(Context mcontext, String login, String contrasena)
	{
		this.context = mcontext;
		
		Usuario usuario;
		ArrayList<Usuario> usuarios = listaUsuarios();
		int largo = usuarios.size();
		
		for(int i=0;i < largo;i++)
		{
			usuario = usuarios.get(i);
			
			if(usuario.login_usuario.equals(login) && usuario.contrasena.equals(contrasena))
			{
				return usuario.id_usuario;
			}
		}
		
		return 0;
	}
	
}
