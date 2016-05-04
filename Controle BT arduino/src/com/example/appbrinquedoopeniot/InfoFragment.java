package com.example.appbrinquedoopeniot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class InfoFragment extends DialogFragment {
	Button googleBtn, googlePlayBtn, facebookBtn;
	

	//private static final String linkCatolica = "http://www.catolicasc.org.br/";
	private static final String linkFacebook = "https://www.facebook.com/WickedBotz/?fref=ts";
	private static final String linkGmail= "https://plus.google.com/u/0/116474376621584890949/posts";
	private static final String linkGooglePlay = "https://www.google.com.br/?gws_rd=ssl";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_info_fragment, container, true);
		getDialog().setTitle("Sobre o aplicativo");
		// Do something else

		
		facebookBtn =(Button)rootView.findViewById(R.id.facebookBtn);
		googleBtn =(Button)rootView.findViewById(R.id.googleBtn);
		googlePlayBtn =(Button)rootView.findViewById(R.id.googlePlayBtn);
		
		facebookBtn.setOnClickListener(new chamarPagina(linkFacebook,"Grupo criador: WhickBotz"));
		googleBtn.setOnClickListener(new chamarPagina(linkGmail,"Desenvolvedor do app"));
		googlePlayBtn.setOnClickListener(new chamarPagina(linkGooglePlay,"Pagina da googlePlay"));
			
			
		
		

		return rootView;
	}
	
	
	
	
	
	public class chamarPagina implements OnClickListener{

		String link;
		String mensagem;
		
		public  chamarPagina(String link, String mensagem) {
			this.link = link;
			this.mensagem = mensagem;
		}
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			startActivity(Intent.createChooser(intent, "Escolha seu navegador |" + mensagem));
		}
		
	}

	
	 

}
