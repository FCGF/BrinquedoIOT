package com.example.appbluetootharduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.R.layout;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.util.Log;

/**
 * Conex�o Bluetooth dentro da aplica��o Android
 * 
 * @author Administrador
 */
public class MainActivity extends Activity {

	Handler h;
	final int RECIEVE_MESSAGE = 1;
	Set<BluetoothDevice> devicesArray;
	ArrayList<String> pairedDevices;
	ArrayList<BluetoothDevice> devices;
	LayoutInflater layoutInflater;
	private StringBuilder sb = new StringBuilder();

	// Representas os botoes
	// Que ao ser pressioando pelo usuario executa uma a��o
	Button btnConectar, btnDesconectar, btnFrente, btnDireita, btnEsquerda, btnTras, btn1, btn2, btn3, btn4, btn5, btn6;
	ToggleButton btnConectar2;

	View dados;
	TextView txtArduino;

	private ConnectedThread mConnectedThread;

	// Requisi��o para Activity de ativa��o do Bluetooth
	// Se numero for maior > 0,este codigo sera devolvido em onActivityResult()
	private static final int REQUEST_ENABLE_BT = 1;

	// BluetoothAdapter � comando de entrada padr�o paras todads intera��es com
	// Bluetooth
	private BluetoothAdapter bluetoothPadrao = null;

	// BluetoothSocket � um ponto de conex�o que permite trocar dados com outro
	// disposivo
	// bleutooth atravs do ImputStream() e OutputStream()
	private BluetoothSocket btSocket = null;

	// Para manipular as transmissoes de dados atraves do socket,
	// � necessario ter ImputStream e um OutputStream,
	// via getImputStream() e getOutputStream()
	private OutputStream outStream = null;// Para saida de informa��o.

	// Para armazenar os dados a serem enviados;
	private String dadosParaEnvio;

	// Para armazenar o endere�o MAC do modulo Bluetooth
	private static String mac = "20:13:06:19:08:29";

	// Um Universally Unique IDentifier(UUID) � um formato padronizado para ID
	// string de 128 bits
	// usado para identificar de forma unica.
	private static final UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private boolean clicando = true;

