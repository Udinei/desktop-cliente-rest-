package conta.to;

import java.math.BigDecimal;

// mesmo to do microservices
public class ContaTO {

    public BigDecimal saldo;
    public String correntista;

    public ContaTO() {
    }

    public ContaTO(BigDecimal saldo, String correntista) {
        this.saldo = saldo;
        this.correntista = correntista;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getCorrentista() {
        return correntista;
    }

    public void setCorrentista(String correntista) {
        this.correntista = correntista;
    }
}
