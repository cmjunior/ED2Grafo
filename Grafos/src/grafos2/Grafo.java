package grafos2;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe Grafo, métodos e propriedades do grafo.
 * @author Celso Marigo Junior
 */
public class Grafo implements Cloneable{
    
    /**
     * Obtem tipoGrafo do Grafo
     * @return 
     */
    public tipoGrafo getTipo() {
        return tipo;
    }

    /**
     * Define tipo do grafo.
     * @param tipo 
     */
    public void setTipo(tipoGrafo tipo) {
        this.tipo = tipo;
    }
    
    /**
     * Tipos de grafo que podem ser implementados
     * Direcionado
     * Não Direcionado
     */
    public enum tipoGrafo{
        direcionado, naoDirecionado
    }

    tipoGrafo tipo;
    ArrayList<Vertice> vertices;
    Integer matriz[][];
    Integer tam;
    ArrayList<Aresta> arestas;

    /**
     * Contructor do grafo
     * @param tipo : tipoGrafo
     * @param tamanho : tamanho inicial do grafo.
     */
    public Grafo(tipoGrafo tipo, Integer tamanho) {
        this.tipo = tipo;
        this.tam = tamanho;
        this.vertices = new ArrayList<Vertice>();
        this.matriz = new Integer[tam][tam];
        this.arestas = new ArrayList<Aresta>();
    }

    /**
     * Incluir novo vertice no grafo
     * @param chave : Valor numerico que identifica o vertice
     * @param nome : Nome do vertice
     * @return 
     */
    public Boolean adicionarVertice(Integer chave, String nome){
        int pos ;
        Vertice i;
        
        pos = posicaoVaga();
        preencheAdjacentes(pos, true);

        i = new Vertice(chave, pos, nome);
        
        this.vertices.add(i);
        
        return true;
    }
    
    /**
     * Obtem a proxima posição vaga na Matriz de Adjacência
     * @return 
     */
    private int posicaoVaga(){
        for (int c = 0; c < this.tam ; c++){
            if (this.matriz[c][0] == null)
                return c;
        }
        Integer ret = this.tam;
        redimensionaMatriz();
        
        return posicaoVaga();
    }

    /**
     * Busca objeto vertice utilizando a chave
     * @param chave
     * @return 
     */
   Vertice pegaVertice(Integer chave) {
        for (Vertice i : this.vertices){
            if (i.getChave() == chave)
                return i;
        }
        return null;    
    }
   
   Vertice getVertice(int pos){
       ArrayList<Vertice> clone = (ArrayList<Vertice>)this.vertices.clone();
       Collections.sort(clone);
       
       try{
           clone.get(pos);
           return clone.get(pos);
       } catch(Exception e){
           return null;
       }
   }

   /**
    * Método auxiliar para liberar espaçõs na Matriz de adjacencia
    * @param posicao
    * @param zera 
    */
    private void preencheAdjacentes(Integer posicao, boolean zera){
        for (int i = 0; i < this.tam; i++) 
            this.matriz[posicao][i] = ( zera ? 0 : null );
        if (!zera)
            for (int i = 0; i < this.tam; i++)
                this.matriz[i][posicao] = null;
    }

