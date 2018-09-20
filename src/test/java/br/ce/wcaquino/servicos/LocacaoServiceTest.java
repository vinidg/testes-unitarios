package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.matchers.MatchersProprios.caiEm;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.verificarDiaSemana;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;

public class LocacaoServiceTest {

	private LocacaoService service;
	private List<Filme> filmes = new ArrayList<>();

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void init() {
		service = new LocacaoService();
	}

	@Test
	public void verificaSeOsValoresEDatadeLocacaoERetorno() throws Exception {
		
		assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		Filme filme = new Filme("Star Wars", 2, 5.0);
		filmes.add(filme);
		Usuario usuario = new Usuario();

		Locacao locacao = service.alugarFilme(usuario, filmes);

		assertThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getValor(), is(not(6.0)));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		assertThat(locacao.getValor(), is(5.0));

	}

	@Test(expected = FilmesSemEstoqueException.class)
	public void aluguelComSucesso() throws Exception {
		Filme filme = new Filme("Star Wars", 0, 5.0);
		filmes.add(filme);
		Usuario usuario = new Usuario();
		service.alugarFilme(usuario, filmes);

	}

	@Test()
	public void lancaExceptionCasoOFilmeNaoTenhaEstoque() throws FilmesSemEstoqueException, LocadoraException {
		Filme filme = new Filme("Star Wars", 0, 5.0);
		filmes.add(filme);
		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(FilmesSemEstoqueException.class);
		exception.expectMessage("Filme sem estoque");

		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void lancaExceptionCasoUsuarioSejaNulo() throws FilmesSemEstoqueException {
		Filme filme = new Filme("Forest Gump", 1, 2.6);
		filmes.add(filme);
		Usuario usuario = null;

		try {
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}

	@Test
	public void lancaExceptionCasoFilmeSejaNulo() throws FilmesSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Usuario 1");

		exception.expect(LocadoraException.class);
		service.alugarFilme(usuario, null);

	}

	@Test
	public void deveDevolverFilmesNaSegundaCasoForSabado() throws FilmesSemEstoqueException, LocadoraException {

		assumeTrue(verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		Usuario usuario = new Usuario("Jorginho");
		List<Filme> filmes = Arrays.asList(new Filme("Filmao", 2, 1.0));

		Locacao alugarFilme = service.alugarFilme(usuario, filmes);

//		boolean ehSegunda = verificarDiaSemana(alugarFilme.getDataRetorno(), Calendar.MONDAY);

//		assertTrue(ehSegunda);
		
//		assertThat(alugarFilme.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
//		assertThat(alugarFilme.getDataRetorno(), caiEm(Calendar.SUNDAY));
		assertThat(alugarFilme.getDataRetorno(), caiNumaSegunda());

	}

}
