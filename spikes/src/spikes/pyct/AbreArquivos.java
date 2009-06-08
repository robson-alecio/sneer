package spikes.pyct;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class AbreArquivos {
	
	int xInicial[]	 = new int [10];
	int xFinal[]	 = new int [10];
	int yInicial[]	 = new int [10];
	int yFinal[]	 = new int [10];
	
	
	String vetorCaminho[] = new String[20];
	String pasta = "C:\\pyct\\";
	String nome;
	String caminho;
	public void abreCordenadas(String cordenadas) throws NumberFormatException, IOException
	{
		int indice = 0;
		
		String texto;
		
		
		int _xInicial = 0,_yInicial = 0,_xFinal = 0, _yFinal = 0;
		
		FileInputStream is = new FileInputStream(cordenadas);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		br.readLine(); // descartei a pimeira linha
		br.readLine(); // descartei a segunda linha
		while((texto = br.readLine())!= null )
		{
			System.out.println(texto);
			StringTokenizer st = new StringTokenizer(texto);
			
			st.nextToken();
			if (st.hasMoreElements()) _xInicial		= Integer.valueOf(st.nextToken()).intValue();
			if (st.hasMoreElements()) _yInicial		= Integer.valueOf(st.nextToken()).intValue();
			if (st.hasMoreElements()) _xFinal		= Integer.valueOf(st.nextToken()).intValue();
			if (st.hasMoreElements()) _yFinal		= Integer.valueOf(st.nextToken()).intValue();
			if (st.hasMoreElements()) nome			= (st.nextToken());
			
			
			caminho	= pasta + nome;
			indice++;
			
			xInicial[indice] 		= _xInicial;
			yInicial[indice] 		= _yInicial;
			xFinal[indice]			= _xFinal;
			yFinal[indice]			= _yFinal;
			vetorCaminho[indice]	= caminho;
		}
	}
}