    /**
     * Remove um vértice do grafo
     * @param chave
     * @return 
     */
    boolean removerVertice(Integer chave){
        for (Vertice i : this.vertices){           
            if (i.getChave() == chave){
                preencheAdjacentes(i.getPos(), false);
                this.vertices.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Adiciona nova aresta entre dois vertices
     * @param v1
     * @param v2
     * @param peso - Coloca o valor do peso na matriz
     * @return 
     */
    boolean adicionarAresta(int v1, int v2, int peso){
       Aresta ar = new Aresta(peso, pegaVertice(v1), pegaVertice(v2));
       
       v1 = achaVertice(v1);
       v2 = achaVertice(v2);
       
       if ((v1 == -1 ) || (v2 == -1))
           return false;
       else {
            this.matriz[v1][v2] = peso;
            this.arestas.add(ar);
            if (this.tipo == tipoGrafo.naoDirecionado){
                this.matriz[v2][v1] = peso;
                Vertice temp;
                temp = ar.getV1();
                ar.setV1(ar.getV2());
                ar.setV2(temp);
                this.arestas.add(ar);
            }
            return true;
       }
    }

    /**
     * Remove aresta entre dois vertices.
     * @param v1
     * @param v2
     * @return 
     */
    boolean removerAresta(int v1, int v2){
        if (this.matriz[v1][v2] == null)
            return false;
        else {
            Aresta ar;
            ar = new Aresta(this.matriz[v1][v2], pegaVertice(v1), pegaVertice(v2));
            this.matriz[v1][v2] = null;
            this.arestas.remove(ar);
            
            if ( this.tipo == tipoGrafo.naoDirecionado ){
                this.matriz[v2][v1] = null;
                Vertice temp = ar.getV1();
                ar.setV1(ar.getV2());
                ar.setV2(temp);
                this.arestas.remove(ar);
            }
            return true;
        }
    }

    /**
     * Verifica se vertices estão conectados por uma aresta.
     * @param v1
     * @param v2
     * @return 
     */
    boolean testarAdjacencia(int v1, int v2){
        v1 = achaVertice(v1);
        if ( v1 == -1) return false;
        v2 = achaVertice(v2);
        if ( v2 == -1 )return false;
        
        return (( this.matriz[v1][v2] != null ) && ( this.matriz[v1][v2] != 0 ));
    }

    /**
     * Lista todos os vizinhos de um vertice
     * @param chave
     * @return 
     */
    public ArrayList<Integer> listarVizinhos(Integer chave){
        
        ArrayList<Integer> vizinhos = new ArrayList<Integer>();
        
        for (Vertice v : this.vertices){
            if ( testarAdjacencia(chave, v.getChave()) )
                vizinhos.add(v.getChave());
        }
        
        return vizinhos;
    }
    
    /**
     * Obtem o Grau de Entrada de um vertice
     */
    public int grauEntrada(Integer pos){
        int grauEntrada = 0;
        for (int i = 0; i <= this.tam-1 ; i++){
            if ((this.matriz[i][pos] != null) && (this.matriz[i][pos] != 0 ))
                grauEntrada ++;
        }
        return grauEntrada;
    }
    
    /**
     * Retorna a posição de um vértice na Matriz.
     * @param chave
     * @return 
     */
    public Integer achaVertice(Integer chave){
        for (Vertice v : this.vertices){
            if (v.getChave() == chave)
                return v.getPos();
        }
        return -1;
    }

    /**
     * Redimensiona matriz de adjacência
     */
    void redimensionaMatriz(){
        Integer newTam = matriz.length + tam,
                oldTam = this.tam,
                tmp[][] = new Integer[newTam][newTam];
                
        for (int i = 0; i < oldTam ; i++)
            for (int j = 0; j < newTam; j++){
                if ((j < oldTam) && (this.matriz[i][i] != null))
                    tmp[i][j] = this.matriz[i][j];
                else
                    tmp[i][j] = 0;
            }        
                  
        this.matriz = tmp;
        this.tam = newTam;
    }
    
    /**
     * Ordem Topologica
     */
    public void ordemTopologica(Grafo copiaGrafo, ArrayList<Integer> lista){
        Integer[] grauEntrada = new Integer[copiaGrafo.vertices.size()];
        Vertice i;
        
        if (!copiaGrafo.vertices.isEmpty()){
            ArrayList<Integer> listaAux = new ArrayList<Integer>();
            for ( int j =  0; j < copiaGrafo.vertices.size(); j++ ){
                i = copiaGrafo.vertices.get(j);
                grauEntrada[j] = grauEntrada(i.getPos());
            }

            for ( int j =  0; j < copiaGrafo.vertices.size(); j++ ){
                if (grauEntrada[j] <= 0){
                    i = copiaGrafo.vertices.get(j);
                    listaAux.add(i.getChave());
                }
            }
            
            for ( Integer j : listaAux ){
                if ( copiaGrafo.achaVertice(j) != null )
                copiaGrafo.removerVertice(j);
            }
            
            Collections.sort(listaAux);
            lista.addAll(listaAux);
            ordemTopologica(copiaGrafo, lista);
        }
    }
    
    /**
     * 
     * @param lista 
     */
    public void arvoreMinima(ArrayList<Aresta> lista){
        //ArrayList<Vertice> conjVertices = (ArrayList<Vertice>)this.vertices.clone();
        ArrayList<Aresta> conjArestas = (ArrayList<Aresta>)this.arestas.clone();
        ConjuntoDisjunto conjVertices = new ConjuntoDisjunto(this.arestas.size()-1);
        Integer cont = 0;
        
        if (! conjArestas.isEmpty()){
            Collections.sort(conjArestas);
        
            do {    
                Aresta ar;
                ar = conjArestas.remove(0);
                
                if ( conjVertices.achaRaiz(ar.getV1().getPos()) != conjVertices.achaRaiz(ar.getV2().getPos()) ){
                    conjVertices.unir(ar.getV1().getPos(), ar.getV2().getPos());
                    lista.add(ar);
                }
            } while (! conjArestas.isEmpty());
        }
    }
    
    /**
     * Menor caminho
     * @return 
     */
    final Integer custoMaximo = 99999;
    
    public Integer menorCaminho(Integer v1, Integer v2, ArrayList<Integer> caminho){
        int totVertices = this.vertices.size();
        int origem = pegaVertice(v1).getPos();
        int destino = pegaVertice(v2).getPos();
        int[] dist = new int[totVertices]; // Distancias
        int[] perm = new int[totVertices]; // Permutações
        int[] path = new int[totVertices]; // Caminhos
        
        Aresta[][] matrizAdj = new Aresta[this.matriz.length][this.matriz.length];
        
        for ( int i = 0; i <= totVertices; i++ ){
            for ( int j = 0; j <= totVertices; j++){
                if (this.matriz[i][j] != null && this.matriz[i][j] != 0)
                    matrizAdj[i][j] = new Aresta(this.matriz[i][j], getVertice(i), getVertice(j));
                else
                    matrizAdj[i][j] = new Aresta(custoMaximo, getVertice(i), getVertice(j));
            }
        }
                
        for (int i = 0; i < totVertices; i++){
            perm[i] = 0;
            dist[i] = custoMaximo;
        }
        
        perm[origem] = 1;
        dist[origem] = 0;
        
        int dc, k, atual, menorDistancia, novaDistancia;
        
        atual = origem;
        k = atual;
        
        while (k != destino){
            menorDistancia = custoMaximo;
            dc = dist[atual];
            
            for (int i = 0; i < totVertices; i++){
                if (perm[i] == 0){
                    novaDistancia = dc + matrizAdj[atual][i].getPeso();
                    
                    if (novaDistancia < dist[i]){
                        dist[i] = novaDistancia;
                        path[i] = atual;
                        k = i;
                    }
                    
                    if (dist[i] < menorDistancia){
                        menorDistancia = dist[i];
                        k = i;
                    }
                    
                }
            }
            
            if (atual == k){
                break;
            }
            
            atual = k;
            perm[atual] = 1;
        }
                
        int rota = destino;
        
        while (rota != origem){
            caminho.add(path[rota]);
            rota = path[rota];
        }        
        return dist[destino];
    }

    @Override
    protected Grafo clone() {
        try{
            Grafo clone;
            clone = (Grafo) super.clone();
            clone.vertices = (ArrayList<Vertice>)this.vertices.clone();
            clone.matriz = (Integer[][])this.matriz.clone();
            return clone;
        }catch (CloneNotSupportedException e){
            System.err.println(this.getClass()+" não é cloneavel.");
            return null;
        }
    }
}