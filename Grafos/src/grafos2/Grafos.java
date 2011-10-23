package grafos2;

import java.io.*;
import java.util.*;

/**
 * Implementação de grafos em Java utilizando uma matriz de adjacência.
 * 
 * @author Celso Marigo Junior
 */
public class Grafos {
    /**
     * Lista de Comandos esperados no arquivo de entrada.
     * Vertices n : Inclui o numero n de vertices no grafo.
     * Edges : Implementa arestas para grafos não direcionados
     * Arcs : Implementa arestas para grafos direcionados
     * Queries : Operaçãoes com o grafo
     */
    enum Comandos { Vertices, Edges, Arcs, Queries, ordemtopologica, Null };
    
    public static void main(String[] args) throws FileNotFoundException {
        Grafo grafo = new Grafo(Grafo.tipoGrafo.naoDirecionado, 1);
        BufferedReader inRead;
        //inRead = new BufferedReader(new InputStreamReader(System.in));
        
        // Entrada de arquivo texto para fins de Debug.
        inRead = new BufferedReader(new FileReader("/tmp/entrada.txt"));
        
        Comandos com;
        com = Comandos.Null;
        
        String linha, aux;
        Integer numVertices, vertices = 0, intaux;
        
        try{
            while((linha = inRead.readLine()) != null){
                StringTokenizer sT = new StringTokenizer(linha);
                String entrada = sT.nextToken();
                
                if (entrada.indexOf('*') == 0){
                    if (entrada.equalsIgnoreCase("*Vertices")){
                        com = Comandos.Vertices;
                        numVertices = Integer.parseInt(sT.nextToken());
                        
                    } else if (entrada.equalsIgnoreCase("*Edges")){
                        grafo.setTipo(Grafo.tipoGrafo.naoDirecionado);
                        com = Comandos.Edges;
                        
                    } else if (entrada.equalsIgnoreCase("*Arcs")){
                        grafo.setTipo(Grafo.tipoGrafo.direcionado);
                        com = Comandos.Arcs;
                        
                    } else {
                        com = Comandos.Queries;
                    }
                } else {
                    if (com.equals(Comandos.Vertices)){                       
                        intaux = Integer.parseInt(entrada);
                        aux = sT.nextToken("\n");
                        if ( grafo.adicionarVertice(intaux, aux))
                            vertices++;
                        else
                            System.err.println("Erro ao adicionar vertice: {ID:"+entrada+",dado:"+aux+"}");
                        
                    } else if (com.equals(Comandos.Edges)){
                       grafo.adicionarAresta(Integer.parseInt(entrada), Integer.parseInt(sT.nextToken()), Integer.parseInt(sT.nextToken())); 
                       
                    } else if (com.equals(Comandos.Arcs)){
                       grafo.adicionarAresta(Integer.parseInt(entrada), Integer.parseInt(sT.nextToken()),Integer.parseInt(sT.nextToken())); 
                       
                    } else if(com.equals(Comandos.Queries)){
                        String saida = "";
                        Integer val;
                        if (entrada.equalsIgnoreCase("get")){
                            val = Integer.parseInt(sT.nextToken());
                            
                            saida += "{\"vertice\"";
                            
                            if (grafo.achaVertice(val) == -1)
                                saida += "{\"ID\":"+val.toString() + ",\"dado\":\",\"resposta\":\"falha\"}";
                            else
                                saida += grafo.pegaVertice(val).toString() +",\"resposta\":\"sucesso\"}";
                            
                        } else if (entrada.equalsIgnoreCase("delete")){
                            val = Integer.parseInt(sT.nextToken());
                            saida += "{\"delete\""+grafo.pegaVertice(val).toString() +",\"resposta\":"+
                                    (grafo.removerVertice( val ) ? "\"sucesso\"" : "\"falha\"")+'}';
                            
                        } else if (entrada.equalsIgnoreCase("vizinhos")){
                            ArrayList<Integer> v = new ArrayList<Integer>();
                                    
                            val = Integer.parseInt(sT.nextToken());
                            
                            saida += "{\"vizinhos\":{\"ID\":"+val.toString();
                            if (grafo.achaVertice(val) == -1)
                                saida += ",\"resposta\":\"falha\",\"vizinhos\":[]";
                            else {                                                    
                                v = grafo.listarVizinhos( val );
                                saida += ",\"resposta\":\"sucesso\",\"vizinhos\":[";
                                aux = "";
                                for (Integer j : v){
                                    saida += aux+j.toString();
                                    aux = ",";
                                }
                                saida += "]}";
                            }   
                        
                        } else if (entrada.equalsIgnoreCase("conexao")){
                            Integer v1, v2;
                            v1 = Integer.parseInt(sT.nextToken());
                            v2 = Integer.parseInt(sT.nextToken());
                            saida = "{\"conexao\":{\"ID1\":"+v1+",\"ID2\":"+v2+","+
                            ( grafo.testarAdjacencia(v1,v2) ?
                                                           "\"sucesso\",\"conexao\":\"sim\"}" :
                                                           "\"falha\",\"conexao\":\"\"}" );

                        } else if (entrada.equalsIgnoreCase("ordemtopologica")){
                            ArrayList<Integer> ordemtop = new ArrayList<Integer>();
                            Grafo copia;

                            copia = (Grafo) grafo.clone();
                            saida += "{\"ordemtop\":[";
                            grafo.ordemTopologica(copia, ordemtop);

                            aux = "";
                            for (Integer j : ordemtop){
                                saida += aux+j.toString();
                                aux = ",";
                            }
                            saida += "]}";
                        } else if (entrada.equalsIgnoreCase("arvoreminima")){
                            ArrayList<Aresta> arvoreminima = new ArrayList<Aresta>();
                            Integer peso = 0;

                            saida += "{\"arvoreminima\":{\"arestas\":[";
                            grafo.arvoreMinima(arvoreminima);

                            aux = "";
                            for (Aresta ar : arvoreminima){
                                saida += aux+"("+ar.getV1().getChave().toString()
                                        +","+ar.getV2().getChave().toString()+")";
                                aux = ",";
                                peso += ar.getPeso();
                            }
                            saida += "],\"custo\":"+peso.toString()+"}";
                        } else if (entrada.equalsIgnoreCase("menorcaminho")){
                            Integer v1, v2, peso = 0;
                            v1 = Integer.parseInt(sT.nextToken());
                            v2 = Integer.parseInt(sT.nextToken());
                            
                            ArrayList<Integer> menorCaminho = new ArrayList<Integer>();
                            
                            saida += "{\"menorcaminho\":{\"ID1\":"+v1+",\"ID2\":"+v2+",\"caminho\":[";
                            peso += grafo.menorCaminho(v1, v2, menorCaminho);
                            
                            aux = "";                        
                            if (menorCaminho.isEmpty())
                                saida += "[],\"custo\":";
                            else
                                for (Integer i : menorCaminho){
                                    saida += aux + i.toString();
                                    aux = ",";
                                }
                                saida += "],\"custo\":"+peso.toString()+"}";
                        }
                            System.out.println(saida);
                    }
                }                  
            }
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
}