	/**
	 * Cria��o da tela
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		// Liga com o layout da tela
		setContentView(R.layout.activity_main);
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		// ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));

		// Referencia dos botoes do Activity_main.xml pelos ID

		dados = (View) findViewById(R.id.dados);

		txtArduino = (TextView) findViewById(R.id.txtArduino);

		ToggleButton btnConectar2 = (ToggleButton) findViewById(R.id.btnConectar);

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
			finish();
			return;
		}
		if (!bluetoothPadrao.isEnabled()) {
			Intent novoIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(novoIntent, REQUEST_ENABLE_BT);
		}

		h = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case RECIEVE_MESSAGE: // if receive massage
					byte[] readBuf = (byte[]) msg.obj;
					String strIncom = new String(readBuf, 0, msg.arg1);

					 txtArduino.setText(strIncom);
					// txtArduino.clearComposingText();

					//sb.append(strIncom);
					//sb.indexOf("\r\n");
					
						
					
				   // txtArduino.setText(readBuf.toString());
				    //sb.delete(0, sb.length());	
					

					// Log.d(TAG, "...String:"+ sb.toString() + "Byte:" +
					// msg.arg1 + "...");
					break;
				}
			};
		};

		btnConectar2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					conectar();
				} else {
					desconectar();

				}
			}
		});
		// Defini��o de interface para ser chamada quando um Botao � clicado.
		/*
		 * btnConectar.setOnClickListener(new View.OnClickListener() {
		 * 
		 * // Ativa quando o bot�o � clicado.
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * // Chama a fun��o que ira conectar o Bluetooth
		 * 
		 * conectar();
		 * 
		 * }
		 * 
		 * });
		 * 
		 * btnDesconectar.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // Chama a fun��o para
		 * desconectar o Bluetooth desconectar();
		 * 
		 * } });
		 */
		btnFrente.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "e";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});

		btnDireita.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "e";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});

		btnEsquerda.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "e";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});
		btnTras.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "e";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});

		btn1.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "e";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});

		btn2.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "e";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});
		btn3.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "g";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});
		btn4.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "h";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});
		btn5.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "i";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});
		btn6.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "j";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});

	}

	private class ConnectedThread extends Thread {
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024]; // buffer store for the stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer); // Get number of bytes and
														// message in "buffer"
					h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget(); // Send
																				// to
																						// message
																						// queue
																						// Handler
				} catch (IOException e) {
					break;
				}
			}
		}

	}

	/**
	 * Resultado de quando a Se o resultado da intera��o com usuario for (OK)
	 * mostra a mensagem de Ativa��o Sen�o mostra a de desativado e fecha a
	 * aplica��o.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

		// Retrono do pedido de ativa��o do Bluetooth
		case REQUEST_ENABLE_BT:

			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(getApplicationContext(), "Bluetooth Ativado XD", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Bluetooth fica desativado entao :( bay", Toast.LENGTH_LONG)
						.show();
				finish();
			}
			break;

		}
	};

	/**
	 * Permite a saida de dados a partir de um socket
	 */
	public void EnviarDados(String data) {
		if (btSocket != null) {

			try {

				outStream = btSocket.getOutputStream();
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
			Toast.makeText(getApplicationContext(), "Bluetooth n�o esta conectado", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * Para se ter Bluetooth device ,usa-se
	 * BluetoothAdapter.getRemoteDevice(String) Para representar um dispostivo
	 * de um endere�o MAC conhecido ou obter a partir de um conjunto de
	 * dispositovos pareados a partir do BluetoothAdapter.getBondedDevices()
	 */
	public void conectar() {
		if (btSocket == null) {
			BluetoothDevice device = bluetoothPadrao.getRemoteDevice(mac);

			try {
				btSocket = device.createInsecureRfcommSocketToServiceRecord(MEU_UUID);
				// Usamos connect() para iniciar a conex�o de saida.
				btSocket.connect();
				Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), "Erro ao conectar", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Bluetooth ja esta conectado", Toast.LENGTH_LONG).show();
		}
		mConnectedThread = new ConnectedThread(btSocket);
		mConnectedThread.start();

	}

	/**
	 * Desconeta o dispositivo
	 */
	public void desconectar() {
		if (btSocket != null) {
			try {
				btSocket.close();
				// e seta socket para null;
				btSocket = null;
				Toast.makeText(getApplicationContext(), "Desconectado", Toast.LENGTH_LONG).show();
			} catch (IOException e) {
			}
		} else {
			Toast.makeText(getApplicationContext(), "Ja esta desconectado", Toast.LENGTH_LONG).show();
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
	 * Trata as a��es de menu
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
			// Toast.makeText(this, "test" + (item.getItemId() + 1),
			// Toast.LENGTH_SHORT).show();
			boolean mostrar = true;

			if (mostrar == true) {
				txtArduino.setVisibility(View.INVISIBLE);
				Toast.makeText(getApplicationContext(), "Invisible", Toast.LENGTH_LONG).show();
				mostrar = false;
			} else {
				txtArduino.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "Visible", Toast.LENGTH_LONG).show();
				mostrar = true;
			}

			break;
		case R.id.item2:
			Toast.makeText(this, "So mostro meu ID XD: " + (item.getItemId() + 1), Toast.LENGTH_SHORT).show();
			break;
		case R.id.item3:

			break;
		case R.id.item4:

			break;
		case R.id.item6:
			dados.setVisibility(View.VISIBLE);
			txtArduino.setVisibility(View.VISIBLE);
			Toast.makeText(getApplicationContext(), "Visible", Toast.LENGTH_LONG).show();
			break;
		case R.id.item7:
			dados.setVisibility(View.INVISIBLE);
			txtArduino.setVisibility(View.INVISIBLE);
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

}
