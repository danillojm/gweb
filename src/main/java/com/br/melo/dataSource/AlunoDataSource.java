package com.br.melo.dataSource;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.br.melo.classAbstrata.ParametroRelatorio;
import com.br.melo.dao.AlunoDAO;
import com.br.melo.entidade.Aluno;

@ManagedBean
@ViewScoped
public class AlunoDataSource extends ParametroRelatorio {

	private Aluno aluno = new Aluno();

	private List<Aluno> alunoList = new ArrayList<Aluno>();

	public AlunoDataSource() {
	 preencheListaAluno(); 
	}
	
	public void preencheListaAluno() {
		AlunoDAO dao = new AlunoDAO();
		getAlunoList().addAll(dao.listar());
	}

	@Override
	public void imprimir(List<Object> list, String nomeRelatorio) {
		// TODO Auto-generated method stub
		super.imprimir(list, nomeRelatorio);
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Aluno> getAlunoList() {
		return alunoList;
	}

	public void setAlunoList(List<Aluno> alunoList) {
		this.alunoList = alunoList;
	}
}
