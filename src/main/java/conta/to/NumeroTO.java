package conta.to;

// mesmo to do microservices
public class NumeroTO {

    public NumeroTO() {
    }

    public NumeroTO(Integer conta) {
        this.conta = conta;
    }

    public Integer conta;

    public Integer getConta() {
        return conta;
    }

    public void setConta(Integer conta) {
        this.conta = conta;
    }
}
