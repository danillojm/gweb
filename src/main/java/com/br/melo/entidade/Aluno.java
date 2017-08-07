package com.br.melo.entidade;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.br.melo.interfaces.EntidadeBean;

@SuppressWarnings("serial")
@Entity
@Table(name = "tb_aluno")
public class Aluno extends EntidadeBean implements Serializable {

	public static final String strDthExclusao = "dthExclusao";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "nom_aluno")
	private String nomAluno;
	@Column(name = "nom_pai")
	private String nomPai;
	@Column(name = "nom_mae")
	private String nomMae;
	@Column(name = "data_nascimento")
	private String dataNascimento;
	@Column(name = "num_telefone")
	private String telefone;
	@Column(name = "num_celular")
	private String numCelular;
	@Column(name = "dth_exclusao")
	public Timestamp dthExclusao;

	@Transient
	private String caminho;

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "id_logradouro") Logradouro logradouro;
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomAluno() {
		return nomAluno;
	}

	public void setNomAluno(String nomAluno) {
		this.nomAluno = nomAluno;
	}

	public String getNomPai() {
		return nomPai;
	}

	public void setNomPai(String nomPai) {
		this.nomPai = nomPai;
	}

	public String getNomMae() {
		return nomMae;
	}

	public void setNomMae(String nomMae) {
		this.nomMae = nomMae;
	}

	/*
	 * public Logradouro getLogradouro() { return logradouro; }
	 * 
	 * public void setLogradouro(Logradouro logradouro) { this.logradouro = logradouro; }
	 */

	 

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Timestamp getDthExclusao() {
		return dthExclusao;
	}

	public void setDthExclusao(Timestamp dthExclusao) {
		this.dthExclusao = dthExclusao;
	}

 
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getNumCelular() {
		return numCelular;
	}

	public void setNumCelular(String numCelular) {
		this.numCelular = numCelular;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	@Override
	public boolean validar() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValoresPadroes() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
