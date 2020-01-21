package br.ce.wcaquino.matchers;

import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class DataComDiferencaDiasMatcher extends TypeSafeMatcher<Date> {
	
	private int dias;
	
	public DataComDiferencaDiasMatcher(int dias) {
		this.dias = dias;
	}

	@Override
	public void describeTo(Description description) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean matchesSafely(Date item) {
		return DataUtils.isMesmaData(item, DataUtils.obterDataComDiferencaDias(dias));
	}

}
