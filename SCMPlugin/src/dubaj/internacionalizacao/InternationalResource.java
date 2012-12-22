package dubaj.internacionalizacao;

public class InternationalResource {
	
	private static IIdioma idioma;
	
	public static IIdioma getIdioma()
	{
		if (idioma == null)
		{
			idioma = new IdiomaIngles();
		}
		
		return idioma;
	}
}
