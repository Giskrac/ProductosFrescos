package cl.inacap.unidad3.tarea3.clases;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import cl.inacap.unidad1.tarea2.activity.R;
import android.content.Context;

public class Usuario {
	public int id_usuario;
	public String nombre_usuario;
	public String login_usuario;
	public String contrasena;
	
	private Context context;
	
	//Se genera y obtiene la lista de usuarios
	public ArrayList<Usuario> listaUsuarios()
	{
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		
		String usuarios[] = this.context.getResources().getStringArray(R.array.usuarios_array);
		
		int cantidad = usuarios.length;
		
		for(int i =0; i < cantidad; i++)
		{
			
			try {
				
				JSONObject jsonUsuarios = new JSONObject(usuarios[i]);
				
				Usuario usuario = new Usuario();
				usuario.id_usuario = jsonUsuarios.getInt("id_usuario");
				usuario.nombre_usuario = jsonUsuarios.getString("nombre_usuario");
				usuario.login_usuario = jsonUsuarios.getString("login_usuario");
				usuario.contrasena = jsonUsuarios.getString("contrasena");
				
				lista.add(usuario);
				
			} catch (JSONException e) {

				e.printStackTrace();
			}
			
			
			
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
