package com.br.melo.forms;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;

import com.br.melo.exception.RestricaoException;
import com.br.melo.interfaces.EntidadeBean;

@SuppressWarnings("serial")
public abstract class FormularioPadrao<T> implements Serializable{

	private EstadoForm estadoAtual;

	private CommandButton btnNovo;
	private CommandButton btnEditar;
	private CommandButton btnExcluir;
	private CommandButton btnSalvar;
	private CommandButton btnSalvarEditar;
	private CommandButton btnLimpar;
	private CommandButton btnSalvarNovo;
	private CommandButton btnCancelar;
	private CommandButton btnPesquisar;

	private HtmlForm formPrincipal = new HtmlForm();
	private DataTable dtPrincipal = new DataTable();
	private Dialog dlgPrincipal = new Dialog();
	private Panel pnPrincipal = new Panel();
	private String msgPadraoGridVazio = "Sem registros...";
	private String msgPadraoErroDesconhecido = "Ops! Ocorreu alguma situação não esperada! Tente novamente, caso persista, entre em contato com nossa equipe técnica.";
	private String msgPadraoCDialog = "Confirma a ação ?";
	private T entidade;
	private T entidadeDataTable;
	private List<T> registros;
	private List<T> registrosFiltrados;
	private Long idEntidadePesq = null;

	public abstract void novo() throws Exception;

	public abstract void editar() throws Exception;

	public abstract void excluir() throws Exception;

	public abstract void cancelar() throws Exception;

	public abstract void salvarNovo() throws Exception;

	public abstract void salvarEditar() throws Exception;

	public abstract void limpar() throws Exception;

	public abstract void pesquisar() throws Exception;

	public abstract void selecionar(boolean isTrue) throws Exception;

	public abstract void recarregar() throws Exception;

	public abstract void checaRestricoesNovo() throws Exception;

	public abstract void checaRestricoesEditar() throws Exception;

	public abstract String getNomTituloForm() throws Exception;

	 
	@PostConstruct
	public void init() {
		 FacesContext.getCurrentInstance().getViewRoot().addPhaseListener(new LifeCycleListener((Formulario<EntidadeBean>) this));
		/* carregarPermissoesModulo(); */
		configurarBotoes();
		configurarDataTable();
		if (estadoAtual == null) {
			setEstadoAtual(EstadoForm.OCIOSO);
		}
	}

	private void configurarBotoes() {

		// BOTÂO NOVO
		getBtnNovo().setValue("Novo");
		getBtnNovo().setIcon("fa fa-plus Fs14 White");
		getBtnNovo().setStyleClass("GreenButton");
		getBtnNovo().setUpdate("@form");
		getBtnNovo().setProcess("@this");

		// BOTÂO EDITAR
		getBtnEditar().setValue("Editar");
		getBtnEditar().setIcon("fa fa-fw fa-edit");
		getBtnEditar().setStyleClass("GreenButton");
		getBtnEditar().setUpdate("@form");

		getBtnPesquisar().setValue("Pesquisar");
		getBtnPesquisar().setIcon("fa fa-search Fs14 White");
		// Colocado true pq senão, ao clicar em pesquisar ele validava os campos fazendo com que causasse um NPE em todos os atributos (objetos) internos do obj. (Francisco)
		getBtnPesquisar().setImmediate(true);

		getBtnCancelar().setValue("Cancelar");
		getBtnCancelar().setIcon("fa fa-remove Fs14 White");
		getBtnCancelar().setStyleClass("OrangeButton");
		getBtnCancelar().setUpdate("@form");
		getBtnCancelar().setProcess("@this");

		getBtnExcluir().setValue("Excluir");
		getBtnExcluir().setIcon("fa fa-trash-o Fs14 White");
		getBtnExcluir().setStyleClass("RedButton");

		getBtnExcluir().setUpdate(" @form");

		getBtnSalvarEditar().setValue("Salvar");
		getBtnSalvarEditar().setIcon("fa fa-check Fs14 White");
		getBtnSalvarEditar().setStyleClass("GreenButton");
		getBtnSalvarEditar().setUpdate(" @form");

		getBtnSalvarNovo().setValue("Salvar");
		getBtnSalvarNovo().setIcon("fa fa-check Fs14 White");
		getBtnSalvarNovo().setStyleClass("GreenButton");
		getBtnSalvarNovo().setUpdate("@form");

	}

