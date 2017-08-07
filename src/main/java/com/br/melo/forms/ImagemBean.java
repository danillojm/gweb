package com.br.melo.forms;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@RequestScoped
public class ImagemBean {

	@ManagedProperty("#{param.caminho}")
	private String caminho;
	private StreamedContent foto;

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public StreamedContent getFoto() throws IOException {
		if (caminho == null || caminho.isEmpty()) {
			Path caminho = Paths.get("C:/Users/Danillo/git/gweb/GWEB/img");
			InputStream fluxo = Files.newInputStream(caminho);
			foto = new DefaultStreamedContent(fluxo);
		} else {
			Path caminho = Paths.get("caminho");
			InputStream fluxo = Files.newInputStream(caminho);
			foto = new DefaultStreamedContent(fluxo);
		}
		return foto;
	}

	public void setFoto(StreamedContent foto) {
		this.foto = foto;
	}

}
