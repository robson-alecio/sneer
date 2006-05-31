package spikes.pyct;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class AbreRelations {
	
	
	String vetorCaminho[] = new String[20];
	String pasta = "C:\\pyct\\";
	String nome;
	String caminho;
	public void abreCordenadas(String cordenadas) throws NumberFormatException, IOException
	{
		int indice = 0;
		
		String texto;
		
		FileInputStream is = new FileInputStream(cordenadas);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		br.readLine(); // descartei a pimeira linha
		br.readLine(); // descartei a segunda linha
		while((texto = br.readLine())!= null )
		{
			System.out.println(texto);
			StringTokenizer st = new StringTokenizer(texto);
			
			st.nextToken();
			
			if (st.hasMoreElements()) nome			= (st.nextToken());
			
			
			caminho	= pasta + nome;
			indice++;
			
			vetorCaminho[indice]	= caminho;
			
			
		}
	}

}
