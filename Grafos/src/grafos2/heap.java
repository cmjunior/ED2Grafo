/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos2;

import java.util.*;

/**
 *
 * @author celso
 */
public class heap {

    enum tipoHeap { Maximo, Minimo };
    
    ArrayList<Integer> heap;
    tipoHeap tipo;

    public heap(tipoHeap tipo) {
        this.heap = new ArrayList<Integer>();
        this.tipo = tipo;
    }
    
    public void push(Integer chave){
        this.heap.add(chave);       
        insereHeap();            
    }
    
    public Integer pop(){
        Integer ret;
        
        ret = this.heap.get(0);
        
        troca( this.heap.size() - 1, 0 );
        
        this.heap.remove(this.heap.size() - 1);
        
        removeHeap();
        
        return ret;       
    }  

    private Boolean compara(Integer v1, Integer v2){
        Boolean ret;
        if (this.tipo.equals(tipoHeap.Maximo))
            ret = ( this.heap.get(v1) > this.heap.get(v2) );
        else
            ret = ( this.heap.get(v1) < this.heap.get(v2) );
        return ret;
    }
    
    private void insereHeap(){
        Integer aux, pos = this.heap.size() - 1;
                
        while ( pos > 0 && pos != null ){
            if ( compara( pos, pai(pos)) ){
                troca( pos, pai(pos));
                pos = pai(pos);
            } else {
                // Não é maior que seu pai mas pode ser maior que ser Irmão...
                aux = irmao(pos);
                if ( aux != null && compara(pos, irmao(pos))){
                    troca(pos, irmao(pos));
                    pos = irmao(pos);
                }
            pos--;
            }
        }
    }
    
    private void removeHeap(){
        Integer pos = 0;
        Integer aux;
        
        while ( pos <= this.heap.size() ){
            aux = maximo( esquerda(pos), direita(pos) );
            if ( aux != null && ! compara( pos, aux ) ){
                troca( pos, aux );
                pos = aux;
            } else
                break;
        }
    }
    
    /**
     * Compara valores do nodo <b>val1</b> e <b>val2</b>.
     * Maximo verifica se o retorno será do maior elemento,
     * ou do menor.
     * 
     * @param maximo
     * @param val1
     * @param val2
     * @return 
     */
    private Integer maximo(Integer val1, Integer val2){
        if ( val1 == null )
            if (val2 == null)
                return null;
            else
                return val2;
        
        if ( val2 == null )
            if (val1 == null)
                return null;
            else
                return val1;
        
        if ( this.tipo.equals(tipoHeap.Maximo) )
            return ( this.heap.get(val1) > this.heap.get(val2) ? val1 : val2 );
        else
            return ( this.heap.get(val1) > this.heap.get(val2) ? val2 : val1 );
    }
    
    /**
     * Troca nodo <b>n1</b> por <b>n2</b>
     * @param v1
     * @param v2 
     */
    private void troca( Integer v1, Integer v2 ){
        Integer buffer;
        buffer = this.heap.get(v1);
        this.heap.set(v1, this.heap.get(v2));
        this.heap.set(v2, buffer);
    }
    
    /**
     * Retorna nodo pai do nodo atual, <b>null</b>
     * caso nodo seja a raiz da heap.
     * @param nodo
     * @return 
     */
    private Integer pai(Integer nodo){
        return ( nodo == 0 ? null : (nodo-1)/2);
    }
    
    /**
     * Retorna filho a direita de <b>nodo</b>,
     * retorna <b>null</b> caso não exista.
     * @param nodo
     * @return 
     */
    private Integer direita(Integer nodo){
        return ( 2 * nodo + 1 > this.heap.size() - 1 ? null : 2 * nodo + 1 );
    }
    
    /**
     * Retorna filho a esquerda de <b>nodo</b>,
     * retorna <b>null</b> caso não exista.
     * @param nodo
     * @return 
     */
    private Integer esquerda(Integer nodo){       
        return ( 2 * nodo + 2 > this.heap.size() - 1 ? null : 2 * nodo + 2);
    }
    
    /**
     * Retorna irmão de <b>nodo</b>:
     * Se pos for par, seu irmão é impar.
     * Se não tem irmão retorna <b>null</b>
     * @param nodo
     * @return 
     */
    private Integer irmao(Integer nodo){
        Integer ret = (( nodo%2) == 0 ? nodo - 1 : nodo + 1);
        return ( ret <= this.heap.size() - 1 ? ret : null);
    }
    
    @Override
    public String toString() {
        Integer nivel = 0,cont = 1;
        String saida = "";
        
        for (Integer i : this.heap){
            if (nivel == cont){
                saida += "\n";
                saida += " [ "+ i.toString() +" ] ";
                cont = cont * 2;
                nivel = 1;
            }else{
                saida += " [ "+ i.toString() +" ] ";
                nivel ++;
            }
        }
        return saida;
    }
}