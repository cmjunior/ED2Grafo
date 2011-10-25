package grafos2;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Classe Grafo, métodos e propriedades do grafo.
 * @author Celso Marigo Junior
 */
public class Grafo implements Cloneable{
    public Integer custoMaximo(){
        return 99999;
    }
     
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
    Aresta matriz[][];
    int tam;
    ArrayList<Aresta> arestas;

    /**
     * Contructor do grafo
     * @param tipo : tipoGrafo
     * @param tamanho : tamanho inicial do grafo.
     */
    public Grafo(tipoGrafo tipo, int tamanho) {
        this.tipo = tipo;
        this.tam = tamanho;
        this.vertices = new ArrayList<Vertice>();
        this.matriz = new Aresta[tam][tam];
        this.arestas = new ArrayList<Aresta>();
        
        for (int i = 0; i < tam; i++)
            for (int j = 0; j < tam; j++)
                this.matriz[i][j] = null;
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
        preencheAdjacentes(pos);

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
        
        return ret;
    }

    /**
    * Método auxiliar para liberar espaçõs na Matriz de adjacencia
    * @param posicao
    * @param zera 
    */
    private void preencheAdjacentes(Integer posicao){
        Aresta ar = new Aresta();
        for (int i = 0; i < this.tam; i++){
            this.matriz[posicao][i] = ar.clone();
            this.matriz[i][posicao] = ar.clone();
        }
    }

