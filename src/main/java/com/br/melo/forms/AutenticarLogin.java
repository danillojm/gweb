package com.br.melo.forms;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.melo.dao.UsuarioDAO;
import com.br.melo.entidade.Usuario;

@ManagedBean
@SessionScoped
public class AutenticarLogin {

	private Usuario usuario;
	Usuario usuarioLogado;
	private String urlRedirect;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostConstruct
	public void iniciar() {
		usuario = new Usuario();
	}

	public void login() {

		FacesMessage message = null;

		logger.info("Inicializando Hibernate Utils");

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String nomUrl = request.getServerName();
		try {
			UsuarioDAO dao = new UsuarioDAO();
			setUsuarioLogado(dao.autenticar(usuario.getNome(), usuario.getSenha()));

			if (getUsuarioLogado() == null) {
				Messages.addGlobalError("Nome ou senha incorreto");
				return;
			}

			FacesContext facesContext = FacesContext.getCurrentInstance();
			((HttpSession) facesContext.getExternalContext().getSession(true)).setAttribute("usuarioLogado", usuarioLogado);

			String url = "./frm_aluno.xhtml";

			if (getUrlRedirect() != null && !getUrlRedirect().isEmpty()) {
				url = getUrlRedirect();
			}

			Faces.redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
			Messages.addGlobalError(e.getMessage());
		}
	}

	public void logOut() throws IOException {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		((HttpSession) facesContext.getExternalContext().getSession(true)).removeAttribute("usuarioLogado");

		((HttpSession) facesContext.getExternalContext().getSession(true)).invalidate();
		facesContext.getExternalContext().redirect("login.xhtml");

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public String getUrlRedirect() {
		return urlRedirect;
	}

	public void setUrlRedirect(String urlRedirect) {
		this.urlRedirect = urlRedirect;
	}
}
