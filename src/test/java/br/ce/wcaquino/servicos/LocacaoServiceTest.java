package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencialDias;
import static br.ce.wcaquino.utils.DataUtils.verificarDiaSemana;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

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

public class LocacaoServiceTest {

	private LocacaoService service;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void init() {
		service = new LocacaoService();
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		
		assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		Usuario usuario = umUsuario().agora();

		Locacao locacao = service.alugarFilme(usuario, filmes);

		error.checkThat(locacao.getValor(), is(equalTo(4.0)));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencialDias(1));

	}

	@Test()
	public void aluguelComSucesso() throws Exception {
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		Usuario usuario = umUsuario().agora();
		service.alugarFilme(usuario, filmes);

	}

	@Test(expected = FilmesSemEstoqueException.class)
	public void lancaExceptionCasoOFilmeNaoTenhaEstoque() throws FilmesSemEstoqueException, LocadoraException {
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());
		Usuario usuario = umUsuario().agora();

		service.alugarFilme(usuario, filmes);

	}

	@Test
	public void lancaExceptionCasoUsuarioSejaNulo() throws FilmesSemEstoqueException {
		List<Filme> filmes = Arrays.asList(umFilme().agora());
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
		Usuario usuario = umUsuario().agora();

		exception.expect(LocadoraException.class);
		
		service.alugarFilme(usuario, null);
	}

	@Test
	public void deveDevolverFilmesNaSegundaCasoForSabado() throws FilmesSemEstoqueException, LocadoraException {

		assumeTrue(verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		Locacao alugarFilme = service.alugarFilme(usuario, filmes);

		assertThat(alugarFilme.getDataRetorno(), caiNumaSegunda());

	}

}
