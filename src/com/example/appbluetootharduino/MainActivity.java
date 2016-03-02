package com.example.appbluetootharduino;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.util.Log;

/**
 * Conex�o Bluetooth dentro da aplica��o Android
 * 
 * @author Administrador
 */
public class MainActivity extends Activity {
	
	Set<BluetoothDevice> devicesArray;
	ArrayList<String> pairedDevices;
	ArrayList<BluetoothDevice> devices;

	// Representas os botoes
	// Que ao ser pressioando pelo usuario executa uma a��o
	Button btnConectar, btnDesconectar, btnFrente, btnDireita, btnEsquerda, btnTras, btn1, btn2, btn3, btn4, btn5, btn6;
	ToggleButton btnConectar2;

	EditText txtMostrar;

	// Requisi��o para Activity de ativa��o do Bluetooth
	// Se numero for maior > 0,este codigo sera devolvido em onActivityResult()
	private static final int REQUEST_ENABLE_BT = 1;

	// BluetoothAdapter � comando de entrada padr�o paras todads intera��es com
	// Bluetooth
	private BluetoothAdapter BluetoothPadr�o = null;

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

		// Referencia dos botoes do Activity_main.xml pelos ID
		
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
		
		btn1.setId(1);
		MyTouchListener touchListener = new MyTouchListener();
	    btn1.setOnTouchListener(touchListener);

		// Obtem o bluetooth padrao do aparelho celular
		BluetoothPadr�o = BluetoothAdapter.getDefaultAdapter();

		// Vereficamos se o aparelho possui adaptador Bluetooth
		if (BluetoothPadr�o == null) {
			Toast.makeText(getApplicationContext(), "Dispostivo nao possui Bluetooth", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		if (!BluetoothPadr�o.isEnabled()) {
			Intent novoIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(novoIntent, REQUEST_ENABLE_BT);
		}

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
/*
		btn1.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, final MotionEvent motionEvent) {

				dadosParaEnvio = "e";
				EnviarDados(dadosParaEnvio);

				return false;
			}
		});*/

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
	
	public class MyTouchListener implements OnTouchListener {
	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
	        switch(v.getId()){
	            case 1:
	            	dadosParaEnvio = "e";
					EnviarDados(dadosParaEnvio);

	                break;
	            case 2:
	                //do stuff for button 2
	                break;
	            case 3:
	                //do stuff for button 3
	                break;
	            case 4:
	                //do stuff for button 4
	                break;
	        }
	        return true;
	    }

	}

	

	private void getPairedDevices() {
		devicesArray = BluetoothPadr�o.getBondedDevices();
		if (devicesArray.size()>0){
			for(BluetoothDevice device:devicesArray){
				pairedDevices.add(device.getName());
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
			BluetoothDevice device = BluetoothPadr�o.getRemoteDevice(mac);

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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * Trata as a��es de menu
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
