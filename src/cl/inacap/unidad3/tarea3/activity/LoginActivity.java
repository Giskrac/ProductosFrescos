package cl.inacap.unidad3.tarea3.activity;

import cl.inacap.unidad3.tarea3.clases.BaseDatos;
import cl.inacap.unidad3.tarea3.clases.Usuario;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {
	
	TextView txt_error_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        BaseDatos db = new BaseDatos(this);
        
        //creamos un textview para mostrar el error de login
        txt_error_login = (TextView)findViewById(R.id.txt_error_login);
        
        Button btn_ingresar = (Button)findViewById(R.id.btn_ingresar);
        btn_ingresar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				validarLoginUsuario();				
			}
        	
        });
    }

    //Se realiza la validacion del usuario
    public void validarLoginUsuario()
    {
    	EditText txt_login = (EditText)findViewById(R.id.txt_login);
    	EditText txt_contrasena = (EditText)findViewById(R.id.txt_contrasena);
    	
    	//volvemos a ocultar el textview del error
    	txt_login.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				txt_error_login.setVisibility(View.INVISIBLE);
				
			}
		});
    	
    	//volvemos a ocultar el textview del error
    	txt_contrasena.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				txt_error_login.setVisibility(View.INVISIBLE);
				
			}
		});
    	
    	Usuario usuario = new Usuario();
    	int id_usuario = usuario.validarLogin(this, txt_login.getText().toString(), txt_contrasena.getText().toString());
    	if(id_usuario != 0)
    	{
    		txt_login.setText("");
    		txt_contrasena.setText("");
    		
    		SharedPreferences.Editor editor = getSharedPreferences("user_data", MODE_PRIVATE).edit();
    		 editor.putInt("id_usuario", id_usuario);
    		 editor.commit();
    		
    		Intent intent = new Intent(LoginActivity.this, ClientesActivity.class);
    		LoginActivity.this.startActivity(intent);    		
    	}
    	else
    	{
    		//mostramos el textview del error
    		txt_error_login.setVisibility(View.VISIBLE);
    	}
    }

}
