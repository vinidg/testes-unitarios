package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorDescontoLocacao {

	private LocacaoService service;

	@Parameter
	public  List<Filme> filmes;
	
	@Parameter(value = 1)
	public Double valorLocacao;
	
	@Parameter(value = 2)
	public  String desc;
	
	@Before
	public void init() {
		service = new LocacaoService();
	}
	
	private static Filme filme1 = new Filme("Star Wars", 5, 4.0);
	private static Filme filme2 = new Filme("Indiana", 1, 4.0);
	private static Filme filme3 = new Filme("Tomb Raider", 4, 4.0);
	private static Filme filme4 = new Filme("Planeta dos Macacos", 7, 4.0);
	private static Filme filme5 = new Filme("Clube da Luta", 5, 4.0);
	private static Filme filme6 = new Filme("Lixomania", 2, 4.0);

	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros(){
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1,filme2,filme3), 11.0, "25%"},
			{Arrays.asList(filme1,filme2,filme3, filme4), 13.0, "50%"},
			{Arrays.asList(filme1,filme2,filme3, filme4, filme5), 14.0, "75%"},
			{Arrays.asList(filme1,filme2,filme3, filme4, filme5, filme6), 14.0, "100%"}
		});
	}
	
	@Test
	public void deveCalcularOValorDeLocacaoComOsDescontos() throws FilmesSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Vinicius");
		
		Locacao alugarFilme = service.alugarFilme(usuario, filmes);

		assertThat(alugarFilme.getValor(), is(valorLocacao));
		
	}
}
