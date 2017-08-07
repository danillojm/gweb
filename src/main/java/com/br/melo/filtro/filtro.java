/*package com.br.melo.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omnifaces.util.Faces;

import com.br.melo.entidade.Usuario;
import com.br.melo.forms.AutenticarLogin;

import br.com.appsware.appsnexus.utils.SessaoUsuario;

 
public class filtro implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		// Acontece antes da chamada
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();

		AutenticarLogin usuarioLogado = (AutenticarLogin) httpRequest.getSession().getAttribute("usuarioLogado");

		if (usuarioLogado != null) {
			String url = "./frm_aluno.xhtml";

			((HttpServletResponse) response).sendRedirect(url);
			chain.doFilter(request, response);
		}
		httpResponse.sendRedirect("./login.xhtml");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
*/