	private void configurarDataTable() {

		// Data Table

		getDtPrincipal().setEmptyMessage(getMsgPadraoGridVazio());
		getDtPrincipal().setScrollable(false);
		getDtPrincipal().setSelectionMode("single");
		getDtPrincipal().setReflow(true);
		getDtPrincipal().setCurrentPageReportTemplate("({currentPage} de {totalPages})");
		getDtPrincipal().setPaginatorTemplate("{CurrentPageReport} {FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink} {RowsPerPageDropdown} {Exporters}");
		getDtPrincipal().setPaginatorPosition("top");
		// getDtPrincipal().setScrollHeight("90%");
		getDtPrincipal().setStyleClass(getDtPrincipal().getStyleClass() + " dataTablePesquisaPadrao tableModal slim slimMargin");
		getDtPrincipal().setRowsPerPageTemplate("10,20,30,50");
		getDtPrincipal().setRows(20);
		getDtPrincipal().setResizableColumns(true);
		getDtPrincipal().setPaginator(true);
		getDtPrincipal().setPaginatorAlwaysVisible(true);

		// Dialog

		getDlgPrincipal().setShowEffect("fade");
		getDlgPrincipal().setHideEffect("fade");
		getDlgPrincipal().setStyleClass(getDlgPrincipal().getStyleClass() + " dlgPesquisaPadrao dialog-tableModel");
		getDlgPrincipal().setWidth("99%");
		getDlgPrincipal().setHeight("95%");
		getDlgPrincipal().setModal(true);
		getDlgPrincipal().setResizable(true);
		getDlgPrincipal().setCloseOnEscape(true);
		getDlgPrincipal().setOnHide((getDlgPrincipal().getOnHide() != null && !getDlgPrincipal().getOnHide().isEmpty()) ? "tgScroll(); " + getDlgPrincipal().getOnHide() : "tgScroll()");
		getDlgPrincipal().setOnShow((getDlgPrincipal().getOnShow() != null && !getDlgPrincipal().getOnShow().isEmpty()) ? "tgScroll(); " + getDlgPrincipal().getOnShow() : "tgScroll()");
		getDlgPrincipal().setPosition("top");
		getDlgPrincipal().setStyle((getDlgPrincipal().getStyle() != null && !getDlgPrincipal().getStyle().isEmpty()) ? getDlgPrincipal().getStyle() + "; margin-top : 20px !important " : "margin-top : 20px !important");

	}

