/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafos2;

/**
 *
 * @author celso
 */
public class Aresta implements Comparable<Aresta> {
    private Integer peso;
    private Vertice v1;
    private Vertice v2;
    
    public Aresta(Integer peso, Vertice v1, Vertice v2) {
        this.peso = peso;
        this.v1 = v1;
        this.v2 = v2;
    }
    
    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Vertice getV1() {
        return v1;
    }

    public void setV1(Vertice v1) {
        this.v1 = v1;
    }

    public Vertice getV2() {
        return v2;
    }

    public void setV2(Vertice v2) {
        this.v2 = v2;
    }

    @Override
    public String toString() {
        return "Aresta{" + "peso=" + peso + ", v1=" + v1.getChave() + ", v2=" + v2.getChave() + '}';
    }
    
    @Override
    public int compareTo(Aresta o) {
        return ( this.peso < o.peso ? -1 :
                (this.peso == o.peso ? 0 : 1));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aresta other = (Aresta) obj;
        if (this.peso != other.peso && (this.peso == null || !this.peso.equals(other.peso))) {
            return false;
        }
        if (this.v1 != other.v1 && (this.v1 == null || !this.v1.equals(other.v1))) {
            return false;
        }
        if (this.v2 != other.v2 && (this.v2 == null || !this.v2.equals(other.v2))) {
            return false;
        }
        return true;
    }    
}
