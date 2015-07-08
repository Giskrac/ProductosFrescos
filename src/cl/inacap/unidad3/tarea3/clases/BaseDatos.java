package cl.inacap.unidad3.tarea3.clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {
	public static String DB_PATH = "./";
	public static String DB_NAME = "pfrescos-db";
	public static int DB_VERSION = 1;	
	
	//Creamos las querys para crear las tablas necesarias para almacenar los datos de nuestra aplicacion
	
	private String CREATE_TABLE_USUARIO = "CREATE TABLE usuario(" 
			+ "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ "nombre_usuario TEXT,"
			+ "login_usuario TEXT,"
			+ "contrasena TEXT"
			+ ");";
	
	private String CREATE_TABLE_CLIENTE = "CREATE TABLE cliente(" 
			+ "id_cliente INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ "nombre_cliente TEXT,"
			+ "direccion_cliente TEXT,"
			+ "telefono_cliente TEXT,"
			+ "id_usuario INTEGER,"
			+ "visible INTEGER"
			+ ");";
	
	private String CREATE_TABLE_PRODUCTO = "CREATE TABLE producto(" 
			+ "id_producto INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ "nombre_producto TEXT,"
			+ "precio_producto INTEGER"
			+ ");";
	
	private String CREATE_TABLE_PEDIDO = "CREATE TABLE pedido(" 
			+ "id_pedido INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ "id_cliente INTEGER,"
			+ "id_usuario INTEGER,"
			+ "id_producto INTEGER,"
			+ "fecha_entrega TEXT,"
			+ "cantidad INTEGER,"
			+ "visible INTEGER"
			+ ");";
	
	private String CREATE_TABLE_POSICION = "CREATE TABLE posicion(" 
			+ "id_direccion INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ "latitud REAL,"
			+ "longitud REAL,"
			+ "direccion TEXT"
			+ ");";
	
	//creamos los insert para llenar las nuevas tablas con datos de prueba	
	private String INSERT_DB_USUARIO = "INSERT INTO usuario(nombre_usuario,login_usuario,contrasena) VALUES('Jose Gonzales','jose','jose'),('Luis Astudillo','luis','luis');"; 
	private String INSERT_DB_CLIENTE = "INSERT INTO cliente(nombre_cliente,direccion_cliente,telefono_cliente,id_usuario,visible) VALUES('Almacen La Joyita','Macul 4535, Macul, Santiago, Chile','56222345678',1,1),('Botilleria don Chuma','Avenida el Valle 123, Peñalolen, Santiago, Chile','56223452345',2,1),('Panaderia Buenos Dias','Las Codornices 376, Macul, Santiago, Chile','56229877854',1,1),('Almacen Don Luis','Macul 10254, Macul, Santiago, Chile','56221265783',1,1),('Almacen Rayito de sol','Valle del sol 0123,La Florida, Santiago, Chile','56221189654',2,1),('Botilleria Lemunaho','Pasaje llanura 35, Peñalolen, Santiago, Chile','56225736547',2,1);";
	private String INSERT_DB_PRODUCTO = "INSERT INTO producto(nombre_producto,precio_producto) VALUES('Coca-Cola 3lt',1200),('Chicles BigTime 100 unidades',2000),('Pepsi 2lt',1500),('Papas fritas Evercrisp 60 unidades',2180),('Alfajos Fruna 20 unidades',3000),('Super 8 20 unidades',6520),('Galletas de Soda Grande',3180),('Galletas de Soda mediana',3145),('Helado Cremino Caja',2599),('Helado Magnum Caja',3250);"; 
	
	public BaseDatos(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

		if(db.isReadOnly())
		{
			db = getWritableDatabase();
		}

		//creamos las tablas
		db.execSQL(CREATE_TABLE_USUARIO);
		db.execSQL(CREATE_TABLE_CLIENTE);
		db.execSQL(CREATE_TABLE_PRODUCTO);
		db.execSQL(CREATE_TABLE_PEDIDO);
		db.execSQL(CREATE_TABLE_POSICION);
		
		//poblamos las tablas con datos de prueba
		db.execSQL(INSERT_DB_USUARIO);
		db.execSQL(INSERT_DB_CLIENTE);
		db.execSQL(INSERT_DB_PRODUCTO);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if(newVersion > oldVersion)
		{
			//CUANDO SE QUIERA HACER UNA RESTRUCTURACION DE LA BASE DE DATOS AQUI ESTARAN 
			//LAS QUERY's PARA HACER LAS MIGRACIONES A LAS NUEVAS TABLAS
			//POR AHORA ESTO NO LO USAREMOS
		}
	}
}
