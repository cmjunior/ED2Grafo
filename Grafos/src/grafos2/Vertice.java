package grafos2;

/**
 * Vertice
 * chave : valor do Vertice
 * pos   : posição do Vertice na Matriz de Adjacencia
 * nome  : Nome do vertice
 * @author Celso Marigo Junior
 */
public class Vertice implements Cloneable, Comparable<Vertice> {
    private Integer chave;
    private Integer pos;
    private String nome;

    public Vertice(Integer chave, Integer pos, String nome) {
        this.chave = chave;
        this.nome = nome;
        this.pos = pos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getChave() {
        return chave;
    }

    public void setChave(Integer chave) {
        this.chave = chave;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }
    
    @Override
    public String toString() {
        return ":{" + "\"ID\":\"" + chave + "\",\"dado\":" + nome ;
    }

    @Override
    protected Vertice clone() {
        try{
            return (Vertice) super.clone();
        }catch (CloneNotSupportedException e){
            System.err.println(this.getClass()+" não é clonavel.");
            return null;
        }
    }

    @Override
    public int compareTo(Vertice o) {
        return ( this.pos < o.pos ? -1 :
                ( this.pos == o.pos ? 0 : 1 ));
    }    
}