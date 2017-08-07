package com.br.melo.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.omnifaces.util.Faces;

import com.br.melo.entidade.Usuario;
import com.br.melo.forms.AutenticarLogin;

@SuppressWarnings("serial")
public class AltenticacaoListiner implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent event) {

		String paginaAtual = Faces.getViewId();

		boolean paginaDeAutenticacao = paginaAtual.contains("login.xhtml");

		if (!paginaDeAutenticacao) {

			AutenticarLogin autenticarLogin = Faces.getSessionAttribute("autenticarLogin");

			if (autenticarLogin == null) {
				Faces.navigate("login.xhtml");
				return;
			}
			Usuario usuario = autenticarLogin.getUsuarioLogado();
			if (usuario == null) {
				Faces.navigate("login.xhtml");
				return;
			}

		}

	}

	@Override
	public void beforePhase(PhaseEvent event) {

	}

	@Override
	public PhaseId getPhaseId() {

		return PhaseId.RESTORE_VIEW;
	}

}
