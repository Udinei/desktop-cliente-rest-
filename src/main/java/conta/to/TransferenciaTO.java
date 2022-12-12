package conta.to;

import java.math.BigDecimal;

// mesmo to do microservices
public class TransferenciaTO {
    public Integer conta1;
    public Integer conta2;
    public BigDecimal valor;

    public TransferenciaTO() {
    }

    public TransferenciaTO(Integer conta1, Integer conta2, BigDecimal valor) {
        this.conta1 = conta1;
        this.conta2 = conta2;
        this.valor = valor;
    }

    public Integer getConta1() {
        return conta1;
    }

    public void setConta1(Integer conta1) {
        this.conta1 = conta1;
    }

    public Integer getConta2() {
        return conta2;
    }

    public void setConta2(Integer conta2) {
        this.conta2 = conta2;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