    /**
     * Redimensiona matriz de adjacência
     */
    void redimensionaMatriz(){
        int newTam = matriz.length + tam,
            oldTam = this.tam;
        
        Aresta tmp[][] = new Aresta[newTam][newTam];
                
        for (int i = 0; i < oldTam ; i++)
            for (int j = 0; j < newTam; j++){
                if ((j < oldTam) && (this.matriz[i][i] != null))
                    tmp[i][j] = this.matriz[i][j];
                else
                    tmp[i][j] = null;
            }        
                  
        this.matriz = tmp;
        this.tam = newTam;
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
   
   /**
    * Retorna vertice a partir de sua posição na matriz
    * @param pos
    * @return 
    */
   Vertice getVertice(int pos){
       for (Vertice v : this.vertices){
           if ( v.getPos() == pos )
               return v;
       }
       return null;
   }
   
    /**
     * Remove um vértice do grafo
     * @param chave
     * @return 
     */
    boolean removerVertice(Integer chave){
        Vertice i = pegaVertice(chave);    
        if ( i != null){
            int pos = i.getPos();
            
            for ( int j = 0; j < this.tam; j++ ){
                this.matriz[j][pos] = null;
                this.matriz[pos][j] = null;
            }
            this.vertices.remove(i);
            return true;
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
       Aresta ar = new Aresta(true, peso, pegaVertice(v1), pegaVertice(v2));
       
       v1 = achaVertice(v1);
       v2 = achaVertice(v2);
       
       if ((v1 == -1 ) || (v2 == -1))
           return false;
       else {
            this.matriz[v1][v2] = ar.clone();
            this.arestas.add(ar);
            
            // Em grafos Não Direcionados, insere aresta contrária.
            if (this.tipo == tipoGrafo.naoDirecionado){
                this.matriz[v2][v1] = ar;
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
            Aresta ar = this.matriz[v1][v2];
            this.arestas.remove(ar);
            this.matriz[v1][v2] = new Aresta();
                        
            // Se grafo não for direcionado, remove aresta contrária
            if ( this.tipo == tipoGrafo.naoDirecionado ){
                this.matriz[v2][v1] = new Aresta();
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
        
        return (( this.matriz[v1][v2] != null ) && (this.matriz[v1][v2].getConectado()));
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
    public int grauEntrada(Grafo grafo, Integer pos){
        int grauEntrada = 0;
        for (int i = 0; i <= grafo.tam-1 ; i++){
            if ( ( grafo.matriz[i][pos] != null ) && (grafo.matriz[i][pos].getConectado()) )
                grauEntrada ++;
        }
        return grauEntrada;
    }
    
    /**
     * Retorna a posição de um vértice na Matriz.
     * @param chave
     * @return 
     */
    public int achaVertice(int chave){
        for (Vertice v : this.vertices){
            if (v.getChave() == chave)
                return v.getPos();
        }
        return -1;
    }
    
    /**
     * Ordem Topologica
     */
    public void ordemTopologica(Grafo copiaGrafo, ArrayList<Integer> lista){
        ArrayList<Integer> listaAux = new ArrayList<Integer>();
        int[] grauEntrada = new int[copiaGrafo.vertices.size()];
        Vertice i;
        
        if (!copiaGrafo.vertices.isEmpty()){
            listaAux.clear();
            for ( int j =  0; j < copiaGrafo.vertices.size(); j++ ){
                i = copiaGrafo.vertices.get(j);
                grauEntrada[j] = grauEntrada(copiaGrafo,i.getPos());
            }

            for ( int j =  0; j < copiaGrafo.vertices.size(); j++ ){
                if (grauEntrada[j] <= 0){
                    i = copiaGrafo.vertices.get(j);
                    listaAux.add(i.getChave());
                }
            }
            
            for ( int j : listaAux ){
                if ( copiaGrafo.achaVertice(j) != -1 )
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
                
        if (! conjArestas.isEmpty()){
            Collections.sort(conjArestas);
        
            while (! conjArestas.isEmpty()){    
                Aresta ar;
                ar = conjArestas.remove(0);
                
                Integer x, y;
                x = conjVertices.achaRaiz(ar.getV1().getPos());
                y = conjVertices.achaRaiz(ar.getV2().getPos());
                
                if ( x != y ){
                    conjVertices.unir(ar.getV1().getPos(), ar.getV2().getPos());
                    lista.add(ar);
                }
            } 
        }
    }
    
    /**
     * Menor caminho
     * @return 
     */
    public Integer menorCaminho(Integer v1, Integer v2, ArrayList<Integer> caminho){
        int totVertices = this.matriz.length;
        int origem = pegaVertice(v1).getPos();
        int destino = pegaVertice(v2).getPos();
        
        int[] dist = new int[totVertices]; // Distancias
        int[] perm = new int[totVertices]; // Permutações
        int[] path = new int[totVertices]; // Caminhos
        
        for (int i = 0; i < totVertices; i++){
            perm[i] = 0;
            dist[i] = custoMaximo();
        }
        
        perm[origem] = 1;
        dist[origem] = 0;
        
        int dc, k, atual, menorDistancia, novaDistancia;
        
        atual = origem;
        k = atual;
        
        while (k != destino){
            menorDistancia = custoMaximo();
            dc = dist[atual];
            
            for (int i = 0; i < totVertices; i++){
                if (perm[i] == 0){
                    novaDistancia = dc + ( this.matriz[atual][i] != null ? this.matriz[atual][i].getPeso() : custoMaximo() );
                    
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
            caminho.add( getVertice(path[rota]).getChave() );
            rota = path[rota];
        }
        Collections.reverse(caminho);
        caminho.add( getVertice(destino).getChave() );
        
        return dist[destino];
    }
    
    @Override
    protected Grafo clone() {
        try{
            Grafo clone;
            clone = (Grafo) super.clone();
            clone.vertices = (ArrayList<Vertice>)this.vertices.clone();
            
            clone.matriz = new Aresta[tam][tam];
            
            for ( int i = 0; i < this.tam; i++ )
                for (int j = 0; j < this.tam; j++)
                    clone.matriz[i][j] = ( this.matriz[i][j] != null ? new Aresta(this.matriz[i][j].getConectado(),this.matriz[i][j].getPeso(),this.matriz[i][j].getV1(),this.matriz[i][j].getV2()) : null);
            
            return clone;
        }catch (CloneNotSupportedException e){
            System.err.println(this.getClass()+" não é cloneavel.");
            return null;
        }
    }
    
}