package com.br.melo.util;

 

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class FormatadorUtil {

	/**
	 * Mantém somente os números do cep passado como parâmetro.
	 * 
	 * @param numCepStr
	 * @return
	 */
	public static String cepToNumero(String numCepStr) {
		try {

			numCepStr = numCepStr.replaceAll("[\\-\\s]", "");

			return numCepStr;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Mantém somente os números do telefone.
	 * 
	 * @param telefone
	 * @return
	 */
	public static String telefoneToNumero(String telefone) {
		return telefone = telefone.replaceAll("[^\\d]", "");
	}

	/**
	 * Converte o número de telefone para a máscara padrão.
	 * 
	 * @param telefone
	 *            Número de telefone sem formatação.
	 * @return
	 */
	public static String numeroToTelefone(String telefone) {

		if (telefone == null) {
			return telefone;
		}
		
		String telefoneStr = telefone.replaceAll("[^\\d]", "");
		
		if (telefoneStr.length() == 10) {
			return "(" + telefoneStr.substring(0, 2) + ")" + " " + telefoneStr.substring(2, 6) + "-" + telefoneStr.substring(6, 10);
		} else if (telefoneStr.length() == 11 && telefoneStr.startsWith("0")) {
			return "(" + telefoneStr.substring(0, 3) + ")" + " " + telefoneStr.substring(3, 7) + "-" + telefoneStr.substring(7, 11);
		} else if (telefoneStr.length() == 11) {
			return "(" + telefoneStr.substring(0, 2) + ")" + " " + telefoneStr.substring(2, 7) + "-" + telefoneStr.substring(7, 11);
		} else if (telefoneStr.length() == 12 && telefoneStr.startsWith("0")) {
			return "(" + telefoneStr.substring(0, 3) + ")" + " " + telefoneStr.substring(3, 8) + "-" + telefoneStr.substring(8, 12);
		} else {
			return telefone;
		}
	}
	
	/**
	 * Formata um cpf ou cnpj. Colocando os separadores.
	 * 
	 * @param cpfCnpj
	 *            Cpf ou cnpj para ser formatado
	 * @return Cpf ou cnpj formatado
	 */
	public static String formatCpfCnpj(String cpfCnpj) {
		if (cpfCnpj == null || cpfCnpj.isEmpty()) {
			return "";
		}
		String cpfCnpjFormatado = cpfCnpj.replaceAll("[^\\d]", "");
		
		if (cpfCnpjFormatado.length() == 11) {
			cpfCnpjFormatado = cpfCnpjFormatado.substring(0, 3) + "." + cpfCnpjFormatado.substring(3, 6) + "." + cpfCnpjFormatado.substring(6, 9) + "-" + cpfCnpjFormatado.substring(9, 11);
		} else if (cpfCnpjFormatado.length() == 14) {
			cpfCnpjFormatado = cpfCnpjFormatado.substring(0, 2) + "." + cpfCnpjFormatado.substring(2, 5) + "." + cpfCnpjFormatado.substring(5, 8) + "/" + cpfCnpjFormatado.substring(8, 12) + "-" + cpfCnpjFormatado.substring(12, 14);
		}

		return cpfCnpjFormatado;
	}

	public static String intToReais(String strVlrCentavos, boolean exibirSinalMoeda) {
		
		if (strVlrCentavos == null || strVlrCentavos.length() == 0) {
			return null;
		}
		
		String sinalMoeda;

		if (exibirSinalMoeda) {
			sinalMoeda = "R$ ";
		} else {
			sinalMoeda = "";
		}	
		
		if (strVlrCentavos.indexOf("-") != -1) {
			sinalMoeda += "-";
			strVlrCentavos = strVlrCentavos.replaceAll("-", "");
		}
		
		// Caso a string seja somente em centavos
		// ou 0
		if (strVlrCentavos.length() == 1) {
			return sinalMoeda + "0,0" + strVlrCentavos;
		} else if (strVlrCentavos.length() == 2) {
			return sinalMoeda + "0," + strVlrCentavos;
		}

		String reais = strVlrCentavos.substring(0, strVlrCentavos.length() - 2);
		String centavos = strVlrCentavos.substring(strVlrCentavos.length() - 2, strVlrCentavos.length());

		String reaisFormatado = "";

		for (int i = reais.length() - 1; i >= 0; i--) {

			reaisFormatado = reais.charAt(i) + reaisFormatado;

			// A segunda condição impede um ponto
			// no começo da String
			if (reaisFormatado.replaceAll("\\.", "").length() % 3 == 0 && i != 0) {
				reaisFormatado = "." + reaisFormatado;
			}
		}

		String vlrFormatado = sinalMoeda + reaisFormatado + "," + centavos;

		return vlrFormatado;
	}
	
	public static String distanciaFormatada(Double distancia) {
		String strDistancia = null;
		double distanciaVendaMetrosFormatado = ((long) (distancia*100))/100.0;
		if(distancia > 1000) {
			double distanciaVendaQuilometrosFormatado = ((long) ((distanciaVendaMetrosFormatado/1000.0)*100))/100.0;
			strDistancia = distanciaVendaQuilometrosFormatado + " km";
		} else {
			strDistancia = distanciaVendaMetrosFormatado + " m";
		}
		
		return strDistancia;
	}
	
	/**
	 * Converte uma string de valor para o formato da RV
	 * Exemplos:
	 * 1500 => 15.00 = R$ 15,00
	 * 125000 => 1250.00 = R$ 1.250,00
	 * @param strValor
	 * @return String
	 */
	public static String intToReaisRV(String strValor) {
		return intToReais(strValor, false).replaceAll("\\.", "").replaceAll(",", "\\.");
	}
	
	public static String doubleToReais(String strVlr, boolean exibirSinalMoeda){
		
		String sinalMoeda;
		
		if (strVlr == null || strVlr.length() == 0) {
			return null;
		}
		
		if (exibirSinalMoeda) {
			sinalMoeda = "R$ ";
		} else {
			sinalMoeda = "";
		}
		
		if (strVlr.indexOf("-") != -1) {
			sinalMoeda += "-";
			strVlr = strVlr.replaceAll("-", "");
		}
		
		StringBuilder parteInteira = null;
		StringBuilder parteFracionaria = null;
		
		if(strVlr.indexOf(".") != -1){
			parteInteira = new StringBuilder(strVlr.substring(0, strVlr.indexOf(".")));
			parteFracionaria = new StringBuilder(strVlr.substring(strVlr.indexOf(".")+1));
		}
		
		if(parteFracionaria.toString().length() == 1){
			parteFracionaria.append("0");
		}		
		
		String parteInteiraBkp = parteInteira.toString();
		
		int j = 1;
		for(int i=parteInteiraBkp.length() - 1; i>0; i--){
			if(j % 3 == 0){
				parteInteira.insert(i, ".");
			}
			
			j++;
		}
		
		return sinalMoeda + parteInteira.toString() + "," + parteFracionaria.toString();
	}
	
	public static String doubleToReais(Double strVlrDouble, boolean exibirSinalMoeda){
		if(strVlrDouble == null){
			return null;
		}
		
		return doubleToReais(strVlrDouble.toString(), exibirSinalMoeda);
	}
	
	/*
	 * Método usado para converter um numerop em Monetário Contabil (ou seja, numeros negativos sao representados com PARENTESES ex.: R$ (70,00) é igual a -70 reias)
	 */
	public static String intToReaisContabil(Long strVlrCentavosLong, boolean exibirSinalMoeda) {
		
		boolean isNegativo = false;
		
		if(strVlrCentavosLong == null){
			return null;
		}
		
		String strVlrCentavos = strVlrCentavosLong.toString();
		
		if (strVlrCentavos.length() == 0) {
			return null;
		}
		
		String sinalMoeda;

		if (exibirSinalMoeda) {
			sinalMoeda = "R$ ";
		} else {
			sinalMoeda = "";
		}	
		
		if (strVlrCentavos.indexOf("-") != -1) {
			sinalMoeda += "(";
			strVlrCentavos = strVlrCentavos.replaceAll("-", "");
			
			isNegativo = true;
		}
		
		// Caso a string seja somente em centavos
		// ou 0
		if (strVlrCentavos.length() == 1) {
			return sinalMoeda + "0,0" + strVlrCentavos;
		} else if (strVlrCentavos.length() == 2) {
			return sinalMoeda + "0," + strVlrCentavos;
		}
		
		String reais = strVlrCentavos.substring(0, strVlrCentavos.length() - 2);
		String centavos = strVlrCentavos.substring(strVlrCentavos.length() - 2, strVlrCentavos.length());
		
		String reaisFormatado = "";

		for (int i = reais.length() - 1; i >= 0; i--) {

			reaisFormatado = reais.charAt(i) + reaisFormatado;

			// A segunda condição impede um ponto
			// no começo da String
			if (reaisFormatado.replaceAll("\\.", "").length() % 3 == 0 && i != 0) {
				reaisFormatado = "." + reaisFormatado;
			}
		}
		
		String vlrFormatado = sinalMoeda + reaisFormatado + "," + centavos;
		
		if (isNegativo) {
			vlrFormatado += ")";
		}
		
		return vlrFormatado;
		
	}

	public static String intToReais(Long valorEmCentavos, boolean exibirSinalMoeda) {
		
		if (valorEmCentavos == null) {
			return null;
		}
		
		return intToReais(valorEmCentavos.toString(), exibirSinalMoeda);
	}
	
	/**
	 * Método utilizado para converter Double para Long levando em consideração o Padrão do Sistema da Aplic.
	 * <br><B> Exemplo:</B> <br>- Valor em Double: 3.5(valor salvo na base) - (valor representando R$ 3,50)<br>
	 * 				- Valor em Long: 350 (valor salvo na base) - (valor representando R$ 3,50)
	 * @param vlr
	 * @return
	 */
	public static Long doubleToLong(Double vlr){
		return Math.round(vlr*100D);
	}
	
	/**
	 * Método utilizado para converter Long para Double levando em consideração o Padrão do Sistema da Aplic.
	 * <br><B> Exemplo:</B> <br>- Valor em Double: 3.5(valor salvo na base) - (valor representando R$ 3,50)<br>
	 * 				- Valor em Long: 350 (valor salvo na base) - (valor representando R$ 3,50)
	 * @param vlr
	 * @return
	 */
	public static Double longToDouble(Long vlr){
		return (double)(vlr / 100D);
	}
	
	public static String intToReais(Double valorEmCentavos, boolean exibirSinalMoeda) {
		
		if (valorEmCentavos == null) {
			return null;
		}
		
		return intToReais(getSomenteNumeros(getDoubleFormatadoComPonto(valorEmCentavos).toString()), exibirSinalMoeda);
	}

	public static String formatValorNumericoComUmaDecimal(Double valor) {
		
		if (valor == null) {
			return null;
		}
		
		String retorno = getDoubleFormatadoComUmaCasaDecimal(valor);
		
		String decimal = retorno.substring(retorno.lastIndexOf(","));
		
		retorno = formatValorNumerico(new Long(retorno.substring(0,retorno.lastIndexOf(",")))) + decimal;
		
		return retorno;
	}
	
	public static String formatValorNumerico(Long valor) {
		
		if (valor == null) {
			return null;
		}
		
		valor = valor * 100;
		
		String retorno = intToReais(valor.toString(), false);
		
		retorno = retorno.substring(0,retorno.lastIndexOf(","));
		
		return retorno;
	}
	
	public static String intToReais(Integer valorEmCentavos, boolean exibirSinalMoeda) {
		
		if (valorEmCentavos == null) {
			return null;
		}
		
		return intToReais(valorEmCentavos.toString(), exibirSinalMoeda);
	}

	public static String numeroToCep(String numCep) {
		return numCep.substring(0, 5) + "-" + numCep.substring(5, 8);
	}
	
	public static String SimNaoPorExtenso(Boolean bool) {
		if (bool!=null) {
			return SimNaoPorExtenso(bool);
		} else {
			return "Não";
		}
	}
	
	public static String SimNaoPorExtenso(boolean bool) {
		if (bool)
			return "Sim";
		else return "Não";
	}

	/**
	 * Formata a String 'N' ou 'S' para Sim ou Não
	 * 
	 * @param sOuN
	 * @return String formatada Sim ou Não
	 */
	public static String SimNaoPorExtenso(String sOuN) {
		if (sOuN == null)
			return "Não";
		else if (sOuN.equals(Utils.SIM))
			return "Sim";
		else if (sOuN.equals(Utils.NAO))
			return "Não";
		else {
			if (sOuN.equalsIgnoreCase("true")) {
				return "Sim";
			} else if (sOuN.equalsIgnoreCase("false")) {
				return "Não";
			} 
		}
		
		return "";
	}
	
	public static Double reaisToDouble(String valorEmReais){
		
		valorEmReais = valorEmReais.replaceAll("[R\\$\\s]", "");
		
		return Double.parseDouble(valorEmReais.replace(",", "."));
	}
	
	/**
	 * Método que recebe um String, que deve representar um valor, e retira
	 * somente os números convertendo para um Long.
	 * 
	 * @param valorEmReais
	 *            Valor em Reais no formato de String
	 * @return Valor em Long ou Null caso o número seja inválido
	 */
	public static Long reaisToInt(String valorEmReais) {
		try {
			int lugarVirgula = valorEmReais.indexOf(",");

			// Caso n tenha informado as casas
			// decimais. Ex.: R$ 200
			if (lugarVirgula == -1) {
				valorEmReais += "00";
				// Caso nem tenha informado os centavos
				// completos. Ex.: R$ 200,1
			} else if (lugarVirgula == valorEmReais.length() - 2) {
				valorEmReais += "0";
			} else {
				valorEmReais = valorEmReais.substring(0, lugarVirgula + 3);
			}

			return Long.parseLong(valorEmReais.replaceAll("[R\\$\\s\\.\\,]", ""));
		} catch (Exception e) {
			return null;
		}
	}
	
	/*
	 * Método usado para converter uma String de Monetario Contabil(ex.: "R$ (70,00)" (isso é -70 reias contabilmente) em Long)
	 */
	public static Long reaisToIntContabil(String valorEmReais) {
		boolean isNegativo = false;
		try {
			
			
			if(valorEmReais.contains("(")){
				valorEmReais = valorEmReais.replace("(", "");
				valorEmReais = valorEmReais.replace(")", "");
				
				isNegativo = true;
			}
			
			int lugarVirgula = valorEmReais.indexOf(",");

			// Caso n tenha informado as casas
			// decimais. Ex.: R$ 200
			if (lugarVirgula == -1) {
				valorEmReais += "00";
				// Caso nem tenha informado os centavos
				// completos. Ex.: R$ 200,1
			} else if (lugarVirgula == valorEmReais.length() - 2) {
				valorEmReais += "0";
			} else {
				valorEmReais = valorEmReais.substring(0, lugarVirgula + 3);
			}

			long retorno = Long.parseLong(valorEmReais.replaceAll("[R\\$\\s\\.\\,]", ""));
			
			return isNegativo ? retorno * -1 : retorno;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retira da string tudo que não seja número, retornando-a após isso.
	 * 
	 * @param string
	 *            Qualquer string que se queira retirar tudo que não seja número
	 * @return String somente contendo os números.
	 */
	public static String getSomenteNumeros(String string) {
		return string.replaceAll("[^\\d]", "");
	}
	
	public static boolean isStringSemDigito(String string) {
		Matcher matcher = Pattern.compile("[\\d]").matcher(string);
		return !matcher.find();
	}
	
	public static boolean isStringSomenteNumerica(String string) {
		try{
			Long.parseLong(string);
		}catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}

	public final static int SENHA_MUITO_FRACA = -1;
	public final static int SENHA_FRACA = 0;
	public final static int SENHA_MEDIA = 1;
	public final static int SENHA_FORTE = 2;
	public final static int SENHA_MUITO_FORTE = 3;
	public final static int SENHA_REALMENTE_MUITO_FORTE = 4;

	/**
	 * Método que checa a força de uma senha passada por parâmetro e retorna um
	 * inteiro -1 quando a senha for muito fraca ou o indice de força da senha.
	 * SENHA_MUITO_FRACA (-1) : Quando a senha tem menos de 6 caracteres e não é
	 * composta por letras e números.
	 * 
	 * @param senha
	 * @return
	 */
	public static int verificarForcaSenha(String senha) {
		int tamanhoSenha = senha.length();
		int forcaSenha = -1;

		if (tamanhoSenha < 6) {
			return SENHA_MUITO_FRACA;
		}

		boolean contemDigitos = false;
		boolean contemLetrasMinusculas = false;
		boolean contemLetrasMaiusculas = false;
		boolean contemEspeciais = false;

		// Checa se quais tipos de caracteres tem.
		Matcher matcher = Pattern.compile("[a-z]").matcher(senha);
		contemLetrasMinusculas = matcher.find();
		matcher.reset(senha);

		matcher.usePattern(Pattern.compile("[A-Z]"));
		contemLetrasMaiusculas = matcher.find();
		matcher.reset();

		matcher.usePattern(Pattern.compile("[\\d]"));
		contemDigitos = matcher.find();
		matcher.reset();

		matcher.usePattern(Pattern.compile("[^\\dA-Za-z]"));
		contemEspeciais = matcher.find();
		matcher.reset();

		// Testa sequencias faceis
		int sequenciaNumeros = 0;
		int sequenciaLetras = 0;
		int repeticoesSequenciadas = 0;

		// for (int i = 0; i < senha.length(); i++) {

		// if (i >= 2) {
		for (int j = senha.length() - 1; j >= 0; j--) {

			char charAtual = senha.charAt(j);

			if (Character.isLetterOrDigit(charAtual)) {
				int atual = 0;
				int anterior = 0;
				int anteriorDaAnterior = 0;

				if (j > 1) {

					char charAnterior = senha.charAt(j - 1);
					char charAnteriorDaAnterior = senha.charAt(j - 2);

					if (Character.isDigit(charAtual) && Character.isDigit(charAnterior) && Character.isDigit(charAnteriorDaAnterior)) {
						atual = Integer.parseInt(String.valueOf(charAtual));
						anterior = Integer.parseInt(String.valueOf(charAnterior));
						anteriorDaAnterior = Integer.parseInt(String.valueOf(charAnteriorDaAnterior));
					} else if (Character.isLetter(charAtual) && Character.isLetter(charAnterior) && Character.isLetter(charAnteriorDaAnterior)) {
						atual = Character.getNumericValue(charAtual);
						anterior = Character.getNumericValue(charAnterior);
						anteriorDaAnterior = Character.getNumericValue(charAnteriorDaAnterior);
					} else {
						continue;
					}

					if ((atual - anterior) == 1 && (anterior - anteriorDaAnterior == 1)) {
						if (Character.isDigit(charAtual)) {
							sequenciaNumeros++;
						} else {
							sequenciaLetras++;
						}
					}

					if (atual == anterior && atual == anteriorDaAnterior) {
						repeticoesSequenciadas++;
					}
				}

				if (sequenciaNumeros > 0 || sequenciaLetras > 0 || repeticoesSequenciadas > 0) {
					continue;
				}
			}
		}

		if (sequenciaNumeros > 0) {
			// System.out.println(sequenciaNumeros +
			// " Existe sequencia numérica.");
			return SENHA_MUITO_FRACA;
		} else {
			// System.out.println(" NÃO Existe sequencia numérica.");
		}

		if (sequenciaLetras > 0) {
			// System.out.println(sequenciaLetras +
			// " Existe sequencia alfanumérica.");
			return SENHA_MUITO_FRACA;
		} 
//		else {
//			System.out.println(" NÃO Existe sequencia alfanumérica.");
//		}

		if (repeticoesSequenciadas > 0) {
			// System.out.println(repeticoesSequenciadas +
			// " Existe repetições sequenciadas.");
			return SENHA_MUITO_FRACA;
		} else {
			// System.out.println(" NÃO Existe repetições sequenciadas.");
		}

		if (contemDigitos) {
			// System.out.println("A senha contém números.");
			forcaSenha++;
		} else {
			// System.out.println("A senha NÃO contém números.");
		}

		if (contemLetrasMinusculas) {
			// System.out.println("A senha contém letras minusculas");
			forcaSenha++;
		} else {
			// System.out.println("A senha NÃO contém letras minusculas");
		}

		if (contemLetrasMaiusculas) {
			// System.out.println("A senha contém letras maiusculas");
			forcaSenha++;
		} else {
			// System.out.println("A senha NÃO contém letras maiusculas");
		}

		if (contemEspeciais) {
			// System.out.println("A senha contém caracteres especiais");
			forcaSenha++;
		} else {
			// System.out.println("A senha NÃO contém caracteres especiais");
		}

		if (tamanhoSenha >= 10) {
			forcaSenha++;
		}

		if (!(contemDigitos && (contemLetrasMaiusculas || contemLetrasMinusculas))) {
			return SENHA_MUITO_FRACA;
		}

		return forcaSenha;

	}

	/**
	 * Calcula um percentual do segundo argumento em relação ao primeiro.
	 * 
	 * @param vlrTotal
	 *            Valor total da conta (100%)
	 * @param vlrRelativo
	 *            Valor relativo ao percentual que se quer como retorno.
	 * @return Percentual da relação
	 */

	public static Double getPorcentagemDouble(Long vlrRelativo, Long vlrTotal) {
		
		if (vlrTotal == null || vlrTotal == 0) {
			return 0.0;
		}
		
		double resultado = ((double) vlrRelativo * 100D) / (double) vlrTotal;

		return resultado;
	}

	/**
	 * Método para truncar um número double, escolhendo a qtd de casas decimais.
	 * 
	 * @param valor
	 *            Valor a ser truncado
	 * @param qtdCasasDecimais
	 *            Quantida de casas decimais no retorno
	 * @return Valor truncado.
	 * @throws ParseException 	
	 */
	public static Double getTruncarDouble(Double valor, int qtdDigitos) throws ParseException {
		
		if(valor == 0.0D) {
			return valor;
		}
		
		NumberFormat numeroFormatador = NumberFormat.getNumberInstance(new Locale("en","us"));
		numeroFormatador.setMaximumFractionDigits(qtdDigitos);
		numeroFormatador.setMinimumFractionDigits(qtdDigitos);
		valor = numeroFormatador.parse(numeroFormatador.format(valor)).doubleValue();
		
		return valor;
	}

	/**
	 * Formata um número double para '0.00'
	 * 
	 * @param valor
	 *            valor a ser formatado
	 * @return Valor formatado
	 */
	public static String getDoubleFormatado(Double valor) {
		if (valor == null) {
			return "";
		}

		DecimalFormat aproximador = new DecimalFormat("##0.00", new DecimalFormatSymbols(new Locale("PT", "BR")));

		return aproximador.format(valor);
	}
	
	/**
	 * Formata um número double para '0.0'
	 * 
	 * @param valor
	 *            valor a ser formatado
	 * @return Valor formatado
	 */
	public static String getDoubleFormatadoComUmaCasaDecimal(Double valor) {
		if (valor == null) {
			return "";
		}

		DecimalFormat aproximador = new DecimalFormat("##0.0", new DecimalFormatSymbols(new Locale("PT", "BR")));

		return aproximador.format(valor);
	}
	
/*	public static String getDoubleFormatado(Double valor, int qtdDecimais) {
		if (valor == null) {
			return "";
		}
		
		if (qtdDecimais <= 0) {
			qtdDecimais = 1;
		}
		
		DecimalFormat aproximador = new DecimalFormat("##0."+Utils.arredondaCampo("", qtdDecimais, "0"), new DecimalFormatSymbols(new Locale("PT", "BR")));
		
		return aproximador.format(valor);
	}*/
	
	/**
	 * Formata um número double para '0.0'
	 * 
	 * @param valor
	 *            valor a ser formatado
	 * @return Valor formatado
	 */
	public static String getDoubleFormatadoComUnidadeMilhar(Long valor) {
		if (valor == null) {
			return "";
		}

		DecimalFormat aproximador = new DecimalFormat("#,##0", new DecimalFormatSymbols(new Locale("PT", "BR")));
		
		aproximador.setCurrency(Currency.getInstance("BRL"));

		return aproximador.format(valor);
	}
	
	public static String numberToReal(Number number){
		NumberFormat aproximador = new DecimalFormat("#,##0", new DecimalFormatSymbols(new Locale("pt", "br")));
		aproximador.setCurrency(Currency.getInstance("BRL"));
		
		NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "br"));
		return formatador.format(number.doubleValue());
	}
	
	public static void main(String[] args) {
		System.out.println(getDoubleFormatadoComPonto(123123D));
	}
	
	public static String getDoubleFormatadoComPonto(Double valor){
		NumberFormat numero =  NumberFormat.getNumberInstance(new Locale("en", "us"));
		numero.setMaximumFractionDigits(2);
		numero.setMinimumFractionDigits(2);
		return numero.format(valor);
	}
	
	public static String iccidFormat(String iccid) {
		String resultado = new String(iccid);
		StringBuilder sb = new StringBuilder();
		
		if (resultado.length() == 20) {
			
			for (int i = 0; i < resultado.length(); i++) {
				sb.append(i != 19 && (i + 1) % 4 == 0 ? resultado.charAt(i) + " " : resultado.charAt(i));
			}
			resultado = sb.toString();
		}
		
		else if (resultado.length() == 19){
			
			for (int i = 0; i < resultado.length(); i++) {
				if(i < 3){
					sb.append((i + 1) % 3 == 0 ? resultado.charAt(i) + " " : resultado.charAt(i));
				}else{
					sb.append(i != 18 && (i - 3 + 1) % 4 == 0 ? resultado.charAt(i) + " " : resultado.charAt(i));
				}
			}
			resultado = sb.toString();
		}
		
		return resultado;
	}
	
	public static String iccidToString(String iccid) {
		String resultado = new String(iccid);
		
		resultado = resultado.replaceAll(" ", "");
		
		return resultado;
	}
	
}

