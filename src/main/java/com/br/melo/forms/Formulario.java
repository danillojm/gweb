package com.br.melo.forms;
 

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.selectbooleanbutton.SelectBooleanButton;
import org.primefaces.component.selectmanybutton.SelectManyButton;
import org.primefaces.component.selectonebutton.SelectOneButton;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.context.RequestContext;

import com.br.melo.dao.DAOGeral;
import com.br.melo.exception.RestricaoException;
import com.br.melo.interfaces.EntidadeBean;
import com.br.melo.interfaces.ExclusaoLogica;
import com.br.melo.util.FormatadorUtils;
import com.br.melo.util.LifeCycleListener;
import com.br.melo.util.Utils;

public abstract class Formulario<T extends EntidadeBean> implements Serializable {

	private static final long serialVersionUID = -3447208948910430820L;

	private EstadoForm estadoAtual = null;

	private CommandButton cbNovo = new CommandButton();
	private CommandButton cbEditar = new CommandButton();
	private CommandButton cbExcluir = new CommandButton();
	private CommandButton cbReativar = new CommandButton();
	private CommandButton cbCancelar = new CommandButton();
	private CommandButton cbSalvarNovo = new CommandButton();
	private CommandButton cbSalvarEditar = new CommandButton();
	private CommandButton cbLimpar = new CommandButton();
	private CommandButton cbPesquisar = new CommandButton();
	private CommandButton cbRecarrregar = new CommandButton();
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
	private Boolean permissaoEditar = false;
	private Boolean permissaoNovo = false;
	private Boolean permissaoExcluir = false;
	private Boolean permissaoPesquisar = false;

	public abstract void novo() throws Exception;

	public abstract void editar() throws Exception;

	public abstract void excluir() throws Exception;

	public abstract void cancelar() throws Exception;

	public void reativar() throws Exception {
		System.out.println("Implemente o método reativar em seu formulário!");
	};

	public abstract void salvarNovo() throws Exception;

	public abstract void salvarEditar() throws Exception;

	public abstract void limpar() throws Exception;

	public abstract void pesquisar() throws Exception;

	public abstract void selecionar(boolean isTrue) throws Exception;

	public abstract void recarregar() throws Exception;

	public abstract void checaRestricoesNovo() throws Exception;
	
	public abstract void checaRestricoesEditar() throws Exception;

