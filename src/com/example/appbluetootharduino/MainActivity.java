package com.example.appbluetootharduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.R;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Conexão Bluetooth dentro da aplicação Android
 * 
 * @author Administrador
 */
public class MainActivity extends Activity {

	String frente;
	String direita;
	String esquerda;
	String tras;
	String x;
	String y;
	String z;
	String a;
	String b;
	String c;

	public static final String PREFS_NAME = "Preferences";
	public String valorX, ValorB;
	Intent Value_Bottons;
	Handler h;
	final int RECIEVE_MESSAGE = 1;
	Set<BluetoothDevice> devicesArray;
	ArrayList<String> pairedDevices;
	ArrayList<BluetoothDevice> devices;
	LayoutInflater layoutInflater;

	// Representas os botoes
	// Que ao ser pressioando pelo usuario executa uma ação
	Button btnConectar, btnDesconectar, btnFrente, btnDireita, btnEsquerda, btnTras, btn1, btn2, btn3, btn4, btn5, btn6;
	ToggleButton btnConectar2;

	View dados;
	TextView txtArduino, txtArduino2, txtArduino3;
	EditText EtxtE, texto;

	BluetoothThread btt;

	// private ConnectedThread mConnectedThread;

	Handler writeHandler;

	// Requisição para Activity de ativação do Bluetooth
	// Se numero for maior > 0,este codigo sera devolvido em onActivityResult()
	private static final int REQUEST_ENABLE_BT = 1;
	public static final int SELECT_PAIRED_DEVICE = 2;
	public static final int VALUE = 3;
	public static final int VALORES = 4;

	// BluetoothAdapter é comando de entrada padrão paras todads interações com
	// Bluetooth
	private BluetoothAdapter bluetoothPadrao = null;

	// BluetoothSocket é um ponto de conexão que permite trocar dados com outro
	// disposivo
	// bleutooth atravs do ImputStream() e OutputStream()
	private BluetoothSocket btSocket = null;

	// Para manipular as transmissoes de dados atraves do socket,
	// è necessario ter ImputStream e um OutputStream,
	// via getImputStream() e getOutputStream()
	private OutputStream outStream = null;// Para saida de informação.

	// Para armazenar os dados a serem enviados;
	private String dadosParaEnvio;

	// Para armazenar o endereço MAC do modulo Bluetooth
	private static String address = "20:13:06:19:08:29";

	// Um Universally Unique IDentifier(UUID) é um formato padronizado para ID
	// string de 128 bits
	// usado para identificar de forma unica.
	private static final UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	boolean ativado = false;