	public void refreshBotoes() {

		getBtnNovo().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO) || this.getEstadoAtual().equals(EstadoForm.OCIOSO));

		getBtnExcluir().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO));
		getBtnEditar().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO));

		getBtnCancelar().setRendered(this.getEstadoAtual().equals(EstadoForm.CADASTRANDO) || this.getEstadoAtual().equals(EstadoForm.EDITANDO));

		getBtnSalvarEditar().setRendered(this.getEstadoAtual().equals(EstadoForm.EDITANDO));
		getBtnSalvarNovo().setRendered(this.getEstadoAtual().equals(EstadoForm.CADASTRANDO));
		getBtnPesquisar().setRendered(this.getEstadoAtual().equals(EstadoForm.OCIOSO) || this.getEstadoAtual().equals(EstadoForm.SELECIONADO));

	}

	public void checaRestricoesExcluir() throws Exception {

	};

	public void excluir(ActionEvent ae) {
		try {
			checaRestricoesExcluir();
			excluir();
			limpar();
			setEstadoAtual(EstadoForm.OCIOSO);
			enviarMensagem("Registro excluído com sucesso.");

		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			if (e instanceof ConstraintViolationException) {
				if (((ConstraintViolationException) e).getSQLException().getMessage().contains("violates foreign key constraint") || ((ConstraintViolationException) e).getSQLException().getMessage().contains("viola restrição de chave estrangeira")) {
					enviarMensagem("Existem registros vinculados esse, não é possível excluir.");
					e.printStackTrace();
				} else {
					enviarMensagem("Não é possível excluir.");
				}
			} else {
				enviarMensagem("Erro ao excluir registro.");
				e.printStackTrace();
			}
		}
	}

	public void selecionar() throws Exception {
		try {
			if (this.getEntidadeDataTable() != null) {
				limpar();
				setEntidade(this.getEntidadeDataTable());
			}

			selecionar(true);
			setEstadoAtual(EstadoForm.SELECIONADO);
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public void editar(ActionEvent ae) {
		try {
			setEstadoAtual(EstadoForm.EDITANDO);
			editar();
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public void salvarEditar(ActionEvent ae) {
		try {
			checaRestricoesEditar();
			salvarEditar();
			setEstadoAtual(EstadoForm.SELECIONADO);
			enviarMensagem("Registro alterado com sucesso.");
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			if (e instanceof ConstraintViolationException && ((ConstraintViolationException) e).getSQLException().getMessage().contains("violates foreign key constraint")) {
				enviarMensagem("Existem registros vinculados, não é possível alterar.");
			} else {
				enviarMensagem("Erro ao alterar registro.");
				e.printStackTrace();
			}
		}
	}

	public void salvarNovo(ActionEvent ae) {
		try {
			checaRestricoesNovo();
			salvarNovo();
			setEstadoAtual(EstadoForm.SELECIONADO);
			enviarMensagem("Registro salvo com sucesso.");
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public void novo(ActionEvent ae) {
		try {
			novo();
			setEstadoAtual(EstadoForm.CADASTRANDO);
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public void cancelar(ActionEvent ae) {
		try {
			cancelar();
			if (getEstadoAtual().equals(EstadoForm.CADASTRANDO)) {
				limpar();
				setEstadoAtual(EstadoForm.OCIOSO);
			} else if (getEstadoAtual().equals(EstadoForm.EDITANDO)) {
				recarregar();
				if (getEntidade() == null) {
					limpar();
					setEstadoAtual(EstadoForm.OCIOSO);
				} else {
					setEstadoAtual(EstadoForm.SELECIONADO);
				}
			} else {
				System.err.println("Cancelar para esse estado de FORM não implementado!!!");
			}
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public boolean isPesquisando() {
		return this.estadoAtual.equals(EstadoForm.PESQUISANDO);
	}

	public boolean isOcioso() {
		return this.estadoAtual.equals(EstadoForm.OCIOSO);
	}

	public boolean isCadastrando() {
		return this.estadoAtual.equals(EstadoForm.CADASTRANDO);
	}

	public boolean isEditando() {
		return this.estadoAtual.equals(EstadoForm.EDITANDO);
	}

	public boolean isSelecionado() {
		return this.estadoAtual.equals(EstadoForm.SELECIONADO);
	}

	public void enviarMensagem(String mensagem) {
		enviarMensagem(mensagem, null, FacesMessage.SEVERITY_INFO);
	}

	public void enviarMensagem(String mensagem, String detalhe, Severity nivel) {
		if (nivel.equals(FacesMessage.SEVERITY_INFO) || nivel.equals(FacesMessage.SEVERITY_ERROR) || nivel.equals(FacesMessage.SEVERITY_FATAL)) {
			RequestContext.getCurrentInstance().execute("window.scrollTo(0,0);");
		}

		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage(nivel, detalhe, mensagem));
	}

	public EstadoForm getEstadoAtual() {
		return estadoAtual;
	}

	public void setEstadoAtual(EstadoForm estadoAtual) {
		this.estadoAtual = estadoAtual;
		refreshBotoes();
	}

	public CommandButton getBtnNovo() {
		return btnNovo;
	}

	public void setBtnNovo(CommandButton btnNovo) {
		this.btnNovo = btnNovo;
	}

	public CommandButton getBtnEditar() {
		return btnEditar;
	}

	public void setBtnEditar(CommandButton btnEditar) {
		this.btnEditar = btnEditar;
	}

	public CommandButton getBtnExcluir() {
		return btnExcluir;
	}

	public void setBtnExcluir(CommandButton btnExcluir) {
		this.btnExcluir = btnExcluir;
	}

	public CommandButton getBtnSalvar() {
		return btnSalvar;
	}

	public void setBtnSalvar(CommandButton btnSalvar) {
		this.btnSalvar = btnSalvar;
	}

	public CommandButton getBtnSalvarEditar() {
		return btnSalvarEditar;
	}

	public void setBtnSalvarEditar(CommandButton btnSalvarEditar) {
		this.btnSalvarEditar = btnSalvarEditar;
	}

	public CommandButton getBtnLimpar() {
		return btnLimpar;
	}

	public void setBtnLimpar(CommandButton btnLimpar) {
		this.btnLimpar = btnLimpar;
	}

	public CommandButton getBtnSalvarNovo() {
		return btnSalvarNovo;
	}

	public void setBtnSalvarNovo(CommandButton btnSalvarNovo) {
		this.btnSalvarNovo = btnSalvarNovo;
	}

	public CommandButton getBtnCancelar() {
		return btnCancelar;
	}

	public void setBtnCancelar(CommandButton btnCancelar) {
		this.btnCancelar = btnCancelar;
	}

	public CommandButton getBtnPesquisar() {
		return btnPesquisar;
	}

	public void setBtnPesquisar(CommandButton btnPesquisar) {
		this.btnPesquisar = btnPesquisar;
	}

	public HtmlForm getFormPrincipal() {
		return formPrincipal;
	}

	public void setFormPrincipal(HtmlForm formPrincipal) {
		this.formPrincipal = formPrincipal;
	}

	public DataTable getDtPrincipal() {
		return dtPrincipal;
	}

	public void setDtPrincipal(DataTable dtPrincipal) {
		this.dtPrincipal = dtPrincipal;
	}

	public Dialog getDlgPrincipal() {
		return dlgPrincipal;
	}

	public void setDlgPrincipal(Dialog dlgPrincipal) {
		this.dlgPrincipal = dlgPrincipal;
	}

	public Panel getPnPrincipal() {
		return pnPrincipal;
	}

	public void setPnPrincipal(Panel pnPrincipal) {
		this.pnPrincipal = pnPrincipal;
	}

	public String getMsgPadraoGridVazio() {
		return msgPadraoGridVazio;
	}

	public void setMsgPadraoGridVazio(String msgPadraoGridVazio) {
		this.msgPadraoGridVazio = msgPadraoGridVazio;
	}

	public String getMsgPadraoErroDesconhecido() {
		return msgPadraoErroDesconhecido;
	}

	public void setMsgPadraoErroDesconhecido(String msgPadraoErroDesconhecido) {
		this.msgPadraoErroDesconhecido = msgPadraoErroDesconhecido;
	}

	public String getMsgPadraoCDialog() {
		return msgPadraoCDialog;
	}

	public void setMsgPadraoCDialog(String msgPadraoCDialog) {
		this.msgPadraoCDialog = msgPadraoCDialog;
	}

	public List<T> getRegistros() {
		return registros;
	}

	public void setRegistros(List<T> registros) {
		this.registros = registros;
	}

	public List<T> getRegistrosFiltrados() {
		return registrosFiltrados;
	}

	public void setRegistrosFiltrados(List<T> registrosFiltrados) {
		this.registrosFiltrados = registrosFiltrados;
	}

	public Long getIdEntidadePesq() {
		return idEntidadePesq;
	}

	public void setIdEntidadePesq(Long idEntidadePesq) {
		this.idEntidadePesq = idEntidadePesq;
	}

	public T getEntidade() {
		return entidade;
	}

	public void setEntidade(T entidade) {
		this.entidade = entidade;
	}

	public T getEntidadeDataTable() {
		return entidadeDataTable;
	}

	public void setEntidadeDataTable(T entidadeDataTable) {
		this.entidadeDataTable = entidadeDataTable;
	}

}