	public abstract String getNomTituloForm() throws Exception;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		//FacesContext.getCurrentInstance().getViewRoot().addPhaseListener(new LifeCycleListener((Formulario<EntidadeBean>) this));
		/*carregarPermissoesModulo();*/
		configurarBotoesToolbar();
		configurarDataTable();
		 setEstadoAtual(EstadoForm.OCIOSO);
	}

	public String getNomTituloFormExporter() {
		try {
			return FormatadorUtils.limpaString(getNomTituloForm()) + "_" + new Date().getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return "erro_ao_gerar_nome";
		}
	}

	public void configurarPermissoesModulo(Boolean permissaoPesquisar, Boolean permissaoNovo, Boolean permissaoEditar, Boolean permissaoExcluir) {
		setPermissaoPesquisar(permissaoPesquisar);
		setPermissaoNovo(permissaoNovo);
		setPermissaoEditar(permissaoEditar);
		setPermissaoExcluir(permissaoExcluir);
	}

	public Boolean isTemAlgumaPermissaoModulo() {
		return getPermissaoEditar() || getPermissaoExcluir() || getPermissaoNovo() || getPermissaoPesquisar();
	}

	/*public void carregarPermissoesModulo() {
		try {
			String nomClasse = this.getClass().getName();

			if (SessaoUsuario.getCurrentInstance().getUsuario() instanceof PossuiPerfil) {
				PossuiPerfil usuario = (PossuiPerfil) SessaoUsuario.getCurrentInstance().getUsuario();
				UsuarioGerencial usuarioGerencial = null;

				if (usuario instanceof UsuarioGerencial) {
					usuarioGerencial = (UsuarioGerencial) usuario;
				}
				
				if (usuarioGerencial != null && usuarioGerencial.getIndSuperUsuario().equals(Utils.SIM)) {
					configurarPermissoesModulo(true, true, true, true);
					return;
				}

				Session sessao = getSessaoUsuario();

				Criteria ct = sessao.createCriteria(UsuarioPerfilModuloPermissao.class);

				ct.createAlias(UsuarioPerfilModuloPermissao.strModulo, UsuarioPerfilModuloPermissao.strModulo);

				ct.add(Restrictions.isNull(UsuarioPerfilModuloPermissao.strModulo + "." + Modulo.strDthExclusao));
				ct.add(Restrictions.eq(UsuarioPerfilModuloPermissao.strModulo + "." + Modulo.strNomClasse, nomClasse));
				ct.add(Restrictions.eq(UsuarioPerfilModuloPermissao.strUsuarioPerfil + "." + UsuarioPerfil.strId, ((PossuiPerfil) SessaoUsuario.getCurrentInstance().getUsuario()).getUsuarioPerfil().getId()));
				ct.setMaxResults(1);

				UsuarioPerfilModuloPermissao permissao = (UsuarioPerfilModuloPermissao) ct.uniqueResult();

				if (permissao != null) {
					if (permissao.getIndPermissaoAlterar().equals(Utils.SIM)) {
						setPermissaoEditar(true);
					}

					if (permissao.getIndPermissaoExcluir().equals(Utils.SIM)) {
						setPermissaoExcluir(true);
					}

					if (permissao.getIndPermissaoPesquisar().equals(Utils.SIM)) {
						setPermissaoPesquisar(true);
					}

					if (permissao.getIndPermissaoCadastrar().equals(Utils.SIM)) {
						setPermissaoNovo(true);
					}

				} else if (permissao == null) {
					configurarPermissoesModulo(false, false, false, false);
				}
			}
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}*/

	private void configurarBotoesToolbar() {
		String msgSemPermissao = "Você não tem permissão para essa ação.";

		getCbNovo().setValue("Novo");
		getCbNovo().setIcon("fa fa-plus Fs14 White");
		getCbNovo().setStyleClass("GreenButton");
		getCbNovo().setUpdate((getCbNovo().getUpdate() != null ? getCbNovo().getUpdate() : "") + " @form");
		/*getCbNovo().setProcess("@this");*/
		/*if (!getPermissaoNovo()) {
			getCbNovo().setDisabled(!getPermissaoNovo());
			getCbNovo().setTitle(msgSemPermissao);
		}
*/
		getCbPesquisar().setValue("Pesquisar");
		getCbPesquisar().setIcon("fa fa-search Fs14 White");
		// Colocado true pq senão, ao clicar em pesquisar ele validava os campos fazendo com que causasse um NPE em todos os atributos (objetos) internos do obj. (Francisco)
		getCbPesquisar().setImmediate(true);
		/*if (!getPermissaoPesquisar()) {
			getCbPesquisar().setDisabled(!getPermissaoPesquisar());
			getCbPesquisar().setTitle(msgSemPermissao);
		}*/

		getCbCancelar().setValue("Cancelar");
		getCbCancelar().setIcon("fa fa-remove Fs14 White");
		getCbCancelar().setStyleClass("OrangeButton");
		getCbCancelar().setUpdate((getCbCancelar().getUpdate() != null ? getCbCancelar().getUpdate() : "") + " @form");
		getCbCancelar().setProcess("@this");

		getCbReativar().setValue("Reativar");
		getCbReativar().setIcon("fa fa-history Fs14 White");
		getCbReativar().setStyleClass("OrangeButton");
		getCbReativar().setUpdate((getCbReativar().getUpdate() != null ? getCbReativar().getUpdate() : "") + " @form");
		if (getEntidade() instanceof ExclusaoLogica) {
			getCbExcluir().setValue("Inativar");
			getCbExcluir().setIcon("fa fa-trash-o Fs14 White");
			getCbExcluir().setStyleClass("OrangeButton");
		} else {
			getCbExcluir().setValue("Excluir");
			getCbExcluir().setIcon("fa fa-trash-o Fs14 White");
			getCbExcluir().setStyleClass("RedButton");
		}
		getCbExcluir().setUpdate((getCbExcluir().getUpdate() != null ? getCbExcluir().getUpdate() : "") + " @form");
		/*if (!getPermissaoExcluir()) {
			getCbReativar().setDisabled(!getPermissaoExcluir());
			getCbReativar().setTitle(msgSemPermissao);
			getCbExcluir().setDisabled(!getPermissaoExcluir());
			getCbExcluir().setTitle(msgSemPermissao);
		}
*/
		getCbEditar().setValue("Editar");
		getCbEditar().setIcon("fa fa-pencil Fs14 White");
		getCbEditar().setUpdate((getCbEditar().getUpdate() != null ? getCbEditar().getUpdate() : "") + " @form");
		/*if (!getPermissaoEditar()) {
			getCbEditar().setDisabled(!getPermissaoEditar());
			getCbEditar().setTitle(msgSemPermissao);
		}*/

		getCbRecarrregar().setIcon("fa fa-refresh Fs14 White");
		getCbRecarrregar().setUpdate((getCbRecarrregar().getUpdate() != null ? getCbRecarrregar().getUpdate() : "") + " @form");

		getCbSalvarEditar().setValue("Salvar");
		getCbSalvarEditar().setIcon("fa fa-check Fs14 White");
		getCbSalvarEditar().setStyleClass("GreenButton");
		getCbSalvarEditar().setUpdate((getCbSalvarEditar().getUpdate() != null ? getCbSalvarEditar().getUpdate() : "") + " @form");

		getCbSalvarNovo().setValue("Salvar");
		getCbSalvarNovo().setIcon("fa fa-check Fs14 White");
		getCbSalvarNovo().setStyleClass("GreenButton");
		getCbSalvarNovo().setUpdate((getCbSalvarNovo().getUpdate() != null ? getCbSalvarNovo().getUpdate() : "") + " @form");

		/*if (!isTemAlgumaPermissaoModulo()) {
			// TODO, criar um alerta ? Dar a opção de enviar uma mensagem
			// solicitando essa liberação ?
			enviarMensagem("Você não tem permissão para executar nenhuma ação nessa Tela, entre em contato com o responsável por liberar acesso na sua Empresa e solicite.");
		}*/

	}

	 

	public Criteria getCtPesquisar() {
		return null;
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

		// <p:dialog widgetVar="dlgPesquisa" showEffect="fade" hideEffect="fade"
		// dynamic="true" width="99%"
		// height="95%" modal="true" resizable="true" closeOnEscape="true"
		// responsive="true"
		// onHide="tgScroll()" onShow="tgScroll()" position="top"
		// style="margin-top : 20px !important"
		// >

	}

	public void setEstadoAtual(EstadoForm estadoAtual) {
		if (this.estadoAtual == null || estadoAtual.equals(EstadoForm.OCIOSO)) {
			bloquearForm(null);
		} else if (estadoAtual.equals(EstadoForm.CADASTRANDO)) {
			desbloquearForm(null);
		} else if (estadoAtual.equals(EstadoForm.SELECIONADO)) {
			bloquearForm(null);
		} else if (estadoAtual.equals(EstadoForm.EDITANDO)) {
			desbloquearForm(null);
		}

		this.estadoAtual = estadoAtual;
		 
		refreshBotoes();
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

	public void refreshBotoes() {

		getCbNovo().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO) || this.getEstadoAtual().equals(EstadoForm.OCIOSO));

		if (getEntidade() instanceof ExclusaoLogica) {
			getCbExcluir().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO) && ((ExclusaoLogica) getEntidade()).getDthExclusao() == null);
			getCbReativar().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO) && ((ExclusaoLogica) getEntidade()).getDthExclusao() != null);
			getCbEditar().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO));
			getCbEditar().setDisabled(((ExclusaoLogica) getEntidade()).getDthExclusao() != null);
		} else {
			getCbExcluir().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO));
			getCbEditar().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO));
			getCbReativar().setRendered(false);
		}
		getCbCancelar().setRendered(this.getEstadoAtual().equals(EstadoForm.CADASTRANDO) || this.getEstadoAtual().equals(EstadoForm.EDITANDO));
		getCbEditar().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO));
		getCbSalvarEditar().setRendered(this.getEstadoAtual().equals(EstadoForm.EDITANDO));
		getCbSalvarNovo().setRendered(this.getEstadoAtual().equals(EstadoForm.CADASTRANDO));
		getCbRecarrregar().setRendered(this.getEstadoAtual().equals(EstadoForm.SELECIONADO));
		getCbPesquisar().setRendered(this.getEstadoAtual().equals(EstadoForm.OCIOSO) || this.getEstadoAtual().equals(EstadoForm.SELECIONADO));

	}
	
	public void checaRestricoesExcluir() throws Exception {
		
	};

	public void recaregar(ActionEvent ae) {
		try {
			recarregar();
			setEstadoAtual(EstadoForm.SELECIONADO);
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			enviarMensagem("Erro ao recarregar o registro.");
		}
	}

	/*public void reativar(ActionEvent ae) {
		try {
			reativar();
			recaregar(ae);
			enviarMensagem("Registro foi novamente ativo com sucesso.");
		} catch (RestricaoException e) {
			enviarMensagem(((RestricaoException) e).getMsg(), null, FacesMessage.SEVERITY_ERROR);
		} catch (Exception e) {
			enviarMensagem("Erro ao reAtivar o registro.");
			e.printStackTrace();
		} finally {
			getDaoPrincipal().close();
		}
	}*/

	public void excluir(ActionEvent ae) {
		try {
			checaRestricoesExcluir();
			 

			if (getEntidade() instanceof ExclusaoLogica) {
				enviarMensagem("Registro inativado com sucesso.");
				setEstadoAtual(EstadoForm.SELECIONADO);
			} else {
				limpar();
				setEstadoAtual(EstadoForm.OCIOSO);
				enviarMensagem("Registro excluído com sucesso.");
			}
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
//			novo();
			setEstadoAtual(EstadoForm.CADASTRANDO);
		} catch (Exception e) {
			enviarMensagem(getMsgPadraoErroDesconhecido());
			e.printStackTrace();
		}
	}

	public void cancelar(ActionEvent ae) {
		try {
			 
			if (getEstadoAtual().equals(EstadoForm.CADASTRANDO)) {
				limpar();
				setEstadoAtual(EstadoForm.OCIOSO);
			} else if (getEstadoAtual().equals(EstadoForm.EDITANDO)) {
				recarregar();
				if (getEntidade() == null || getEntidade().getId() == null) {
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

	public static UIComponent findComponent(UIComponent base, String id) {
		if (id.equals(base.getId()))
			return base;

		UIComponent kid = null;
		UIComponent result = null;
		Iterator<UIComponent> kids = base.getFacetsAndChildren();
		while (kids.hasNext() && (result == null)) {
			kid = (UIComponent) kids.next();
			// System.out.println(kid.getId());
			if (id.equals(kid.getId())) {
				result = kid;
				break;
			}
			result = findComponent(kid, id);
			if (result != null) {
				break;
			}
		}
		return result;
	}

	public static void findComponentAndSetReadOnly(UIComponent base, boolean isReadOnly) {
		UIComponent kid = null;
		UIComponent result = null;
		Iterator<UIComponent> kids = base.getFacetsAndChildren();
		while (kids.hasNext() && (result == null)) {
			kid = (UIComponent) kids.next();
			String stilo = (String) kid.getAttributes().get("styleClass");
			if (stilo != null && stilo.contains("naoBloquear")) {
				continue;
			}
			if (kid instanceof InputText) {
				((InputText) kid).setReadonly(isReadOnly);
			} else if (kid instanceof InputTextarea) {
				((InputTextarea) kid).setReadonly(isReadOnly);
			} else if (kid instanceof SelectOneMenu) {
				((SelectOneMenu) kid).setDisabled(isReadOnly);
			} else if (kid instanceof SelectOneRadio) {
				((SelectOneRadio) kid).setDisabled(isReadOnly);
			} else if (kid instanceof SelectManyButton) {
				((SelectManyButton) kid).setDisabled(isReadOnly);
			} else if (kid instanceof Calendar) {
				((Calendar) kid).setDisabled(isReadOnly);
			} else if (kid instanceof SelectOneButton) {
				((SelectOneButton) kid).setDisabled(isReadOnly);
			} else if (kid instanceof SelectBooleanButton) {
				((SelectBooleanButton) kid).setDisabled(isReadOnly);
			} else {
				findComponentAndSetReadOnly(kid, isReadOnly);
			}
		}
	}

	public void bloquearForm(String idComponentPai) {

		UIComponent comp = null;

		if (idComponentPai != null) {
			comp = findComponent(FacesContext.getCurrentInstance().getViewRoot(), idComponentPai);
		} else {
			comp = getPnPrincipal();
		}
		if (comp != null) {
			findComponentAndSetReadOnly(comp, true);
		} else {
			System.err.println("fomulárioBase não encontrado, comportamento padrão não irá funcionar! Set o Binding do div contatiner do seu form para pnPrincipal");
		}
	}

	public void desbloquearForm(String idComponentPai) {
		UIComponent comp = null;

		if (idComponentPai != null) {
			comp = findComponent(FacesContext.getCurrentInstance().getViewRoot(), idComponentPai);
		} else {
			comp = getPnPrincipal();
		}
		if (comp != null) {
			findComponentAndSetReadOnly(comp, false);
		} else {
			System.err.println("fomulárioBase não encontrado, comportamento padrão não irá funcionar! Set o Binding do div contatiner do seu form para pnPrincipal");
		}
	}

	public EstadoForm getEstadoAtual() {
		return estadoAtual;
	}

	public CommandButton getCbNovo() {
		return cbNovo;
	}

	public void setCbNovo(CommandButton cbNovo) {
		this.cbNovo = cbNovo;
	}

	public CommandButton getCbEditar() {
		return cbEditar;
	}

	public void setCbEditar(CommandButton cbEditar) {
		this.cbEditar = cbEditar;
	}

	public CommandButton getCbExcluir() {
		return cbExcluir;
	}

	public void setCbExcluir(CommandButton cbExcluir) {
		this.cbExcluir = cbExcluir;
	}

	public CommandButton getCbCancelar() {
		return cbCancelar;
	}

	public void setCbCancelar(CommandButton cbCancelar) {
		this.cbCancelar = cbCancelar;
	}

	public CommandButton getCbSalvarNovo() {
		return cbSalvarNovo;
	}

	public void setCbSalvarNovo(CommandButton cbSalvarNovo) {
		this.cbSalvarNovo = cbSalvarNovo;
	}

	public CommandButton getCbSalvarEditar() {
		return cbSalvarEditar;
	}

	public void setCbSalvarEditar(CommandButton cbSalvarEditar) {
		this.cbSalvarEditar = cbSalvarEditar;
	}

	public CommandButton getCbLimpar() {
		return cbLimpar;
	}

	public void setCbLimpar(CommandButton cbLimpar) {
		this.cbLimpar = cbLimpar;
	}

	public CommandButton getCbPesquisar() {
		return cbPesquisar;
	}

	public void setCbPesquisar(CommandButton cbPesquisar) {
		this.cbPesquisar = cbPesquisar;
	}

	public CommandButton getCbRecarrregar() {
		return cbRecarrregar;
	}

	public void setCbRecarrregar(CommandButton cbRecarrregar) {
		this.cbRecarrregar = cbRecarrregar;
	}

	public HtmlForm getFormPrincipal() {
		return formPrincipal;
	}

	public void setFormPrincipal(HtmlForm formPrincipal) {
		this.formPrincipal = formPrincipal;
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

	public abstract DAOGeral<T> getDaoPrincipal();

	public T getEntidade() {
		return entidade;
	}

	public void setEntidade(T entidade) {
		if (entidade != null) {
			System.out.println("Entidade selecionada [" + entidade.getId() + "]");
			if (entidade.getId() == null || !entidade.getId().equals(getIdEntidadePesq())) {
				setIdEntidadePesq(null);
			}
		} else {
			setIdEntidadePesq(null);
			System.out.println("Entidade de-selecionada");
		}
		this.entidade = entidade;
	}

	/*public Session getSessaoUsuario() {
		return SessaoUsuario.getCurrentInstance() != null ? SessaoUsuario.getCurrentInstance().getSessao() : null;
	}*/

	public List<T> getRegistros() {
		return registros;
	}

	public void setRegistros(List<T> registros) {
		this.registros = registros;
	}

	public void pesquisar(ActionEvent ae) {
		try {
			pesquisar();
			// setEstadoPesquisando(true);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

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

	public String getMsgPadraoCDialog() {
		return msgPadraoCDialog;
	}

	public void setMsgPadraoCDialog(String msgPadraoCDialog) {
		this.msgPadraoCDialog = msgPadraoCDialog;
	}

	public T getEntidadeDataTable() {
		return entidadeDataTable;
	}

	public void setEntidadeDataTable(T entidadeDataTable) {
		this.entidadeDataTable = entidadeDataTable;
	}

	public CommandButton getCbReativar() {
		return cbReativar;
	}

	public void setCbReativar(CommandButton cbReativar) {
		this.cbReativar = cbReativar;
	}

	public List<T> getRegistrosFiltrados() {
		return registrosFiltrados;
	}

	public void setRegistrosFiltrados(List<T> registrosFiltrados) {
		this.registrosFiltrados = registrosFiltrados;
	}

	public String getMsgPadraoErroDesconhecido() {
		return msgPadraoErroDesconhecido;
	}

	public void setMsgPadraoErroDesconhecido(String msgPadraoErroDesconhecido) {
		this.msgPadraoErroDesconhecido = msgPadraoErroDesconhecido;
	}

	public Long getIdEntidadePesq() {
		return idEntidadePesq;
	}

	public void setIdEntidadePesq(Long idEntidadePesq) {
		this.idEntidadePesq = idEntidadePesq;
	}

	public Boolean getPermissaoEditar() {
		return permissaoEditar;
	}

	public void setPermissaoEditar(Boolean permissaoEditar) {
		this.permissaoEditar = permissaoEditar;
	}

	public Boolean getPermissaoNovo() {
		return permissaoNovo;
	}

	public void setPermissaoNovo(Boolean permissaoNovo) {
		this.permissaoNovo = permissaoNovo;
	}

	public Boolean getPermissaoExcluir() {
		return permissaoExcluir;
	}

	public void setPermissaoExcluir(Boolean permissaoExcluir) {
		this.permissaoExcluir = permissaoExcluir;
	}

	public Boolean getPermissaoPesquisar() {
		return permissaoPesquisar;
	}

	public void setPermissaoPesquisar(Boolean permissaoPesquisar) {
		this.permissaoPesquisar = permissaoPesquisar;
	}
}
