package com.br.melo.forms;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.br.melo.dao.AlunoDAO;
import com.br.melo.dao.DAOGeral;
import com.br.melo.entidade.Aluno;
import com.br.melo.util.HibernateUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class AlunoForm extends Formulario<Aluno> implements Serializable {

	Aluno aluno;
	private Aluno entidadeDataTable;

	private List<Aluno> alunoList = new ArrayList<Aluno>();

	public AlunoForm() {
		setEntidade(new Aluno());

	}

	@Override
	public void editar() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void novo() throws Exception {

		setEntidade(new Aluno());

	}

	@Override
	@SuppressWarnings("unchecked")
	public void pesquisar() {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		Criteria ct = sessao.createCriteria(Aluno.class);
		ct.add(Restrictions.isNull(Aluno.strDthExclusao));
		setAlunoList(ct.list());
	}

	public void gerarNumeroMatricula() {

		Random gerador = new Random();

		String numero1 = Integer.toString(gerador.nextInt(9));
		String numero2 = Integer.toString(gerador.nextInt(9));
		String numero3 = Integer.toString(gerador.nextInt(9));
		String numero4 = Integer.toString(gerador.nextInt(9));

		String numeroMatricula = numero1 + "" + numero2 + "" + numero3 + "" + numero4;

		System.out.println(numeroMatricula);

	}

	@Override
	public void excluir() throws Exception {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();
		try {

			getAluno().setDthExclusao(new Timestamp(new Date().getTime()));
			sessao.update(getAluno());
			sessao.beginTransaction().commit();
			limpar();
			enviarMensagem("Registro Excluido com sucesso...");
		} catch (Exception e) {
			e.printStackTrace();
			enviarMensagem("Erro ao Excluir os Dados, se persistir entre em contato com o suporte Técnico!");
			sessao.beginTransaction().rollback();

		} finally {
			sessao.close();
		}

	}

	public void imprimir() {

		AlunoDAO dao = new AlunoDAO();

		try {
			String nomeDoRelatorio = "rel_aluno";
			Connection conexao = HibernateUtil.getConexao();
			String caminho = Faces.getRealPath("/relatorios/" + nomeDoRelatorio + ".jasper");

			Map<String, Object> params = new HashedMap();
			JasperPrint relatorio = JasperFillManager.fillReport(caminho, null, new JRBeanCollectionDataSource(dao.listar()));
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void cancelar() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void salvarNovo() throws Exception {
		Session sessao = HibernateUtil.getFabricaDeSessoes().openSession();

		try {
			/*if (getEntidade().getCaminho() == null) {
				enviarMensagem("O campo foto é obrigatório");
				return;
			}*/

			sessao.merge(getEntidade());

			Path origem = Paths.get(getEntidade().getCaminho());
			Path destino = Paths.get("C:/Users/Danillo/git/gweb/GWEB/img" + getEntidade().getId() + ".png");
			Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
			sessao.beginTransaction().commit();
			enviarMensagem("Produto salvo com sucesso");
		} catch (RuntimeException | IOException erro) {
			enviarMensagem("Ocorreu um erro ao tentar salvar o produto");
			erro.printStackTrace();
		}
	}

	@Override
	public void salvarEditar() throws Exception {
		// TODO Auto-generated method stub

	}

	public void limpar() throws Exception {
		setAluno(new Aluno());

	}

	@Override
	public void selecionar() throws Exception {
		setEntidade(this.getEntidadeDataTable());
	}

	public void upload(FileUploadEvent event) {
		try {
			UploadedFile arquivo = event.getFile();
			Path arquivoTemp = Files.createTempFile(null, null);
			Files.copy(arquivo.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
			getEntidade().setCaminho(arquivoTemp.toString());
		} catch (IOException erro) {
			enviarMensagem("Erro ao tentar Realizar Upload Do arquivo");
		}
	}

	public static void main(String[] args) {
		Random gerador = new Random();

		String numero1 = Integer.toString(gerador.nextInt(9));
		String numero2 = Integer.toString(gerador.nextInt(9));
		String numero3 = Integer.toString(gerador.nextInt(9));
		String numero4 = Integer.toString(gerador.nextInt(9));

		String numeroMatricula = numero1 + numero2 + numero3 + numero4;

		System.out.println(numeroMatricula);

	}

	public void geraRelatorio(List<Aluno> alunos) {

		InputStream fonte = AlunoForm.class.getResourceAsStream("/relatorios/rel_aluno.jrxml");
		try {
			JasperReport jasperReport = JasperCompileManager.compileReport(fonte);

			JRBeanCollectionDataSource dataSourse = new JRBeanCollectionDataSource(alunos);

			JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dataSourse);
			JasperViewer.viewReport(print, false);

		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public Aluno getEntidadeDataTable() {
		return entidadeDataTable;
	}

	public void setEntidadeDataTable(Aluno entidadeDataTable) {
		this.entidadeDataTable = entidadeDataTable;
	}

	@Override
	public void selecionar(boolean isTrue) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void recarregar() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void checaRestricoesNovo() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void checaRestricoesEditar() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNomTituloForm() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DAOGeral<Aluno> getDaoPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

}
