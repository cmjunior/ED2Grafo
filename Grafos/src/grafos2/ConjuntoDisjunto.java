package grafos2;

import java.util.ArrayList;

/**
 *
 * @author celso
 */
public class ConjuntoDisjunto {
    private Integer[] conjArvores;

    public ConjuntoDisjunto(Integer tamanho) {
        this.conjArvores = new Integer[tamanho];
        
        for (Integer i = 0 ; i < tamanho; i++){
            conjArvores[i] = i;
        }
    }
    
    public Integer achaRaiz(Integer pos){
        ArrayList<Integer> lista = new ArrayList<Integer>();
        
        while (pos != conjArvores[pos]){
            lista.add(conjArvores[pos]);
            pos = conjArvores[pos];
        }
        
        while (! lista.isEmpty())
            conjArvores[lista.remove(0)] = pos;
        
        return pos;
    }
    
    public void unir(Integer a, Integer b){
        conjArvores[achaRaiz(b)] = achaRaiz(a);
    }
}