	/**
	 * Criação da tela
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		setContentView(R.layout.activity_main);
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		Value_Bottons = new Intent(this, Values_bottons.class);
		resgatarValores();

		dados = (View) findViewById(R.id.dados);

		txtArduino = (TextView) findViewById(R.id.txtArduino);

		btnConectar = (Button) findViewById(R.id.btnConectar);
		btnConectar.setText("Conectar");

		btnFrente = (Button) findViewById(R.id.bt_frente);

		btnDireita = (Button) findViewById(R.id.bt_direita);
		btnEsquerda = (Button) findViewById(R.id.bt_esquerda);
		btnTras = (Button) findViewById(R.id.bt_tras);
		btn1 = (Button) findViewById(R.id.bt_x);
		btn2 = (Button) findViewById(R.id.bt_y);
		btn3 = (Button) findViewById(R.id.bt_z);
		btn4 = (Button) findViewById(R.id.bt_a);
		btn5 = (Button) findViewById(R.id.bt_b);
		btn6 = (Button) findViewById(R.id.bt_c);

		// Obtem o bluetooth padrao do aparelho celular
		bluetoothPadrao = BluetoothAdapter.getDefaultAdapter();

		// Vereficamos se o aparelho possui adaptador Bluetooth
		if (bluetoothPadrao == null) {
			Toast.makeText(getApplicationContext(), "Dispostivo nao possui Bluetooth", Toast.LENGTH_LONG).show();
			// finish();
			return;
		}
		if (!bluetoothPadrao.isEnabled()) {
			Intent novoIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(novoIntent, REQUEST_ENABLE_BT);
		}

		btnFrente.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = frente;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});

		btnDireita.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = direita;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});

		btnEsquerda.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = esquerda;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});
		btnTras.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = tras;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});

		btn1.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = x;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});

		btn2.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = y;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});
		btn3.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = z;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});
		btn4.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = a;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});
		btn5.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = b;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});
		btn6.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				if (btt != null) {
					Message msg = Message.obtain();
					msg.obj = c;
					writeHandler.sendMessage(msg);
				} else {
					Toast.makeText(getApplicationContext(), "Bluetooth nao conectado", Toast.LENGTH_LONG).show();
				}

				return false;
			}
		});

	}

	public void resgatarValores() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		frente = settings.getString("frente", "");
		direita = settings.getString("direita", "");
		esquerda = settings.getString("esquerda", "");
		tras = settings.getString("tras", "");
		x = settings.getString("x", "");
		y = settings.getString("y", "");
		z = settings.getString("z", "");
		a = settings.getString("a", "");
		b = settings.getString("b", "");
		c = settings.getString("c", "");
	}

	public void connectButtonPressed(View v) {
		if (btt == null) {
			Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
			startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
		} else {
			btnConectar.setText("Conectar");
			// ativado = false;
			// if(btt != null) {
			btt.interrupt();
			btt = null;
			// }
		}

		if (btt != null) {
			// Get the handler that is used to send messages
			writeHandler = btt.getWriteHandler();

			// Run the thread
			btt.start();

			TextView tv = (TextView) findViewById(R.id.btnConectar);
			btnConectar.setText("Connecting...");
			// btnConectar.setEnabled(false);

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

		// Retrono do pedido de ativação do Bluetooth
		case REQUEST_ENABLE_BT:

			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(getApplicationContext(), "Bluetooth Ativado XD", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Bluetooth fica desativado entao :( bay", Toast.LENGTH_LONG)
						.show();
				finish();
			}
			break;
		case SELECT_PAIRED_DEVICE:
			if (resultCode == RESULT_OK) {
				btnConectar = (Button) findViewById(R.id.btnConectar);
				if (btt == null) {

					btt = new BluetoothThread(data.getStringExtra("btDevAddress"), new Handler() {

						@Override
						public void handleMessage(Message message) {

							String s = (String) message.obj;

							String textoB[] = s.split(";");

							// Do something with the message
							if (s.equals("CONNECTED")) {
								btnConectar.setText("Desconectar");
								btnConectar.setEnabled(true);

								// ativado = true;
							} else if (s.equals("DISCONNECTED")) {

								// tv.setText("Disconnected.");
							} else if (s.equals("CONNECTION FAILED")) {

								// tv.setText("Connection failed!");
								btt = null;
							} else {
								TextView tv = (TextView) findViewById(R.id.);
								TextView tv2 = (TextView) findViewById(R.id.txtArduino2);
								TextView tv3 = (TextView) findViewById(R.id.txtArduino3);

								

								for (int i = 0; i < textoB.length; i++) {
									switch (i) {
									case 0:
										tv.setText(textoB[0]);
										break;
									case 1:
										tv2.setText(textoB[1]);
										break;
									case 2:
										tv3.setText(textoB[2]);
										break;
									case 3:
										
										break;
									case 4:

										break;

									default:
										Toast.makeText(getApplicationContext(), "Numeros maximos de linhas ultrapassado!!!!!!!", Toast.LENGTH_LONG)
										.show();
										break;
									}
								}
							}
						}
					});
				}

				if (btt != null) {
					// Get the handler that is used to send messages
					writeHandler = btt.getWriteHandler();

					// Run the thread
					btt.start();

					TextView tv = (TextView) findViewById(R.id.btnConectar);
					btnConectar.setText("Connecting...");
					btnConectar.setEnabled(false);
				}
				// break;

			} else {
				Toast.makeText(getApplicationContext(), "Nenhum dispositivo Selecionado", Toast.LENGTH_LONG).show();
			}
			break;
		case VALORES:
			if (resultCode == RESULT_OK) {
				frente = data.getStringExtra("frente");
				direita = data.getStringExtra("direita");
				esquerda = data.getStringExtra("esquerda");
				tras = data.getStringExtra("tras");
				x = data.getStringExtra("x");
				y = data.getStringExtra("y");
				z = data.getStringExtra("z");
				a = data.getStringExtra("a");
				b = data.getStringExtra("b");
				c = data.getStringExtra("c");
			} else {
				Toast.makeText(getApplicationContext(), "Mudanças nao salvas", Toast.LENGTH_LONG).show();
			}

			break;
		}
	};

	/**
	 * Permite a saida de dados a partir de um socket
	 */
	public void EnviarDados(String data) {
		if (btt != null) {// btSocket

			try {

				outStream = btt.socket.getOutputStream();// btSocket
			} catch (IOException e) {
				// TODO Colocar o tratamento da excecao (uma mensagem de erro
				// deve
				// aparecer na tela do usuario
			}

			String mensagem = data;
			byte[] msgBuffer = mensagem.getBytes();

			try {
				// Enviar conteudo pelo bluetooth
				outStream.write(msgBuffer);
			} catch (IOException e) {
				// TODO Colocar o tratamento da excecao (uma mensagem de erro
				// deve
				// aparecer na tela do usuario
			}
		} else {
			Toast.makeText(getApplicationContext(), "Bluetooth não esta conectado", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * Apresenta a barra de menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/*
	 * Trata as ações de menu
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int panel, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Toast.makeText(this, "Sair", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case R.id.item1:

			break;
		case R.id.item2:
			Toast.makeText(this, "So mostro meu ID XD: " + (item.getItemId() + 1), Toast.LENGTH_SHORT).show();
			break;
		case R.id.item3:
			// Intent Value_Bottons = new Intent(this, Values_bottons.class);
			Value_Bottons.putExtra("frente", frente);
			Value_Bottons.putExtra("direita", direita);
			Value_Bottons.putExtra("esquerda", esquerda);
			Value_Bottons.putExtra("tras", tras);
			Value_Bottons.putExtra("x", x);
			Value_Bottons.putExtra("y", y);
			Value_Bottons.putExtra("z", z);
			Value_Bottons.putExtra("a", a);
			Value_Bottons.putExtra("b", b);
			Value_Bottons.putExtra("c", c);
			startActivityForResult(Value_Bottons, VALORES);

			break;
		case R.id.item4:

			break;
		case R.id.item6:
			dados.setVisibility(View.VISIBLE);
			txtArduino.setVisibility(View.VISIBLE);
			// txtArduino2.setVisibility(View.VISIBLE);
			Toast.makeText(getApplicationContext(), "Visible", Toast.LENGTH_LONG).show();
			break;
		case R.id.item7:
			dados.setVisibility(View.INVISIBLE);
			txtArduino.setVisibility(View.INVISIBLE);
			// txtArduino2.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Invisible", Toast.LENGTH_LONG).show();
			break;

		}

		return true;
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onPause() {
		super.onPause();

		if (btt != null) {
			btt.interrupt();
			btt = null;
			btnConectar.setText("Conectar");
		}

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("frente", frente);
		editor.putString("direita", direita);
		editor.putString("esquerda", esquerda);
		editor.putString("tras", tras);
		editor.putString("x", x);
		editor.putString("y", y);
		editor.putString("z", z);
		editor.putString("a", a);
		editor.putString("b", b);
		editor.putString("c", c);

		// Confirma a gravação dos dados
		editor.commit();
	}

}
